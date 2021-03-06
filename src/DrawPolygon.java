import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;


public class DrawPolygon{
	
	  public ArrayList<Point> QuickHull (ArrayList<Point> points) {
	    ArrayList<Point> convexHull = new ArrayList<Point>();
	    int minPoint = -1, maxPoint = -1;
	    int minX = Integer.MAX_VALUE;
	    int maxX = Integer.MIN_VALUE;
	    
	    if (points.size() <= 2){
	    	return (ArrayList)points.clone();
	    }
	    
	    for (int i = 0; i < points.size(); i++) {
	      if (points.get(i).x < minX) {
	        minX = points.get(i).x;
	        minPoint = i;
	      } 
	      if (points.get(i).x > maxX) {
	        maxX = points.get(i).x;
	        maxPoint = i;       
	      }
	    }
	    
	    Point A = points.get(minPoint);
	    Point B = points.get(maxPoint);
		convexHull.add(A);
	    convexHull.add(B);
	    points.remove(A);
	    points.remove(B);
	    
	    ArrayList<Point> leftSet = new ArrayList<Point>();
	    ArrayList<Point> rightSet = new ArrayList<Point>();
	    
	    for (int i = 0; i < points.size(); i++) {
	      Point p = points.get(i);
	      if (pointLocation(A,B,p) == -1)
	        leftSet.add(p);
	      else
	        rightSet.add(p);
	    }
	    hullSet(A,B,rightSet,convexHull);
	    hullSet(B,A,leftSet,convexHull);
	    
	    return convexHull;
	  }
	  
	  private void hullSet(Point A, Point B, ArrayList<Point> set, ArrayList<Point> hull) {
	    int insertPosition = hull.indexOf(B);
	    if (set.size() == 0){
	    	return;
	    }
	    if (set.size() == 1) {
	      Point p = set.get(0);
	      set.remove(p);
	      hull.add(insertPosition,p);
	      return;
	    }
	    int dist = Integer.MIN_VALUE;
	    int furthest_Point = -1;
	    for (int i = 0; i < set.size(); i++) {
	      Point p = set.get(i);
	      int distance  = distance(A,B,p);
	      if (distance > dist) {
	        dist = distance;
	        furthest_Point = i;
	      }
	    }
	    Point P = set.get(furthest_Point);
	    set.remove(furthest_Point);
	    hull.add(insertPosition,P);
	    
	    ArrayList<Point> leftSetAP = new ArrayList<Point>();
	    for (int i = 0; i < set.size(); i++) {
	      Point M = set.get(i);
	      if (pointLocation(A,P,M)==1) {
	        leftSetAP.add(M);
	      }
	    }
	    
	    ArrayList<Point> leftSetPB = new ArrayList<Point>();
	    for (int i = 0; i < set.size(); i++) {
	      Point M = set.get(i);
	      if (pointLocation(P,B,M)==1) {
	        leftSetPB.add(M);
	      }
	    }
	    hullSet(A,P,leftSetAP,hull);
	    hullSet(P,B,leftSetPB,hull);
	}
	
	  private int pointLocation(Point A, Point B, Point P) {
		    int pointPosition = (B.x-A.x)*(P.y-A.y) - (B.y-A.y)*(P.x-A.x);
		    if(pointPosition > 0){
		    	return 1;
		    }else{
		    	return -1;
		    }
	  }
		  
	  
	  private int distance(Point A, Point B, Point C) {
		    int ABx = B.x-A.x;
		    int ABy = B.y-A.y;
		    int num = ABx*(A.y-C.y)-ABy*(A.x-C.x);
		    if (num < 0){
		    	num = -num;
		    }
		    return num;
	  }   
	  
	  
// get the grow points - circular robot
	  public ArrayList<Point> cgrowPolygon(ArrayList<Point> Points, int circleRobotR){
		  ArrayList<Point> growPoints = new ArrayList<Point>();
		  for(int i = 0; i < Points.size()-1;i++){
			  growPoints.add(cgetGrowPoint(Points.get(i),Points.get(i+1),circleRobotR,1)); 
			  growPoints.add(cgetGrowPoint(Points.get(i+1),Points.get(i),circleRobotR,-1));
		  }
		  growPoints.add(cgetGrowPoint(Points.get(Points.size()-1),Points.get(0),circleRobotR,1));
		  growPoints.add(cgetGrowPoint(Points.get(0),Points.get(Points.size()-1),circleRobotR,-1));
		  
		  return growPoints;
	  }
	  
	  private Point cgetGrowPoint(Point p1, Point p2,int r,int testSide){
		  Point growP = null;
		  Boolean Perpendicular;
		  int x1 = p1.x;
		  int x2 = p2.x;
		  int y1 = p1.y;
		  int y2 = p2.y;
		  //p1 - p2 distance
		  Double distance = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
		  double sin = Math.abs(x1-x2)/ distance;
		  double cos = Math.abs(y1-y2)/ distance;
		  
		  Double newX,newY;
		  newX = (x1-r*cos);
		  newY = (y1+r*sin);
		  
		  Perpendicular = Math.abs((newX - x2)*(newX - x2)+(newY - y2)*(newY - y2) -
				  (newX-x1)*(newX-x1) - (newY-y1)*(newY-y1) - (x2-x1)*(x2-x1)-(y2-y1)*(y2-y1)) <= 1;
		  // testSide used to test which side the point is, -1 is at right of the line, 1 is left of the line
		  //perpendicular is used to test if the the point line is perpendicular of the line
		  if(pointLocation(p1,p2,new Point(newX.intValue(),newY.intValue())) == testSide && Perpendicular)
			  growP = new Point(newX.intValue(),newY.intValue());
		  
		  newX = (x1+r*cos);
		  newY = (y1+r*sin);
		  Perpendicular = Math.abs((newX - x2)*(newX - x2)+(newY - y2)*(newY - y2) -
				  (newX-x1)*(newX-x1) - (newY-y1)*(newY-y1) - (x2-x1)*(x2-x1)-(y2-y1)*(y2-y1)) <= 1;
		  if(pointLocation(p1,p2,new Point(newX.intValue(),newY.intValue())) == testSide && Perpendicular)
			  growP = new Point(newX.intValue(),newY.intValue());
		  
		  newX = (x1+r*cos);
		  newY = (y1-r*sin);
		  Perpendicular = Math.abs((newX - x2)*(newX - x2)+(newY - y2)*(newY - y2) -
				  (newX-x1)*(newX-x1) - (newY-y1)*(newY-y1) - (x2-x1)*(x2-x1)-(y2-y1)*(y2-y1)) <= 1;
		  if(pointLocation(p1,p2,new Point(newX.intValue(),newY.intValue())) == testSide && Perpendicular)
			  growP = new Point(newX.intValue(),newY.intValue());
		  
		  newX = (x1-r*cos);
		  newY = (y1-r*sin);
		  Perpendicular = Math.abs((newX - x2)*(newX - x2)+(newY - y2)*(newY - y2) -
				  (newX-x1)*(newX-x1) - (newY-y1)*(newY-y1) - (x2-x1)*(x2-x1)-(y2-y1)*(y2-y1)) <= 1;
		  if(pointLocation(p1,p2,new Point(newX.intValue(),newY.intValue())) == testSide && Perpendicular) 
			  growP = new Point(newX.intValue(),newY.intValue());
		  
		  return growP;
	  }
	  
	  
	  
	  // get grow points for rectangular robot
	  public ArrayList<Point> rgrowPolygon(ArrayList<Point> Nodes, int width, int length, Polygon polygon){
		  int l = length;
		  int w = width;
		  int x,y;
		  ArrayList<Point> growPoints = new ArrayList<Point>();
		  
		  for (Point n : Nodes){
			  x = (int) n.getX();
			  y = (int) n.getY();
			  if(!polygon.intersects(x-l, y-w, l, w)){
				  growPoints.add(new Point((x-l/2),(y-w/2)));
			  }
			  if(!polygon.intersects(x-l, y, l, w)){
				  growPoints.add(new Point((x-l/2),(y+w/2)));
			  }
			  if(!polygon.intersects(x, y-w, l, w)){
				  growPoints.add(new Point((x+l/2),(y-w/2)));
			  }
			  if(!polygon.intersects(x, y, l, w)){
				  growPoints.add(new Point(x+l/2,(y+w/2)));
			  }
		  }
		  
		  return growPoints;
	  }
	  
}
