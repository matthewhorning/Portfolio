package model;

import java.util.ArrayList;
import view.*;

public class Table 
{
    private int numHands;

    private User user;
    private Bot bot;
    private Player dealer; // functions as ptr to player in dealer position
    private Player non_dealer; // functions as ptr to player in non_dealer position
    private Player current_player; // function as ptr to player whose turn it is
    private Player non_current_player; // functions as ptr to player whose turn it is not
    private boolean all_in_called;

    private ArrayList<Card> community_cards;

    private int user_bet; //amount committed by player in current round
    private int bot_bet; // amount commited by bot in current round
    private int pot; 
    private int raise_amount;

    private Round current_round;
    private boolean hand_complete;
    private PokerTable viewTable;

    public Table(User u, Bot b)
    {
        this.numHands = 0;

        this.user = u;
        this.bot = b;
        this.dealer = null;
        this.non_dealer = null;
        this.current_player = null;
        
        this.community_cards = new ArrayList<Card>();

        this.user_bet = 0;
        this.bot_bet = 0;
        this.pot = 0;
        this.raise_amount = 0;
        
        this.current_round = null;
        this.hand_complete = false;
        this.all_in_called = false;
    }

    public ArrayList<Card> getCommunityCards()
    {
        return community_cards;
    }

    public void clearCommunityCards()
    {
        community_cards.clear();
    }

    public Bot getBot()
    {
        return bot;
    }

    public User getUser()
    {
        return user;
    }

    public int getPot()
    {
        return pot;
    }

    public boolean handIsComplete()
    {
        return hand_complete;
    }

    public void setHandComplete(boolean b)
    {
        hand_complete = b;
    }

    public int getRaiseAmount()
    {
        return raise_amount;
    }

    public void clearRaiseAmount()
    {
        raise_amount = 0;
    }
    public void setRaiseAmount(int amount)
    {
        raise_amount = amount;
    }

    public void setAllInCalled(boolean status)
    {
        this.all_in_called = status;
    }

    public boolean getAllInCalled()
    {
        return all_in_called;
    }

    public void swapCurrentandNonCurrentPlayers()
    {
        Player temp = current_player;
        current_player = non_current_player;
        non_current_player = temp;
    }

    public Player getNonCurrentPlayer()
    {
        return non_current_player;
    }

    public void setNonCurrentPlayer(Player p)
    {
        this.non_current_player = p;
    }
    
    /**
     * get the player whose turn it is
     * @return player whose turn it is
     */
    public Player getCurrentPlayer()
    {
        return current_player;
    }
    

    /**
     * sets current_player to player whose turn it is
     * @param p player to set current_player to
     */
    public void setCurrentPlayer(Player p)
    {
        this.current_player = p;
    }
    
    /**
     * gets bot_bet which represents the current amount of chips committed to the round by the bot
     * @return bot_bet
     */
    public int getBotBet()
    {
        return bot_bet;
    }

    /**
     * gets user_bet which represents the current amount of chips committed to the round by the user
     * @return user_bet 
     */
    public int getUserBet()
    {
        return user_bet;
    }

    /**
     * Initializes user_bet and bot_bet for the start of a hand
     */
    public void postBlinds()
    {
        numHands++;

        if ( getDealer() instanceof User)
        {
            this.user_bet = 1;
            user.removeFromStack(1);
            this.bot_bet = 2;
            bot.removeFromStack(2);
        }
        else
        {
            this.bot_bet = 1;
            bot.removeFromStack(1);
            this.user_bet = 2;
            user.removeFromStack(2);
        }
    }

    /**
     * Returns the current round
     * @param round
     */
    public Round getCurrentRound()
    {
        return this.current_round;
    }

    /**
     * Sets round to given round
     * @param round round to set private member to
     */
    public void setRound(Round round)
    {
        this.current_round = round;
    }

    /**
     * Puts player into dealer position
     * @param p player to be put into dealer position
     */
    public void setDealer(Player p)
    {
        this.dealer = p;
    }

    /**
     * Puts player into non-dealer position
     * @param p player to but put into non-dealer position
     */
    public void setNonDealer(Player p)
    {
        this.non_dealer = p;
    }

    /**
     * returns the player not in dealer position
     */
    public Player getNonDealer()
    {
        return non_dealer;
    }

    /**
     * returns the player in dealer position
     */
    public Player getDealer()
    {
        return dealer;
    }

    /**
     * Flips dealer and non_dealer
     */
    public void moveDealer()
    {
        Player temp = dealer;
        dealer = non_dealer;
        non_dealer = temp;
    }

    public void printCommunityCards()
    {
        for (Card card : community_cards)
        {
            System.out.print(card + " ");
        }
    }

    /**
     * adds a single card to the community cards on the table
     * @param c card to be added to the community cards
     */
    public void addToTable(Card c)
    {
        community_cards.add(c);
    }

    /**
     * updates the current amount committed for a given Player
     * @param user if true update user_bet; else update bot_bet
     * @param amount value that either user_bet or bot_bet takes
     */
    public void updateBetAmount(boolean user, int amount)
    {
        if (user)
        {
            user_bet = amount;
        }
        else
        {
            bot_bet = amount;
        }
    }

    /**
     * updates the pot amount and committed amounts when a betting round has ended
     */
    public void pushToPot()
    {
        pot += user_bet + bot_bet;
        user_bet = 0;
        bot_bet = 0;
    }

    public int getNumHands()
    {
        return numHands;
    }
    
    public void reset()
    {
        numHands = 0;
        pot = 0;
        user_bet = 0;
        bot_bet = 0;
        raise_amount = 0;
        community_cards = new ArrayList<Card>();
    }
    /**
     * moves the chips from the pot to the winning player's stack
     * @param p winning player
     */
    public void pushToWinner(Player p)
    {
        p.addToStack(pot);
        pot = 0;
    }

    public void pushToBoth(Player user, Player bot)
    {
        int user_half = pot/2;
        pot -= pot/2;

        user.addToStack(user_half);
        bot.addToStack(pot);

        pot = 0;
    }
}