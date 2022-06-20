package tests;

import model.*;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;


public class HandClassifierTest {
    @Test
    public void isStraightFlushCorrect()
    {
        ArrayList<Card> community_cards = new ArrayList<Card>();
        Hand testHand = new Hand();
        testHand.add(new Card(7, 'H'));
        testHand.add(new Card(8, 'H'));
        community_cards.add(new Card(9, 'H'));
        community_cards.add(new Card(10, 'H'));
        community_cards.add(new Card(11, 'H'));

        HandClassifier testClassifier = new HandClassifier(testHand.getCards(), community_cards);
        assertTrue(testClassifier.isStraightFlush());
    }

    @Test
    public void isStraightFlushIncorrect()
    {
        ArrayList<Card> community_cards = new ArrayList<Card>();
        Hand testHand = new Hand();
        testHand.add(new Card(11, 'H'));
        testHand.add(new Card(9, 'H'));
        community_cards.add(new Card(10, 'H'));
        community_cards.add(new Card(8, 'H'));
        community_cards.add(new Card(7, 'S'));

        HandClassifier testClassifier = new HandClassifier(testHand.getCards(), community_cards);
        assertFalse(testClassifier.isStraightFlush());
    }

    @Test
    public void isQuadsCorrect()
    {
        ArrayList<Card> community_cards = new ArrayList<Card>();
        Hand testHand = new Hand();
        testHand.add(new Card(5, 'H'));
        testHand.add(new Card(5, 'S'));
        community_cards.add(new Card(5, 'D'));
        community_cards.add(new Card(5, 'C'));
        community_cards.add(new Card(7, 'H'));

        HandClassifier testClassifier = new HandClassifier(testHand.getCards(), community_cards);
        assertTrue(testClassifier.isQuads());
    }

    @Test
    public void isQuadsIncorrect()
    {
        ArrayList<Card> community_cards = new ArrayList<Card>();
        Hand testHand = new Hand();
        testHand.add(new Card(5, 'D'));
        testHand.add(new Card(9, 'H'));
        community_cards.add(new Card(10, 'H'));
        community_cards.add(new Card(8, 'H'));
        community_cards.add(new Card(7, 'S'));

        HandClassifier testClassifier = new HandClassifier(testHand.getCards(), community_cards);
        assertFalse(testClassifier.isQuads());
    }

    @Test
    public void isFullHouseCorrect()
    {
        ArrayList<Card> community_cards = new ArrayList<Card>();
        Hand testHand = new Hand();
        testHand.add(new Card(3, 'H'));
        testHand.add(new Card(3, 'S'));
        community_cards.add(new Card(3, 'C'));
        community_cards.add(new Card(6, 'S'));
        community_cards.add(new Card(6, 'H'));

        HandClassifier testClassifier = new HandClassifier(testHand.getCards(), community_cards);
        assertTrue(testClassifier.isFullHouse());
    }

    @Test
    public void isFullHouseIncorrect()
    {
        ArrayList<Card> community_cards = new ArrayList<Card>();
        Hand testHand = new Hand();
        testHand.add(new Card(5, 'D'));
        testHand.add(new Card(9, 'H'));
        community_cards.add(new Card(10, 'H'));
        community_cards.add(new Card(8, 'H'));
        community_cards.add(new Card(7, 'S'));

        HandClassifier testClassifier = new HandClassifier(testHand.getCards(), community_cards);
        assertFalse(testClassifier.isFullHouse());
    }

    @Test
    public void isFlushCorrect()
    {
        ArrayList<Card> community_cards = new ArrayList<Card>();
        Hand testHand = new Hand();
        testHand.add(new Card(3, 'D'));
        testHand.add(new Card(2, 'D'));
        community_cards.add(new Card(12, 'D'));
        community_cards.add(new Card(8, 'D'));
        community_cards.add(new Card(6, 'D'));

        HandClassifier testClassifier = new HandClassifier(testHand.getCards(), community_cards);
        assertTrue(testClassifier.isFlush());
    }

    @Test
    public void isFlushIncorrect()
    {
        ArrayList<Card> community_cards = new ArrayList<Card>();
        Hand testHand = new Hand();
        testHand.add(new Card(5, 'D'));
        testHand.add(new Card(9, 'H'));
        community_cards.add(new Card(10, 'H'));
        community_cards.add(new Card(8, 'H'));
        community_cards.add(new Card(7, 'S'));

        HandClassifier testClassifier = new HandClassifier(testHand.getCards(), community_cards);
        assertFalse(testClassifier.isFlush());
    }

    @Test
    public void isStraightCorrect()
    {
        ArrayList<Card> community_cards = new ArrayList<Card>();
        Hand testHand = new Hand();
        testHand.add(new Card(7, 'D'));
        testHand.add(new Card(6, 'H'));
        community_cards.add(new Card(5, 'S'));
        community_cards.add(new Card(4, 'D'));
        community_cards.add(new Card(3, 'D'));

        HandClassifier testClassifier = new HandClassifier(testHand.getCards(), community_cards);
        assertTrue(testClassifier.isStraight());
    }

    @Test
    public void isStraightIncorrect()
    {
        ArrayList<Card> community_cards = new ArrayList<Card>();
        Hand testHand = new Hand();
        testHand.add(new Card(5, 'D'));
        testHand.add(new Card(9, 'H'));
        community_cards.add(new Card(10, 'H'));
        community_cards.add(new Card(8, 'H'));
        community_cards.add(new Card(7, 'S'));

        HandClassifier testClassifier = new HandClassifier(testHand.getCards(), community_cards);
        assertFalse(testClassifier.isStraight());
    }

    @Test
    public void isTripsCorrect()
    {
        ArrayList<Card> community_cards = new ArrayList<Card>();
        Hand testHand = new Hand();
        testHand.add(new Card(2, 'C'));
        testHand.add(new Card(2, 'S'));
        community_cards.add(new Card(2, 'D'));
        community_cards.add(new Card(13, 'S'));
        community_cards.add(new Card(6, 'H'));

        HandClassifier testClassifier = new HandClassifier(testHand.getCards(), community_cards);
        assertTrue(testClassifier.isTrips());
    }

    @Test
    public void isTripsIncorrect()
    {
        ArrayList<Card> community_cards = new ArrayList<Card>();
        Hand testHand = new Hand();
        testHand.add(new Card(7, 'D'));
        testHand.add(new Card(6, 'H'));
        community_cards.add(new Card(5, 'S'));
        community_cards.add(new Card(4, 'D'));
        community_cards.add(new Card(3, 'D'));

        HandClassifier testClassifier = new HandClassifier(testHand.getCards(), community_cards);
        assertFalse(testClassifier.isTrips());
    }

    @Test
    public void isTwoPairCorrect()
    {
        ArrayList<Card> community_cards = new ArrayList<Card>();
        Hand testHand = new Hand();
        testHand.add(new Card(3, 'D'));
        testHand.add(new Card(3, 'H'));
        community_cards.add(new Card(2, 'S'));
        community_cards.add(new Card(2, 'D'));
        community_cards.add(new Card(5, 'D'));

        HandClassifier testClassifier = new HandClassifier(testHand.getCards(), community_cards);
        assertTrue(testClassifier.isTwoPair());
    }

    @Test
    public void isTwoPairIncorrect()
    {
        ArrayList<Card> community_cards = new ArrayList<Card>();
        Hand testHand = new Hand();
        testHand.add(new Card(7, 'D'));
        testHand.add(new Card(6, 'H'));
        community_cards.add(new Card(5, 'S'));
        community_cards.add(new Card(4, 'D'));
        community_cards.add(new Card(3, 'D'));

        HandClassifier testClassifier = new HandClassifier(testHand.getCards(), community_cards);
        assertFalse(testClassifier.isTwoPair());
    }

    @Test
    public void isPairCorrect()
    {
        ArrayList<Card> community_cards = new ArrayList<Card>();
        Hand testHand = new Hand();
        testHand.add(new Card(3, 'D'));
        testHand.add(new Card(3, 'H'));
        community_cards.add(new Card(7, 'S'));
        community_cards.add(new Card(8, 'D'));
        community_cards.add(new Card(5, 'D'));

        HandClassifier testClassifier = new HandClassifier(testHand.getCards(), community_cards);
        assertTrue(testClassifier.isPair());
    }

    @Test
    public void isPairIncorrect()
    {
        ArrayList<Card> community_cards = new ArrayList<Card>();
        Hand testHand = new Hand();
        testHand.add(new Card(3, 'D'));
        testHand.add(new Card(3, 'H'));
        community_cards.add(new Card(2, 'S'));
        community_cards.add(new Card(2, 'D'));
        community_cards.add(new Card(5, 'D'));

        HandClassifier testClassifier = new HandClassifier(testHand.getCards(), community_cards);
        assertTrue(testClassifier.isPair());
        //Changes to assertTrue - isPair() correctly returns true here, but classify() will return HandStrength.TwoPair
    }
}