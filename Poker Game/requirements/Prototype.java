import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


public class Prototype
{
    private int frameSize = 600;
    private int width = 900;
    private int height = 600;
    private JFrame mainFrame;

    public Prototype(int size)
    {
        ImageIcon dealer_chip = new ImageIcon("assets/dealer_chip.png");
        this.frameSize = size;

        // top level frame for the game
        mainFrame = new JFrame("Texas Hold'em");
        mainFrame.setPreferredSize(new Dimension(width, height));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // top level panel representing our card table
        JPanel cardTable = new JPanel();
        cardTable.setLayout(new BoxLayout(cardTable, BoxLayout.Y_AXIS));

        // panel to show NPC's hand
        HandPanel npcHand = new HandPanel("NPC", height/2);
        npcHand.add(new Card(2, Card.Suite.CLUBS), false);
        npcHand.add(new Card(11, Card.Suite.DIAMONDS), false);

        JPanel topFrame = new JPanel();
        topFrame.setBackground(new Color(0,101,77));
        topFrame.setPreferredSize(new Dimension(200, 200));

        topFrame.add(npcHand);

        HandPanel midPanel = new HandPanel("CARDS IN PLAY", height/2);
        //midPanel.setPreferredSize(new Dimension(300, frameSize/3));
        midPanel.add(new Card(14, Card.Suite.DIAMONDS), true);
        midPanel.add(new Card(12, Card.Suite.SPADES), true);
        midPanel.add(new Card(11, Card.Suite.HEARTS), true);
        midPanel.add(new Card(4, Card.Suite.SPADES), true);
        midPanel.add(new Card(3, Card.Suite.HEARTS), true);

        JPanel npcMoneyPanel = new JPanel();
        npcMoneyPanel.setBackground(new Color(0,101,77));
        JLabel labe1 = new JLabel("NPC has: $500");
        labe1.setForeground(Color.WHITE);
        npcMoneyPanel.add(labe1);

        topFrame.add(npcMoneyPanel);
 
        FlowLayout experimentLayout = new FlowLayout();
        topFrame.setLayout(experimentLayout);

        JPanel moneyPanel = new JPanel();
        moneyPanel.setBackground(new Color(0,101,77));
        JLabel labe = new JLabel("Money in play: 500");
        labe.setForeground(Color.WHITE);
        moneyPanel.add(labe);
        labe.setText("Money in play: 500");
        
        moneyPanel.setPreferredSize(new Dimension(200, 200));

        JPanel midFrame = new JPanel();
        midFrame.setBackground(new Color(0,101,77));
        midFrame.setPreferredSize(new Dimension(200, 200));

        midFrame.setLayout(experimentLayout);

        midFrame.add(moneyPanel);
        midFrame.add(midPanel);

        JPanel bottomFrame = new JPanel();
        bottomFrame.setBackground(new Color(0,101,77));
        bottomFrame.setPreferredSize(new Dimension(200, 200));

        // label to show dealer chip
        ImageLabel dealer = new ImageLabel(dealer_chip);
        //dealer.setPreferredSize(new Dimension(25,25));

        // panel to show user's hand
        HandPanel userHand = new HandPanel("YOU", height/2);
        userHand.add(new Card(14, Card.Suite.CLUBS), true);
        userHand.add(new Card(12, Card.Suite.CLUBS), true);

        JPanel userMoneyPanel = new JPanel();
        userMoneyPanel.setBackground(new Color(0,101,77));
        JLabel labe2 = new JLabel("You have: $500");
        labe2.setForeground(Color.WHITE);
        userMoneyPanel.add(labe2);
        
        cardTable.add(topFrame);
        cardTable.add(midFrame);
        bottomFrame.add(dealer);
        bottomFrame.add(userHand);
        bottomFrame.add(userMoneyPanel);

        // panel for buttons
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(0, 101, 77));
        JButton call = new JButton("Call");
        JButton check = new JButton("Check");
        JButton fold = new JButton("Fold");
        JButton raise = new JButton("Raise");

        //box layout for control panel
        BoxLayout boxLayout1 = new BoxLayout(controlPanel, BoxLayout.Y_AXIS);
        controlPanel.setLayout(boxLayout1);

        controlPanel.add(call);
        controlPanel.add(check);
        controlPanel.add(fold);
        controlPanel.add(raise);
        bottomFrame.add(controlPanel);
        bottomFrame.setLayout(experimentLayout);

        cardTable.add(bottomFrame);

        // place card table into the top level frame
        mainFrame.add(cardTable);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    /**
     * Do not remove this meethod
     */
    public JFrame getFrame()
    {
        return mainFrame;
    }
}
