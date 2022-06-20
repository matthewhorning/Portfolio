import java.util.ArrayList;
import java.util.Random;

public class RandomStrategy implements BotStrategy
{
    @Override 
    public Action botAct(ArrayList<Action> available_actions, Table table)
    {
        Random random = new Random();
        int size = available_actions.size();
        int random_index = random.nextInt(size);
        Action random_action = available_actions.remove(random_index);
        if (random_action == Action.RAISE) 
        {
            int raise_amount = ( Integer.max(table.getUserBet(), 1) * 3);
            table.getBot().removeFromStack(raise_amount - table.getBotBet() );
            table.setRaiseAmount( raise_amount);
            table.updateBetAmount(false, raise_amount);
        }
        
        
        System.out.println("\nBot's action: " + random_action);
        return random_action;
    }
}   