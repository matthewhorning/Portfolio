package view;

import model.Card;

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
   protected Card card;
   protected boolean showFace;
   protected String suiteString ="";
   protected String faceString = "";

   protected Color color;

   public CardIcon(int width, Card c)
   {
      this.showFace = true;
      this.width = width;
      this.height = (int)(width * 1.5);
      this.card = c;
      this.suiteString ="";
      this.faceString = "";
      String suit = c.getSuit();
      switch (suit)
      {
          case "SPADES":
              this.suiteString = "\u2660";
              this.color = Color.BLACK;
              break;
          case "CLUBS":
              this.suiteString = "\u2663";
              this.color = Color.BLACK;
              break;
          case "DIAMONDS":
              this.suiteString = "\u25C6";
              this.color = Color.RED;
              break;
          case "HEARTS":
              this.suiteString = "\u2665";
              this.color = Color.RED;
              break;
      }
      this.faceString = c.getStringValue();

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
            ImageIcon iconBg = new ImageIcon(this.getClass().getResource("assets/cardback.png"));
            //ImageIcon iconBg = new ImageIcon("assets/cardback.png");
           
            Image iconIm = iconBg.getImage();
            

            g2.drawImage(iconIm, 0, 0, this.width, this.height, null);
          //g2.setColor(Color.RED);
          //g2.fillRect(x+this.width/8, y+this.height/8, this.width*3/4, this.height*3/4);
      }     
   }
}