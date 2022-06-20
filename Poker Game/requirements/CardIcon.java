import java.awt.Rectangle;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;

public class CardIcon implements Icon
{

   protected int width;
   protected int height;
   protected boolean showFace;
   protected String suiteString ="";
   protected String faceString = "";

   protected Color color;

   public CardIcon(int width, Card c)
   {
      this.showFace = true;
      this.width = width;
      this.height = (int)(width * 1.5);
      this.suiteString ="";
      this.faceString = "";
      Card.Suite suite = c.getSuite();
      switch (suite)
      {
          case SPADES:
              this.suiteString = "\u2660";
              this.color = Color.BLACK;
              break;
          case CLUBS:
              this.suiteString = "\u2663";
              this.color = Color.BLACK;
              break;
          case DIAMONDS:
              this.suiteString = "\u25C6";
              this.color = Color.RED;
              break;
          case HEARTS:
              this.suiteString = "\u2665";
              this.color = Color.RED;
              break;
      }
      Card.Face face = c.getFace();
      switch (face)
      {
          case ACE:
              faceString = "A";
              break;
          case KING:
              faceString = "K";
              break;
          case QUEEN:
              faceString = "Q";
              break;
          case JACK:
              faceString = "J";
              break;
          default:
              faceString = Integer.toString(face.getValue());
      }

   }

   public void setShowFace(boolean showFace)
   {
       this.showFace = showFace;
   }
   @Override 
   public int getIconWidth()
   {
       return this.width;
   }

   @Override
   public int getIconHeight()
   {
       return this.height;
   }
   @Override
   public void paintIcon(Component c, Graphics g, int x, int y)
   {
      Graphics2D g2 = (Graphics2D)g;

      // white rectangle represents the card background
      g2.setColor(Color.WHITE);
      g2.fillRect(x, y, this.width, this.height);
      g2.setColor(Color.BLACK);
      Rectangle r = new Rectangle(this.width, this.height);
      g2.draw(r);


      if (this.showFace)
      {
          // Suite
          g2.setFont(new Font("Symbola",Font.PLAIN,this.height/3));
          g2.setColor(this.color);
          g2.drawString(suiteString, x+0, y+this.height/4);

          // Card face
          g2.setFont(new Font("Symbola",Font.BOLD,this.height/3));
          g2.drawString(faceString, this.width/3, this.height*2/3);
      }
      else
      {
          BufferedImage cardback = null;
          try {
              cardback = ImageIO.read(new File("assets/cardback.png"));
          } catch (IOException e) {}

          g2.drawImage(cardback, 0, 0, this.width, this.height, null);


          //g2.fillRect(x+5, y+5, this.width-10, this.height-10);

          //color red

      }     
   }
}
