import java.util.HashMap;

public class Card
{
   enum Suite {CLUBS, DIAMONDS, HEARTS, SPADES}
   enum Face  {
       ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(5), SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(11), QUEEN(12), KING(13), ACE(14);
  
       private final int value;
       Face(int v)
       {
           this.value = v;
       }

       private static HashMap<Integer, Face> map = new HashMap<Integer, Face>();
       static 
       {
          for (Face face: Face.values()) 
          {
              map.put(face.value, face);
          }
       }
       public static Face valueOf(int v)
       {
           return map.get(v);
       }
       public int getValue()
       {
           return this.value;
       }
   }

   protected Face face;
   protected Suite suite;

   public Card(int v, Suite s)
   {
       this.face = Face.valueOf(v);
       this.suite = s;
   }

   public Suite getSuite()
   {
       return this.suite;
   }
   public Face getFace()
   {
       return this.face;
   }

   @Override
   public String toString()
   {
       String result = new String();
       switch (face)
       {
           case ACE:
               result+="Ace";
               break;
           case KING:
               result+="King";
               break;
           case QUEEN:
               result+="Queen";
               break;
           case JACK:
               result+="Jack";
               break;
           default:
               result+=String.valueOf(face);

       }
       result+=" - "+suite;
       return result;
   }
}
