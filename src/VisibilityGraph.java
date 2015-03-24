import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class VisibilityGraph {
    
    public ArrayList<Line2D> createVisibilityGraph(ArrayList<Point> polygonNodes,ArrayList<Line2D> polygonLines,
    												ArrayList<Polygon> polygons)
    {
//    		Line2D ol1 = new Line2D.Double(); 
//    		Line2D ol2 = new Line2D.Double();
//    		ol1.setLine(new Point(300,300),new Point(600,200));
//    		ol2.setLine(new Point(500,500), new Point(500,300));
//    		System.out.println(intersection(ol1,ol2));
    		
            ArrayList<Line2D> vizLines = new ArrayList<Line2D>();
            for (int i = 0; i < polygonNodes.size(); i++){
            	for (int j = 0; j< polygonNodes.size(); j++){
            		if(i != j){
            			 Line2D.Double tempLine = new Line2D.Double(polygonNodes.get(i), polygonNodes.get(j));
            			 //check if this line intersects any of the grown polygon
            			 //step 1: if the intersect point is not the vertex of the polygon, then the line intersect the polygon  
                         boolean intersects = false;
                         for(Line2D polyLine : polygonLines)
                         {			
                                 Point intersectP = intersection(tempLine,polyLine);
//                                 System.out.println(intersectP +  " l: " + polyLine.getP1() + " " + polyLine.getP2());
                                 if(!polygonNodes.contains(intersectP) && intersectP != null){
                                	 intersects = true;
                                	 break;
                                 }
                         }
//                         System.out.println("1 i: " + i + "j: " + j + intersects + polygonNodes.get(i) + polygonNodes.get(j));
                         //if this line does NOT intersect any grown polygon, add it to the visibility lines
                         if(!intersects){
                        	 
                        	 // step 2: if the line's point1 and point2 is located at polygon vertex, then it is inside the polygon
                             // so set intersect true
                             for(Polygon polygon : polygons)
                             {
                            	 int[] points_x = polygon.xpoints;
                            	 int[] points_y = polygon.ypoints;
                            	 ArrayList<Point2D> testPoints = new ArrayList<Point2D>();
                            	 for(int pn = 0; pn < points_x.length;pn++){
                            		 Point2D testP = new Point(points_x[pn],points_y[pn]);
                            		 testPoints.add(testP);
                            	 }
                            	 
                            	 if(testPoints.contains(tempLine.getP1()) && testPoints.contains(tempLine.getP2())){
                            		 intersects = true;
                            		 for(Line2D l : polygonLines){
                            			 boolean b1 = l.getP1().equals(tempLine.getP1()) && l.getP2().equals(tempLine.getP2());
                            			 boolean b2 = l.getP1().equals(tempLine.getP2()) && l.getP2().equals(tempLine.getP1());
                            			 if(b1 || b2){
                            				 intersects = false;
                                    		 break;
                            			 }
                            		 }
                            	 }
                            	 // the points are two different polygons vertex(one polygon's vertex is insiede other polygons) 
                            	 if(polygon.contains(tempLine.getP1())){
                            		 if(!testPoints.contains(tempLine.getP1())){
                            			 intersects = true;
                            		 }
                            	 }else if(polygon.contains(tempLine.getP2())){
                            		 if(!testPoints.contains(tempLine.getP2())){
                            			 intersects = true;
                            		 }
                            	 }
                             }
//                             System.out.println("2 i: " + i + "j: " + j + intersects);
                         }
                         if(!intersects){vizLines.add(tempLine);}
            		}
            	}
            }
            return vizLines;
    }
    
    //get the intersect points between two lines
	public Point intersection(Line2D l1, Line2D l2) {
		int x1 = (int) l1.getX1();
		int x2 = (int) l1.getX2();
		int y1 = (int) l1.getY1();
		int y2 = (int) l1.getY2();
		
		int x3 = (int) l2.getX1();
		int x4 = (int) l2.getX2();
		int y3 = (int) l2.getY1();
		int y4 = (int) l2.getY2();
	    int distance = (x1-x2)*(y3-y4) - (y1-y2)*(x3-x4);
	    if (distance == 0) return null;
	    
	    int xi = ((x3-x4)*(x1*y2-y1*x2)-(x1-x2)*(x3*y4-y3*x4))/distance;
	    int yi = ((y3-y4)*(x1*y2-y1*x2)-(y1-y2)*(x3*y4-y3*x4))/distance;
	    
	    Point p = new Point(xi,yi);
	    if (xi < Math.min(x1,x2) || xi > Math.max(x1,x2)) return null;
	    if (xi < Math.min(x3,x4) || xi > Math.max(x3,x4)) return null;
	    
	    if (yi < Math.min(y1,y2) || yi > Math.max(y1,y2)) return null; 
	    if (yi < Math.min(y3,y4) || yi > Math.max(y3,y4)) return null;
	    
	    return p;
	}
	
//	public ArrayList<Point> collisionDetction(ArrayList<Polygon> polygons){
//		ArrayList<Polygon> testP = new ArrayList<Polygon>();
//		ArrayList<ArrayList<Polygon>> storeP = new ArrayList<ArrayList<Polygon>>();
//		Boolean test = false;
//		
//		testP.addAll(polygons);
//		for (int i = 0; i < polygons.size(); i++){
//			ArrayList<Polygon> midSP = new ArrayList<Polygon>();
//			testP.remove(polygons.get(i));
//			
//			for(ArrayList<Polygon> lp : storeP){
//				if(lp.contains(polygons.get(i))){
//					test = true;
//				}
//			}
//			
//			for (int j = 0;j < testP.size(); j++){
//				if(testIntersection(polygons.get(i),testP.get(j))){
//					if(!storeP.isEmpty()){
//						if(test){
//							for(ArrayList<Polygon> lp : storeP){
//								if(lp.contains(polygons.get(i))){
//									lp.add(testP.get(j));
//								}
//							}
//						}
//					}
//					if(!test){
//						midSP.add(testP.get(j));
//					}
//				}
//			}
//			
//			if(!test){
//				midSP.add(polygons.get(i));
//				storeP.add(midSP);
//			}
//			
//			test = false;
//		}
//		
//		// get new nodes after collision detection
//		ArrayList<Point> newNodes = new ArrayList<Point>();
//		ArrayList<Point> storeNodes = new ArrayList<Point>();
//		Polygon theP =  new Polygon();
//		for(int i = 0; i < storeP.size(); i++){
//			for(int j = 0; j < storeP.get(i).size(); j++){
//				theP = storeP.get(i).get(j);
//				for(int l = 0; l < theP.xpoints.length; l++){
//					newNodes.add(new Point(theP.xpoints[l],theP.ypoints[l]));
//				}
//			}
//			storeNodes.addAll(DP.QuickHull(newNodes));
//			newNodes.clear();
//		}
//		
//		return storeNodes;
//	}
//	
//	//test if two polygons are intersected
//	public static boolean testIntersection(Polygon p1, Polygon p2) {
//		Point p; 
//		for(int i = 0; i < p2.npoints;i++) {
//			p = new Point(p2.xpoints[i],p2.ypoints[i]);
//			if(p1.contains(p))
//				return true; 
//			} 
//		
//		for(int i = 0; i < p1.npoints;i++) {
//			p = new Point(p1.xpoints[i],p1.ypoints[i]); 
//			if(p2.contains(p)) 
//				return true; 
//			}
//		
//		return false; 
//	}
	
	
}
