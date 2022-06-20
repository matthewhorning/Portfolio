package tests;

import model.*;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class GameLogicTest {
    @Test
    public void actionOpenTrueCorrect()
    {
        Bot testBot = new Bot();
        User testUser = new User();
        Table testTable = new Table(testUser, testBot);
        testTable.updateBetAmount(false, 50);
        testTable.updateBetAmount(true, 25);
        testTable.setDealer(testUser);
        testTable.setNonDealer(testBot);
        testBot.setActedThisRound(true);
        testUser.setActedThisRound(true);
        assertTrue(GameLogic.actionOpen(testTable));
    }

    @Test
    public void actionOpenFalseCorrect()
    {
        Bot testBot = new Bot();
        User testUser = new User();
        Table testTable = new Table(testUser, testBot);
        testTable.updateBetAmount(false, 50);
        testTable.updateBetAmount(true, 50);
        testTable.setDealer(testUser);
        testTable.setNonDealer(testBot);
        testBot.setActedThisRound(true);
        testUser.setActedThisRound(true);
        assertFalse(GameLogic.actionOpen(testTable));
    }

    @Test
    public void getAvailableActionsBetEqualCorrect()
    {
        Bot testBot = new Bot();
        User testUser = new User();
        Table testTable = new Table(testUser, testBot);
        testTable.updateBetAmount(false, 50);
        testTable.updateBetAmount(true, 50);
        ArrayList<Action> testActions = new ArrayList<Action>();
        testActions.add(Action.CHECK);
        testActions.add(Action.RAISE);
        assertEquals(testActions, GameLogic.getAvailableActions(testTable));
    }

    @Test
    public void getAvailableActionsBetUnequalCorrect()
    {
        Bot testBot = new Bot();
        User testUser = new User();
        Table testTable = new Table(testUser, testBot);
        testTable.updateBetAmount(false, 50);
        testTable.updateBetAmount(true, 25);
        ArrayList<Action> testActions = new ArrayList<Action>();
        testActions.add(Action.CALL);
        testActions.add(Action.FOLD);
        testActions.add(Action.RAISE);
        assertEquals(testActions, GameLogic.getAvailableActions(testTable));
    }
}