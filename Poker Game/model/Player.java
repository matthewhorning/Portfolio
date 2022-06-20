package model;

import java.util.ArrayList;

public abstract class Player 
{
    protected int stack;
    protected Hand hand;
    protected boolean acted_this_round;
    protected Action currentAction;
    protected boolean hasActed;

    public Player()
    {
        this.stack = 200;
        this.hand = new Hand();
        this.acted_this_round = false;
    }

    public void resetHand()
    {
        hand = new Hand();
    }

    public void setCurrentAction(Action a)
    {
        currentAction = a;
        hasActed = true;
    }

    public void resetActions()
    {
        setActedThisRound(false);
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
     * changes players acted_this_round property
     * @param b boolean to set acted_this_round to
     */
    public void setActedThisRound(boolean b)
    {
        acted_this_round = b;
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

    public void reset()
    {
        this.stack = 200;
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