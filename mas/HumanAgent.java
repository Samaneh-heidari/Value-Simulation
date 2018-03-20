package mas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import javax.media.jai.Histogram;

import org.jaxen.function.ext.LowerFunction;

import valueFramework.*;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class HumanAgent {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	public static final int numOfPersonalValues = 10;
	private ArrayList<Action> possibleActions;
	private Map<String, RandomTree> valueTrees;

	public HumanAgent() {
		createYourOwnRandomValueTrees();
		possibleActions = new ArrayList<Action>();
	}

	public HumanAgent(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid;
	}

	private void createYourOwnRandomValueTrees() {
		valueTrees  = new HashMap<String, RandomTree>();
		for(int i = 0; i < numOfPersonalValues ; i ++){//TODO:number of values in Schwartz Value System
			RandomTree tree = new RandomTree();			
			Node root = tree.randomTreeBuilder(5, 3, Integer.toString(i));			
			valueTrees.put(root.getValueName(), tree);
		}
	}

	@SuppressWarnings("unchecked")
	@ScheduledMethod(start = 1, interval = 1, shuffle = true)
	public void step() {
		ArrayList<Action> possibleActions = new ArrayList<Action>();
		possibleActions = selectPossibleActionsBasedonPerspective();//TODO: for now, perspective is off. So, all the actions are possible actions
		ArrayList<RandomTree> selectedValue = selectAbstractValuesAccordingToActions(possibleActions);
		ArrayList<Action> filterdActions = new ArrayList<Action>();
		filterdActions = (ArrayList<Action>) filterActionsAccordingToTheMostImportantValue(possibleActions, selectedValue)[0];
		ArrayList<RandomTree>relatedValues = new ArrayList<RandomTree>();
		relatedValues =  (ArrayList<RandomTree>) filterActionsAccordingToTheMostImportantValue(possibleActions, selectedValue)[1];
		for(int i =0; i < relatedValues.size(); i++){
			relatedValues.get(i).getWaterTank().increasingLevel();
		}
		//TODO: check if it is pointer object and if you use the following code the result is still the same. I mean by accessing the tree from valueTrees you still get the updated water tank level
//		selectedValue.getWaterTank().increaseLevel(levelOfSatisfaction);
		System.out.println(filterdActions);
	}
	
	
	private Object[] filterActionsAccordingToTheMostImportantValue(
			ArrayList<Action> possibleActionsSet, ArrayList<RandomTree> selectedValues) {
		//TODO: filter the input list, apply the formula and return the final action set that is filtered by 
		//the most important values.
		//1.Prioritize values
		//2.select the highest priorities
		//3.select actions related to that
		//4.return the arraylist <object> in which object is {arraylist<action> and arrayList<values>}
		ArrayList<Action> returnedActions = new ArrayList<Action>();
		ArrayList<RandomTree> returnedValues = new ArrayList<RandomTree>();
		if(selectedValues == null | possibleActionsSet == null)
			return null;
		ArrayList<RandomTree> prioritizedAbstractValues = prioritizingValues(selectedValues);
		double prvPrio = -100;
		double crrPrio = prioritizedAbstractValues.get(0).getWaterTank().getPriorityPercentage();
		for(int i = 0; i < selectedValues.size(); i++){
			ArrayList<Action> aa = filterActions(possibleActionsSet,selectedValues.get(i));
			if(aa.size() != 0 )
				returnedValues.add(selectedValues.get(i));
			for(int j = 0; j < aa.size(); j++)
				returnedActions.add(aa.get(j));
			prvPrio = crrPrio;
			crrPrio = prioritizedAbstractValues.get(i).getWaterTank().getPriorityPercentage();
			if (crrPrio != prvPrio){
				if (returnedActions.size() != 0)
					break;
				else
					continue;
			}
			else
				continue;
		}
		Object[] returnedResults = new Object[]{returnedActions, returnedValues};
		return returnedResults;
	}

	private ArrayList<RandomTree> prioritizingValues(ArrayList<RandomTree> selectedValues) {		
		Collections.sort(selectedValues);
		return selectedValues;
	}

	private ArrayList<Action> selectPossibleActionsBasedonPerspective() {
		//TODO: it should select all the possible actions that are active in this perspective
		//so TODO: add perspective and link it to actions. But, for now it can return actions randomly
//		ArrayList<Action> possibleActions = new ArrayList<Action>();
		return possibleActions;			
	}

	private ArrayList<RandomTree> selectAbstractValuesAccordingToActions(
			ArrayList<Action> possibleActionsSet) {
		//TODO: picks values that are linked to the possible actions. and then apply the priority function on them
		//then returns the values and their importance . 
		//priority is a signed calculated like this : (level-thres)/thres *100;
		//this list contains at the values with the same priority.
		//TODO: check if the list is empty write a message that values are not applicable here.
		ArrayList<RandomTree> outValues = new ArrayList<RandomTree> ();
		for (int i = 0; i < possibleActionsSet.size(); i++) {
			ArrayList<RandomTree> val = findAbstractValues(possibleActionsSet.get(i));
			for(int j= 0 ; j < val.size(); j++){
				outValues.add(val.get(j));
			}
		}		
		return outValues;
	}

	private ArrayList<RandomTree> findAbstractValues(Action action) {
		ArrayList<Node> absValues = new ArrayList<Node>();
		ArrayList<RandomTree> rndTrees = new ArrayList<RandomTree>();
		ArrayList<Object[]> concreteValues = action.getRelatedConcreteValues();
		for (int i = 0; i < concreteValues.size(); i++) {
			Node prnt = (Node)(concreteValues.get(i))[0];
			while(prnt!=null){
				prnt = prnt.getParent();
			}
			if(!absValues.contains(prnt)) absValues.add(prnt);			
		}
		
		for(int i = 0; i < absValues.size(); i++){
			if(valueTrees.containsKey(absValues.get(i).getValueName()))
				rndTrees.add(valueTrees.get(absValues.get(i).getValueName()));
		}		
		
		return rndTrees;
	}

	private ArrayList<Action> selectActionsAccordingToTheMostImportantValue(ArrayList<Action> possibleActionsSet) {
		ArrayList<Action> filteredActions = new ArrayList<Action>();
		//TODO: first select only values that are linked to the possibleActions
		//start with finding the two lowest water level tanks
		double lowestLevel = Double.MAX_VALUE;
		RandomTree lowestValue = null;
		double secondLowestLevel = Double.MAX_VALUE;
		RandomTree secondLowestValue = null;
		double tempLevel;
		for (RandomTree node:valueTrees.values()) {
			tempLevel = node.getWaterTank().getFilledLevel();
			if(tempLevel < lowestLevel /* && lowestValue < secondLowest*/){
				lowestLevel = tempLevel;
				lowestValue = node;
				secondLowestLevel = lowestLevel;
				secondLowestValue = lowestValue;
			}
			else if (lowestLevel > tempLevel & tempLevel > secondLowestLevel){
				secondLowestLevel = tempLevel;
				secondLowestValue = node;
			}
		}
		
		//*NOTE : the importance of a value is a complement of its level. Meaning that if a water tank has the lowest level of water, it has the highest priority.

		//comparing the priorities
		//TODO: without considering perspective.
		//there are three modes : if v1 >> v2, if v1 > v2, if v1 == v2. TODO: we skip the second condition for now. 
		if(lowestLevel < secondLowestLevel){
//			return lowestValue;
			filteredActions = filterActions(possibleActions, lowestValue);			
		}
		else if (lowestLevel == secondLowestLevel){
			//find how many of 
			for (int i = 0; i < possibleActions.size(); i++) {
				int numOfPossitiveContibutedValues_lowestLevel = possibleActions.get(i).checkRalatedValueInValueTree(lowestValue.getRoot(), true);
				int numOfPossitiveContibutedValues_secondLowestLevel = possibleActions.get(i).checkRalatedValueInValueTree(lowestValue.getRoot(), true);
				if(numOfPossitiveContibutedValues_lowestLevel > numOfPossitiveContibutedValues_secondLowestLevel){
					filteredActions = filterActions(possibleActions, lowestValue);
					break;
				}
				else if(numOfPossitiveContibutedValues_lowestLevel < numOfPossitiveContibutedValues_secondLowestLevel){
					filteredActions = filterActions(possibleActions,secondLowestValue);
					break;//TODO: check if it breaks from for loop;
				}
				else if(numOfPossitiveContibutedValues_lowestLevel == numOfPossitiveContibutedValues_secondLowestLevel){//TODO: check if it's correct
					int numOfNegativeContibutedValues_secondLowestLevel = possibleActions.get(i).checkRalatedValueInValueTree(lowestValue.getRoot(), false);
					int numOfNegativeContibutedValues_lowestLevel = possibleActions.get(i).checkRalatedValueInValueTree(lowestValue.getRoot(), false);
					filteredActions = filterActions(possibleActions, secondLowestValue);
				}				
			}
			
		}else{
			System.err.println("lowest level cannot be greater that second lowest levle! level = " + lowestLevel + " for  " + lowestValue.getRoot().getValueName() + ", second lowest level = " + secondLowestLevel + " for " + secondLowestValue.getRoot().getValueName());
		}		
		return null;
	}

	private ArrayList<Action> filterActions(ArrayList<Action> possibleActions,
			RandomTree lowestValue) {
		//check all the possibleActions that are related to the given value
		ArrayList<Action> filteredActions = new ArrayList<Action>();				
		for (int i = 0; i < possibleActions.size(); i++) {
			int numOfPossitiveContibutedValues = possibleActions.get(i).checkRalatedValueInValueTree(lowestValue.getRoot(), true);
//			int numOfNegativeContibutedValues = possibleActions.get(i).checkRalatedValueInValueTree(lowestValue.getRoot(), false);
			if(numOfPossitiveContibutedValues != 0){
				filteredActions.add(possibleActions.get(i));
			}
		}
		return filteredActions;		
	}

	
}
