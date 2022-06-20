import java.util.ArrayList;

public class Bot extends Player
{
    private BotStrategy strategy;
    
    public void setStrategy(BotStrategy s)
    {
        strategy = s;
    }

    public Action act(ArrayList<Action> available_actions, Table table)
    {
        Action action = strategy.botAct(available_actions, table);
        
        acted_this_round = true;
        
        return action;
    }
    
    @Override
    public String toString()
    {
        return "bot";
    }
}