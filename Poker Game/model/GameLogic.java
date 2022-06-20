package model;

import java.util.ArrayList;
import java.lang.Math;

public class GameLogic 
{
    public static void determineDealer(Deck deck, Player user, Player bot, Table table)
    {
        deck.dealToPlayer(user);
        deck.dealToPlayer(bot);
        
        Card user_card = user.getHand().getCards().get(0);
        Card bot_card = bot.getHand().getCards().get(0);

        Player dealer = user_card.compareTo(bot_card)>0 ? user:bot;
        table.setDealer(dealer);
        Player non_dealer = user_card.compareTo(bot_card)>0 ? bot:user;
        table.setNonDealer(non_dealer);
    }
    

    public static ArrayList<Action> getAvailableActions(Table table)
    {
        ArrayList<Action> available_actions = new ArrayList<Action>();

        int user_bet = table.getUserBet();
        int bot_bet = table.getBotBet();

        if (bot_bet == user_bet)
        {
            available_actions.add(Action.CHECK);
            available_actions.add(Action.RAISE);
        } 
        else
        {
            available_actions.add(Action.CALL);
            available_actions.add(Action.FOLD);
            available_actions.add(Action.RAISE);
        }

        if (table.getCurrentPlayer() == table.getBot() )
        {
            if (table.getUserBet() - table.getBotBet() >= table.getCurrentPlayer().getStack() )
            {
                available_actions.remove(Action.RAISE);
            }

            if (table.getUser().getStack() == 0) available_actions.remove(Action.RAISE);
            if (table.getBot().getStack() <= 3 * table.getUserBet() ) available_actions.remove(Action.RAISE);

        }


        else // user is current
        {
            if (table.getBotBet() - table.getUserBet() >= table.getCurrentPlayer().getStack() )
            {
                available_actions.remove(Action.RAISE);
            }
        }

        return available_actions;
    }

    public static boolean actionOpen(Table table)
    {
        boolean action_open = true;
        boolean both_acted = ( table.getDealer().actedThisRound() && table.getNonDealer().actedThisRound() );
        boolean both_have_money = ( table.getBot().getStack() > 0 && table.getUser().getStack() > 0);
        boolean allInCalled = table.getAllInCalled();

        if ( table.getBotBet() == table.getUserBet() && both_acted) action_open = false;
        if ( !both_have_money && allInCalled ) action_open = false;
        
        return action_open;
    }

    // 0 indicates h1 wins, 1 indicates h2 wins, 2 indicates a true trie
    public static int breakTie(HandClassifier h1, HandClassifier h2)
    {
        ArrayList<Card> all_cards_1 = h1.getCards();
        all_cards_1.sort( new ValueComparator() );
        ArrayList<Card> all_cards_2 = h2.getCards();
        all_cards_2.sort( new ValueComparator() );

        HandStrength strength = h1.classify();
        

        switch (strength)
        {
            case HIGHCARD:
                for (int i = 6; i >= 2; i--)
                {
                    if ( all_cards_1.get(i).value > all_cards_2.get(i).value ) return 0;
                    else if (all_cards_1.get(i).value < all_cards_2.get(i).value) return 1;
                    if (i == 2) return 2;
                }
                break;


            case ONEPAIR:
                //get pairs
                ArrayList<Card> pair1 = new ArrayList<Card>();
                ArrayList<Card> pair2 = new ArrayList<Card>();

                for (int i = 0; i < all_cards_1.size() - 1 ; i++)
                {
                    if (all_cards_1.get(i).value == all_cards_1.get(i+1).value)
                    {
                        pair1.add(all_cards_1.get(i));
                        pair1.add(all_cards_1.get(i+1));
                    } 
                    if (all_cards_2.get(i).value == all_cards_2.get(i+1).value)
                    {
                        pair2.add(all_cards_2.get(i));
                        pair2.add(all_cards_2.get(i+1));
                    }
                }
                if (pair1.get(0).value > pair2.get(0).value) return 0;
                if (pair1.get(0).value < pair2.get(0).value) return 1;

                //if pairs are equal
                if (pair1.get(0).value == pair2.get(0).value)
                {
                    //Get best 5 cards for each hand
                    //

                    ArrayList<Card> top5_1 = new ArrayList<Card>();
                    top5_1.add(pair1.get(0));
                    top5_1.add(pair1.get(1));

                    int index = 6;
                    for (int i = 0; i < 3; i++)
                    {
                        while (all_cards_1.get(index).value == pair1.get(0).value) index--;
                        top5_1.add( all_cards_1.get(index) );
                        index--;
                    }
                    top5_1.sort( new ValueComparator() );

                    ArrayList<Card> top5_2 = new ArrayList<Card>();
                    top5_2.add(pair2.get(0));
                    top5_2.add(pair2.get(1));

                    index = 6;
                    for (int i = 0; i < 3; i++)
                    {
                        while (all_cards_2.get(index).value == pair2.get(0).value) index--;
                        top5_2.add( all_cards_2.get(index) );
                        index--;
                    }
                    top5_2.sort( new ValueComparator() );

                    //look for high card to break tie
                    for (int i = 4; i >= 0; i--)
                    {
                        if (top5_1.get(i).value > top5_2.get(i).value ) return 0;
                        if (top5_1.get(i).value < top5_2.get(i).value ) return 1;
                        if (i == 0 && top5_1.get(i).value == top5_2.get(i).value) return 2;
                    }

                }
                break;


            case TWOPAIR:
                //get pairs
                pair1 = new ArrayList<Card>();
                pair2 = new ArrayList<Card>();

                for (int i = all_cards_1.size() - 1; i > 0 ; i--)
                {
                    if (all_cards_1.get(i).value == all_cards_1.get(i-1).value)
                    {
                        pair1.add(all_cards_1.get(i) );
                        pair1.add(all_cards_1.get(i-1) );
                    } 
                    if (all_cards_2.get(i).value == all_cards_2.get(i-1).value)
                    {
                        pair2.add(all_cards_2.get(i) );
                        pair2.add(all_cards_2.get(i-1) );
                    }
                }

                //get biggest and smallest pair values
                int maxval_1 = 0;
                int minval_1 = Integer.MAX_VALUE;
                for (Card card : pair1)
                {
                    if (card.value > maxval_1) maxval_1 = card.value;
                    if (card.value < minval_1) minval_1 = card.value;

                }

                int maxval_2 = 0;
                int minval_2 = Integer.MAX_VALUE;
                for (Card card : pair2)
                {
                    if (card.value > maxval_2) maxval_2 = card.value;
                    if (card.value < minval_2) minval_2 = card.value;
                }

                if (maxval_1 > maxval_2) return 0;
                if (maxval_1 < maxval_2) return 1;

                //if largest pairs are equal check smallest pairs
                if (maxval_1 == maxval_2) 
                {
                    if (minval_1 > minval_2) return 0;
                    if (minval_1 < minval_2) return 1;
                    
                    //if smallest pairs are equal check remaining high card
                    if (minval_1 == minval_2)
                    {
                        //Get best 5 cards for each hand
                        //
                        int last_index = pair1.size() - 1;
                        ArrayList<Card> top5_1 = new ArrayList<Card>();
                        top5_1.add(pair1.get(last_index--));
                        top5_1.add(pair1.get(last_index--));
                        top5_1.add(pair1.get(last_index--));
                        top5_1.add(pair1.get(last_index));

                        int index = 6;
                        
                        
                        while (all_cards_1.get(index).value == pair1.get(pair1.size()-1).value) index--;
                        top5_1.add( all_cards_1.get(index) );
                        
                        //top5_1.sort( new ValueComparator() );

                        ArrayList<Card> top5_2 = new ArrayList<Card>();
                        last_index = pair2.size() - 1;
                        top5_2.add(pair2.get(last_index--));
                        top5_2.add(pair2.get(last_index--));
                        top5_2.add(pair2.get(last_index--)); 
                        top5_2.add(pair2.get(last_index)); 

                        index = 6;
                        
                        
                        while (all_cards_2.get(index).value == pair2.get(pair2.size()-1).value) index--;
                        top5_2.add( all_cards_2.get(index) );
                        
                        //top5_2.sort( new ValueComparator() );

                        //look for high card to break tie
                        for (int i = 4; i >= 0; i--)
                        {
                            if (top5_1.get(i).value > top5_2.get(i).value ) return 0;
                            if (top5_1.get(i).value < top5_2.get(i).value ) return 1;
                            if (i == 0 && top5_1.get(i).value == top5_2.get(i).value) return 2;
                        }
                    }
                }
                break;
                

            case TRIPS:
                //get trips
                ArrayList<Card> trips1 = new ArrayList<Card>();
                ArrayList<Card> trips2 = new ArrayList<Card>();

                for (int i = 0; i < all_cards_1.size() - 2 ; i++)
                {
                    if (all_cards_1.get(i).value == all_cards_1.get(i+1).value && all_cards_1.get(i+1).value == all_cards_1.get(i+2).value)
                    {
                        trips1.add(all_cards_1.get(i) );
                        trips1.add(all_cards_1.get(i+1) );
                        trips1.add(all_cards_1.get(i+2) );
                    } 
                    if (all_cards_2.get(i).value == all_cards_2.get(i+1).value && all_cards_2.get(i+1).value == all_cards_2.get(i+2).value)
                    {
                        trips2.add(all_cards_1.get(i) );
                        trips2.add(all_cards_1.get(i+1) );
                        trips2.add(all_cards_1.get(i+2) );
                    } 
                }

                if (trips1.get(0).value > trips2.get(0).value) return 0;
                if (trips1.get(0).value < trips2.get(0).value) return 1;

                //if trips are equal check high cards
                if (trips1.get(0).value == trips2.get(0).value)
                {

                    //Get best 5 cards for each hand
                    //
                    ArrayList<Card> top5_1 = new ArrayList<Card>();
                    top5_1.add(trips1.get(0));
                    top5_1.add(trips1.get(1));
                    top5_1.add(trips1.get(2));
                   

                    int index = 6;
                    for (int i = 0; i < 2; i++)
                    {
                        while (all_cards_1.get(index).value == trips1.get(2).value) index--;
                        top5_1.add( all_cards_1.get(index) );
                        index--;
                    }
                    
                    top5_1.sort( new ValueComparator() );

                    ArrayList<Card> top5_2 = new ArrayList<Card>();
                    top5_2.add(trips2.get(0));
                    top5_2.add(trips2.get(1));
                    top5_2.add(trips2.get(2));

                    index = 6;
                    
                    
                    index = 6;
                    for (int i = 0; i < 2; i++)
                    {
                        while (all_cards_2.get(index).value == trips2.get(2).value) index--;
                        top5_2.add( all_cards_2.get(index) );
                        index--;
                    }
                    
                    top5_2.sort( new ValueComparator() );

                    //look for high card to break tie
                    for (int i = 4; i >= 0; i--)
                    {
                        if (top5_1.get(i).value > top5_2.get(i).value ) return 0;
                        if (top5_1.get(i).value < top5_2.get(i).value ) return 1;
                        if (i == 0 && top5_1.get(i).value == top5_2.get(i).value) return 2;
                    }
                }
                break;


            case STRAIGHT:
                //find high cards in straights

                int count = 1;
                int straight_high_value_1 = 0;
                for (int i = 0; i < all_cards_1.size() - 1 ; i++)
                {
                    if (all_cards_1.get(i).value == all_cards_1.get(i+1).value) continue;
                    if (all_cards_1.get(i).value == all_cards_1.get(i+1).value - 1)
                    {
                        count++;
                    }
                    else count = 1;
                    
                    if (count >= 5) straight_high_value_1 = all_cards_1.get(i+1).value;
                }
                if (count == 4) straight_high_value_1 = 5;


                count = 1;
                int straight_high_value_2 = 0;
                for (int i = 0; i < all_cards_2.size() - 1 ; i++)
                {
                    if (all_cards_2.get(i).value == all_cards_2.get(i+1).value) continue;
                    if (all_cards_2.get(i).value == all_cards_2.get(i+1).value - 1)
                    {
                        count++;
                    }
                    else count = 1;
                    
                    if (count >= 5) straight_high_value_2 = all_cards_1.get(i+1).value;
                }
                if (count == 4) straight_high_value_2 = 5;

                if (straight_high_value_1 > straight_high_value_2) return 0;
                if (straight_high_value_1 < straight_high_value_2) return 1;
                if (straight_high_value_1 == straight_high_value_2) return 2;
                break;
            

            case FLUSH:
                all_cards_1.sort(new SuitComparator() );

                count = 1;
                int maxval_1_index = -1;
            
                for (int i = 0; i < all_cards_1.size() - 1 ; i++)
                {
                    if (all_cards_1.get(i).suit == all_cards_1.get(i+1).suit)
                    {
                        count++;
                        if (count >= 5) maxval_1_index = i + 1;
                    }
                    else count = 1;
                }

                ArrayList<Card> top5_1 = new ArrayList<Card>();
                for (int i = 0; i < 5; i++)
                {
                    top5_1.add( all_cards_1.get(maxval_1_index--) );
                }
                top5_1.sort(new ValueComparator() );


                all_cards_2.sort(new SuitComparator() );

                count = 1;
                int maxval_2_index = -1;

                for (int i = 0; i < all_cards_2.size() - 1 ; i++)
                {
                    if (all_cards_2.get(i).suit == all_cards_2.get(i+1).suit)
                    {
                        count++;
                        if (count >= 5) maxval_2_index = i + 1;
                    }
                    else count = 1;
                }

                ArrayList<Card> top5_2 = new ArrayList<Card>();
                for (int i = 0; i < 5; i++)
                {
                    top5_2.add( all_cards_2.get(maxval_2_index--) );
                }
                top5_2.sort(new ValueComparator() );

                for (int i = 4; i >= 0; i--)
                {
                    if (top5_1.get(i).value > top5_2.get(i).value) return 0;
                    if (top5_1.get(i).value < top5_2.get(i).value) return 1;
                    if (i == 0 && top5_1.get(i).value == top5_2.get(i).value) return 2;
                }
                
            
            case FULLHOUSE:
                //get trips
                trips1 = new ArrayList<Card>();
                trips2 = new ArrayList<Card>();

                for (int i = 0; i < all_cards_1.size() - 2 ; i++)
                {
                    if (all_cards_1.get(i).value == all_cards_1.get(i+1).value && all_cards_1.get(i+1).value == all_cards_1.get(i+2).value)
                    {
                        trips1.add(all_cards_1.get(i) );
                        trips1.add( all_cards_1.get(i+1) );
                        trips1.add(all_cards_1.get(i+2) );
                    } 

                    if (all_cards_2.get(i).value == all_cards_2.get(i+1).value && all_cards_2.get(i+1).value == all_cards_2.get(i+2).value)
                    {
                        trips2.add(all_cards_1.get(i) );
                        trips2.add(all_cards_1.get(i+1) );
                        trips2.add(all_cards_1.get(i+2) );
                    } 
                }

                if (trips1.get(0).value > trips2.get(0).value) return 0;
                if (trips1.get(0).value < trips2.get(0).value) return 1;

                //if trips are equal check remaining pair
                if (trips1.get(0).value == trips2.get(0).value) 
                {
                    pair1 = new ArrayList<Card>();
                    //pair1_index = 0;

                    pair2 = new ArrayList<Card>();
                    //pair2_index = 0;

                    for (int i = 0; i < all_cards_1.size() - 1; i++)
                    {
                        if (all_cards_1.get(i) != trips1.get(0) && all_cards_1.get(i) == all_cards_1.get(i+1) )
                        {
                            pair1.add(all_cards_1.get(i) );
                            pair1.add(all_cards_1.get(i+1) );
                        }

                        if (all_cards_2.get(i) != trips2.get(0) && all_cards_2.get(i) == all_cards_2.get(i+1) )
                        {
                            pair2.add(all_cards_2.get(i) );
                            pair2.add(all_cards_2.get(i+1) );
                        }
                    }

                    if (pair1.get(0).value > pair2.get(0).value) return 0;
                    if (pair1.get(0).value < pair2.get(0).value) return 1;
                    if (pair1.get(0).value == pair2.get(0).value) return 2;
                }
            
            
            case QUADS:
                //get quads
                ArrayList<Card> quads1 = new ArrayList<Card>();
                ArrayList<Card> quads2 = new ArrayList<Card>();

                for (int i = 0; i < all_cards_1.size() - 3 ; i++)
                {
                    if (all_cards_1.get(i).value == all_cards_1.get(i+1).value && all_cards_1.get(i+1).value == all_cards_1.get(i+2).value && all_cards_1.get(i+2).value == all_cards_1.get(i+3).value )
                    {
                        quads1.add(all_cards_1.get(i) );
                        quads1.add(all_cards_1.get(i+1) );
                        quads1.add(all_cards_1.get(i+2) );
                        quads1.add(all_cards_1.get(i+3) );

                    } 
                    if (all_cards_2.get(i).value == all_cards_2.get(i+1).value && all_cards_2.get(i+1).value == all_cards_2.get(i+2).value && all_cards_2.get(i+2).value == all_cards_2.get(i+3).value )
                    {
                        quads2.add( all_cards_2.get(i) );
                        quads2.add( all_cards_2.get(i+1) );
                        quads2.add( all_cards_2.get(i+2) );
                        quads2.add( all_cards_2.get(i+3) );
                    } 
                }

                if (quads1.get(0).value > quads2.get(0).value) return 0;
                if (quads1.get(0).value < quads2.get(0).value) return 1;

                //check remaining high card
                if (quads1.get(0).value == quads2.get(0).value)
                {
                    for (int i = 6; i >= 0; i--)
                    {
                        if (all_cards_1.get(i).value > all_cards_2.get(i).value && all_cards_1.get(i).value != quads1.get(0).value) return 0;
                        if (all_cards_1.get(i).value < all_cards_2.get(i).value && all_cards_2.get(i).value != quads2.get(0).value) return 1;
                        if (i == 0) return 2;
                    }
                }


            case STRAIGHTFLUSH:
                
                //find high cards in straight flush

                all_cards_1.sort( new SuitComparator() );
                all_cards_2.sort( new SuitComparator() );

                count = 1;
                int straightflush_high_value_1 = 0;
                for (int i = 0; i < all_cards_1.size() - 1 ; i++)
                {
                    if (all_cards_1.get(i).value == all_cards_1.get(i+1).value) continue;
                    if (all_cards_1.get(i).value == all_cards_1.get(i+1).value - 1 && all_cards_1.get(i).suit == all_cards_1.get(i+1).suit)
                    {
                        count++;
                    }
                    else count = 1;
                    
                    if (count >= 5) straightflush_high_value_1 = all_cards_1.get(i+1).value;
                }
                if (count == 4) straightflush_high_value_1 = 5;


                count = 1;
                int straightflush_high_value_2 = 0;
                for (int i = 0; i < all_cards_2.size() - 1 ; i++)
                {
                    if (all_cards_2.get(i).value == all_cards_2.get(i+1).value) continue;
                    if (all_cards_2.get(i).value == all_cards_2.get(i+1).value - 1 && all_cards_2.get(i).suit == all_cards_2.get(i+1).suit)
                    {
                        count++;
                    }
                    else count = 1;
                    
                    if (count >= 5) straight_high_value_2 = all_cards_1.get(i+1).value;
                }
                if (count == 4) straightflush_high_value_2 = 5;


                if (straightflush_high_value_1 > straightflush_high_value_2) return 0;
                if (straightflush_high_value_1 < straightflush_high_value_2) return 1;
                if (straightflush_high_value_1 == straightflush_high_value_2) return 2;
                break;

        }
        return -1; // error

    }
}