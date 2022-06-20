import java.util.ArrayList;
import java.lang.Math;

public class GameLogic 
{
    public static void determineDealer(Deck deck, Player user, Player bot, Table table)
    {
        deck.dealToPlayer(user);
        deck.dealToPlayer(bot);
        
        Card user_card = user.getHand().getCards().remove(0);
        Card bot_card = bot.getHand().getCards().remove(0);

        Player dealer = user_card.compareTo(bot_card)>0 ? user:bot;
        table.setDealer(dealer);
        Player non_dealer = user_card.compareTo(bot_card)>0 ? bot:user;
        table.setNonDealer(non_dealer);
    }
    
    public static void beginRound(Table table)
    {
        System.out.println( "\nROUND: " + table.getCurrentRound() );
        while ( actionOpen(table) && !table.handIsComplete() )
        {
            System.out.println("\nPot: " + table.getPot() + " | User bet: " + table.getUserBet() + " | Bot bet: " + table.getBotBet() );
            System.out.println("User stack: " + table.getUser().getStack() );
            System.out.println("Bot stack: " + table.getBot().getStack() );
            Player current_player = table.getCurrentPlayer();
            ArrayList<Action> available_actions = GameLogic.getAvailableActions(table);

            int difference = Math.abs( table.getBotBet() - table.getUserBet() );
            int max_bet = Integer.max( table.getUserBet(), table.getBotBet() );
            Action selected_action = current_player.act(available_actions, table);

            switch (selected_action)
            {
                case FOLD:
                    table.pushToPot();
                    table.pushToWinner( table.getNonCurrentPlayer() );
                    table.setHandComplete(true);
                    break;
                case CHECK:
                    break;
                case CALL:
                    current_player.removeFromStack(difference);
                    if (current_player instanceof User) table.updateBetAmount(true, max_bet);
                    else table.updateBetAmount(false, max_bet);
                    break;
                case RAISE:
                    if (current_player instanceof User)
                    {
                        current_player.removeFromStack( table.getRaiseAmount() - table.getUserBet());
                        table.updateBetAmount(true, table.getRaiseAmount());
                        table.clearRaiseAmount();
                    }
                    if (current_player instanceof Bot)
                    {
                        current_player.removeFromStack( table.getRaiseAmount() - table.getBotBet());
                        table.updateBetAmount(false, table.getRaiseAmount() );
                        table.clearRaiseAmount();
                    }
            }
            table.swapCurrentandNonCurrentPlayers();
        }
        table.pushToPot();
    }

    public static ArrayList<Action> getAvailableActions(Table table)
    {
        ArrayList<Action> available_actions = new ArrayList<Action>();

        int user_bet = table.getUserBet();
        int bot_bet = table.getBotBet();

        if (bot_bet == user_bet)
        {
            available_actions.add(Action.CHECK);
            available_actions.add(Action.RAISE);
        } 
        else
        {
            available_actions.add(Action.CALL);
            available_actions.add(Action.FOLD);
            available_actions.add(Action.RAISE);
        }

        return available_actions;
    }

    public static boolean actionOpen(Table table)
    {
        boolean action_open = true;
        boolean both_acted = ( table.getDealer().actedThisRound() && table.getNonDealer().actedThisRound() );
        if ( table.getBotBet() == table.getUserBet() && both_acted ) action_open = false;
        return action_open;
    }
}