import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

public class UserInterface implements ActionListener{
	
	private DrawPolygon DP;
	private VisibilityGraph VG;
	private FindShortestPath SP;
	
	private JFrame frame;
	private Container contentPanel;
	private JPanel buttonPanel;
	private DrawCanvas robotPanel;
	private ArrayList<Point> pointClicked;
	private ArrayList<Point> growpointClicked;
	
	private Boolean setStart;
	private Boolean setEnd;
	private Point startPoint;
	private Point endPoint;
	
	private ArrayList<Polygon> polygons;
	private ArrayList<Polygon> growpolygons;
	private ArrayList<Point> polygonNodes;
	private ArrayList<Point> growNodes;
	private ArrayList<Line2D> polygonLines;
	private ArrayList<Line2D> growLines;
	private ArrayList<Line2D> visibilityLines;
	private ArrayList<Line2D> growvisibilityLines;
	private boolean setVisibility;
	private boolean showShortestPath;
	private ArrayList<Line2D> shortestPath;
	private ArrayList<Line2D> growshortestPath;
	
	private boolean circleRobot = false;
	private boolean pointRobot = true;
	//the radius of circle robot 
	private final int circleRobotR = 30;
	
	public UserInterface(){
		frame = new JFrame("Robot Motion");
		contentPanel = frame.getContentPane(); 
		buttonPanel = new JPanel();
		robotPanel = new DrawCanvas();
		pointClicked = new ArrayList<Point>();
		growpointClicked = new ArrayList<Point>();
		setStart = false;
		setEnd = false;
		startPoint = new Point(-1,-1);
		endPoint = new Point(-1,-1);
		setVisibility = false;
		showShortestPath = false;
		
		MakeFrame();
		
		DP = new DrawPolygon();
		VG = new VisibilityGraph();
		SP = new FindShortestPath();
		polygons = new ArrayList<Polygon>(); 
		growpolygons = new ArrayList<Polygon>(); 
		polygonNodes = new ArrayList<Point>();
		polygonLines = new ArrayList<Line2D>();
		growNodes = new ArrayList<Point>();
		polygonLines = new ArrayList<Line2D>();
		growLines = new ArrayList<Line2D>();
		visibilityLines = new ArrayList<Line2D>();
		growvisibilityLines = new  ArrayList<Line2D>();
		shortestPath = new ArrayList<Line2D>();
		growshortestPath = new ArrayList<Line2D>();
	}
	
	private void MakeFrame(){
		contentPanel.setLayout(new BorderLayout());
		
		//build buttonPanel
		buttonPanel.setLayout(new GridLayout(15,1));
		addButton(buttonPanel,"Point Robot");
		addButton(buttonPanel,"Circle Robot");
		addButton(buttonPanel,"Start Point");
		addButton(buttonPanel,"End Point");
		addButton(buttonPanel,"<html>Draw<br />Abstacles</html>");
		addButton(buttonPanel,"<html>Visibility<br />Graph</html>");
		addButton(buttonPanel,"<html>Shortest Path</html>");
		addButton(buttonPanel,"Clean All");
		addButton(buttonPanel,"Start");
		
		buttonPanel.setBorder(new EtchedBorder());
		contentPanel.add(buttonPanel, BorderLayout.WEST);	
		
		robotPanel.setBorder(new EtchedBorder());
		contentPanel.add(robotPanel,BorderLayout.CENTER);
		
		frame.setSize(1200,800);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}
	
	private void addButton(Container p, String button){
		JButton btn = new JButton(button);
        btn.setPreferredSize(new Dimension(100, 30));
		
		btn.addActionListener(this);
		btn.setFocusable(false);
		p.add(btn);
	}
	
	public void actionPerformed(ActionEvent e) {
		String choice = e.getActionCommand();
		//draw convex hull
		if(choice == "<html>Draw<br />Abstacles</html>"){
			drawShape();
		}else if(choice == "Start Point"){
			setStart = true;
			setEnd = false;
		}else if(choice == "End Point"){
			setEnd = true;
			setStart = false;
		}else if(choice == "<html>Shortest Path</html>"){
			if(showShortestPath)showShortestPath = false;
					else showShortestPath = true;
			robotPanel.repaint();
		}else if(choice == "Clean All"){
			cleanAll();
		}else if(choice == "<html>Visibility<br />Graph</html>"){
			showVG();
		}else if(choice == "Point Robot"){
			circleRobot = false;
			pointRobot = true;
		}else if(choice == "Circle Robot"){
			circleRobot = true;
			pointRobot = false;
		}
		robotPanel.repaint();
	}
	
	private void drawShape(){
		
		//for point circle
		pointClicked = DP.QuickHull(pointClicked);
		polygons.add(creatPolygon(pointClicked,polygonLines,polygonNodes));   
		
		//for circle robot - grow the nodes
		growpointClicked = DP.growPolygon(pointClicked,circleRobotR);
		//get growing polygon
		growpolygons.add(creatPolygon(growpointClicked,growLines,growNodes));
		
		pointClicked.clear();
		growpointClicked.clear();
//		robotPanel.repaint();
	}
	
	
	private Polygon creatPolygon(ArrayList<Point> points, ArrayList<Line2D> lines,ArrayList<Point> nodes){
		int xPoly[] = new int[30];
		int yPoly[] = new int[30];
		for (int i = 0; i < points.size(); i++){
			//used to draw polygons
			 xPoly[i] = points.get(i).x;
			 yPoly[i] = points.get(i).y;
			 //used to draw visibility graph
			 nodes.add(points.get(i));
			 //collect the edges of each polygons
			 if(i == points.size()-1){
				 Line2D line = new Line2D.Double();
				 line.setLine(points.get(i),points.get(0));
				 lines.add(line);
			 }else{
				 Line2D line = new Line2D.Double();
				 line.setLine(points.get(i),points.get(i+1));
				 lines.add(line);
			 }
		}
		
		return new Polygon(xPoly,yPoly,points.size());
	}
	
	
	
	private void cleanAll(){
		setStart = false;
		setEnd = false;
		startPoint.x = -1;
		startPoint.y = -1;
		endPoint.y = -1;
		endPoint.x = -1;
		pointClicked.clear();
		polygons.clear();
		visibilityLines.clear();
		polygonNodes.clear();
		polygonLines.clear();
		shortestPath.clear();
		
		growpointClicked.clear();
		growpolygons.clear();
		growvisibilityLines.clear();
		growNodes.clear();
		growLines.clear();
		growshortestPath.clear();
		
//		robotPanel.repaint();
	}
	
	private void showVG(){
		//show visibility graph
		setVisibility = true;
		if(startPoint.x == -1 ){
			JOptionPane.showMessageDialog(frame, "Please set the Start point.");
			setVisibility = false;
		}else if(endPoint.x == -1){
			JOptionPane.showMessageDialog(frame, "Please set the End point.");
			setVisibility = false;
		}
		
		// for point robot
		polygonNodes.add(0, startPoint);
        polygonNodes.add(endPoint);
        visibilityLines = VG.createVisibilityGraph(polygonNodes,polygonLines,polygons);
        shortestPath = SP.DijkstraAlgorithm(visibilityLines,startPoint,endPoint);
        
        //for circle robot
        growNodes.add(0, startPoint);
	    growNodes.add(endPoint);
        growvisibilityLines = VG.createVisibilityGraph(growNodes,growLines,growpolygons);
        growshortestPath = SP.DijkstraAlgorithm(growvisibilityLines, startPoint, endPoint);
        
		//find the shortestPath
		
		
//		robotPanel.repaint();
	}
	
	class DrawCanvas extends JPanel{
		private static final long serialVersionUID = 1L;
		
		public DrawCanvas(){
			this.addMouseListener(new MouseAdapter() {
	             public void mouseClicked(MouseEvent e) {
	            	 if(setStart == true){
	    	        	 startPoint.x = e.getPoint().x;
	    	        	 startPoint.y = e.getPoint().y;
	    	         }else if(setEnd == true){
	    	        	 endPoint.x = e.getPoint().x;
	    	        	 endPoint.y = e.getPoint().y;
	    	         }else {
	    	        	 pointClicked.add(e.getPoint());
	    	         }    
	            	 
	                 repaint();
	             }
	         });	
		} 
		
		@Override
	      public void paintComponent(Graphics g) {
	         super.paintComponent(g);
	         
	         //draw start point
	         if(startPoint.x >= 0 || startPoint.y >= 0 ){
	        	 String message_start = "Start";
	        	 g.setColor(Color.RED);
	        	
	        	 
	        	 if(circleRobot){
		        	 g.fillOval(startPoint.x-circleRobotR, startPoint.y-circleRobotR, circleRobotR*2, circleRobotR*2);
		        	 g.drawString(message_start,startPoint.x-60,startPoint.y-15);
		         }else{
		        	 g.fillRect(startPoint.x-5,startPoint.y-5,10,10);
		        	 g.drawString(message_start,startPoint.x-40,startPoint.y+12);
		         }
	        	 setStart = false;
	         }
	         
	         //draw end point
	         if(endPoint.x >= 0 || endPoint.y >= 0){
	        	 String message_end = "End";
	        	 g.setColor(Color.RED);
	        	 g.drawString(message_end, endPoint.x+12, endPoint.y+12);
	        	 g.fillRect(endPoint.x-5, endPoint.y-5, 10, 10);
	        	 setEnd = false;
	         }
	         
	         //store the clicked point to draw a polygon
	         if(pointClicked.size() != 0){
	        	 g.setColor(Color.BLACK);
	        	 for (int i = 0; i < pointClicked.size(); i++){
	        		 g.fillRect(pointClicked.get(i).x-2, pointClicked.get(i).y-2, 4, 4);
	        	 }    
	         }
	         
	         //draw visibility lines
	         if(setVisibility){
	        	 if(pointRobot){
	        		 for (Line2D l : visibilityLines){
		        		 g.setColor(Color.BLACK);
		        		 g.drawLine((int)l.getX1(),(int)l.getY1(),(int)l.getX2(),  (int)l.getY2());
		        	 }
	        	 }else if(circleRobot){
	        		 for (Line2D l : growvisibilityLines){
		        		 g.setColor(Color.BLACK);
		        		 g.drawLine((int)l.getX1(),(int)l.getY1(),(int)l.getX2(),  (int)l.getY2());
		        	 }
	        	 }
	         }

	         //show shortest point
	         if(pointRobot){
	        	 if(showShortestPath && shortestPath.size() != 0){
		        	 for (Line2D l : shortestPath){
		        		 g.setColor(Color.BLUE);
		        		 g.drawLine((int)l.getX1(),(int)l.getY1(),(int)l.getX2(),  (int)l.getY2());
		        	 }
		         }
	         }else if(circleRobot){
	        	 if(showShortestPath && growshortestPath.size() != 0){
		        	 for (Line2D l : growshortestPath){
		        		 g.setColor(Color.BLUE);
		        		 g.drawLine((int)l.getX1(),(int)l.getY1(),(int)l.getX2(),  (int)l.getY2());
		        	 }
		         }
	         }
	         
	         
	         if(circleRobot){
	        	 if(growpolygons.size() != 0){
		        	 for (int i = 0; i < polygons.size(); i++){
		        		 g.setColor(Color.GRAY);
		        		 g.drawPolygon(growpolygons.get(i));
		        		 g.fillPolygon(growpolygons.get(i));
		        	 }
		        	 
		        	 for (Line2D l : polygonLines){
		        		 g.drawOval((int)l.getX1() - circleRobotR, (int)l.getY1() - circleRobotR,circleRobotR*2,circleRobotR*2);
		        		 g.fillOval((int)l.getX1() - circleRobotR, (int)l.getY1() - circleRobotR,circleRobotR*2,circleRobotR*2);
		        	 }
		        	 
	        	 }
	         }
	         
	         //draw polygons
	         if(polygons.size() != 0){
	        	 for (int i = 0; i < polygons.size(); i++){
	        		 g.setColor(Color.BLACK);
	        		 g.drawPolygon(polygons.get(i));
	        		 g.fillPolygon(polygons.get(i));
	        	 }
	         }
	      }	
	}
}




























