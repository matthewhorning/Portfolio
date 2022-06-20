package view;

import model.*;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import controller.PokerUI;

public class PokerTable implements PokerUI 
{
    protected int height = 950;
    protected int width = 1000;
    protected String botLevel;
    protected String userName;
    protected int userPot = 500; // Default starting amount
    protected int botPot = 500;
    protected String playerName;
    protected Bot bot;
    protected User user;
    protected Player dealer; // functions as ptr to player in dealer position
    protected Player non_dealer; // functions as ptr to player in non_dealer position
    protected Player current_player; // function as ptr to player whose turn it is
    protected Player non_current_player; // functions as ptr to player whose turn it is not

    private ArrayList<Card> community_cards;

    protected Table table;
    protected JFrame mainFrame;
    protected JPanel cardTable;
    protected JLabel communityPot;
    protected JLabel userMoney;
    protected JLabel botMoney;
    protected JLabel userBet;
    protected JLabel botBet;
    protected JButton getDataButton;
    protected JLabel numHandsPlayed;
    protected JButton raiseButton;
    protected JButton callButton;
    protected JButton checkButton;
    protected JButton foldButton;
    protected JTextField raiseTextField;
    protected JTextField botBuyInField;
    protected JTextField userBuyInField;
    protected JTextField userNameField;
    protected JComboBox<String> botIDBox;
    protected ImageLabel userDealer;
    protected ImageLabel botDealer;
    protected HandPanel userHandPanel = new HandPanel("User", width);
    protected HandPanel botHandPanel = new HandPanel("Bot", width);
    protected HandPanel communityPanel = new HandPanel("Community", width);

    public PokerTable(Table table) {
        this.table = table;
        this.raiseTextField = new JTextField();
    }

    public User getUser() {
        return table.getUser();
    }

    public Bot getBot() {
        return table.getBot();
    }

    public Table getTable() {
        return table;
    }

    public void setSize(int width, int height) {
        mainFrame.setPreferredSize(new Dimension(width, 1000));
    }

    public void createTable() {
        mainFrame = new JFrame("Shrek Hold'em");
        mainFrame.setPreferredSize(new Dimension(250, 200));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardTable = new JPanel();
        cardTable.setBackground(new Color(21, 53, 86));
        cardTable.setLayout(new BoxLayout(cardTable, BoxLayout.Y_AXIS));

        // Display the text Fields
        JLabel userNameLabel = new JLabel("User Name: ");
        userNameLabel.setForeground(Color.WHITE);
        userNameField = new JTextField();
        userNameField.setMaximumSize(new Dimension(300, 50));

        JLabel botIDLabel = new JLabel("Bot Type: ");
        botIDLabel.setForeground(Color.WHITE);

        String[] bot_ids = { "easy", "hard" };

        botIDBox = new JComboBox<String>(bot_ids);
        botIDBox.setMaximumSize(new Dimension(300, 50));

        ImageIcon iconA = new ImageIcon(this.getClass().getResource("assets/donkey.png"));
        ImageLabel botImageA = new ImageLabel(iconA);
        botImageA.setPreferredSize(new Dimension(width / 5, height / 5));
        ImageIcon iconB = new ImageIcon(this.getClass().getResource("assets/shrek.jpg"));
        ImageLabel botImageB = new ImageLabel(iconB);
        botImageB.setPreferredSize(new Dimension(width / 5, height / 7));

        getDataButton = new JButton("Confirm");

        String num_hands_string = "";
        try {
            File numHands = new File("numHands.txt");
            Scanner myReader = new Scanner(numHands);
            while (myReader.hasNextLine()) {
              num_hands_string = myReader.nextLine();
            }
            myReader.close();
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred reading.");
            e.printStackTrace();
          }

        numHandsPlayed = new JLabel("Total number of hands played: " + num_hands_string);
        numHandsPlayed.setForeground(Color.WHITE);


        cardTable.add(userNameLabel);
        cardTable.add(userNameField);
        cardTable.add(botIDLabel);
        cardTable.add(botIDBox);
        
        cardTable.add(getDataButton);
        cardTable.add(numHandsPlayed);

        mainFrame.add(cardTable);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    public void displayTable() {
        mainFrame.remove(cardTable);
        mainFrame.revalidate();
        mainFrame.repaint();

        cardTable = new JPanel();
        cardTable.setBackground(new Color(21, 53, 86));
        cardTable.setLayout(new BoxLayout(cardTable, BoxLayout.Y_AXIS));

        // Create dealer icons
        ImageIcon dealerIcon = new ImageIcon(this.getClass().getResource("assets/dealer_chip.png"));
        botDealer = new ImageLabel(dealerIcon);
        userDealer = new ImageLabel(dealerIcon);

        // Create bot panel
        JPanel botPanel = new JPanel();
        botPanel.setOpaque(false);
        botPanel.setPreferredSize(new Dimension(width, height / 4));

        // Create bot image panel
        JPanel botImagePanel = new JPanel();
        botImagePanel.setPreferredSize(new Dimension(width / 3, height / 5));
        botImagePanel.setOpaque(false);
        // Determine which icon to use
        String iconString = "";


        if (this.botLevel.equals("hard")) {
            iconString = "assets/ultimateshrek.png";
        } else {
            iconString = "assets/donkey.png";
        }

        // Create the bot image
        ImageIcon icon = new ImageIcon(this.getClass().getResource(iconString));
        ImageLabel botImage = new ImageLabel(icon);
        botImage.setOpaque(false);
        botImage.setPreferredSize(new Dimension(width / 4, height / 5));
        this.botMoney = new JLabel(Integer.toString(botPot));
        this.botMoney.setForeground(Color.WHITE);
        this.botBet = new JLabel("You have bet: 0");
        this.botBet.setForeground(Color.WHITE);
        botImagePanel.add(this.botMoney);
        botImagePanel.add(this.botDealer);
        botImagePanel.add(this.botBet);
        botImagePanel.add(botImage);

        // Create the bot hand panel
        if (this.botLevel.equals("easy")) {
            botHandPanel = new HandPanel("Donkey", width);
        } else {
            botHandPanel = new HandPanel("Shrek", width);
        }

        // botHandPanel = new HandPanel("Bot", width);
        this.botHandPanel.setPreferredSize(new Dimension(width / 3, height / 3));

        updateHandPanel(table.getBot().getHand(), false, false);
        botPanel.add(botHandPanel);
        botPanel.add(botImagePanel);

        // Create the middle handPanel for community cards
        JPanel middlePanel = new JPanel();
        middlePanel.setPreferredSize(new Dimension(width, height / 5));
        middlePanel.setOpaque(false);
        this.communityPot = new JLabel("0");
        this.communityPot.setForeground(Color.WHITE);
        middlePanel.add(communityPot);
        this.communityPanel = new HandPanel("", width);
        communityPanel.setOpaque(false);
        middlePanel.add(communityPanel);

        // Create the User Panel
        JPanel userPanel = new JPanel();
        userPanel.setPreferredSize(new Dimension(width, height / 3));
        userPanel.setOpaque(false);

        // Create the user Money Panel
        JPanel userMoneyPanel = new JPanel();
        userMoneyPanel.setPreferredSize(new Dimension(width / 3, height / 3));
        userMoneyPanel.setOpaque(false);
        this.userMoney = new JLabel(Integer.toString(userPot));
        this.userMoney.setForeground(Color.WHITE);
        this.userBet = new JLabel("You have bet: 0");
        this.userBet.setForeground(Color.WHITE);
        userMoneyPanel.add(this.userMoney);
        userMoneyPanel.add(this.userDealer);
        userMoneyPanel.add(this.userBet);
        raiseButton = new JButton("Raise");
        callButton = new JButton("Call");
        checkButton = new JButton("Check");
        foldButton = new JButton("Fold");
        raiseTextField.setPreferredSize(new Dimension(75,25));

        userMoneyPanel.add(Box.createHorizontalStrut(20));
        userMoneyPanel.add(checkButton);
        userMoneyPanel.add(callButton);
        userMoneyPanel.add(foldButton);
        userMoneyPanel.add(Box.createVerticalStrut(20));
        userMoneyPanel.add(raiseButton);
        userMoneyPanel.add(raiseTextField);

        // Create user hand panel
        this.userHandPanel = new HandPanel(userName, width);
        this.userHandPanel.setPreferredSize(new Dimension(width / 3, height / 3));
        updateHandPanel(table.getUser().getHand(), true, false);
        updateHandPanel(table.getBot().getHand(), false, false);

        userPanel.add(this.userHandPanel);
        userPanel.add(userMoneyPanel);

        cardTable.add(botPanel);
        cardTable.add(middlePanel);
        cardTable.add(userPanel);
        mainFrame.add(cardTable);
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.revalidate();

    }

    public boolean playAgain(boolean userWin) {
        String r = "LOSE";
        if (userWin == true) {
            r = "WIN";
        }
        int choice = JOptionPane.showConfirmDialog(mainFrame, "You " + r + " the game! Play Again?", "Results",
                JOptionPane.YES_NO_OPTION);
        return choice == JOptionPane.YES_OPTION;
    }

    public boolean handOver(int winner) {
        String r = "LOSE";
        if (winner == 0) r = "WIN";
        if (winner == 1) r = "LOSE";
        if (winner == 2) r = "TIE";
        int choice = JOptionPane.showConfirmDialog(mainFrame, "You " + r + " the hand. Play Again?", "Results",
                JOptionPane.YES_NO_OPTION);
        return choice == JOptionPane.YES_OPTION;
    }

    public void setDealer(boolean isUser) {
        if (isUser) {
            this.userDealer.setVisible(true);
            this.botDealer.setVisible(false);
        } else {
            this.userDealer.setVisible(true);
            this.botDealer.setVisible(false);
        }
    }

    /**
     * Adds a card to the community hand panel
     * 
     * @param c card C
     */
    public void addToCommunityHand(ArrayList<Card> cards) {
        this.communityPanel.clearCards();
        for (Card c : cards) {
            this.communityPanel.add(c, true);
        }
    }

    /**
     * Clears the community hand panel
     */
    public void clearCommunityHand() {
        this.communityPanel.clearCards();
        communityPanel.revalidate();
        communityPanel.repaint();
    }

    public void updateDealerIcon() {
        if (table.getDealer() == table.getUser()) {
            userDealer.setVisible(true);
            botDealer.setVisible(false);
        } else {
            userDealer.setVisible(false);
            botDealer.setVisible(true);
        }
    }

    /**
     * Updates the HandPanel to display the correct cards
     * 
     * @param hand
     * @param isUser
     */
    public void updateHandPanel(Hand hand, boolean isUser, boolean showdown) {
        if (isUser) {
            this.userHandPanel.clearCards();
        } else {
            this.botHandPanel.clearCards();
        }

        for (Card c : hand.getCards()) {
            // Add to HandPanel
            if (isUser) {
                userHandPanel.add(c, true);
            } else {
                if (showdown) botHandPanel.add(c, true);
                else botHandPanel.add(c, false);
            }
        }

        if (isUser) {
            userHandPanel.revalidate();
            userHandPanel.repaint();
        } else {
            botHandPanel.revalidate();
            botHandPanel.repaint();
        }

    }

    public String getBotLevel() {
        return this.botLevel;
    }

    /**
     * Updates the UserPot JLabel
     * 
     * @param amount
     */
    public void updateUserPot(int amount) {
        userMoney.setText("Money Left: " + Integer.toString(amount) + "  |  ");
    }

    /**
     * Updates the BotPot JLabel
     * 
     * @param amount
     */
    public void updateBotPot(int amount) {
        botMoney.setText("Money Left: " + Integer.toString(amount) + "  |  ");
    }

    /**
     * Updates the CommunityPot JLabel
     * 
     * @param amount
     */
    public void updateCommunityPot(int amount) {
        communityPot.setText("POT: " + Integer.toString(amount));
    }

    public void determineRaiseAmount()
    {   int amount = 0;
        try{
            amount = Integer.parseInt( raiseTextField.getText() );
        }
        catch (NumberFormatException e) {
            System.out.println("raise textfield empty");
        }
        if (amount <= Integer.max(table.getBotBet(), table.getUserBet() ) ) amount = Integer.max(2, 2 * Integer.max(table.getBotBet(), table.getUserBet() ) );
        if (amount > table.getUser().getStack() + table.getUserBet() ) amount = table.getUser().getStack() + table.getUserBet();
        table.setRaiseAmount( amount );
    }

    public void determineData() {
        // Get the bots number
        String botStringID = (String) botIDBox.getSelectedItem();
        
        botLevel = botStringID;
        userName = userNameField.getText();
    }

    @Override
    public void setOnConfirm(ActionListener l) {
        getDataButton.addActionListener(l);
    }

    @Override
    public void setOnUserRaise(ActionListener l) {
        raiseButton.addActionListener(l);

    }

    @Override
    public void setOnUserCheck(ActionListener l) {
        checkButton.addActionListener(l);
    }

    @Override
    public void setOnUserFold(ActionListener l) {
        foldButton.addActionListener(l);
    }

    @Override
    public void setOnUserCall(ActionListener l) {
        callButton.addActionListener(l);
    }

    public ArrayList<Card> getCommunityCards() {
        return community_cards;
    }

    @Override
    public void updateBotBet(int amount) {
        this.botBet.setText("Amount bet:  "+ Integer.valueOf(amount) + " ");

    }

    @Override
    public void updateUserBet(int amount) {
        this.userBet.setText("Amount bet:  " + Integer.valueOf(amount) + " ");
    }

    @Override
    public int amountPrompt() {

        int amount = 0;
        String choice = JOptionPane.showInputDialog(mainFrame, "Enter Amount: ");
        try {
            amount = Integer.parseInt(choice);
        } catch (NumberFormatException e) {
        }
        return amount;
    }

    @Override
    public void disableFold() {
        foldButton.setEnabled(false);
    }

    @Override
    public void enableFold() {
        foldButton.setEnabled(true);
    }

    @Override
    public void disableCall() {
        callButton.setEnabled(false);
    }

    @Override
    public void enableCall() {
        callButton.setEnabled(true);
    }

    @Override
    public void disableRaise() {
        raiseButton.setEnabled(false);
    }

    @Override
    public void enableRaise() {
        raiseButton.setEnabled(true);
    }

    @Override
    public void disableCheck() {
        checkButton.setEnabled(false);
    }

    @Override
    public void enableCheck() {
        checkButton.setEnabled(true);
    }

    @Override
    public void enableAllActions() {
        checkButton.setEnabled(true);
        raiseButton.setEnabled(true);
        callButton.setEnabled(true);
        foldButton.setEnabled(true);
    }
}