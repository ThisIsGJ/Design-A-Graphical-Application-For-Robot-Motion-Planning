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
	private JPanel setInput;
	private JPanel textPanel;
	
	private ArrayList<Point> pointClicked;
	private ArrayList<Point> cgrowpointClicked;
	private ArrayList<Point> rgrowpointClicked;
	
	private Boolean setStart;
	private Boolean setEnd;
	private Point startPoint;
	private Point endPoint;
	
	private ArrayList<Polygon> polygons;
	private ArrayList<Polygon> cgrowpolygons;
	private ArrayList<Polygon> rgrowpolygons;
	
	private ArrayList<Point> polygonNodes;
	private ArrayList<Point> cgrowNodes;
	private ArrayList<Point> rgrowNodes;
	
	private ArrayList<Line2D> polygonLines;
	private ArrayList<Line2D> cgrowLines;
	private ArrayList<Line2D> rgrowLines;
	
	private ArrayList<Line2D> visibilityLines;
	private ArrayList<Line2D> cgrowvisibilityLines;
	private ArrayList<Line2D> rgrowvisibilityLines;
	
	private boolean setVisibility;
	private boolean showShortestPath;
	private ArrayList<Line2D> shortestPath;
	private ArrayList<Line2D> cgrowshortestPath;
	private ArrayList<Line2D> rgrowshortestPath;
	JTextArea textArea;
	
	private boolean circleRobot = false;
	private boolean pointRobot = true;
	//the radius of circle robot 
	private int circleRobotR = 30;
	
	private boolean rectangleRobot = false;
	private int recRobotw = 20;
	private int recRobotl = 60;
	
	String content;
	
	public UserInterface(){
		frame = new JFrame("Robot Motion");
		contentPanel = frame.getContentPane(); 
		buttonPanel = new JPanel();
		setInput = new JPanel();
		robotPanel = new DrawCanvas();
		textPanel = new JPanel();
		
		pointClicked = new ArrayList<Point>();
		cgrowpointClicked = new ArrayList<Point>();
		rgrowpointClicked = new ArrayList<Point>();
		
		setStart = false;
		setEnd = false;
		startPoint = new Point(-1,-1);
		endPoint = new Point(-1,-1);
		setVisibility = false;
		showShortestPath = false;

		DP = new DrawPolygon();
		VG = new VisibilityGraph();
		SP = new FindShortestPath();
		
		polygons = new ArrayList<Polygon>(); 
		cgrowpolygons = new ArrayList<Polygon>();
		rgrowpolygons = new ArrayList<Polygon>();
		
		polygonNodes = new ArrayList<Point>();
		cgrowNodes = new ArrayList<Point>();
		rgrowNodes = new ArrayList<Point>();
		
		polygonLines = new ArrayList<Line2D>();
		cgrowLines = new ArrayList<Line2D>();
		rgrowLines = new ArrayList<Line2D>();
		
		visibilityLines = new ArrayList<Line2D>();
		cgrowvisibilityLines = new  ArrayList<Line2D>();
		rgrowvisibilityLines = new  ArrayList<Line2D>();
		
		shortestPath = new ArrayList<Line2D>();
		cgrowshortestPath = new ArrayList<Line2D>();
		rgrowshortestPath = new ArrayList<Line2D>();
		
		MakeFrame();
	}
	
	private void MakeFrame(){
		// the main panel
		contentPanel.setLayout(new BorderLayout());
		
		//build buttonPanel
		buttonPanel.setLayout(new GridLayout(12,1));
		addButton(buttonPanel,"Point Robot");
		addButton(buttonPanel,"Circle Robot");
		addButton(buttonPanel,"<html>Rectangle<br />Robot</html>");
		addButton(buttonPanel,"Start Point");
		addButton(buttonPanel,"End Point");
		addButton(buttonPanel,"<html>Draw<br />Obstacles</html>");
		addButton(buttonPanel,"<html>Visibility<br />Graph</html>");
		addButton(buttonPanel,"<html>Shortest Path</html>");
		addButton(buttonPanel,"Clean All");
		
		buttonPanel.setBorder(new EtchedBorder());
		contentPanel.add(buttonPanel, BorderLayout.WEST);	
		
		robotPanel.setBorder(new EtchedBorder());
		contentPanel.add(robotPanel,BorderLayout.CENTER);
		
		setInput.setLayout(new GridLayout(8,1));
		JLabel readMe1 = new JLabel("<html>Radius are used to<br>change the size of<br>Circular Robot</html>", SwingConstants.CENTER);
		setInput.add(readMe1);
		addButton(setInput,"Radius+");
		addButton(setInput,"Radius-");
		
		JLabel readMe2 = new JLabel("<html>Length and Width<br>are used to change<br>the size of<br>Rectangular Robot</html>", SwingConstants.CENTER);
		setInput.add(readMe2);
		addButton(setInput,"Length+");
		addButton(setInput,"Length-");
		addButton(setInput,"Width+");
		addButton(setInput,"Width-");
		setInput.setBorder(new EtchedBorder());
		
		
		contentPanel.add(setInput,BorderLayout.EAST);
		
		textPanel.setBorder(new EtchedBorder());
		
		//explain how to use
		content = " Robot Motion Planning! \n I will teach you how you use this application here!\n Firstly, you need build the environments. You could follow the guide which is shown below:" +
				"\n You can use the \"Start Point\" and \"End Point\" to set the start and end position for the robot!" +
				"\n You can click on the right canvas to add the points and these points will used to build the obstacles.\n After the points added, you could click " +
				"the \"Draw Obstacles\" button to draw the obstacles.\n If you want to see the visibility graph of the environment, you could click the \"Visibility Graph\" button." +
				"\n If you want to know which path is the shortest path, you can click \"Shortest Path\" button. The bule line is the shortest path." +
				"\n If you want to clean the canvas, just click the \"Clean All\" button";
		textArea = new JTextArea(content);
		JScrollPane stext = new JScrollPane(textArea);
		stext.setPreferredSize(new Dimension(980, 100));
		textPanel.add(stext,BorderLayout.WEST);
		contentPanel.add(textPanel,BorderLayout.SOUTH);
		
		frame.setSize(1200,800);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}
	
	private void addButton(Container panel, String button){
		JButton btn = new JButton(button);
        btn.setPreferredSize(new Dimension(100, 30));
		btn.addActionListener(this);
		btn.setFocusable(false);
		panel.add(btn);
	}
	
	public void actionPerformed(ActionEvent e) {
		String choice = e.getActionCommand();
		//draw convex hull
		if(choice == "<html>Draw<br />Obstacles</html>"){
			if(pointClicked.size() > 2){
				textArea.append("\n The obstacles have been drawn.");
				textArea.setCaretPosition(textArea.getDocument().getLength());
				drawShape();
			}else{
				textArea.append("\n Please set at least 3 vertice of the obstacles on the right canvas. ");
				textArea.setCaretPosition(textArea.getDocument().getLength());
			}
		}else if(choice == "Start Point"){ 
			textArea.append("\n Now you can set the start point by clicking on the right canvas. If you want to " +
					"reset the start point, just click the \"Start Point\" and reset it again.");
			textArea.setCaretPosition(textArea.getDocument().getLength());
			setStart = true;
			setEnd = false;
		}else if(choice == "End Point"){
			textArea.append("\n Now you can set the end point by clicking on the right canvas. If you want to " +
					"reset the end position, just click the \"End Point\" and reset it again");
			textArea.setCaretPosition(textArea.getDocument().getLength());
			setEnd = true;
			setStart = false;
		}else if(choice == "<html>Shortest Path</html>"){
			if(startPoint.x != -1 && endPoint.x != -1){
				textArea.append("\n The blue line is t he shortest path.");
				textArea.setCaretPosition(textArea.getDocument().getLength());
				if(showShortestPath)showShortestPath = false;
						else showShortestPath = true;
				showVG();
			}else{
				textArea.append("\n You haven't set the start point or end point yet. Please set them on the environment panel firstly.");
				textArea.setCaretPosition(textArea.getDocument().getLength());
			}
		}else if(choice == "Clean All"){
			textArea.append("\n The canvas have been clean up.");
			textArea.setCaretPosition(textArea.getDocument().getLength());
			cleanAll();
		}else if(choice == "<html>Visibility<br />Graph</html>"){
			if(startPoint.x != -1 && endPoint.x != -1){
				showVG();
				//show visibility graph
				if(!setVisibility) setVisibility = true;
				else if(setVisibility) setVisibility = false;
				
				textArea.append("\n Visibility Graph consist of all the black lines includes the edges of obstacles.");
				textArea.setCaretPosition(textArea.getDocument().getLength());
			}else{
				textArea.append("\n You haven't set the start point or end point yet. Please set them on the environment panel firstly.");
				textArea.setCaretPosition(textArea.getDocument().getLength());
			}
		}else if(choice == "Point Robot"){
			textArea.append("\n Now, the robot is a point.");
			textArea.setCaretPosition(textArea.getDocument().getLength());
			rectangleRobot = false;
			circleRobot = false;
			pointRobot = true;
		}else if(choice == "Circle Robot"){
			textArea.append("\n Now, the robot's shape is a point.");
			textArea.setCaretPosition(textArea.getDocument().getLength());
			circleRobot = true;
			pointRobot = false;
			rectangleRobot = false;
		}else if(choice == "<html>Rectangle<br />Robot</html>"){
			textArea.append("\n Now, the robot's shape is a rectangle.");
			textArea.setCaretPosition(textArea.getDocument().getLength());
			rectangleRobot = true;
			circleRobot = false;
			pointRobot = false;
		}else if(choice == "Radius+" && circleRobot){
			textArea.append("\n The radius of the robot has been increased by 5cm.");
			textArea.setCaretPosition(textArea.getDocument().getLength());
			circleRobotR = circleRobotR + 5;
			reDrawShape();
			showVG();

		}else if(choice == "Radius-" && circleRobot){
			textArea.append("\n The radius of the robot has been decreased by 5cm.");
			textArea.setCaretPosition(textArea.getDocument().getLength());
			circleRobotR = circleRobotR - 5;
			reDrawShape();
			showVG();
		}else if(choice == "Length+" && rectangleRobot){
			textArea.append("\n The length of the robot has been increased by 5cm.");
			textArea.setCaretPosition(textArea.getDocument().getLength());
			recRobotl = recRobotl + 5;
			reDrawShape();
			showVG();
		}else if(choice == "Length-" && rectangleRobot){
			textArea.append("\n The length of the robot has been decreased by 5cm.");
			textArea.setCaretPosition(textArea.getDocument().getLength());
			recRobotl = recRobotl- 5;
			reDrawShape();
			showVG();
		}else if(choice == "Width+" && rectangleRobot){
			textArea.append("\n The width of the robot has been increased by 5cm.");
			textArea.setCaretPosition(textArea.getDocument().getLength());
			recRobotw = recRobotw + 5;
			reDrawShape();
			showVG();
		}else if(choice == "Width-" && rectangleRobot){
			textArea.append("\n The width of the robot has been decreased by 5cm.");
			textArea.setCaretPosition(textArea.getDocument().getLength());
			recRobotw = recRobotw - 5;
			reDrawShape();
			showVG();
		}
		
		robotPanel.repaint();
	}
	
	// used for user change the size of robot
	private void reDrawShape(){
		
		ArrayList<Polygon> newPolygons = new ArrayList<Polygon>();
		newPolygons.addAll(polygons);
		polygons.clear();
		cgrowpolygons.clear();
		rgrowpolygons.clear();
		
		polygonNodes.clear();
		cgrowNodes.clear();
		rgrowNodes.clear();
		
		polygonLines.clear();
		cgrowLines.clear();
		rgrowLines.clear();
		
		for(Polygon p : newPolygons){
			// get all the point of each polygon
			int[] x = p.xpoints;
			int[] y = p.ypoints;
			for(int i = 0; i < x.length; i++){
				pointClicked.add(new Point(x[i],y[i]));
			}
			
			//for point circle
			pointClicked = DP.QuickHull(pointClicked);
			polygons.add(creatPolygon(pointClicked,polygonLines,polygonNodes));   
			
			//for circle robot - grow the nodes
			cgrowpointClicked = DP.cgrowPolygon(pointClicked,circleRobotR);
			
			//get growing polygon for circle robot
			cgrowpolygons.add(creatPolygon(cgrowpointClicked,cgrowLines,cgrowNodes));
			
			
			//get grow nodes for rectangle robot
			rgrowpointClicked = DP.rgrowPolygon(pointClicked,recRobotw,recRobotl,creatPolygon(pointClicked,polygonLines,polygonNodes));
			rgrowpointClicked = DP.QuickHull(rgrowpointClicked);
			//get growing polygon for rectangle robot
			rgrowpolygons.add(creatPolygon(rgrowpointClicked,rgrowLines,rgrowNodes));
			
			pointClicked.clear();
			cgrowpointClicked.clear();
			rgrowpointClicked.clear();
			
		}
	}
	
	private void drawShape(){
		
		//for point circle
		pointClicked = DP.QuickHull(pointClicked);
		polygons.add(creatPolygon(pointClicked,polygonLines,polygonNodes));   
		
		//for circle robot - grow the nodes
		cgrowpointClicked = DP.cgrowPolygon(pointClicked,circleRobotR);
		
		//get growing polygon for circle robot
		cgrowpolygons.add(creatPolygon(cgrowpointClicked,cgrowLines,cgrowNodes));
		
		
		//get grow nodes for rectangle robot
		rgrowpointClicked = DP.rgrowPolygon(pointClicked,recRobotw,recRobotl,creatPolygon(pointClicked,polygonLines,polygonNodes));
		rgrowpointClicked = DP.QuickHull(rgrowpointClicked);
		//get growing polygon for rectangle robot
		rgrowpolygons.add(creatPolygon(rgrowpointClicked,rgrowLines,rgrowNodes));
		
		pointClicked.clear();
		cgrowpointClicked.clear();
		rgrowpointClicked.clear();
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
//			 collect the edges of each polygons
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
		
		cgrowpointClicked.clear();
		cgrowpolygons.clear();
		cgrowvisibilityLines.clear();
		cgrowNodes.clear();
		cgrowLines.clear();
		cgrowshortestPath.clear();
		
		rgrowpointClicked.clear();
		rgrowpolygons.clear();
		rgrowvisibilityLines.clear();
		rgrowNodes.clear();
		rgrowLines.clear();
		rgrowshortestPath.clear();
		
//		robotPanel.repaint();
	}
	
	private void showVG(){
		
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
        // collision detection 
        visibilityLines = VG.createVisibilityGraph(polygonNodes,polygonLines,polygons);
        shortestPath = SP.DijkstraAlgorithm(visibilityLines,startPoint,endPoint);
        
        cgrowNodes.add(0, startPoint);
	    cgrowNodes.add(endPoint);
	    
	    rgrowNodes.add(0, startPoint);
	    rgrowNodes.add(endPoint);
	    
	    // collision detection 
        cgrowvisibilityLines = VG.createVisibilityGraph(cgrowNodes,cgrowLines,cgrowpolygons);
        rgrowvisibilityLines = VG.createVisibilityGraph(rgrowNodes,rgrowLines,rgrowpolygons);
        //find the shortestPath
        cgrowshortestPath = SP.DijkstraAlgorithm(cgrowvisibilityLines, startPoint, endPoint);
        rgrowshortestPath = SP.DijkstraAlgorithm(rgrowvisibilityLines, startPoint, endPoint);
        
	}

	class DrawCanvas extends JPanel{
		private static final long serialVersionUID = 1L;
		
		public DrawCanvas(){
			this.addMouseListener(new MouseAdapter() {
	             public void mouseClicked(MouseEvent e) {
	            	 if(setStart == true){
	    	        	 startPoint.x = e.getPoint().x;
	    	        	 startPoint.y = e.getPoint().y;
	    	        	 setStart = false;
	    	         }else if(setEnd == true){
	    	        	 endPoint.x = e.getPoint().x;
	    	        	 endPoint.y = e.getPoint().y;
	    	        	 setEnd = false;
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
		         }else if(rectangleRobot){
		        	 g.fillRect(startPoint.x-recRobotl/2,startPoint.y-recRobotw/2,recRobotl,recRobotw);
		        	 g.drawString(message_start,startPoint.x-60,startPoint.y-15);
		         }else{
		        	 g.fillRect(startPoint.x-5,startPoint.y-5,10,10);
		        	 g.drawString(message_start,startPoint.x-40,startPoint.y+12);
		         }
	         }
	         
	         //draw end point
	         if(endPoint.x >= 0 || endPoint.y >= 0){
	        	 String message_end = "End";
	        	 g.setColor(Color.RED);
	        	 g.drawString(message_end, endPoint.x+12, endPoint.y+12);
	        	 g.fillRect(endPoint.x-5, endPoint.y-5, 10, 10);
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
	        		 for (Line2D l : cgrowvisibilityLines){
		        		 g.setColor(Color.BLACK);
		        		 g.drawLine((int)l.getX1(),(int)l.getY1(),(int)l.getX2(),  (int)l.getY2());
		        	 }
	        	 }else if(rectangleRobot){
	        		 for (Line2D l : rgrowvisibilityLines){
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
	        	 if(showShortestPath && cgrowshortestPath.size() != 0){
		        	 for (Line2D l : cgrowshortestPath){
		        		 g.setColor(Color.BLUE);
		        		 g.drawLine((int)l.getX1(),(int)l.getY1(),(int)l.getX2(),  (int)l.getY2());
		        	 }
		         }
	         }else if(rectangleRobot){
	        	 if(showShortestPath && rgrowshortestPath.size() != 0){
		        	 for (Line2D l : rgrowshortestPath){
		        		 g.setColor(Color.BLUE);
		        		 g.drawLine((int)l.getX1(),(int)l.getY1(),(int)l.getX2(),  (int)l.getY2());
		        	 }
		         }
	         }
	         if(circleRobot){
	        	 if(cgrowpolygons.size() != 0){
		        	 for (int i = 0; i < polygons.size(); i++){
		        		 g.setColor(Color.GRAY);
		        		 g.drawPolygon(cgrowpolygons.get(i));
		        		 g.fillPolygon(cgrowpolygons.get(i));
		        	 }
		        	 
		        	 for (Line2D l : polygonLines){
		        		 g.drawOval((int)l.getX1() - circleRobotR, (int)l.getY1() - circleRobotR,circleRobotR*2,circleRobotR*2);
		        		 g.fillOval((int)l.getX1() - circleRobotR, (int)l.getY1() - circleRobotR,circleRobotR*2,circleRobotR*2);
		        	 }
		        	 
	        	 }
	         }else if(rectangleRobot){
	        	 if(rgrowpolygons.size() != 0){
		        	 for (int i = 0; i < polygons.size(); i++){
		        		 g.setColor(Color.GRAY);
		        		 g.drawPolygon(rgrowpolygons.get(i));
		        		 g.fillPolygon(rgrowpolygons.get(i));
		        	 }
	        	 }
	         }
	         
	         //draw original polygons
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




























