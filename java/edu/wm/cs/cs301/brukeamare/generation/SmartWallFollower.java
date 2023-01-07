/**
 * 
 */
package edu.wm.cs.cs301.brukeamare.generation;

import static edu.wm.cs.cs301.brukeamare.gui.Apptesting.tag;

import android.util.Log;

import edu.wm.cs.cs301.brukeamare.extra.Robot;
import edu.wm.cs.cs301.brukeamare.extra.RobotDriver;
import edu.wm.cs.cs301.brukeamare.extra.Robot.Direction;
import edu.wm.cs.cs301.brukeamare.extra.Robot.Turn;
import edu.wm.cs.cs301.brukeamare.extra.StatePlaying;

/**
 * Class Name: SmartWallFollower.java
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

public class SmartWallFollower implements RobotDriver {

	/**
	 * Instance of robot drivers robot
	 */
	public Robot robocop;
	/**
	 * Instance of maze
	 */
	public Maze Wizmaze;
	/*
	 * boolean for if it is in loop of not
	 */
	public boolean isinloop=false;
	public StatePlaying currentState;

	/*
	 * array keeps track of previous position that robot was in
	 */
	public int [] rsult= new int[2];
	/*
	 * 2d array to keep track of how many times cells in rooms is visited
	 */
	public int [] [] roomz;	
	/**
	 * Constructor method
	 */
	public SmartWallFollower() {

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
		int [][] daroom= new int[Wizmaze.getWidth()][Wizmaze.getHeight()];
		roomz= daroom;
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
	 * exit, for instance, if it determines it runs
	 * in a cycle and can't resolve this it changes it course by making a right next time 
	 * that it is has a wall in front of it when it is outside rooms..
	 * @return true if driver successfully reaches the exit, false otherwise
	 * @throws Exception thrown if robot stopped due to some problem, e.g. lack of energy
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		try {

		

		while(!((UnreliableRobot)robocop).hasStopped()) {
			if(!currentState.paused){
				drive1Step2Exit();
			}

			
			
			if(((UnreliableRobot)robocop).isAtExit()==true) {
				boolean gut =drive1Step2Exit();
				
				if (gut== true) {
					
					return true;
				}
				return false;
			}
			Thread.sleep((long)(1000*(4- currentState.speed)));
		}
		if(((UnreliableRobot)robocop).hasStopped()==true) {
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
	 * yet if sensors crash it another sensor by turning and using the appropriate sensor.
	 * @return true if it moved the robot to an adjacent cell, false otherwise
	 * @throws Exception thrown if robot stopped due to some problem, e.g. lack of energy
	 */
	@Override
	public boolean drive1Step2Exit() throws Exception {
		try {
		
		
			
			if(((UnreliableRobot)robocop).hasStopped()==true) {
					throw new Exception("Robot Depleted Battery");
			}
		/*
		 * code for getting out of loop starts here, it activates inloopmode
		 * where it will turn right nect time the robot has a wall in front 
		 * of it when its outside of the room
		 */

			int[] fe= ((UnreliableRobot)robocop).getCurrentPosition();
			if(robocop.isInsideRoom()&& !(fe[0]== rsult[0]&&fe[1]== rsult[1]) ) {

				roomz[fe[0]][fe[1]]+=1;

				if(roomz[fe[0]][fe[1]]>2) {
					isinloop=true;
					/*
					 * this if statment contains code as a back up if 
					 * the initial attemtp to get out the loop fails
					 * it uses getneighbor closest to exit to get the robot out 
					 * the loop then goes back to regular wallfollower protocol
					 */
					if(roomz[fe[0]][fe[1]]>4) {	
					
					
					
					int boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.LEFT);
					if (boo==-1) {
						while(boo==-1) {
							((UnreliableRobot)robocop).rotate(Turn.LEFT);
							boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.FORWARD);
							if(boo==-1) {
								((UnreliableRobot)robocop).rotate(Turn.LEFT);
								boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.RIGHT);
								if (boo==-1) {
									((UnreliableRobot)robocop).rotate(Turn.LEFT);
									boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.BACKWARD);
									if (boo==-1) {
										((UnreliableRobot)robocop).rotate(Turn.LEFT);
										boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.LEFT);
										continue;
									}
									((UnreliableRobot)robocop).rotate(Turn.LEFT);
									continue;
								}
								((UnreliableRobot)robocop).rotate(Turn.AROUND);
								continue;
							}
							((UnreliableRobot)robocop).rotate(Turn.RIGHT);
							}
						
					}
					
					
					
					
					
					
					
					
					while(!(boo==0 &&!robocop.isInsideRoom()) ) {
						int[] fo= ((UnreliableRobot)robocop).getCurrentPosition();
						int [] grit=Wizmaze.getNeighborCloserToExit(fo[0],fo[1]);
						fo[0]= grit[0]-fo[0];
						fo[1]= grit[1]-fo[1];
						CardinalDirection cef = CardinalDirection.getDirection(fo[0], fo[1]);
						if(((UnreliableRobot)robocop).getCurrentDirection()!=cef) {
							int teg= ((UnreliableRobot)robocop).getCurrentDirection().angle();
							int get = cef.angle();
							teg= teg-get;
							
							if ((teg== (90)) || (teg==(-270) )) {
								((UnreliableRobot)robocop).rotate(Turn.RIGHT);
							}
							if ((teg== (-90)) || (teg==(270) )) {
								((UnreliableRobot)robocop).rotate(Turn.LEFT);
							}
							if ((teg== (-180)) || (teg==(180) )) {
								((UnreliableRobot)robocop).rotate(Turn.AROUND);
							}
						}	
						rsult= ((UnreliableRobot)robocop).getCurrentPosition();
						((UnreliableRobot)robocop).move(1);
						
						
						
						
						
						
						boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.LEFT);
						
						if (boo==-1) {
							while(boo==-1) {
								((UnreliableRobot)robocop).rotate(Turn.LEFT);
								boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.FORWARD);
								if(boo==-1) {
									((UnreliableRobot)robocop).rotate(Turn.LEFT);
									boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.RIGHT);
									if (boo==-1) {
										((UnreliableRobot)robocop).rotate(Turn.LEFT);
										boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.BACKWARD);
										if (boo==-1) {
											((UnreliableRobot)robocop).rotate(Turn.LEFT);
											boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.LEFT);
											continue;
										}
										((UnreliableRobot)robocop).rotate(Turn.LEFT);
										continue;
									}
									((UnreliableRobot)robocop).rotate(Turn.AROUND);
									continue;
								}
								((UnreliableRobot)robocop).rotate(Turn.RIGHT);
								}
							}
					
					}
					isinloop=false;
					return true;
						
					}
				}
				
				
			}
			
			if(isinloop) {
				Floorplan dafloor= Wizmaze.getFloorplan();

				
				if(dafloor.hasWall(fe[0],fe[1], ((UnreliableRobot)robocop).getCurrentDirection()) && !robocop.isInsideRoom()){
					((UnreliableRobot)robocop).rotate(Turn.RIGHT);
					isinloop=false;
				}
			}
			
			/*
			 * code for getting out of the loop ends here
			 */
			
			while(((UnreliableRobot)robocop).isAtExit()!=true) {
				
				
				
				
				
				
				
				int boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.LEFT);
				if (boo==-1) {
				while(boo==-1) {
					((UnreliableRobot)robocop).rotate(Turn.LEFT);
					boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.FORWARD);
					if(boo==-1) {
						((UnreliableRobot)robocop).rotate(Turn.LEFT);
						boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.RIGHT);
						if (boo==-1) {
							((UnreliableRobot)robocop).rotate(Turn.LEFT);
							boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.BACKWARD);
							if (boo==-1) {
								((UnreliableRobot)robocop).rotate(Turn.LEFT);
								boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.LEFT);
								continue;
							}
							((UnreliableRobot)robocop).rotate(Turn.LEFT);
							continue;
						}
						((UnreliableRobot)robocop).rotate(Turn.AROUND);
						continue;
					}
					((UnreliableRobot)robocop).rotate(Turn.RIGHT);
					}
				}
				
				if(boo!=0){
					((UnreliableRobot)robocop).rotate(Turn.LEFT);
					
				}
				
				
				
				
				
				
				
				
				
				int bea=((UnreliableRobot)robocop).distanceToObstacle(Direction.FORWARD);
				if (bea==-1) {
				while(bea==-1) {
					((UnreliableRobot)robocop).rotate(Turn.LEFT);
					bea=((UnreliableRobot)robocop).distanceToObstacle(Direction.RIGHT);
					if(bea==-1) {
						((UnreliableRobot)robocop).rotate(Turn.LEFT);
						bea=((UnreliableRobot)robocop).distanceToObstacle(Direction.BACKWARD);
						if (bea==-1) {
							((UnreliableRobot)robocop).rotate(Turn.LEFT);
							bea=((UnreliableRobot)robocop).distanceToObstacle(Direction.LEFT);
							if (bea==-1) {
								((UnreliableRobot)robocop).rotate(Turn.LEFT);
								bea=((UnreliableRobot)robocop).distanceToObstacle(Direction.FORWARD);
								continue;
							}
							((UnreliableRobot)robocop).rotate(Turn.LEFT);
							continue;
						}
						((UnreliableRobot)robocop).rotate(Turn.AROUND);
						continue;
					}
					((UnreliableRobot)robocop).rotate(Turn.RIGHT);
					}

				}
				if(bea!=0) {
					
					rsult= ((UnreliableRobot)robocop).getCurrentPosition();
					((UnreliableRobot)robocop).move(1);
					
					return true;
				}
				rsult= ((UnreliableRobot)robocop).getCurrentPosition();
				((UnreliableRobot)robocop).rotate(Turn.RIGHT);
				return true;
			}
	
			
			
			
			
			
			
			int exit=((UnreliableRobot)robocop).distanceToObstacle(Direction.FORWARD);
			if (exit==-1) {
			while(exit==-1) {
				((UnreliableRobot)robocop).rotate(Turn.LEFT);
				exit=((UnreliableRobot)robocop).distanceToObstacle(Direction.RIGHT);
				if(exit==-1) {
					((UnreliableRobot)robocop).rotate(Turn.LEFT);
					exit=((UnreliableRobot)robocop).distanceToObstacle(Direction.BACKWARD);
					if (exit==-1) {
						((UnreliableRobot)robocop).rotate(Turn.LEFT);
						exit=((UnreliableRobot)robocop).distanceToObstacle(Direction.LEFT);
						if (exit==-1) {
							((UnreliableRobot)robocop).rotate(Turn.LEFT);
							exit=((UnreliableRobot)robocop).distanceToObstacle(Direction.FORWARD);
							continue;
						}
						((UnreliableRobot)robocop).rotate(Turn.LEFT);
						continue;
					}
					((UnreliableRobot)robocop).rotate(Turn.AROUND);
					continue;
				}
				((UnreliableRobot)robocop).rotate(Turn.RIGHT);
				}
			}
			
			
			
			
			
			if(exit==Integer.MAX_VALUE) {
				((UnreliableRobot)robocop).move(1);
				return true;
			}
			if(exit!=Integer.MAX_VALUE) {

				int boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.LEFT);
				
				if (boo==-1) {
				while(boo==-1) {
					((UnreliableRobot)robocop).rotate(Turn.LEFT);
					boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.FORWARD);
					if(boo==-1) {
						((UnreliableRobot)robocop).rotate(Turn.LEFT);
						boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.RIGHT);
						if (boo==-1) {
							((UnreliableRobot)robocop).rotate(Turn.LEFT);
							boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.BACKWARD);
							if (boo==-1) {
								((UnreliableRobot)robocop).rotate(Turn.LEFT);
								boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.LEFT);
								continue;
							}
							((UnreliableRobot)robocop).rotate(Turn.LEFT);
							continue;
						}
						((UnreliableRobot)robocop).rotate(Turn.AROUND);
						continue;
					}
					((UnreliableRobot)robocop).rotate(Turn.RIGHT);
					}
				}
				if (boo== Integer.MAX_VALUE) {
					((UnreliableRobot)robocop).rotate(Turn.LEFT);
					((UnreliableRobot)robocop).move(1);
					return true;
				}
				
				
				boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.RIGHT);
				
				if (boo==-1) {
				while(boo==-1) {
					((UnreliableRobot)robocop).rotate(Turn.LEFT);
					boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.BACKWARD);
					if(boo==-1) {
						((UnreliableRobot)robocop).rotate(Turn.LEFT);
						boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.LEFT);
						if (boo==-1) {
							((UnreliableRobot)robocop).rotate(Turn.LEFT);
							boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.FORWARD);
							if (boo==-1) {
								((UnreliableRobot)robocop).rotate(Turn.LEFT);
								boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.RIGHT);
								continue;
							}
							((UnreliableRobot)robocop).rotate(Turn.LEFT);
							continue;
						}
						((UnreliableRobot)robocop).rotate(Turn.AROUND);
						continue;
					}
					((UnreliableRobot)robocop).rotate(Turn.RIGHT);
					}
				}
				if (boo== Integer.MAX_VALUE) {
					((UnreliableRobot)robocop).rotate(Turn.RIGHT);
					((UnreliableRobot)robocop).move(1);
					return true;
				}
				
				
				
				
				boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.BACKWARD);
				
				if (boo==-1) {
				while(boo==-1) {
					((UnreliableRobot)robocop).rotate(Turn.LEFT);
					boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.LEFT);
					if(boo==-1) {
						((UnreliableRobot)robocop).rotate(Turn.LEFT);
						boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.FORWARD);
						if (boo==-1) {
							((UnreliableRobot)robocop).rotate(Turn.LEFT);
							boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.RIGHT);
							if (boo==-1) {
								((UnreliableRobot)robocop).rotate(Turn.LEFT);
								boo=((UnreliableRobot)robocop).distanceToObstacle(Direction.BACKWARD);
								continue;
							}
							((UnreliableRobot)robocop).rotate(Turn.LEFT);
							continue;
						}
						((UnreliableRobot)robocop).rotate(Turn.AROUND);
						continue;
					}
					((UnreliableRobot)robocop).rotate(Turn.RIGHT);
					}
				}
				if (boo== Integer.MAX_VALUE) {
					((UnreliableRobot)robocop).rotate(Turn.AROUND);
					((UnreliableRobot)robocop).move(1);
					return true;
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
		float rem= 3500- ((UnreliableRobot)robocop).getBatteryLevel();
		return rem;
	}
	/**
	 * Returns the total length of the journey in number of cells traversed. 
	 * Being at the initial position counts as 0. 
	 * System
	 * This is used as a measure of efficiency for a robot driver.
	 * @return the total length of the journey in number of cells traversed
	 */
	@Override
	public int getPathLength() {
		int path= robocop.getOdometerReading();
		return path;
	}
	
}



