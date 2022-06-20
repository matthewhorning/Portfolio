package model;

import java.util.ArrayList;
import java.util.Random;

public class Deck
{
   private ArrayList<Card> cards;
   Random randomizer;

   public Deck()
   {
     cards = new ArrayList<Card>();
     for (int i = 2; i <= 14; i++)
     {
         cards.add(new Card(i, 's'));
         cards.add(new Card(i, 'h'));
         cards.add(new Card(i, 'c'));
         cards.add(new Card(i, 'd'));
     }
     randomizer = new Random();
     this.shuffle();
   }

   /**
    * Shuffles the deck in random order 
    */
   public void shuffle()
   {
       for (int i = 0; i < cards.size(); i++)
       {
           int swap_with = randomizer.nextInt(cards.size());
           Card a = cards.get(i);
           cards.set(i, cards.get(swap_with));
           cards.set(swap_with, a);
       }
   }

   /**
    * Deals one card to the given player
    * @param p the player who gets one card from the top of the deck
    */
   public void dealToPlayer(Player p)
   {
       Card c = cards.remove(cards.size()-1);
       p.addToHand(c);
   }

   /**
    * Deals two cards to each player for the preflop betting round
    * @param u @param b the players who gets two cards from the top of the deck
    */
    public void dealPreFlop(Player u, Player b)
    {
        Card c;
        for (int i = 1; i <= 2; i++)
        {
            c = cards.remove(cards.size()-1);
            u.addToHand(c);

            c = cards.remove(cards.size()-1);
            b.addToHand(c);
        }
    }

   /**
    * Deals three cards to the table for the Flop betting round
    * @param t the table who gets three cards from the top of the deck
    */
   public void dealFlop(Table t)
   {
       for (int i = 1; i <= 3; i++)
       {
           Card c = cards.remove(cards.size()-1);
           t.addToTable(c);
       }
   }

   /**
    * Deals one card to the table for the Turn betting round
    * @param t the table who gets one card from the top of the deck 
    */
    public void dealTurn(Table t)
    {
        Card c = cards.remove(cards.size()-1);
        t.addToTable(c);
    }

    /**
     * Deals one card to the table for the River betting round
     * functionally equivalent to this.dealTurn(Table t)
     * @param t the table who gets one card from the top of the deck
     */
    public void dealRiver(Table t)
    {
        dealTurn(t);
    }

}
