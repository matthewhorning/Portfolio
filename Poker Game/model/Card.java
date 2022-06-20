package model;

import java.util.Comparator;

public class Card implements Comparable<Card>
{
   int value;
   char suit;

   public Card(int v, char s)
   {
       this.value = v;
       this.suit = s;
   }

   public String getSuit()
   {
       if (suit == 's') {return "SPADES";}
       else if (suit == 'h') {return "HEARTS";}
       else if (suit == 'c') {return "CLUBS";}
       else {return "DIAMONDS";}
   }

   /**
    * A check to see if the card is an Ace
    * @return true if card is an ace, false otherwise
    */
   public boolean isAce()
   {
       return value == 14;
   }

   /**
    * A check to see if the card has a value of 10 
    * (either a face card or a 10-card)
    * @return true if the card's value is 10, false otherwise
    */
   public boolean isTen()
   {
       return value >= 10;
   }

   /**
    * Gets String value of card for use in display
    * @return the string value of the card
    */
   public String getStringValue()
    {
       String result;
       switch (this.value)
       {
           case 11:
               result ="J";
               break;
           case 12:
               result ="Q";
               break;
           case 13:
               result ="K";
               break;
           case 14:
               result ="A";
               break;
           default:
               result = String.valueOf(this.value);
       }
       return result;
    }

    @Override
    public int compareTo(Card c)
    {
        if (this.value == c.value)
        {
            return c.suit - this.suit; // suits are used for tie and ranked in ascending alphabetic order
        }
        return this.value - c.value;
    }


   @Override
   public String toString()
   {
       String result = new String();
       switch (value)
       {
           case 11:
               result+="J";
               break;
           case 12:
               result+="Q";
               break;
           case 13:
               result+="K";
               break;
           case 14:
               result+="A";
               break;
           default:
               result+=String.valueOf(value);

       }
       result+=suit;
       return result;
    }
}
