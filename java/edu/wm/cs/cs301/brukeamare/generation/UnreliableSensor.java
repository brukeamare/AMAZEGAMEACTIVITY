/**
 * 
 */
package edu.wm.cs.cs301.brukeamare.generation;

import edu.wm.cs.cs301.brukeamare.extra.Constants;
import edu.wm.cs.cs301.brukeamare.extra.DistanceSensor;
import edu.wm.cs.cs301.brukeamare.extra.Robot.Direction;
import edu.wm.cs.cs301.brukeamare.extra.RobotDriver;

/**
 * Class Name: UnreliableSensor.java
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
 * ReliableSensor.java
 * 
 * 
 * @author BRUKE AMARE
 *
 */

public class UnreliableSensor implements DistanceSensor, Runnable{
	/**
	 * instance of maze
	 */
	public Maze damaze;
	public int meanTimeBetweenFailures=4000;
	public int meanTimeToRepair=2000;
	/**
	 * Instance of direction of sensor
	 */
	public boolean waitasec= false;
	public Direction dadirection;
	
	public boolean pleasestop=false;
	public boolean started=false;
	
	public boolean Operational=true;
	public Thread op;
	/**
	 * constructor Method
	 * @throws InterruptedException 
	 */
	public UnreliableSensor() {
		op= new Thread(this);

		
	}
	
	/**
	 * Tells the distance to an obstacle (a wallboard) that the sensor
	 * measures.  Yet might be not operational at times. Implements a thread 
	 * that turns makes it fail and repair for certain amounts of time
	 * The sensor is assumed to be mounted in a particular
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
					if(UnreliableRobot.batterylevel>=1) {
					UnreliableRobot.batterylevel-=1;
					return steps;
					}
					else{throw new Exception();}
				}
				x+= ner[0];
				y+= ner[1];
				steps++;
			}
		}catch(ArrayIndexOutOfBoundsException e) {
			UnreliableRobot.batterylevel-=1;
			return Integer.MAX_VALUE;
		}catch(Exception e){
			throw new Exception();
			
		}
		if(UnreliableRobot.batterylevel>=1) {
			UnreliableRobot.batterylevel-=1;
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

	/**
	 * Method starts a concurrent, independent failure and repair
	 * process that makes the sensor fail and repair itself.
	 * This creates alternating time periods of up time and down time.
	 * Up time: The duration of a time period when the sensor is in 
	 * operational is characterized by a distribution
	 * whose mean value is given by parameter meanTimeBetweenFailures.
	 * Down time: The duration of a time period when the sensor is in repair
	 * and not operational is characterized by a distribution
	 * whose mean value is given by parameter meanTimeToRepair.
	 * 
	 * This an optional operation. If not implemented, the method
	 * throws an UnsupportedOperationException.
	 * 
	 * @param meanTimeBetweenFailures is the mean time in seconds, must be greater than zero
	 * @param meanTimeToRepair is the mean time in seconds, must be greater than zero
	 * @throws UnsupportedOperationException if method not supported
	 */
	@Override
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {


			try {
				

				started= true;
				while(!pleasestop){

					Thread.sleep(meanTimeBetweenFailures);
					Operational=false;
					Thread.sleep(meanTimeToRepair);
					Operational=true;
				}
				
			} catch (InterruptedException e) {
				throw new UnsupportedOperationException();
			}
		
		
		
	}

	/**
	 * This method stops a failure and repair process and
	 * leaves the sensor in an operational state.
	 * 
	 * It is complementary to starting a 
	 * failure and repair process. 
	 * 
	 * Intended use: If called after starting a process, this method
	 * will stop the process as soon as the sensor is operational.
	 * 
	 * If called with no running failure and repair process, 
	 * the method will return an UnsupportedOperationException.
	 * 
	 * This an optional operation. If not implemented, the method
	 * throws an UnsupportedOperationException.
	 * 
	 * @throws UnsupportedOperationException if method not supported
	 */
	@Override
	public void stopFailureAndRepairProcess() 
			throws UnsupportedOperationException {
		if (!started) {
			throw new UnsupportedOperationException();
		}
		while(!Operational) {
			
		}
		pleasestop=true;
		Operational=true;
		

	}
	/**
	 * Run method since we want object to run a thread that crashes and repairs the sensor
	 */
	@Override
	public void run() {
		startFailureAndRepairProcess(meanTimeBetweenFailures,meanTimeToRepair);
		
	}
}

