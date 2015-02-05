import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

public class UserInterface implements ActionListener{
	
	private JFrame frame;
	private Container contentPanel;
	private JPanel buttonPanel;
	private DrawCanvas robotPanel;
	private ArrayList<Point> pointClicked;
	
	private Boolean setStart;
	private Boolean setEnd;
	private Point startPoint;
	private Point endPoint;
	
	public UserInterface(){
		frame = new JFrame("Robot Motion");
		contentPanel = frame.getContentPane(); 
		buttonPanel = new JPanel();
		robotPanel = new DrawCanvas();
		pointClicked = new ArrayList<Point>();
		setStart = false;
		setEnd = false;
		startPoint = new Point(0,0);
		endPoint = new Point(0,0);
		
		MakeFrame();
		
	}
	
	private void MakeFrame(){
		contentPanel.setLayout(new BorderLayout());
		
		//build buttonPanel
		buttonPanel.setLayout(new GridLayout(15,1));
		addButton(buttonPanel,"Start Point");
		addButton(buttonPanel,"End Point");
		addButton(buttonPanel,"Draw Shape");
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
		if(choice == "Draw Shape"){
			System.out.println("Drawed!!");
		}else if(choice == "Start Point"){
			setStart = true;
			setEnd = false;
		}else if(choice == "End Point"){
			setEnd = true;
			setStart = false;
		}else if(choice == "Clean All"){
			setStart = false;
			setEnd = false;
			startPoint.x = 0;
			startPoint.y = 0;
			endPoint.y = 0;
			endPoint.x = 0;
			pointClicked.clear();
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
	    	         }else{
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
	         if(startPoint.x != 0 || startPoint.y != 0 ){
	        	 g.setColor(Color.RED);
	        	 g.fillRect(startPoint.x,startPoint.y,10,10);
	        	 setStart = false;
	         }
	         
	         //draw end point
	         if(endPoint.x != 0 || endPoint.y != 0){
	        	 g.setColor(Color.BLUE);
	        	 g.fillRect(endPoint.x, endPoint.y, 10, 10);
	        	 setEnd = false;
	         }
	         
	         if(pointClicked.size() != 0){
	        	 g.setColor(Color.BLACK);
	        	 for (int i = 0; i < pointClicked.size(); i++){
	        		 g.fillRect(pointClicked.get(i).x, pointClicked.get(i).y, 5, 5);
	        	 }    
	         }
	      }	
	}
	
}




































