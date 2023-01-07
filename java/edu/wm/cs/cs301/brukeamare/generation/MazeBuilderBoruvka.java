package edu.wm.cs.cs301.brukeamare.generation;

import java.util.logging.Logger;
import java.util.*;
import java.util.ArrayList;

public class MazeBuilderBoruvka extends MazeBuilder implements Runnable{
	
	private static final Logger LOGGER = Logger.getLogger(MazeBuilderBoruvka.class.getName());
	
	//storage for weights and random numbers
	private ArrayList<ArrayList<Integer>> edgestorageEast = new ArrayList<ArrayList<Integer>>();//double array list
	private ArrayList<ArrayList<Integer>> edgestorageSouth = new ArrayList<ArrayList<Integer>>();//double array list
	private ArrayList<Integer> randomnums= new ArrayList<Integer>();//double array list
	
	//dictionaries
	private static Hashtable<ArrayList<Integer>, Integer> cordinatepar = new Hashtable<ArrayList<Integer>, Integer>();//dictionary for coordinates to parent numbers
	private static Hashtable<Integer, ArrayList<Integer>> parentcor = new Hashtable<Integer, ArrayList<Integer>>();// dictionary for parent numbers to coordinates
	
	//forest array lists
	private ArrayList<ArrayList<Integer>> dav  = new ArrayList<ArrayList<Integer>>();//double array list
	private ArrayList<Integer> remaining  = new ArrayList<Integer>();//double array list
	private int internalwallscalc= (2*width*height)-width-height-((width*height)-1);
	
	// arrays
	private int [] parent;


	
	/***pseudo code:
	* get the floor plan and assessing each position a parent number 
	* assigning walls random weights
	* then random pick a parent and find the wall with least weight,
	* delete the wall board and make the adjacent cell a child of the initial cells tree
	*change the cells parent node to that of the tree, and remove the adjacent's parent tree number from the array list of remaining tree
	* then repeat this randomly through the parent node list, until you are left with one tree
	*/
	
	
	/** Constructor
	 * 
	 */
	public MazeBuilderBoruvka(){
		super();// supper class
		LOGGER.config("Using Boruvka's algorithm to generate maze.");
		
	}
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * This method generates pathways into the maze by using Boruvka's algorithm to generate a spanning tree.
	 * The cells are the nodes of the graph and the spanning tree. 
	 * An edge represents a weight for each wall that one can choose the cheapest wall to delete, which was priced randomly
	 * Every time we delete a wall we connect the two trees into one combined tree.
	 * Then connects the trees together until there is one tree left
	 */
	@Override
	protected void generatePathways() {
		




		GenerateForest();
		GenerateRandomWeights();
		
		//set every cells parent to themselves
		
		
		
		parent= new int[width*height];
		for( int i=0; i< width*height ; i++) {
			
			parent[i]=i;

		}
	
		int num=0;
		while(remaining.size()>1) {	
			
			System.out.println("try: "+num);
			num++;
			if (num>20) {
				break;
			}

			
			//get every cheapest wall for every tree and add to candidates
			
			for (ArrayList<Integer> i : dav) {
				ArrayList<Wallboard> tempcandidates= new ArrayList<Wallboard>();
				int cheapestedge=((width-1)*height)+((height-1)*width)+1;
				if(i.size()!=0) {
					for (Integer j: i) {
					
						int finx = parentcor.get(j).get(0);
						int finy = parentcor.get(j).get(1);
						
						Wallboard candidatedwall;
	
						if (Arethereavailablewalls(finx,finy)) {
							
								
								candidatedwall=getcheapestwall(finx,finy);
								
	
								if (cheapestedge>getEdgeWeight(finx,finy, candidatedwall.getDirection())) {
									
									tempcandidates.add(candidatedwall);
									if (tempcandidates.size()>1) {
										tempcandidates.remove(0);
									}
									
									cheapestedge=getEdgeWeight(finx,finy, candidatedwall.getDirection());
								}
							
		
						}
					}
					if (cheapestedge!=((width-1)*height)+((height-1)*width)+1) {

						floorplan.deleteWallboard(tempcandidates.get(0));	
						Mergedatrees(tempcandidates.get(0).getX(), tempcandidates.get(0).getY());
						tempcandidates.clear();

	
					}
					
				}
		}		
		}
		if (rooms == 0) {
			for( int i=1; i< width; i++) {
				for( int j=1; j< height ; j++) {
					if(floorplan.hasWall(i,j,CardinalDirection.North)){
						continue;
					}
					if(floorplan.hasWall(i,j-1,CardinalDirection.West)){
						continue;
					
					}
					if(floorplan.hasWall(i-1,j-1,CardinalDirection.South)){
						continue;
				
					}
					if(floorplan.hasWall(i-1,j,CardinalDirection.East)){
						continue;
				
					}
					Wallboard cat= new Wallboard(i,j,CardinalDirection.North);
					boolean dawg =true;
					floorplan.addWallboard(cat,dawg);
				}	
			}
			
			
		}
		

	}
	
	
	
	
	
	
	
	
	
	
	
	
	/**set up two dictionaries, one arrays and a double array list and an Array list of integers randomly assigned for edge weights
	 * 
	 */
	public void GenerateForest() {
		//dictionaries
		int num=0;
		for( int i=0; i< width ; i++) {
			for( int j=0; j< height ; j++) {
				ArrayList<Integer> arrl  = new ArrayList<Integer>();
				arrl.add(i);
				arrl.add(j);

				cordinatepar.put(arrl,num);
				num++;

			}
		}
		int mun=0;
		for( int i=0; i< width ; i++) {
			for( int j=0; j< height ; j++) {
				ArrayList<Integer> arrl  = new ArrayList<Integer>();
				
				arrl.add(i);
				arrl.add(j);

				parentcor.put(mun,arrl);

				mun++;

			}
		}
		//forest of trees
		for( int i=0; i< num ; i++) {
			ArrayList<Integer> arrl  = new ArrayList<Integer>();
			arrl.add(i);
			dav.add(arrl);
		}

		//array list of remaining trees
		for( int i=0; i<num ; i++) {
			remaining.add(i);
		}
		
		//set up for random numbers of weights

		int rem=((width-1)*height)+((height-1)*width);
		ArrayList<Integer> duuuu  = new ArrayList<Integer>();
		for( int i=0; i<rem ; i++) {
			duuuu.add(i);
		}
		
		while( rem>0) {
			int summer= random.nextIntWithinInterval(0, rem-1);

			int maaan=duuuu.get(summer);
			duuuu.remove(summer);
			rem--;
			randomnums.add(maaan);

			
		}

		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/** assigning random weights for each horizontal wall then each vertical wall
	 * 
	 */
	public void GenerateRandomWeights() {
		
		
		//South walls
		for( int i=0; i< width ; i++) {
			ArrayList<Integer> arrl  = new ArrayList<Integer>();
			for(int j=0; j<(height-1); j++) {

				int cop= randomnums.get(0);
				randomnums.remove(0);
				arrl.add(cop);
				
			}	
			edgestorageSouth.add(arrl);
		}
		//East walls
		for( int i=0; i< height ; i++) {
			ArrayList<Integer> arrl  = new ArrayList<Integer>();
			for(int j=0; j<(width-1); j++) {
				int cop= randomnums.get(0);
				randomnums.remove(0);
				arrl.add(cop);
				
			}	
			edgestorageEast.add(arrl);
			
			
		}

	}
	
	
	
	
	
	
	
	
	
	
	/** gets weights, by changing the cell and direction to get any wall, then calling the get east or get south function
	 * 
	 * @param x
	 * @param y
	 * @param cd
	 * @return integer
	 */
	public int getEdgeWeight(int x, int y, CardinalDirection cd) {
		
		if (cd== CardinalDirection.South) {

			return getSouth(x,y);
		}
		if (cd== CardinalDirection.West) {
			x=x-1;
			return getEast(x,y);
		}
		if (cd== CardinalDirection.East) {
			return getEast(x,y);
		}
		if (cd== CardinalDirection.North) {
			y=y-1;
			return getSouth(x,y);
		}
		
		return 0;
	}
	
	
	
	
	
	
	
	
	
	
	/**get South horizontal  wall weights
	 * 
	 * @param x
	 * @param y
	 * @return int
	 */
	public int getSouth(int x, int y) {
		
		
		return edgestorageSouth.get(x).get(y);
	}
	
	
	
	/** get east vertical wall weights
	 * 
	 * @param x
	 * @param y
	 * @return int
	 */
	public int getEast(int x, int y) {

		return edgestorageEast.get(y).get(x);
	}
	
	
	
	
	
	
	
	
	
	
	/**Checks if two adjacent cell in provided direction is of the same tree
	 * 
	 * @param x
	 * @param y
	 * @return true if the two adjacent cells are of the same tree
	 */
	public boolean AreTheyBrothers(int x, int y, CardinalDirection cd) {
		
		//reverse engineer coordinate and direction to get neighbor
		int ax=0;
		int ay=0;
		if(cd== CardinalDirection.East) {
			ax=x+1;
			ay=y;
		}
		if(cd== CardinalDirection.South) {
			ax=x;
			ay=y+1;
		}
		if(cd== CardinalDirection.West) {
			ax=x-1;
			ay=y;
		}
		if(cd== CardinalDirection.North) {
			ax=x;
			ay=y-1;
		}
		
		//find tree number
		ArrayList<Integer> BeforearrayCordinate= new ArrayList<Integer>();
		BeforearrayCordinate.add(x);
		BeforearrayCordinate.add(y);

		int BeforeTree1=cordinatepar.get(BeforearrayCordinate);


		
		ArrayList<Integer> AfterarrayCordinate= new ArrayList<Integer>();
		AfterarrayCordinate.add(ax);
		AfterarrayCordinate.add(ay);

		int AfterTree2=cordinatepar.get(AfterarrayCordinate);

		

		int BeforeParent=parent[BeforeTree1];
		int AfterParent=parent[AfterTree2];


		if(BeforeParent==AfterParent) {
			
			return true;
		}

		return false;

	}
	
	
	
	
	

	/** checks the provided cells wall in all directions and gets the cheapest wall that is not a border, and that exists.
	 * 
	 * 
	 * @param x
	 * @param y
	 * @return the cheapest wall board
	 */
	public Wallboard getcheapestwall(int x, int y) {
		
		Wallboard reusedWallboard = new Wallboard(x, y, CardinalDirection.East) ;
		Wallboard takehomeWallboard = new Wallboard(x, y, CardinalDirection.East);
		int edge1=((width-1)*height)+((height-1)*width)+1;
		
		for (CardinalDirection cd : CardinalDirection.values()) {
			reusedWallboard.setLocationDirection(x, y, cd);

			if (floorplan.hasWall(x,y,cd) && !floorplan.isPartOfBorder(reusedWallboard)){
				edge1= getEdgeWeight(x,y,cd );
				takehomeWallboard.setLocationDirection(x, y, reusedWallboard.getDirection());


				break;
			}
		}

		for (CardinalDirection cd : CardinalDirection.values()) {

			reusedWallboard.setLocationDirection(x, y, cd);
			if (floorplan.hasWall(x,y,cd) && !floorplan.isPartOfBorder(reusedWallboard)){
				if(edge1>getEdgeWeight(x,y,cd )) {
					edge1=getEdgeWeight(x,y,cd );

					takehomeWallboard.setLocationDirection(x, y, reusedWallboard.getDirection());


					
				}
			}

		}

		return takehomeWallboard;
		
	}
	
	

	
	/** checks all walls in a cell if there is at least one wall that is not a border and is not deleted
	 *  that also does not have an adjacent cell that is of the same tree
	 * 
	 * @param x coordinate integer
	 * @param y coordinate integer
	 * @return true if there are available walls
	 * @see #AreTheyBrothers(int,int,CardinalDirection)
	 */
	public boolean Arethereavailablewalls(int x, int y) {
		Wallboard reusedWallboard = new Wallboard(x, y, CardinalDirection.East) ;
		
		for (CardinalDirection cd : CardinalDirection.values()) {
			reusedWallboard.setLocationDirection(x, y, cd);
			if (floorplan.hasWall(x,y,cd) && !floorplan.isPartOfBorder(reusedWallboard)){

				if(AreTheyBrothers(x,y,cd)==false) {
					
					return true;
				}
			}
		}
		
		return false;
	}
	

	/** looks at a coordinate cell and checks if there is any torn down walls
	 * if there is, it connects the two cells trees if they are not already of the same tree,
	 * 
	 * this is down by changing every single parent node to that of the initial tree's parent node
	 * 
	 * @param i
	 * @param j
	 */
	public void Mergedatrees(int i,int j) {

		

				for (CardinalDirection cd : CardinalDirection.values()) {

					if (!floorplan.hasWall(i,j,cd)){
						

						if (!AreTheyBrothers(i,j,cd)){
	// if the cell is not the of the same parent then absorb tree, change rank parent.

							int ax=0;
							int ay=0;
							if(cd== CardinalDirection.East) {
								ax=i+1;
								ay=j;
							}
							if(cd== CardinalDirection.South) {
								ax=i;
								ay=j+1;
							}
							if(cd== CardinalDirection.West) {
								ax=i-1;
								ay=j;
							}
							if(cd== CardinalDirection.North) {
								ax=i;
								ay=j-1;
							}
							
			//find tree number using coordinate
							ArrayList<Integer> BeforearrayCordinate= new ArrayList<Integer>();
							BeforearrayCordinate.add(i);
							BeforearrayCordinate.add(j);
							int BeforeTree1=cordinatepar.get(BeforearrayCordinate);
							
							ArrayList<Integer> AfterarrayCordinate= new ArrayList<Integer>();
							AfterarrayCordinate.add(ax);
							AfterarrayCordinate.add(ay);
							int AfterTree2=cordinatepar.get(AfterarrayCordinate);
							
							//find its parents
							int BeforeParent=parent[BeforeTree1];
							int AfterParent=parent[AfterTree2];
							
							//find rank


							
			//compare ranks and decide which tree gets absorbed by the other
							

								for(int k=0;k<parent.length;k++) {
									//change the smaller ranks parent to bigger ranks parent
									if (parent[k]==AfterParent) {
										parent[k]=BeforeParent;

									}
								}
								//combine trees leafs to bigger ranked tree
								ArrayList<Integer> Beforelist1=dav.get(BeforeParent);
								ArrayList<Integer> Afterlist2=dav.get(AfterParent);
								

								
								for(int x:parent) {
									//change the smaller ranks parent to bigger ranks parent
									if (x==BeforeParent) {
										

									}
								}
								Beforelist1.addAll(Afterlist2);
								


								//remove the smaller ranked tree from remaining tree
			
								remaining.remove(Integer.valueOf(AfterParent));
								dav.get(AfterParent).clear();
								


						}
					}
				}
			
		
		
	}
	
}

