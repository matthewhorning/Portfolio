import java.util.ArrayList;

public interface BotStrategy
{
    public Action botAct(ArrayList<Action> available_actions, Table table);
}