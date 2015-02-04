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
	
	public UserInterface(){
		frame = new JFrame("Robot Motion");
		contentPanel = frame.getContentPane(); 
		buttonPanel = new JPanel();
		robotPanel = new DrawCanvas();
		pointClicked = new ArrayList<Point>();
		
		MakeFrame();
		
	}
	
	private void MakeFrame(){
		contentPanel.setLayout(new BorderLayout());
		
		//build buttonPanel
		buttonPanel.setLayout(new GridLayout(15,1));
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
		if(choice == "Start"){
			System.out.println("Start");
		}
	}
	
	class DrawCanvas extends JPanel{
		private static final long serialVersionUID = 1L;
		
		public DrawCanvas(){
			this.addMouseListener(new MouseAdapter() {
	             public void mouseClicked(MouseEvent e) {
	                 pointClicked.add(e.getPoint());
	                 repaint();
	             }
	         });	
		} 
		
		@Override
	      public void paintComponent(Graphics g) {
	         super.paintComponent(g);
	         if(pointClicked.size() != 0){
	        	 for (int i = 0; i < pointClicked.size(); i++){
	        		 g.fillRect(pointClicked.get(i).x, pointClicked.get(i).y, 5, 5);
	        	 }    
	         }
	      }	
	}
	
	
}




































