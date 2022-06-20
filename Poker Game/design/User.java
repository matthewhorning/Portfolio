import java.util.ArrayList;
import java.util.Scanner;

public class User extends Player
{
    public Action act(ArrayList<Action> available_actions, Table table)
    {
        Action selected_action = Action.RAISE;

        Scanner input = new Scanner(System.in);
        System.out.println("\nPlease enter one of the available actions: ");
        for (Action action : available_actions)
        {
            if (action == Action.FOLD) System.out.print("(f) ");
            if (action == Action.CHECK) System.out.print("(x) ");
            if (action == Action.CALL) System.out.print("(c) ");
            if (action == Action.RAISE) System.out.print("(r) ");
            System.out.print(action + " | ");
        }
        System.out.println();

        char action = input.next().charAt(0); //currently no input validation
        switch (action)
        {
            case 'f':
                selected_action = Action.FOLD;
                break;
            case 'x':
                selected_action = Action.CHECK;
                break;
            case 'c':
                selected_action = Action.CALL;
                break;
            case 'r':
                selected_action = Action.RAISE;
                System.out.println("How much would you like to raise to? ");
                int raise_amount = input.nextInt();
                table.setRaiseAmount(raise_amount);
                break;
        }
       // input.close();
        
        acted_this_round = true;
        return selected_action;
    }

    public void resetActedThisRound()
    {
        acted_this_round = false;
    }

    @Override
    public String toString()
    {
        return "user";
    }
}