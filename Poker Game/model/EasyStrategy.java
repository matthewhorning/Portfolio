package model;

import java.util.ArrayList;

public class EasyStrategy implements BotStrategy
{
    @Override 
    public Action botAct(ArrayList<Action> available_actions, Table table)
    {
        if (table.getUserBet() > table.getBot().getStack()/3)
        {
            Action bot_action = Action.FOLD;
            return bot_action;
        }

        return available_actions.get(0);
    }

    @Override
    public int getLevel()
    {
        return 0;
    }
}