import java.util.ArrayList;

public class HandClassifier
{
    private ArrayList<Card> all_cards;

    public HandClassifier(ArrayList<Card> hole_cards, ArrayList<Card> community_cards)
    {
        all_cards = new ArrayList<Card>();
        
        for (Card card : hole_cards) 
        {
            this.all_cards.add(card);
        }
        for (Card card : community_cards) 
        {
            this.all_cards.add(card);
        }

        all_cards.sort(null);
    }

    public boolean isStraightFlush()
    {
        return false;
    }

    public boolean isQuads()
    {
        return false;
    }

    public boolean isFullHouse()
    {
        return true;
    }

    public boolean isFlush()
    {
        return true;
    }

    public boolean isStraight()
    {
        return false;
    }

    public boolean isTrips()
    {
        return true;
    }

    public boolean isTwoPair()
    {
        return true;
    }

    public boolean isPair()
    {
        return true;
    }

    public HandStrength classify()
    {
        if ( isStraightFlush() ) return HandStrength.STRAIGHTFLUSH;
        if ( isQuads() ) return HandStrength.QUADS;
        if ( isFullHouse() ) return HandStrength.FULLHOUSE;
        if ( isFlush() ) return HandStrength.FLUSH;
        if ( isStraight() ) return HandStrength.STRAIGHT;
        if ( isTrips() ) return HandStrength.TRIPS;
        if ( isTwoPair() ) return HandStrength.TWOPAIR;
        if ( isPair() ) return HandStrength.ONEPAIR;
        else return HandStrength.HIGHCARD;
    }
}