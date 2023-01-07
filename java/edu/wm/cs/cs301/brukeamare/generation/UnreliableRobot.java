/**
 * 
 */
package edu.wm.cs.cs301.brukeamare.generation;



import java.util.ArrayList;

import edu.wm.cs.cs301.brukeamare.extra.Constants;
import edu.wm.cs.cs301.brukeamare.extra.DistanceSensor;
import edu.wm.cs.cs301.brukeamare.extra.Robot;
import edu.wm.cs.cs301.brukeamare.extra.StatePlaying;

/**
 * Class Name: UnreliableRobot.java
 * 
 * Responsibilities:
 * setController
 * addDistanceSensor
 * getCurrentPosition
 * getCurrentDirection
 * getBatteryLevel
 * setBatteryLevel
 * getEnergyForFullRotation
 * getEnergyForStepForward
 * getOdometerReading
 * resetOdometer
 * rotate
 * move
 * jump
 * isAtExit
 * isInsideRoom
 * hasStopped
 * distanceToObstacle
 * canSeeThroughTheExitIntoEternity
 * startFailureAndRepairProcess
 * stopFailureAndRepairProcess
 * 
 * Collaborators:
 * Robot.java
 * 
 * @author BRUKE AMARE
 *
 */

public class UnreliableRobot implements Robot{

	/**
	 * Instance of control
	 */
	public StatePlaying controllerz;
	/**
	 * arraylist that holds the robots sensors
	 */
	public ArrayList<UnreliableSensor> sensorarrays= new ArrayList<UnreliableSensor>();
	/**
	 * Instance of robots sensor
	 */
	public UnreliableSensor dasensor;
	/**
	 * varibale keeps track of robots battery
	 */
	public static float batterylevel;
	
	public float batterylevelinit;
	/**
	 * variable keeps track of path moved by robot
	 */
	public int odometer;
	
	public int skillLevel;


	
	
	/**
	 * Provides the robot with a reference to the controller to cooperate with. 
	 * Compatible with both reliable and unreliable sensors.
	 * The robot memorizes the controller such that this method is most likely called only once
	 * and for initialization purposes. The controller serves as the main source of information
	 * for the robot about the current position, the presence of walls, the reaching of an exit.
	 * The controller is assumed to be in the playing state.
	 * @param controller is the communication partner for robot
	 * @throws IllegalArgumentException if controller is null, 
	 * or if controller is not in playing state, 
	 * or if controller does not have a maze
	 */
	/**
	 * Constructor method
	 */
	public UnreliableRobot() {
		
	}

	
	@Override
	public void setController(StatePlaying controller) {

		if (controller==null|| controller.getMaze()==null||controller.started == false) {
			throw new IllegalArgumentException();
		}
		controllerz= controller;
	}

	/**
	 * Adds a distance sensor to the robot such that it measures in the given direction.
	 * This method is used when a robot is initially configured to get ready for operation.
	 * The point of view is that one mounts a sensor on the robot such that the robot
	 * can measure distances to obstacles or walls in that particular direction.
	 * For example, if one mounts a sensor in the forward direction, the robot can tell
	 * with the distance to a wall for its current forward direction, more technically,
	 * a method call distanceToObstacle(FORWARD) will return a corresponding distance.
	 * So a robot with a left and forward sensor will internally have 2 DistanceSensor
	 * objects at its disposal to calculate distances, one for the forward, one for the
	 * left direction.
	 * A robot can have at most four sensors in total, and at most one for any direction.
	 * If a robot already has a sensor for the given mounted direction, adding another
	 * sensor will replace/overwrite the current one for that direction with the new one.
	 * @param sensor is the distance sensor to be added
	 * @param mountedDirection is the direction that it points to relative to the robot's forward direction
	 */
	@Override
	public void addDistanceSensor(DistanceSensor sensor, Direction mountedDirection) {
		if(sensorarrays.size()!=4) {
			dasensor= (UnreliableSensor) sensor;
			dasensor.setSensorDirection(mountedDirection);
			dasensor.setMaze(controllerz.getMaze());
			for (int i=0; i<sensorarrays.size(); i++) {
				
				if (sensorarrays.get(i).dadirection==mountedDirection) {
					dasensor= (UnreliableSensor) sensor;
					dasensor.setSensorDirection(mountedDirection);
					sensorarrays.set(i,dasensor);
					return;
				}
			}
			sensorarrays.add(dasensor);
			return;
		}	
		for (int i=0; i<4; i++) {
			
			if (sensorarrays.get(i).dadirection==mountedDirection) {
				dasensor= (UnreliableSensor) sensor;
				dasensor.setSensorDirection(mountedDirection);
				sensorarrays.set(i,dasensor);
			}
			
		}
		
	}
		
	
	/**
	 * Provides the current position as (x,y) coordinates for 
	 * the maze as an array of length 2 with [x,y].
	 * @return array of length 2, x = array[0], y = array[1]
	 * and ({@code 0 <= x < width, 0 <= y < height}) of the maze
	 * @throws Exception if position is outside of the maze
	 */
	@Override
	public int[] getCurrentPosition() throws Exception {
		int[]rezz= new int[2];
		rezz=controllerz.getCurrentPosition();
		
		if (rezz[0] < 0 || rezz[0] >= controllerz.getMaze().getWidth()|| rezz[1] < 0 || rezz[1] >= controllerz.getMaze().getHeight()) {
			throw new Exception();
		}
		return rezz;
	}
	/**
	 * Provides the robot's current direction.
	 * @return cardinal direction is the robot's current direction in absolute terms
	 */	
	@Override
	public CardinalDirection getCurrentDirection() {
		
		return controllerz.getCurrentDirection();
	}
	/**
	 * Returns the current battery level.
	 * The robot has a given battery level (energy level) 
	 * that it draws energy from during operations. 
	 * The particular energy consumption is device dependent such that a call 
	 * for sensor distance2Obstacle may use less energy than a move forward operation.
	 * If battery {@code level <= 0} then robot stops to function and hasStopped() is true.
	 * @return current battery level, {@code level > 0} if operational. 
	 */
	@Override
	public float getBatteryLevel() {
		
		if(batterylevel>0) {
			return batterylevel;
		}
		return 0;
	}
	/**
	 * Sets the current battery level.
	 * The robot has a given battery level (energy level) 
	 * that it draws energy from during operations. 
	 * The particular energy consumption is device dependent such that a call 
	 * for distance2Obstacle may use less energy than a move forward operation.
	 * If battery {@code level <= 0} then robot stops to function and hasStopped() is true.
	 * @param level is the current battery level
	 * @throws IllegalArgumentException if level is negative 
	 */
	@Override
	public void setBatteryLevel(float level) {
		if (batterylevel<0) {
			throw new IllegalArgumentException(); 
			
		}
		batterylevel= level;
		batterylevelinit= level;
	}
	/**
	 * Gives the energy consumption for a full 360 degree rotation.
	 * Scaling by other degrees approximates the corresponding consumption. 
	 * @return energy for a full rotation
	 */
	@Override
	public float getEnergyForFullRotation() {
		return 12;
	}
	/**
	 * Gives the energy consumption for moving forward for a distance of 1 step.
	 * For simplicity, we assume that this equals the energy necessary 
	 * to move 1 step and that for moving a distance of n steps 
	 * takes n times the energy for a single step.
	 * @return energy for a single step forward
	 */
	@Override
	public float getEnergyForStepForward() {
		return 6;
	}

	/** 
	 * Gets the distance traveled by the robot.
	 * The robot has an odometer that calculates the distance the robot has moved.
	 * Whenever the robot moves forward, the distance 
	 * that it moves is added to the odometer counter.
	 * The odometer reading gives the path length if its setting is 0 at the start of the game.
	 * The counter can be reset to 0 with resetOdomoter().
	 * @return the distance traveled measured in single-cell steps forward
	 */
	@Override
	public int getOdometerReading() {
		return odometer;
	}

	/** 
     * Resets the odometer counter to zero.
     * The robot has an odometer that calculates the distance the robot has moved.
     * Whenever the robot moves forward, the distance 
     * that it moves is added to the odometer counter.
     * The odometer reading gives the path length if its setting is 0 at the start of the game.
     */
	@Override
	public void resetOdometer() {
		odometer=0;
	}
	/**
	 * Turn robot on the spot for amount of degrees. 
	 * If robot runs out of energy, it stops, 
	 * which can be checked by hasStopped() == true and by checking the battery level. 
	 * @param turn is the direction to turn and relative to current forward direction. 
	 */
	@Override
	public void rotate(Turn turn) {
		if(hasStopped() == false) {

			if (turn== Turn.LEFT) {

				if(batterylevel>=3) {
					controllerz.handleUserInput(Constants.UserInput.LEFT, 0);;
					batterylevel-=3;
				}

			}
			if (turn== Turn.RIGHT) {

				if(batterylevel>=3) {
					controllerz.handleUserInput(Constants.UserInput.RIGHT, 0);;
				batterylevel-=3;
				}

			}
			if (turn== Turn.AROUND) {

				if(batterylevel>=6) {
					controllerz.handleUserInput(Constants.UserInput.RIGHT, 0);;
					controllerz.handleUserInput(Constants.UserInput.RIGHT, 0);;
				batterylevel-=6;
				}

			}

		}
	}
	/**
	 * Moves robot forward a given number of steps. A step matches a single cell.
	 * If the robot runs out of energy somewhere on its way, it stops, 
	 * which can be checked by hasStopped() == true and by checking the battery level. 
	 * If the robot hits an obstacle like a wall, it remains at the position in front 
	 * of the obstacle and also hasStopped() == true as this is not supposed to happen.
	 * This is also helpful to recognize if the robot implementation and the actual maze
	 * do not share a consistent view on where walls are and where not.
	 * @param distance is the number of cells to move in the robot's current forward direction 
	 * @throws IllegalArgumentException if distance not positive
	 */
	@Override
	public void move(int distance) {
		if (distance<0) {
			throw new IllegalArgumentException();
		}
		int[] ye = null;
		try {
			ye = getCurrentPosition();
		} catch (Exception e) {
			
			e.printStackTrace();
			return;
		}
		int steps=0;
		Floorplan dafloor= controllerz.getMaze().getFloorplan();
		while(hasStopped() == false && distance!= steps) {
			if (!dafloor.hasWall(ye[0],ye[1], getCurrentDirection())){

				if(batterylevel>=6) {
					batterylevel-=6;
					odometer+=distance;
					
					controllerz.pathlengthy= odometer;
					controllerz.handleUserInput(Constants.UserInput.UP, 0);;
					

				}

			}
			steps++;
		}
	}
	/**
	 * Makes robot move in a forward direction even if there is a wall
	 * in front of it. In this sense, the robot jumps over the wall
	 * if necessary. The distance is always 1 step and the direction
	 * is always forward.
	 * If the robot runs out of energy somewhere on its way, it stops, 
	 * which can be checked by hasStopped() == true and by checking the battery level.
	 * If the robot tries to jump over an exterior wall and
	 * would land outside of the maze that way,  
	 * it remains at its current location and direction,
	 * hasStopped() == true as this is not supposed to happen.
	 */
	@Override
	public void jump() {
		int[] ye = null;
		try {
			ye = getCurrentPosition();
		} catch (Exception e) {

			e.printStackTrace();
			return;
		}
		CardinalDirection cod =getCurrentDirection();
		int [] mo=cod.getDxDyDirection();
		if(hasStopped() == false && batterylevel>40 && ye[0]+mo[0] >= 0 && ye[0]+mo[0] < controllerz.getMaze().getWidth() && ye[1]+mo[1] >= 0 && ye[1]+mo[1] < controllerz.getMaze().getHeight()) {
			
			if(batterylevel>=40) {
				controllerz.handleUserInput(Constants.UserInput.JUMP, 0);;
			batterylevel-=40;
			odometer+=1;
			}
			else {
				while(!hasStopped()){
					
					distanceToObstacle(Direction.FORWARD);
				}
				distanceToObstacle(Direction.FORWARD);
			}
		}
	}
	
	/**
	 * Tells if the current position is right at the exit but still inside the maze. 
	 * The exit can be in any direction. It is not guaranteed that 
	 * the robot is facing the exit in a forward direction.
	 * @return true if robot is at the exit, false otherwise
	 */
	@Override
	public boolean isAtExit() {
		int[] gif = null;
		try {
			gif = getCurrentPosition();
			
		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}
		int [] gof=controllerz.getMaze().getExitPosition();
		if (gif[0]== gof[0] && gif[1]==gof[1]) {
			return true;
		}
		return false;
	}
	
	/**
	 * Tells if current position is inside a room. 
	 * @return true if robot is inside a room, false otherwise
	 */	
	@Override
	public boolean isInsideRoom() {
		int[] gif = null;
		try {
			gif = getCurrentPosition();
		} catch (Exception e) {
			
			e.printStackTrace();
			return false;
		}
		if (controllerz.getMaze().isInRoom(gif[0],gif[1])) {
			return true;
		}
		return false;
	}

	/**
	 * Tells if the robot has stopped for reasons like lack of energy, 
	 * hitting an obstacle, etc.
	 * Once a robot is has stopped, it does not rotate or 
	 * move anymore.
	 * @return true if the robot has stopped, false otherwise
	 */
	@Override
	public boolean hasStopped() {
		if(batterylevel<=0) {
			
			
			for (int i=0; i<4; i++) {
				for (Direction j : Direction.values()) {
					if (sensorarrays.get(i).dadirection== j) {
						stopFailureAndRepairProcess(j);
					}
				}
			}
			
			
			return true;
		}
		return false;
	}

	/**
	 * Tells the distance to an obstacle (a wall) 
	 * in the given direction.
	 * The direction is relative to the robot's current forward direction.
	 * Distance is measured in the number of cells towards that obstacle, 
	 * e.g. 0 if the current cell has a wallboard in this direction, 
	 * 1 if it is one step forward before directly facing a wallboard,
	 * Integer.MaxValue if one looks through the exit into eternity.
	 * The robot uses its internal DistanceSensor objects for this and
	 * delegates the computation to the DistanceSensor which need
	 * to be installed by calling the addDistanceSensor() when configuring
	 * the robot.
	 * @param direction specifies the direction of interest
	 * @return number of steps towards obstacle if obstacle is visible 
	 * in a straight line of sight, Integer.MAX_VALUE otherwise
	 * @throws UnsupportedOperationException if robot has no sensor in this direction
	 * or the sensor exists but is currently not operational
	 */
	@Override
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		CardinalDirection cdss = getCurrentDirection();
		if (direction== Direction.LEFT) {
			cdss=cdss.rotateClockwise();
			
			
		}
		if (direction== Direction.BACKWARD) {
			cdss=cdss.rotateClockwise();
			cdss=cdss.rotateClockwise();
		}
		if (direction== Direction.RIGHT) {
			cdss=cdss.rotateClockwise();
			cdss=cdss.rotateClockwise();
			cdss=cdss.rotateClockwise();
		}
		float[] fl= new float[1];
		fl[0]= batterylevel;
		int num=-1;
		int sum=0;
		try {
		for (int i=0; i<4; i++) {
			
			if (sensorarrays.get(i).dadirection== direction) {
				num= i;
			}
		}
		
		
		
		if(num==-1) {
			throw new IllegalArgumentException("no valid sensors avaliable");
		}
		boolean soo= sensorarrays.get(num).Operational;
		if (!soo) {
			
			return -1;
		}
		sum= sensorarrays.get(num).distanceToObstacle(getCurrentPosition(),cdss,fl );
				}catch(Exception e){ throw new UnsupportedOperationException();
		}
		
		
		
		
		
		return sum;
		
	}

	
	/**
	 * Tells if a sensor can identify the exit in the given direction relative to 
	 * the robot's current forward direction from the current position.
	 * It is a convenience method is based on the distanceToObstacle() method and transforms
	 * its result into a boolean indicator.
	 * @param direction is the direction of the sensor
	 * @return true if the exit of the maze is visible in a straight line of sight
	 * @throws UnsupportedOperationException if robot has no sensor in this direction
	 * or the sensor exists but is currently not operational
	 */
	@Override
	public boolean canSeeThroughTheExitIntoEternity(Direction direction) throws UnsupportedOperationException {
		CardinalDirection cdss = getCurrentDirection();
		if (direction== Direction.LEFT) {
			cdss=cdss.rotateClockwise();
			
			
		}
		if (direction== Direction.BACKWARD) {
			cdss=cdss.rotateClockwise();
			cdss=cdss.rotateClockwise();
		}
		if (direction== Direction.RIGHT) {
			cdss=cdss.rotateClockwise();
			cdss=cdss.rotateClockwise();
			cdss=cdss.rotateClockwise();
		}
		float[] fl= new float[1];
		fl[0]= batterylevel;
		
		int num=-1;
		int sum=0;
		try {
		for (int i=0; i<4; i++) {
			
			if (sensorarrays.get(i).dadirection== direction) {
				num= i;
			}
		}
		
		
		
		
		if(num==-1) {
			throw new IllegalArgumentException("no valid sensors avaliable");
		}
		boolean soo= sensorarrays.get(num).Operational;
		if (!soo) {
			return false;
		}
		sum= sensorarrays.get(num).distanceToObstacle(getCurrentPosition(),cdss,fl );
				}catch(Exception e){ throw new UnsupportedOperationException();}
		
		
		
		
		if (sum==Integer.MAX_VALUE){
			return true;
		}
		return false;
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
	 * @param direction the direction the sensor is mounted on the robot
	 * @param meanTimeBetweenFailures is the mean time in seconds, must be greater than zero
	 * @param meanTimeToRepair is the mean time in seconds, must be greater than zero
	 * @throws UnsupportedOperationException if method not supported
	 */
	@Override
	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		
		int num=-1;
		
		try {
		for (int i=0; i<4; i++) {
			
			if (sensorarrays.get(i).dadirection== direction) {
				num= i;
			}
		}
		
		
		
		
		if(num==-1) {
			throw new IllegalArgumentException("no valid sensors avaliable");
		}
		
		
		sensorarrays.get(num).meanTimeBetweenFailures= meanTimeBetweenFailures;
		sensorarrays.get(num).meanTimeToRepair=meanTimeToRepair;
		sensorarrays.get(num).op.start();
		
				}catch(Exception e){ throw new UnsupportedOperationException();}
		
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
	 * @param direction the direction the sensor is mounted on the robot
	 * @throws UnsupportedOperationException if method not supported
	 */
	@Override
	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
		int num=-1;
		
		try {
		for (int i=0; i<4; i++) {
			
			if (sensorarrays.get(i).dadirection== direction) {
				num= i;
			}
		}
		
		
		
		
		if(num==-1) {
			throw new IllegalArgumentException("no valid sensors avaliable");
		}
		
		
		sensorarrays.get(num).stopFailureAndRepairProcess();
				}catch(Exception e){ throw new UnsupportedOperationException();}
		
	}
	
}


