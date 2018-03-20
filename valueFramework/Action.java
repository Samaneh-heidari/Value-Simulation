package valueFramework;

import java.util.ArrayList;
import java.util.List;

import mas.FWContextBuilder;

public class Action {
	private ArrayList<Object[]> relatedConcreteValues;//arraylist of list[2] : [node as a concrete value, +/- contribution]
	private String title;
	public Action() {
		relatedConcreteValues = new ArrayList<Object[]>();	
		title = Integer.toString(FWContextBuilder.actionNumber);
		FWContextBuilder.actionNumber++;
	}
	
	public void addRelatedValues(Node concreteValue, Boolean contribution){
		Object[] input = {concreteValue, concreteValue};
		relatedConcreteValues.add(input);
//		System.out.println(input[0]);
	}

	public ArrayList<Object[]> getRelatedConcreteValues(){
		return relatedConcreteValues;
	}
	public int checkRalatedValueInValueTree(Node root, boolean contributionType) {
		//look into the value tree until you get the concrete values and check if the concrete values are in the list of relatedValues of the actionC
		return sweepTree(root, contributionType);
		 
	}

	private int sweepTree(Node root, boolean contributionType) {
		int numOfRelatedConcreteValues = 0;
		if(root.getChildren().size() ==0)//it's a leaf
		{
			Object[] inList = {root, contributionType};//TODO: check the if condition
			if(relatedConcreteValues.contains(inList))
				numOfRelatedConcreteValues++;
			
		}
		else{
			List<Node> children = root.getChildren();
			for (int i = 0; i < children.size(); i++) {
				numOfRelatedConcreteValues = numOfRelatedConcreteValues + sweepTree(children.get(i), contributionType); 
			}			
		}
		return numOfRelatedConcreteValues;
	}

	@Override
    public String toString(){
        return "action "+ title +", done" ;
    }
	
}
