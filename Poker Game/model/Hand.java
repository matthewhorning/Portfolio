package model;

import java.util.ArrayList;

public class Hand
{
    private ArrayList<Card> cards;

    public Hand()
    {
        cards = new ArrayList<Card>();
    }

    /**
     * adds a card to the hand
     * @param c card to be added to hand
     */
    public void add(Card c)
    {
        cards.add(c);
    }

    /**
     * displays the hand in terminal
     */
    public void show()
    {
        for (Card c: cards)
        {
            System.out.println(c);
        }
    }

    /**
     * remove all cards from the hand
     */
    public void clearHand()
    {
        cards.clear();
    }

    /**
     * basic getter for the underlying arraylist
     * @return return arraylist that represents the hand
     */
    public ArrayList<Card> getCards()
    {
        return this.cards;
    }

    @Override 
    public String toString()
    {
        String result = "";
        for (Card card: cards)
        {
            result += card;
            result += " ";
        }
        return result;
    }
}
    
