package com.college.civilengineeringtools;

public class ResultCalculator {

	private double length, x;
	private double forceA, forceB, moment, shear;
	private double totalForceA, totalForceB, totalMoment, totalShear;
	
	String type;
	
	public static ResultCalculator instance = null;
	
	public static ResultCalculator getInstance() {
		if(instance == null) {
			instance = new ResultCalculator();
		} 
		return instance;
	}

	protected ResultCalculator() {

		length = 0;
		x = 0;
		type = null;

		forceA = forceB = moment = shear = 0;
		
		totalForceA = totalForceB = totalMoment = totalShear = 0;

	}

	public double solveTotalShear(PointLoadManager ptMan, DistributedLoadManager distMan, final double l, final double pos) {
		
		totalShear = totalForceA = totalForceB = 0;
		length = l;
		x = pos;

		for (int i = 0; i < ptMan.getPtLoads().size(); i++) {

			PointLoad pt = ptMan.getPtLoads().get(i);
			solveReactionForces(pt);
			solveShear(pt);
		}
		
		for (int i = 0; i < distMan.getDistLoads().size(); i++) {
			
			DistributedLoad dl = distMan.getDistLoads().get(i);
			solveReactionForces(dl);
			solveShear(dl);
		}
		
		return totalShear;
	}
	
	public double solveTotalMoment(PointLoadManager ptMan, DistributedLoadManager distMan, final double l, final double pos) {
		
		totalMoment = 0;
		length = l;
		x = pos;

		for (int i = 0; i < ptMan.getPtLoads().size(); i++) {
			
			PointLoad pt = ptMan.getPtLoads().get(i);
			solveMoment(pt);
		}
	

		for (int i = 0; i < distMan.getDistLoads().size(); i++) {
			
			DistributedLoad dl = distMan.getDistLoads().get(i);
			solveMoment(dl);
		}
		
		return totalMoment;

	}

	private void solveReactionForces(PointLoad pt) {

		double P = pt.getMagnitude();
		double a = pt.getPosition();
		double b = length - a;

		if (MainActivity.beamType == BeamOptionsActivity.SIMPLE_BEAM) {
			forceA = P * b / length;
			forceB = P * a / length;
		} else if (MainActivity.beamType == BeamOptionsActivity.CANTILEVER) {
			forceA = P;
			forceB = 0;
		}
		
		totalForceA += forceA;
		totalForceB += forceB;
		
		forceA = forceB = 0;

	}

	private void solveReactionForces(DistributedLoad dl) {
		
		double w1 = dl.getStartingMagnitude();
		double w2 = dl.getEndingMagnitude();
		double a = dl.getStartingPosition();
		double b = dl.getEndingPosition() - dl.getStartingPosition();
		double c = length - dl.getEndingPosition();
		
		if(MainActivity.beamType == BeamOptionsActivity.SIMPLE_BEAM) {
			forceA = w1 * b * (2 * c + b) / (2 * length);
			forceB = w1 * b * (2 * a + b) / (2 * length);
		} else if (MainActivity.beamType == BeamOptionsActivity.CANTILEVER) {
			forceA = w1 * b;
			forceB = 0;
		}
		
		totalForceA += forceA;
		totalForceB += forceB;
		
		forceA = forceB = 0;
	
	}

	private void solveMoment(PointLoad pt) {

		double P = pt.getMagnitude();
		double a = pt.getPosition();
		double b = length - a;

		if (MainActivity.beamType == BeamOptionsActivity.SIMPLE_BEAM) {
			if(x < a) 
				moment = P * b * x / length;
			else 
				moment = P * a * (length - x) / length;
		} else if (MainActivity.beamType == BeamOptionsActivity.CANTILEVER) {
			if(x < a) 
				moment = -P * (a - x);
			else
				moment = 0;
		}
		
		totalMoment += moment;
		
		moment = 0;

	}

	private void solveMoment(DistributedLoad dl) {

		double w1 = dl.getStartingMagnitude();
		double w2 = dl.getEndingMagnitude();
		double a = dl.getStartingPosition();
		double b = dl.getEndingPosition() - dl.getStartingPosition();
		double c = length - dl.getEndingPosition();
		
		if(MainActivity.beamType == BeamOptionsActivity.SIMPLE_BEAM) {
			if(x <= a)
				moment = (w1 * b * (2 * c + b) / (2 * length)) * x;
			else if(x > a && x <= (a + b))
				moment = (w1 * b * (2 * c + b) / (2 * length)) * x - (w1 / 2) * (x - a) * (x - a);
			else
				moment = (w1 * b * (2 * a + b) / (2 * length)) * (length - x);
		} else if (MainActivity.beamType == BeamOptionsActivity.CANTILEVER) {
			if(x <= a)
				moment = w1 * b * x - (w1 * b * (a + (b / 2)));  
			else if(x > a && x <= (a + b))
				moment = -w1 * ((a + b) - x) * ((a + b) - x) / 2;
			else
				moment = 0;
		}
		
		totalMoment += moment;
		
		moment = 0;
		
	}

	private void solveShear(PointLoad pt) {

		double P = pt.getMagnitude();
		double a = pt.getPosition();
		double b = length - a;

		if (MainActivity.beamType == BeamOptionsActivity.SIMPLE_BEAM) {
			if(x < a) 
				shear = P * b / length;
			else 
				shear = -(P * a / length);
		} else if (MainActivity.beamType == BeamOptionsActivity.CANTILEVER) {
			if (x < a)
				shear = P;
			else
				shear = 0;
		}
		
		totalShear += shear;
		
		shear = 0;
	}

	private void solveShear(DistributedLoad dl) {

		double w1 = dl.getStartingMagnitude();
		double w2 = dl.getEndingMagnitude();
		double a = dl.getStartingPosition();
		double b = dl.getEndingPosition() - dl.getStartingPosition();
		double c = length - dl.getEndingPosition();
		
		if (MainActivity.beamType == BeamOptionsActivity.SIMPLE_BEAM) {
			if(x <= a) 
				shear = w1 * b * (2 * c + b) / (2 * length);
			else if(x > a && x <= (a + b))
				shear = (w1 * b * (2 * c + b) / (2 * length)) - w1 * (x - a);
			else 
				shear = -(w1 * b * (2 * a + b) / (2 * length));
		} else if (MainActivity.beamType == BeamOptionsActivity.CANTILEVER) {
			if(x <= a) 
				shear = w1 * b;
			else if(x > a && x <= (a + b))
				shear = w1 * (-x + a + b);
			else 
				shear = 0;
		}
		
		totalShear += shear;
		
		shear = 0;
		
	}
	
	final public double getTotalReactionA() {
		return totalForceA;
	}
	
	final public double getTotalReactionB() {
		return totalForceB;
	}
	
	final public double getTotalShear() {
		return totalShear;
	}
	
	final public double getTotalMoment() {
		return totalMoment;
	}
	
	public void reset() {
		forceA = forceB = moment = shear = totalForceA = totalForceB = totalMoment = totalShear = 0;
	}
	


}
