import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;


public class FindShortestPath {

	 public ArrayList<Line2D> DijkstraAlgorithm(ArrayList<Line2D> vizGraph,Point2D start, Point2D goal){       
             //create the initial list of graph nodes and where they can reach
             ArrayList<GraphNode> nodes = new ArrayList<GraphNode>();
             for(Line2D l1 : vizGraph){
                     Point2D p1 = l1.getP1();
                     Point2D p2 = l1.getP2();
                     
                     //add the graph node for p1 if it has not already been created
                     createGraphNode(vizGraph, nodes, p1, start, goal);
                     
                     //add the graph node for p2 if it has not already been created
                     createGraphNode(vizGraph, nodes, p2, start, goal);
             }
                         
             //create the 2D array of infinity distances
             double[][] dijGraph = new double[nodes.size()][nodes.size()];
             for(int x=0; x<nodes.size(); x++){
                     for(int y=0; y<nodes.size(); y++)
                     {
                             dijGraph[x][y] = Double.MAX_VALUE;
                     }
             }
             
             //set up the starting parameters and exit conditions
             boolean goalFound = false;
             int startIndex = findStartEndNode(nodes, start);
             int endIndex = findStartEndNode(nodes, goal);
             GraphNode curNode = nodes.get(startIndex);
             dijGraph[startIndex][startIndex] = 0;
             curNode.distance = 0;
             
             //dijkstra's search loop:
             while(!goalFound){
                     for(GraphNode gn : nodes){
                             //iterate through the list of nodes and check which nodes are reachable by the original
                             if(curNode.reachables.contains(gn.point) && !gn.visited){
                                     if(curNode.index == gn.index)
                                             dijGraph[curNode.index][gn.index] = 0;
                                     else{
                                             double dist = curNode.point.distance(gn.point) + curNode.distance;
                                             if(dist < dijGraph[curNode.index][gn.index]){
                                                     dijGraph[curNode.index][gn.index] = dist;
                                                     if(dist < gn.distance){
                                                             gn.previous = curNode;
                                                             gn.distance = dist;
                                                     }
                                             }
                                     }
                             }
                     }
                     //set the current node to visited
                     curNode.visited = true;
                     if(curNode.isGoal)
                             goalFound = true;
                     
                     //find the next "current node" to iterate through
                     curNode = findNextCurNode(nodes);
                     
                     if(curNode == null)
                             goalFound = true;
             }
             
             //now that we've created the 2D array, go backwards from the goal to find the shortest path
             ArrayList<Line2D> shortestPath = new ArrayList<Line2D>();
             GraphNode cur = nodes.get(endIndex);
             boolean pathFound = false;
             while(!pathFound){
                     shortestPath.add(new Line2D.Double(cur.point, cur.previous.point));
                     cur = cur.previous;
                     if(cur.isStart)
                             pathFound = true;
             }
             Collections.reverse(shortestPath);
             
             return shortestPath;
     }
	
	
	 private void createGraphNode(ArrayList<Line2D> vizGraph,
             ArrayList<GraphNode> nodes, Point2D p1, Point2D start, Point2D goal) {
     
     //make sure that the list of graph nodes does not already contain p1 
		 if(!graphNodeContains(nodes, p1)){
		         ArrayList<Point2D> reachables = new ArrayList<Point2D>();
		         
		         //determine which points are reachable by p1
		         for(Line2D l2 : vizGraph){
		                 //if p1 on line2, then it must reach the other point
		                 if(p1.equals(l2.getP1()) && !reachables.contains(l2.getP2())){
		                	 reachables.add(l2.getP2());
		                 }
		                 if(p1.equals(l2.getP2()) && !reachables.contains(l2.getP1())){
	                         reachables.add(l2.getP1());
		                 }
		         }
		         
		         //check if p1 is the start or the goal
		         GraphNode gn = new GraphNode(p1, reachables, nodes.size());
		         if(gn.point.equals(start))
		                 gn.isStart = true;
		         
		         else if(gn.point.equals(goal))
		                 gn.isGoal = true;
		         
		         //add p1 to the list total list of graph nodes
		         nodes.add(gn);
		 }
	 }
	 
     private class GraphNode{
             public Point2D point;
             public ArrayList<Point2D> reachables;
             public GraphNode previous;
             public double distance;
             public int index = 0;
             public boolean visited;
             public boolean isStart;
             public boolean isGoal;
             
             public boolean equals(GraphNode gn){
                     return this.point.equals(gn.point);
             }
             
             //constructor
             public GraphNode(Point2D p, ArrayList<Point2D> r, int in){
                     point = p;
                     index = in;
                     previous = null;
                     distance = Double.MAX_VALUE;
                     visited = false;
                     isStart = false;
                     isGoal = false;
                     reachables = r;
             }
     }
     
     private boolean graphNodeContains(ArrayList<GraphNode> nodes, Point2D p){
             for(GraphNode n : nodes){
                     if(n.equals(p))
                             return true;
             }
             
             return false;
     }
     
     private int findStartEndNode(ArrayList<GraphNode> nodes, Point2D start_goal){
             int index = 0;
             for(GraphNode gn : nodes){
                     if(gn.point.equals(start_goal))
                             return index;
                     
                     index++;
             }
             
             return -1;
     }
     
     private GraphNode findNextCurNode(ArrayList<GraphNode> nodes){
             double smallestDist = Double.MAX_VALUE;
             GraphNode next = null;
             for(GraphNode gn : nodes){
                     if(!gn.visited && gn.distance < smallestDist)
                     {
                             next = gn;
                             smallestDist = gn.distance;
                     }
             }
             
             return next;
     }
	 
}
