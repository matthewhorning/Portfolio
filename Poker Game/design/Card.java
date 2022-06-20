public class Card implements Comparable<Card>
{
   int value;
   char suit;

   public Card(int v, char s)
   {
       this.value = v;
       this.suit = s;
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
    * The numeric value of the card in the game of BlackJack
    * @return the numeric value of the card
    */
   public int getValue()
    {
       int v = value;
       if (value >11)
       {
           v = 10;
       }
       return v;
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
