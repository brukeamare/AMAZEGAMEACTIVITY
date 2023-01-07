/**
 * 
 */
package edu.wm.cs.cs301.brukeamare.generation;

import static edu.wm.cs.cs301.brukeamare.gui.Apptesting.tag;

import android.util.Log;

import edu.wm.cs.cs301.brukeamare.extra.Robot;
import edu.wm.cs.cs301.brukeamare.extra.Robot.Direction;
import edu.wm.cs.cs301.brukeamare.extra.Robot.Turn;
import edu.wm.cs.cs301.brukeamare.extra.RobotDriver;
import edu.wm.cs.cs301.brukeamare.extra.StatePlaying;


/**
 * Class Name: Wizard.java
 * 
 * Responsibilities:
 * setRobot
 * setMaze
 * drive2Exit
 * drive1Step2Exit
 * getEnergyConsumption
 * getPathLength
 * 
 * Collaborators:
 * RobotDriver.java
 * WallFollower.java
 * 
 * 
 * @author BRUKE AMARE
 *
 */
public class Wizard implements RobotDriver {
	/**
	 * Instance of robot drivers robot
	 */
	public Robot robocop;
	/**
	 * Instance of maze
	 */
	public Maze Wizmaze;
	public StatePlaying currentState;

	
	/**
	 * Constructor method
	 */
	public Wizard() {
		
	}
	/**
	 * Assigns a robot platform to the driver. 
	 * The driver uses a robot to perform, this method provides it with this necessary information.
	 * @param r robot to operate
	 */
	@Override
	public void setRobot(Robot r) {
		robocop=r;
		
	}
	
	
	/**
	 * Provides the robot driver with the maze information.
	 * Only some drivers such as the wizard rely on this information to find the exit.
	 * @param maze represents the maze, must be non-null and a fully functional maze object.
	 */
	@Override
	public void setMaze(Maze maze) {
		Wizmaze=maze;
		
	}
	
	
	/**
	 * Drives the robot towards the exit following
	 * its solution strategy and given the exit exists and  
	 * given the robot's energy supply lasts long enough. 
	 * When the robot reached the exit position and its forward
	 * direction points to the exit the search terminates and 
	 * the method returns true.
	 * If the robot failed due to lack of energy or crashed, the method
	 * throws an exception.
	 * If the method determines that it is not capable of finding the
	 * exit it returns false, for instance, if it determines it runs
	 * in a cycle and can't resolve this.
	 * @return true if driver successfully reaches the exit, false otherwise
	 * @throws Exception thrown if robot stopped due to some problem, e.g. lack of energy
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		try {
		while(((ReliableRobot)robocop).hasStopped()!=true) {
			Log.v("Wizard", "While loop running");
			if(!currentState.paused) {

				drive1Step2Exit();

			}
			if(currentState.stopped){
				return false;
			}
			
			
			if(((ReliableRobot)robocop).isAtExit()==true) {
				boolean gut =drive1Step2Exit();
				
				if (gut== true) {
					return true;
				}
				return false;
			}
			long time =(1000*(4- currentState.speed));
			Thread.sleep(time);
		}
		if(((ReliableRobot)robocop).hasStopped()==true) {
			throw new Exception("Robot Depleted Battery");
		}	
		}catch(Exception e) {throw new Exception("Robot stopped");}
		
		
		return false;
	}
	
	
	
	
	/**
	 * Drives the robot one step towards the exit following
	 * its solution strategy and given the exists and 
	 * given the robot's energy supply lasts long enough.
	 * It returns true if the driver successfully moved
	 * the robot from its current location to an adjacent
	 * location.
	 * At the exit position, it rotates the robot 
	 * such that if faces the exit in its forward direction
	 * and returns false. 
	 * If the robot failed due to lack of energy or crashed, the method
	 * throws an exception. 
	 * @return true if it moved the robot to an adjacent cell, false otherwise
	 * @throws Exception thrown if robot stopped due to some problem, e.g. lack of energy
	 */
	@Override
	public boolean drive1Step2Exit() throws Exception {
		try {
		int [] result= ((ReliableRobot)robocop).getCurrentPosition();
		if(((ReliableRobot)robocop).hasStopped()==true) {
			throw new Exception("Robot Depleted Battery");
		}
		while(((ReliableRobot)robocop).isAtExit()!=true) {
			
			

			int [] grit=Wizmaze.getNeighborCloserToExit(result[0],result[1]);
			result[0]= grit[0]-result[0];
			result[1]= grit[1]-result[1];
			CardinalDirection cef = CardinalDirection.getDirection(result[0], result[1]);
			if(((ReliableRobot)robocop).getCurrentDirection()!=cef) {
				int teg= ((ReliableRobot)robocop).getCurrentDirection().angle();
				int get = cef.angle();
				teg= teg-get;
				
				if ((teg== (90)) || (teg==(-270) )) {
					((ReliableRobot)robocop).rotate(Turn.RIGHT);
				}
				if ((teg== (-90)) || (teg==(270) )) {
					((ReliableRobot)robocop).rotate(Turn.LEFT);
				}
				if ((teg== (-180)) || (teg==(180) )) {
					((ReliableRobot)robocop).rotate(Turn.AROUND);
				}
			}	
			((ReliableRobot)robocop).move(1);
			
			int [] grot= ((ReliableRobot)robocop).getCurrentPosition();
			if(grot[0] == grit[0]&& grot[1] == grit[1]) {
				return true;
			}
			return false;
		}
		if(((ReliableRobot)robocop).canSeeThroughTheExitIntoEternity(Direction.FORWARD)==true) {
			((ReliableRobot)robocop).move(1);
			return true;
		}
		if(((ReliableRobot)robocop).canSeeThroughTheExitIntoEternity(Direction.FORWARD)!=true) {
			for (Direction i : Direction.values()) {
				boolean def = ((ReliableRobot)robocop).canSeeThroughTheExitIntoEternity(i);
				if (def== true) {
					if (i== Direction.LEFT) {
						((ReliableRobot)robocop).rotate(Turn.LEFT);
					}
					if (i== Direction.RIGHT) {
						((ReliableRobot)robocop).rotate(Turn.RIGHT);
					}
					if (i== Direction.BACKWARD) {
						((ReliableRobot)robocop).rotate(Turn.AROUND);
					}
					
					((ReliableRobot)robocop).move(1);
				
					return true;
				}
			}
		}
		}catch(Exception e) {throw new Exception("Robot stopped");}

		return false;

		
		
		
		
	}
	/**
	 * Returns the total energy consumption of the journey, i.e.,
	 * the difference between the robot's initial energy level at
	 * the starting position and its energy level at the exit position. 
	 * This is used as a measure of efficiency for a robot driver.
	 * @return the total energy consumption of the journey
	 */
	@Override
	public float getEnergyConsumption() {
		float rem= 3500- ((ReliableRobot)robocop).getBatteryLevel();
		return rem;
	}
	/**
	 * Returns the total length of the journey in number of cells traversed. 
	 * Being at the initial position counts as 0. 
	 * This is used as a measure of efficiency for a robot driver.
	 * @return the total length of the journey in number of cells traversed
	 */
	@Override
	public int getPathLength() {
		int path= robocop.getOdometerReading();
		return path;
	}
	
}

