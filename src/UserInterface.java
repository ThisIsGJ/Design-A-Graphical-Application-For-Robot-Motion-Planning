import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
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
	
	private Boolean setStart;
	private Boolean setEnd;
	private Point startPoint;
	private Point endPoint;
	
	private ArrayList<Polygon> polygons;
	private ArrayList<Point2D> polygonNodes;
	private ArrayList<Line2D> visibilityLines;
	private ArrayList<Line2D> polygonLines;
	private boolean setVisibility = false;
	private ArrayList<Point2D> shortestPath;
	
	public UserInterface(){
		frame = new JFrame("Robot Motion");
		contentPanel = frame.getContentPane(); 
		buttonPanel = new JPanel();
		robotPanel = new DrawCanvas();
		pointClicked = new ArrayList<Point>();
		setStart = false;
		setEnd = false;
		startPoint = new Point(-1,-1);
		endPoint = new Point(-1,-1);
		
		MakeFrame();
		
		polygons = new ArrayList<Polygon>(); 
		DP = new DrawPolygon();
		VG = new VisibilityGraph();
		SP = new FindShortestPath();
		polygonNodes = new ArrayList<Point2D>();
		polygonLines = new ArrayList<Line2D>();
		visibilityLines = new ArrayList<Line2D>();
		shortestPath = new ArrayList<Point2D>();
	}
	
	private void MakeFrame(){
		contentPanel.setLayout(new BorderLayout());
		
		//build buttonPanel
		buttonPanel.setLayout(new GridLayout(15,1));
		addButton(buttonPanel,"Start Point");
		addButton(buttonPanel,"End Point");
		addButton(buttonPanel,"Draw Shape");
		addButton(buttonPanel,"<html>Visibility<br />Graph</html>");
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
		if(choice == "Draw Shape"){
			pointClicked = DP.QuickHull(pointClicked);
			int xPoly[] = new int[30];
			int yPoly[] = new int[30];
			for (int i = 0; i < pointClicked.size(); i++){
				//used to draw polygons
				 xPoly[i] = pointClicked.get(i).x;
				 yPoly[i] = pointClicked.get(i).y;
				 //used to draw visibility graph
				 polygonNodes.add(pointClicked.get(i));
				 //collect the edges of each polygons
				 if(i == pointClicked.size()-1){
					 Line2D line = new Line2D.Double();
					 line.setLine(pointClicked.get(i),pointClicked.get(0));
					 polygonLines.add(line);
				 }else{
					 Line2D line = new Line2D.Double();
					 line.setLine(pointClicked.get(i),pointClicked.get(i+1));
					 polygonLines.add(line);
				 }
			}
			
			Polygon Polygon = new Polygon(xPoly,yPoly,pointClicked.size());
			polygons.add(Polygon);   
			pointClicked.clear();
			robotPanel.repaint();
		}else if(choice == "Start Point"){
			setStart = true;
			setEnd = false;
		}else if(choice == "End Point"){
			setEnd = true;
			setStart = false;
		}else if(choice == "Clean All"){
			setStart = false;
			setEnd = false;
			startPoint.x = -1;
			startPoint.y = -1;
			endPoint.y = -1;
			endPoint.x = -1;
			pointClicked.clear();
			polygons.clear();
			visibilityLines.clear();
			setVisibility = false;
			polygonNodes.clear();
			polygonLines.clear();
			shortestPath.clear();
			robotPanel.repaint();
		}else if(choice == "<html>Visibility<br />Graph</html>"){
			setVisibility = true;
			if(startPoint.x == -1 ){
				JOptionPane.showMessageDialog(frame, "Please set the Start point.");
				setVisibility = false;
			}else if(endPoint.x == -1){
				JOptionPane.showMessageDialog(frame, "Please set the End point.");
				setVisibility = false;
			}
			polygonNodes.add(0, startPoint);
            polygonNodes.add(endPoint);
			visibilityLines = VG.createVisibilityGraph(polygonNodes,polygonLines,polygons);
			shortestPath = SP.DijkstraAlgorithm(polygonNodes);
			robotPanel.repaint();
		}
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
	        	 g.drawString(message_start,startPoint.x+12,startPoint.y+12);
	        	 g.fillRect(startPoint.x,startPoint.y,10,10);
	        	 setStart = false;
	         }
	         
	         //draw end point
	         if(endPoint.x >= 0 || endPoint.y >= 0){
	        	 String message_end = "End";
	        	 g.setColor(Color.BLUE);
	        	 g.drawString(message_end, endPoint.x+12, endPoint.y+12);
	        	 g.fillRect(endPoint.x, endPoint.y, 10, 10);
	        	 setEnd = false;
	         }
	         
	         //store the clicked point to draw a polygon
	         if(pointClicked.size() != 0){
	        	 g.setColor(Color.BLACK);
	        	 for (int i = 0; i < pointClicked.size(); i++){
	        		 g.fillRect(pointClicked.get(i).x, pointClicked.get(i).y, 5, 5);
	        	 }    
	         }
	         
	         if(setVisibility == true){
	        	 for (Line2D l : visibilityLines){
	        		 g.setColor(Color.GRAY);
	        		 g.drawLine((int)l.getX1(),(int)l.getY1(),(int)l.getX2(),  (int)l.getY2());
	        	 }
	         }
	         
	         
	         if(polygons.size() != 0){
	        	 for (int i = 0; i < polygons.size(); i++){
	        		 g.setColor(Color.BLACK);
	        		 g.drawPolygon(polygons.get(i));
	        		 g.fillPolygon(polygons.get(i));
	        	 }
	         }
	         
	         
	         
//	         int x[] = {10,10,300,300};
//			 int y[] = {10,300,280,10};
//	         Polygon test = new Polygon(x,y,4);
//	         g.drawPolygon(test);
//	         Point2D pp = new Point(300,300);
//	         System.out.println(test.xpoints);
	         
	      }	
	}
	

	
}




























