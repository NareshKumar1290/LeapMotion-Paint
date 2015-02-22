
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JButton;

public class LeapButton extends JButton{
	
	private boolean expanding = false;
	private int originalSizeX, originalSizeY;
	private double expansionMultiplier;
	public boolean canExpand = false;
	
	LeapButton(String label, double expansionMultiplier){
		super(label);
		this.expansionMultiplier = expansionMultiplier;
	}
	
	public Rectangle getBigBounds(){
		Rectangle rect = getBounds();
		rect.width = rect.width + 30 ;
		rect.height = rect.height + 30 ;
		
		rect.x = rect.x - 15;
		rect.y = rect.y - 15;
		return rect;
	}
	
	public void expand(){
		
		if(!expanding){
			canExpand = true;
			expanding = true;
			
				(new Thread(){
					public void run(){
					Color originalColor = getBackground();
					setBackground(Color.green);
					originalSizeX = getPreferredSize().width;
					originalSizeY = getPreferredSize().height;
					
					int targetSizeX = (int) (originalSizeX * expansionMultiplier);
					int targetSizeY = (int) (originalSizeY * expansionMultiplier);
					int stepX = (targetSizeX - originalSizeX) / 10;
					int stepY = (targetSizeY - originalSizeY) / 10;
					
					while(canExpand && getPreferredSize().width < targetSizeX){
						setPreferredSize(new Dimension(getPreferredSize().width + stepX, getPreferredSize().height + stepY));
						revalidate();
						try{Thread.sleep(75);}
						catch(Exception e) {}
						
						if(getPreferredSize().width >= targetSizeX)
							doClick();
						else
							revalidate();
						
						setPreferredSize(new Dimension(originalSizeX, originalSizeY));
						revalidate();
						expanding = false;
						
						setBackground(originalColor);
					}
				}
			}).start();
		}
	}
}
