
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.InteractionBox;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Vector;

public class LeapaintListener extends Listener{

	private InteractionBox normalizedBox;
	public Leapaint paint;
	public Frame frame;
	
	public LeapaintListener(Leapaint newPaint){
		paint = newPaint;
	}
	
	public void onInit(Controller controller){
		System.out.println("Initialized");
	}
	
	public void onConnect(Controller controller){
		System.out.println("Connected");
	}
	
	public void onDisconnect(Controller controller){
		System.out.println("Disconnected");
	}
	
	public void onExit(Controller controller){
		System.out.println("Exited");
	}
	
	public void onFrame(Controller controller){
		frame = controller.frame();
		
		if(!(frame.fingers().isEmpty())){
			Finger frontMost = frame.fingers().frontmost();
			Vector position = new Vector(-1, -1, -1);
			normalizedBox = frame.interactionBox();
			
			position.setX(normalizedBox.normalizePoint(frontMost.tipPosition()).getX());
			position.setY(normalizedBox.normalizePoint(frontMost.tipPosition()).getY());
			position.setZ(normalizedBox.normalizePoint(frontMost.tipPosition()).getZ());
			
			position.setX(position.getX() * paint.getBounds().width);
			position.setY(position.getY() * paint.getBounds().height);
			
			position.setY(position.getY() * -1);
			position.setY(position.getY() + paint.getBounds().height);
			
			paint.prevX = paint.x;
			paint.prevY = paint.y;
			
			paint.x = (int) position.getX();
			paint.y = (int) position.getY();
			paint.z = (int) position.getZ();
			
			paint.paintPanel.repaint();
			
			if(paint.button1.getBigBounds().contains((int)position.getX(), (int)position.getY()))
				paint.button1.expand();
			else
				paint.button1.canExpand = false;
			
			if(paint.button2.getBigBounds().contains((int)position.getX(), (int)position.getY()))
				paint.button2.expand();
			else
				paint.button2.canExpand = false;
			
			if(paint.button3.getBigBounds().contains((int)position.getX(), (int)position.getY()))
				paint.button3.expand();
			else
				paint.button3.canExpand = false;
			
			if(paint.button4.getBigBounds().contains((int)position.getX(), (int)position.getY()))
				paint.button4.expand();
			else
				paint.button4.canExpand = false;
			
		}
	}
	
}