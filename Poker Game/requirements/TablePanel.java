import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Graphics;
import java.io.IOException;
import java.io.File;


public class TablePanel extends JPanel{

  private Image bgImage;

  public TablePanel(){

    try {
      bgImage = ImageIO.read(new File("assets/black_felt.png"));
      System.out.println("Fucking did it");
  } catch (IOException e) {}

  }


  @Override
  protected void paintComponent(Graphics g) {

    super.paintComponent(g);
        g.drawImage(bgImage, 0, 0, null);
  }
}
