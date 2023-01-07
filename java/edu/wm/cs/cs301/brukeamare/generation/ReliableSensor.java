/**
 * 
 */
package edu.wm.cs.cs301.brukeamare.generation;



import edu.wm.cs.cs301.brukeamare.extra.DistanceSensor;
import edu.wm.cs.cs301.brukeamare.extra.Robot.Direction;




/**
 * Class Name: ReliableSensor.java
 * 
 * Responsibilities:
 * distanceToObstacle
 * setMaze
 * setSensorDirection
 * getEnergyConsumptionForSensing
 * startFailureAndRepairProcess
 * stopFailureAndRepairProcess
 * 
 * Collaborators:
 * DistanceSensor.java
 * UnreliableSensor.java
 * 
 * 
 * @author BRUKE AMARE
 *
 */
public class ReliableSensor implements DistanceSensor {
	/**
	 * instance of maze
	 */
	public Maze damaze;
	/**
	 * Instance of direction of sensor
	 */
	public Direction dadirection;
	/**
	 * constructor Method
	 */
	public ReliableSensor() {
		
	}
	
	/**
	 * Tells the distance to an obstacle (a wallboard) that the sensor
	 * measures. The sensor is assumed to be mounted in a particular
	 * direction relative to the forward direction of the robot.
	 * Distance is measured in the number of cells towards that obstacle, 
	 * e.g. 0 if the current cell has a wallboard in this direction, 
	 * 1 if it is one step in this direction before directly facing a wallboard,
	 * Integer.MaxValue if one looks through the exit into eternity.
	 * 
	 * This method requires that the sensor has been given a reference
	 * to the current maze and a mountedDirection by calling 
	 * the corresponding set methods with a parameterized constructor.
	 * 
	 * @param currentPosition is the current location as (x,y) coordinates
	 * @param currentDirection specifies the direction of the robot
	 * @param powersupply is an array of length 1, whose content is modified 
	 * to account for the power consumption for sensing
	 * @return number of steps towards obstacle if obstacle is visible 
	 * in a straight line of sight, Integer.MAX_VALUE otherwise.
	 * @throws Exception with message 
	 * SensorFailure if the sensor is currently not operational
	 * PowerFailure if the power supply is insufficient for the operation
	 * @throws IllegalArgumentException if any parameter is null
	 * or if currentPosition is outside of legal range
	 * ({@code currentPosition[0] < 0 || currentPosition[0] >= width})
	 * ({@code currentPosition[1] < 0 || currentPosition[1] >= height}) 
	 * @throws IndexOutOfBoundsException if the powersupply is out of range
	 * ({@code powersupply < 0}) 
	 */
	
	@Override
	public int distanceToObstacle(int[] currentPosition, CardinalDirection currentDirection, float[] powersupply) throws Exception {
		
		
		Floorplan dafloor= damaze.getFloorplan();
		
		if (currentPosition[0] < 0 || currentPosition[0] >= damaze.getWidth()|| currentPosition[1] < 0 || currentPosition[1] >= damaze.getHeight() || currentPosition==null|| currentDirection==null||powersupply==null) {
			throw new IllegalArgumentException("IllegalArgumentException"); 

		}
		if (currentPosition[0] < 0 ) {
			throw new Exception("SensorFailure") ;
			
		}
		if (powersupply.length < 1 ) {
			throw new IndexOutOfBoundsException("IndexOutOfBoundsException"); 

		}
		if (powersupply[0] < 1 ) {
			throw new Exception("PowerFailure");
			
		}
		
		int steps= 0;
		int x=currentPosition[0];
		int y= currentPosition[1];
		boolean um=true;
		int [] ner=currentDirection.getDxDyDirection();

		try {
			while(um) {
				if (dafloor.hasWall(x,y, currentDirection)){
					if(ReliableRobot.batterylevel>=1) {
					ReliableRobot.batterylevel-=1;
					return steps;
					}
					else{throw new Exception();}
				}
				x+= ner[0];
				y+= ner[1];
				steps++;
			}
		}catch(ArrayIndexOutOfBoundsException e) {
			ReliableRobot.batterylevel-=1;
			return Integer.MAX_VALUE;
		}catch(Exception e){
			throw new Exception();
			
		}
		if(ReliableRobot.batterylevel>=1) {
			ReliableRobot.batterylevel-=1;
			}
			else{throw new Exception();}
		return Integer.MAX_VALUE;
	}

	/**
	 * Provides the maze information that is necessary to make
	 * a DistanceSensor able to calculate distances.
	 * @param maze the maze for this game
	 * @throws IllegalArgumentException if parameter is null
	 * or if it does not contain a floor plan
	 */
	@Override
	public void setMaze(Maze maze) {
		Floorplan dafloor= maze.getFloorplan();
		if(maze==null || dafloor==null) {
			throw new IllegalArgumentException() ;
		}
		damaze=maze;
	}

	/**
	 * Provides the angle, the relative direction at which this 
	 * sensor is mounted on the robot.
	 * If the direction is left, then the sensor is pointing
	 * towards the left hand side of the robot at a 90 degree
	 * angle from the forward direction. 
	 * @param mountedDirection is the sensor's relative direction
	 * @throws IllegalArgumentException if parameter is null
	 */
	@Override
	public void setSensorDirection(Direction mountedDirection) {
		
		if(mountedDirection==null) {
			throw new IllegalArgumentException() ;
		}
		dadirection= mountedDirection;
	}

	/**
	 * Returns the amount of energy this sensor uses for 
	 * calculating the distance to an obstacle exactly once.
	 * This amount is a fixed constant for a sensor.
	 * @return the amount of energy used for using the sensor once
	 */
	@Override
	public float getEnergyConsumptionForSensing() {

		return 1;
	}

	////////
	@Override
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopFailureAndRepairProcess() 
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		
	}
	
}

	
