package valueFramework;
import java.util.Random;

import repast.simphony.engine.schedule.ScheduledMethod;

//TODO: update it with Maarten sent code
public class WaterTank{
	private double capacity;
	private double filledLevel;
	private double drainingAmount;// = 0.5;
	private double increasingAmount;//0.5;
	private double threshould;
	
	private int maxCapacity;// = 70;
	private int minCapacity;// = 30;
	private int maxFilledLevel;// = 70;
	private int minFilledLevel;// = 20;
	
	private int minThresould;// = 25;
	private int maxThresould;// = 65;
		
	public WaterTank(double drainingAmountIn) {
		maxCapacity = 70;
		minCapacity = 30;
		maxFilledLevel = 70;
		minFilledLevel = 20;
		
		minThresould = 25;
		maxThresould = 65;
		
		Random rand = new Random();
		int cp = rand.nextInt((maxCapacity - minCapacity) + 1) + minCapacity;
		int fl = rand.nextInt((maxFilledLevel - minFilledLevel) + 1) + minFilledLevel;
		int tr = rand.nextInt((maxThresould - minThresould) + 1) + minThresould;
	    setCapacity(cp); //TODO:check if the automatic cast from int to double works fine
	    setFilledLevel(fl);
	    setThresould(tr);
	    setIncreasingAmount(capacity-threshould);
	   setDrainingAmount(drainingAmountIn);
	}
	
	public WaterTank() {
		maxCapacity = 70;
		minCapacity = 30;
		maxFilledLevel = 70;
		minFilledLevel = 20;
		drainingAmount = 10;
		minThresould = 25;
		maxThresould = 65;
		
		Random rand = new Random();
		int cp = rand.nextInt((maxCapacity - minCapacity) + 1) + minCapacity;
		int fl = rand.nextInt((maxFilledLevel - minFilledLevel) + 1) + minFilledLevel;
		int tr = rand.nextInt((maxThresould - minThresould) + 1) + minThresould;
	    setCapacity(cp); //TODO:check if the automatic cast from int to double works fine
	    setFilledLevel(fl);
	    setThresould(tr);
	   setDrainingAmount(drainingAmount);
	}

	
	private void setDrainingAmount(double drainingAmountIn) {
		this.drainingAmount = drainingAmountIn;
	}

	public WaterTank(int capacity, int filledLevel, int threshould, double drainingAmount) {
		setCapacity(capacity); //TODO:check if the automatic cast from int to double works fine
	    setFilledLevel(filledLevel);
	    setThresould(threshould);
	}
	
	@ScheduledMethod(start = 1, interval = 1, shuffle = true)
	public void steps(){
		draining();
		increasingLevel();
	}
	
	public void draining() {		
		filledLevel = Math.max(0, filledLevel - drainingAmount);
	}
	
	
	public void increasingLevel(){//TODO: level of satisfaction?
		filledLevel = Math.min(capacity, filledLevel + increasingAmount);
	}
	
	public double getCapacity() {
		return capacity;
	}

	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	public double getFilledLevel() {
		return filledLevel;
	}

	public void setFilledLevel(double filledLevel) {
		this.filledLevel = filledLevel;
	}
	
	public void setThresould(double thre) {
		this.threshould = thre;
	}
	
	public double getPriorityPercentage(){
		return (((double)(filledLevel - threshould)/threshould)*100.0);
	}
	
	public void setIncreasingAmount(double increasingA) {
		this.threshould = increasingA;
	}
	
	public double getIncreasingAmount(){
		return increasingAmount;
	}
}
