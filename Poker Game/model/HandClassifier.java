package model;

import java.util.ArrayList;
import java.util.Comparator;

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
    }

    public ArrayList<Card> getCards()
    {
        return all_cards;
    }

    //
    // methods do not work for straights using Ace as low card
    //

    public boolean isStraightFlush()
    {
        all_cards.sort(new SuitComparator() );

        int count = 1;
        char suit = 'c';
        for (int i = 0; i < all_cards.size() - 1 ; i++)
        {
            //if two adjacent cards are of same suit and sequential in value
            if (all_cards.get(i).suit == all_cards.get(i+1).suit && all_cards.get(i).value == all_cards.get(i+1).value - 1)
            {
                count++;
            }
            else count = 1;

            if (count == 4) 
            {
                suit = all_cards.get(i).suit;
            }
            if (count == 5) return true;
        }

        if (count == 4)
        {
            Card ace = new Card(14, suit);
            Card duece = new Card(2, suit);
            Card three = new Card(3, suit);
            Card four = new Card(4, suit);
            Card five = new Card(5, suit);

            ArrayList<Card> wheel = new ArrayList<Card>();
            wheel.add(ace);
            wheel.add(duece);
            wheel.add(three);
            wheel.add(four);
            wheel.add(five);

            boolean isWheel = true;
            
            for (Card card : wheel)
            {
                if (!all_cards.contains(card) ) isWheel = false;
            }
            
            if (isWheel) return true;

        }
        return false;
    }

    public boolean isQuads()
    {
        all_cards.sort(new ValueComparator() );

        int count = 1;
        for (int i = 0; i < all_cards.size() - 1 ; i++)
        {
            if (all_cards.get(i).value == all_cards.get(i+1).value) count++;
            else count = 1;

            if (count == 4) return true;
        }
        return false;
    }

    public boolean isFullHouse()
    {
        all_cards.sort(new ValueComparator() );
        boolean hasTrips = false;
        int trip_value = 0;
        int count = 1;
        for (int i = 0; i < all_cards.size() - 1 ; i++)
        {
            if (all_cards.get(i).value == all_cards.get(i+1).value) count++;
            else count = 1;

            if (count == 3)
            {
                hasTrips = true;
                trip_value = all_cards.get(i).value;
            }
        }

        boolean hasPair = false;
        count = 1;
        for (int i = 0; i < all_cards.size() - 1 ; i++)
        {
            if (all_cards.get(i).value == all_cards.get(i+1).value && all_cards.get(i).value != trip_value) count++;
            else count = 1;

            if (count == 2)
            {
                hasPair = true;
                trip_value = all_cards.get(i).value;
            }
        }

        if (hasPair && hasTrips) return true;
        else return false;
    }

    public boolean isFlush()
    {
        all_cards.sort(new SuitComparator() );

        int count = 1;
        for (int i = 0; i < all_cards.size() - 1 ; i++)
        {
            if (all_cards.get(i).suit == all_cards.get(i+1).suit)
            {
                count++;
            }
            else count = 1;
            
            if (count == 5) return true;
        }
        return false;
    }

    public boolean isStraight()
    {
        all_cards.sort(new ValueComparator() );

        int count = 1;
        boolean count_was_4 = false;
        for (int i = 0; i < all_cards.size() - 1 ; i++)
        {
            if (all_cards.get(i).value == all_cards.get(i+1).value) continue;
            if (all_cards.get(i).value == all_cards.get(i+1).value - 1)
            {
                count++;
            }
            else count = 1;
            
            if (count == 4) count_was_4 = true;
            if (count == 5) return true;
        }

        if (count_was_4)
        {
            Card ace = new Card(14, 'c');
            Card duece = new Card(2, 'c');
            Card three = new Card(3, 'c');
            Card four = new Card(4, 'c');
            Card five = new Card(5, 'c');

            ArrayList<Card> wheel = new ArrayList<Card>();
            wheel.add(ace);
            wheel.add(duece);
            wheel.add(three);
            wheel.add(four);
            wheel.add(five);

            int wheel_count = 0;
            for (Card wheel_card : wheel)
            {
                for (Card hand_card : all_cards)
                {
                    if (wheel_card.value == hand_card.value)
                    {
                        wheel_count++;
                        break;
                    }
                }
            }
            
            if (wheel_count == 5) return true;

        }
        return false;
    }

    public boolean isTrips()
    {
        all_cards.sort(new ValueComparator() );

        int count = 1;
        for (int i = 0; i < all_cards.size() - 1 ; i++)
        {
            if (all_cards.get(i).value == all_cards.get(i+1).value) count++;
            else count = 1;

            if (count == 3) return true;
        }
        return false;
    }

    public boolean isTwoPair()
    {
        all_cards.sort(new ValueComparator() );

        int numPair = 0;
        int count = 1;
        for (int i = 0; i < all_cards.size() - 1 ; i++)
        {
            if (all_cards.get(i).value == all_cards.get(i+1).value)
            {
                count++;
                if (count != 2) continue;
            } 

            if (count == 2) 
            {
                numPair++;
                count = 1; 
            }

        }
        if (numPair == 2 || numPair == 3) return true;
        else return false;
    }

    public boolean isPair()
    {
        all_cards.sort(new ValueComparator() );

        int count = 1;
        for (int i = 0; i < all_cards.size() - 1 ; i++)
        {
            if (all_cards.get(i).value == all_cards.get(i+1).value) count++;
            else count = 1;

            if (count == 2) return true;
        }
        return false;
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