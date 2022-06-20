package tests;

import model.*;

import org.junit.*;
import static org.junit.Assert.*;

public class TableTest {
    @Test
    public void setCurrentPlayerCorrect()
    {
        Bot testBot = new Bot();
        User testUser = new User();
        Table testTable = new Table(testUser, testBot);
        testTable.setCurrentPlayer(testUser);
        assertEquals(testUser, testTable.getCurrentPlayer());
    }

    @Test
    public void setNonCurrentPlayerCorrect()
    {
        Bot testBot = new Bot();
        User testUser = new User();
        Table testTable = new Table(testUser, testBot);
        testTable.setNonCurrentPlayer(testUser);
        assertEquals(testUser, testTable.getNonCurrentPlayer());
    }

    @Test
    public void swapCurrentAndNonCurrentPlayersCorrect()
    {
        Bot testBot = new Bot();
        User testUser = new User();
        Table testTable = new Table(testUser, testBot);
        testTable.setCurrentPlayer(testUser);
        testTable.setNonCurrentPlayer(testBot);
        testTable.swapCurrentandNonCurrentPlayers();
        assertEquals(testBot, testTable.getCurrentPlayer());
    }

    @Test
    public void updateBetAmountCorrect()
    {
        Bot testBot = new Bot();
        User testUser = new User();
        Table testTable = new Table(testUser, testBot);
        testTable.updateBetAmount(true, 50);
        assertEquals(50, testTable.getUserBet());
    }

    @Test
    public void pushToPotCorrect()
    {
        Bot testBot = new Bot();
        User testUser = new User();
        Table testTable = new Table(testUser, testBot);
        testTable.updateBetAmount(true, 50);
        testTable.updateBetAmount(false, 50);
        testTable.pushToPot();
        assertEquals(100, testTable.getPot());
    }

    @Test
    public void postBlindsUserDealsCorrect()
    {
        Bot testBot = new Bot();
        User testUser = new User();
        Table testTable = new Table(testUser, testBot);
        testTable.setDealer(testUser);
        testTable.postBlinds();
        assertEquals(199, testUser.getStack());
    }

    @Test
    public void postBlindsBotDealsCorrect()
    {
        Bot testBot = new Bot();
        User testUser = new User();
        Table testTable = new Table(testUser, testBot);
        testTable.setDealer(testBot);
        testTable.postBlinds();
        assertEquals(198, testUser.getStack());
    }

    @Test
    public void moveDealerCorrect()
    {
        Bot testBot = new Bot();
        User testUser = new User();
        Table testTable = new Table(testUser, testBot);
        testTable.setDealer(testBot);
        testTable.setNonDealer(testUser);
        testTable.moveDealer();
        assertEquals(testUser, testTable.getDealer());
    }

    @Test
    public void pushToWinnerCorrect()
    {
        Bot testBot = new Bot();
        User testUser = new User();
        Table testTable = new Table(testUser, testBot);
        testTable.updateBetAmount(true, 50);
        testTable.updateBetAmount(false, 50);
        testTable.pushToPot();
        testTable.pushToWinner(testUser);
        assertEquals(300, testUser.getStack());
    }
}