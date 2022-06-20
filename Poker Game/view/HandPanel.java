package view;

import model.Card;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;

public class HandPanel extends JPanel
{
    private JPanel labelPanel;
    private JPanel cardPanel;
    private int width;

    public HandPanel(String title, int width)
    {
        this.setBackground(new Color(21,53,86));
        this.width = width;
        labelPanel = new JPanel();
        labelPanel.setOpaque(false);
        cardPanel = new JPanel();
        cardPanel.setOpaque(false);
        this.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        labelPanel.add(titleLabel);

        this.add(labelPanel, BorderLayout.NORTH);
        this.add(cardPanel, BorderLayout.CENTER);
        
    }
    public void clearCards()
    {
        cardPanel.removeAll();
    }
    public void add(Card c, boolean show)
    {
        CardIcon cardIcon = new CardIcon(width/10, c);
        cardIcon.setShowFace(show);

        JLabel cardLabel = new JLabel(cardIcon);
        cardPanel.add(cardLabel);
        cardPanel.revalidate();
        cardPanel.repaint();
    }
}
