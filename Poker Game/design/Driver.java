import java.util.ArrayList;
import java.util.Scanner;

public class Driver
{
    //we can throw InterruptedException because there is only 1 thread
    public static void main(String []args) throws InterruptedException
    {
        Scanner input = new Scanner(System.in);

        User user = new User();
        Bot bot = new Bot();
        bot.setStrategy( new RandomStrategy() );

        Table table = new Table(user, bot);
        Deck deck = new Deck();

        GameLogic.determineDealer(deck, user, bot, table);

        Player dealer;
        Player non_dealer;

        boolean play = true;
        while (play)
        {
            dealer = table.getDealer();
            non_dealer = table.getNonDealer();
            System.out.println("\nDealer is: " + dealer);
            dealer.resetActions();
            non_dealer.resetActions();
            table.setRound(Round.PREFLOP);
            table.postBlinds();
            deck.dealPreFlop(user, bot);
            table.setCurrentPlayer(dealer);
            table.setNonCurrentPlayer(non_dealer);
            System.out.println("User's Hand: " + table.getUser().getHand() );
            GameLogic.beginRound(table);

            if  ( !table.handIsComplete() )
            {

                table.setRound(Round.FLOP);
                deck.dealFlop(table);
                System.out.println("Community Cards: ");
                table.printCommunityCards();
                dealer.resetActions();
                non_dealer.resetActions();
                table.setCurrentPlayer(non_dealer);
                table.setNonCurrentPlayer(dealer);
                GameLogic.beginRound(table);

                if ( !table.handIsComplete() )
                {
                    table.setRound(Round.TURN);
                    deck.dealTurn(table);
                    System.out.println("Community Cards: ");
                    table.printCommunityCards();
                    dealer.resetActions();
                    non_dealer.resetActions();
                    table.setCurrentPlayer(non_dealer);
                    table.setNonCurrentPlayer(dealer);
                    GameLogic.beginRound(table);
                    
                    if ( !table.handIsComplete() )
                    {
                        table.setRound(Round.RIVER);
                        deck.dealRiver(table);
                        System.out.println("Community Cards: ");
                        table.printCommunityCards();
                        dealer.resetActions();
                        non_dealer.resetActions();
                        table.setCurrentPlayer(non_dealer);
                        table.setNonCurrentPlayer(dealer);
                        GameLogic.beginRound(table);

                        if ( !table.handIsComplete() )
                        {
                            HandClassifier user_hand_classifier = new HandClassifier( user.hand.getCards(), table.getCommunityCards() );
                            HandStrength user_strength = user_hand_classifier.classify();

                            HandClassifier bot_hand_classifier = new HandClassifier( bot.hand.getCards(), table.getCommunityCards() );
                            HandStrength bot_strength = bot_hand_classifier.classify();

                            if (user_strength.compareTo(bot_strength) > 0)
                            {
                                table.pushToWinner(user);
                            }
                            else table.pushToWinner(bot);

                            table.setHandComplete(true);
                        }
                    }
                }
            }
            System.out.println("Play another hand? (y/n)");
            char newHand = input.next().charAt(0);
            if (newHand == 'y') 
            {
                play = true;   
                table.moveDealer();

                dealer.resetActions();
                dealer.getHand().clearHand();

                non_dealer.resetActions();
                non_dealer.getHand().clearHand();

                table.clearCommunityCards();
                table.setHandComplete(false);
            }
            else play = false;
        }
    }
}