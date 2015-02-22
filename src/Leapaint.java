
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.leapmotion.leap.Controller;
	
public class Leapaint extends JFrame{

	/**
	 * 
	 */
	private static Leapaint paint;
	public int prevX = -1, prevY = -1;
	public int x = -1, y = -1;
	public double z = -1;
	
	public Color inkColor = Color.MAGENTA;
	
	public class Line{
		public int startX, startY, endX, endY;
		public Color color;
		
		Line(int startX, int startY, int endX, int endY, Color color){
			this.startX = startX;
			this.startY = startY;
			this.endX = endX;
			this.endY = endY;
			this.color = color;
		}
	}
	
	public List<Line> lines = new ArrayList<Line>();
	public LeapButton button1, button2, button3, button4;
	
	public JPanel buttonPanel;
	public JPanel paintPanel;
	
	Leapaint(){
		super("Leapaint - Place a finger in view to draw!");
		buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.setBackground(new Color(215, 215, 215));
		
		button1 = new LeapButton("Red", 1.5);
		button1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				inkColor = Color.RED;
			}
		});
		
		button2 = new LeapButton("Blue", 1.5);
		button2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				inkColor = Color.BLUE;
			}
		});
		
		button3 = new LeapButton("Purple", 1.5);
		button3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				inkColor = Color.MAGENTA;
			}
		});
		
		button4 = new LeapButton("Save", 1.5);
		button4.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				saveImage("leapaint");
			}
		});
		
		buttonPanel.add(button1);
		buttonPanel.add(button2);
		buttonPanel.add(button3);
		
		buttonPanel.add(Box.createVerticalStrut(1));
		buttonPanel.add(button4);
		
		paintPanel = new JPanel(){
			public void paintComponent(Graphics g){
				super.paintComponent(g);
				
				Graphics2D g2 = (Graphics2D) g;
				g2.setStroke(new BasicStroke(3));
				
				if(z <= 0.5)
					lines.add(new Line(prevX, prevY, x, y, inkColor));
				for(Line line : lines){
					g2.setColor(line.color);
					g2.drawLine(line.startX, line.startY, line.endX, line.endY);
					buttonPanel.repaint();
					
					if(z <= 0.95 && z != -1.0){
						g2.setColor((z <= 0.5) ? inkColor : new Color(0, 255, 153));
						int cursorSize = (int) Math.max(20, 100 - z * 100);
						g2.fillOval(x, y, cursorSize, cursorSize);
					}
				}
			}
		};
		
		paintPanel.setOpaque(false);
		getContentPane().add(buttonPanel, BorderLayout.NORTH);
		getContentPane().add(paintPanel);
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 500);
		setVisible(true);
	}
	
	
	
	public void saveImage(String imageName){
		Point pos = getContentPane().getLocationOnScreen();
		Rectangle screenRect = getContentPane().getBounds();
		screenRect.x = pos.x;
		screenRect.y = pos.y;
		
		try{
			BufferedImage capture = new Robot().createScreenCapture(screenRect);
			ImageIO.write(capture, "bmp", new File(imageName + ".bmp"));
		}catch(Exception e){}
	}
	
	public static void main(String args[]){
		paint = new Leapaint();
		
		LeapaintListener listener = new LeapaintListener(paint);
		Controller controller = new Controller();
		controller.addListener(listener);
	}
}
