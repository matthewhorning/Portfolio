package controller;

import model.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public interface PokerUI {
    public void setOnUserRaise(ActionListener raiseListener);
    public void setOnUserCheck(ActionListener checkListener);
    public void setOnUserFold(ActionListener foldListener);
    public void setOnUserCall(ActionListener callListener);
    public void setOnConfirm(ActionListener confirmListener);

    public void updateBotBet(int amount);
    public void updateUserBet(int amount);
    public void determineData();
    public void createTable();
    public void displayTable();

    public boolean playAgain(boolean userWin);
    public boolean handOver(int winner);

    public void setDealer(boolean isUser);
    public void addToCommunityHand(ArrayList<Card> cards);
    public void clearCommunityHand();
    public void updateHandPanel(Hand hand, boolean isUser, boolean showdown);
    public void updateUserPot(int amount);
    public void updateBotPot(int amount);
    public void updateCommunityPot(int amount);
    public int amountPrompt();
    public void disableFold();
    public void enableFold();
    public void disableCall();
    public void enableCall();
    public void disableRaise();
    public void enableRaise();
    public void disableCheck();
    public void enableCheck();
    public void enableAllActions();
	public void setSize(int i, int j);
	public String getBotLevel();
	public Table getTable();
	public void updateDealerIcon();
	public Bot getBot();
	public User getUser();
	public void determineRaiseAmount();
}