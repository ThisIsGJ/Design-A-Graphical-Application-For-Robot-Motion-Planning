import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class VisibilityGraph {
    
    public ArrayList<Line2D> createVisibilityGraph(ArrayList<Point> polygonNodes,ArrayList<Line2D> polygonLines,
    												ArrayList<Polygon> polygons)
    {
            ArrayList<Line2D> vizLines = new ArrayList<Line2D>();
                        
            //Collect the edges of each polygon;
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
                                 if(!polygonNodes.contains(intersectP) && intersectP != null){
                                	 intersects = true;
                                	 break;
                                 }
                         }
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
                        		 break;
                        	 }
                         }
                         //if this line does NOT intersect any grown polygon, add it to the visibility lines
                         if(!intersects){
                                 vizLines.add(tempLine);
                         }
            		}
            	}
            }
           
            vizLines.addAll(polygonLines);
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
	    return p;
	}
	
}
