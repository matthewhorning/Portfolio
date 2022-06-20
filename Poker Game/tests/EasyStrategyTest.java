package tests;

import model.*;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class EasyStrategyTest {
    @Test
    public void botActFoldCorrect()
    {
        Bot testBot = new Bot();
        User testUser = new User();
        Table testTable = new Table(testUser, testBot);
        testUser.addToStack(3000);
        testTable.updateBetAmount(false, 50);
        testTable.updateBetAmount(true, 300);
        testBot.setStrategy(new EasyStrategy());
        ArrayList<Action> available_actions = GameLogic.getAvailableActions(testTable);
        assertEquals(Action.FOLD, testBot.act(available_actions, testTable));
    }

    @Test
    public void botActCheckCorrect()
    {
        Bot testBot = new Bot();
        User testUser = new User();
        Table testTable = new Table(testUser, testBot);
        testTable.updateBetAmount(false, 50);
        testTable.updateBetAmount(true, 50);
        testBot.setStrategy(new EasyStrategy());
        ArrayList<Action> available_actions = GameLogic.getAvailableActions(testTable);
        assertEquals(Action.CHECK, testBot.act(available_actions, testTable));
    }
}