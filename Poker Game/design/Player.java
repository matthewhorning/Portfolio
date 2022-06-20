import java.util.ArrayList;

public abstract class Player 
{
    protected int stack;
    protected Hand hand;
    protected boolean acted_this_round;

    public Player()
    {
        this.stack = 200;
        this.hand = new Hand();
        this.acted_this_round = false;
    }

    public void resetActions()
    {
        acted_this_round = false;
    }

    public boolean actedThisRound()
    {
        return acted_this_round;
    }

    public int getStack()
    {
        return stack;
    }

    /**
     * adds card to player's hand
     * @param c card to be added to player's hand
     */
    public void addToHand(Card c)
    {
        hand.add(c);
    }

    /**
     * removes chips from Player's stack 
     * @param amount amount of chips player bet that needs to be subtracted from stack size
     */
    public void removeFromStack(int amount)
    {
        this.stack -= amount;
    }

    /**
     * add chips to Player's stack
     * @param amount amount of chips to be added to Player's stack
     */
    public void addToStack(int amount)
    {
        this.stack += amount;
    }

    /**
     * basic getter for Player's hand
     */
    public Hand getHand()
    {
        return hand;
    }

    public abstract Action act(ArrayList<Action> available_actions, Table table);


}   