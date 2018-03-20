package mas;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;
import valueFramework.RandomTree;;

public class FWContextBuilder implements ContextBuilder<Object> {

	public static int actionNumber = 0;
	@Override
	public Context build(Context<Object> context) {
		context.setId ("ValueFW");
		ContinuousSpaceFactory spaceFactory =
		ContinuousSpaceFactoryFinder . createContinuousSpaceFactory ( null );
		ContinuousSpace < Object > space =
		spaceFactory . createContinuousSpace ("cspace", context ,
		new RandomCartesianAdder < Object >() ,
		new repast . simphony . space . continuous . WrapAroundBorders () ,
		50 , 50);
		
		GridFactory gridFactory = GridFactoryFinder . createGridFactory ( null );
		Grid < Object > grid = gridFactory . createGrid ("gspace", context ,
		new GridBuilderParameters < Object >( new WrapAroundBorders () ,
		new SimpleGridAdder < Object >() ,
		true , 50 , 50));

		
		
		
		
		/*System.out.println("salam");
		RandomTreeBuilder builder = new RandomTreeBuilder();
		builder.createRandomTree(5, 3, "root");	    
	    System.out.println("bye");*/
	    
	    return context;
	}

}
