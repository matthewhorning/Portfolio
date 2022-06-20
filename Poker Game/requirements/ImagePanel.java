//custom component from java2s.com

import java.awt.*;
import javax.swing.*;

public class ImagePanel extends JPanel {
	private Image img;

  	public ImagePanel(String img) {
    		this(new ImageIcon(img).getImage());
  	}	

  	public ImagePanel(Image img) {
    		this.img = img;
    		Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
		//Dimension size = new Dimension(900, 600);
    		setPreferredSize(size);
    		setMinimumSize(size);
    		setMaximumSize(size);
    		setSize(size);
    		setLayout(null);
 	}
	
	@Override 
  	protected void paintComponent(Graphics g) {
    		g.drawImage(img, 0, 0, this);
  	}
}
