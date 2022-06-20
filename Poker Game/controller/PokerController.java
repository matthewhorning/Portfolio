package controller;

import model.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class PokerController {
    protected ActionListener foldListener;
    protected ActionListener callListener;
    protected ActionListener raiseListener;
    protected ActionListener checkListener;
    protected ActionListener confirmListener;
    protected ActionListener highScoresListener;

    protected Bot bot;
    protected User user;
    protected PokerUI pokerTable;
    protected Table table;
    protected Player dealer;
    protected Player non_dealer;
    protected Deck deck;
    protected Round round;

    protected Action cur_action;

    public ArrayList<Action> all_actions;


    public PokerController(PokerUI p) {
        
        this.bot = p.getBot();
        this.user = p.getUser();
        this.deck = new Deck();
        this.pokerTable = p;
        this.cur_action = null;
        this.table = p.getTable();
        this.dealer = p.getTable().getDealer();
        this.non_dealer = p.getTable().getNonDealer();

        all_actions = new ArrayList<Action>();
        all_actions.add(Action.FOLD);
        all_actions.add(Action.CHECK);
        all_actions.add(Action.CALL);
        all_actions.add(Action.RAISE);

        foldListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userFold();
            }
        };

        callListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userCall();
            }
        };

        raiseListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userRaise();
            }
        };

        checkListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userCheck();
            }
        };

        confirmListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    startGame();
                } catch (InterruptedException ex) {
                    System.out.println(ex);
                }
            }
        };

        this.pokerTable.setOnConfirm(confirmListener);
    }

    public void setActionListeners() {
        this.pokerTable.setOnUserFold(foldListener);
        this.pokerTable.setOnUserCall(callListener);
        this.pokerTable.setOnUserRaise(raiseListener);
        this.pokerTable.setOnUserCheck(checkListener);
    }

    public void lockButtons(ArrayList<Action> available_actions) {
        for (Action a : available_actions) {
            if (a == Action.RAISE) {
                this.pokerTable.disableRaise();
            } else if (a == Action.CALL) {
                this.pokerTable.disableCall();
            } else if (a == Action.FOLD) {
                this.pokerTable.disableFold();
            } else if (a == Action.CHECK) {
                this.pokerTable.disableCheck();
            }
        }
    }

    public void unlockButtons(ArrayList<Action> available_actions) {
        for (Action a : available_actions) {
            if (a == Action.RAISE) {
                this.pokerTable.enableRaise();
            } else if (a == Action.CALL) {
                this.pokerTable.enableCall();
            } else if (a == Action.FOLD) {
                this.pokerTable.enableFold();
            } else if (a == Action.CHECK) {
                this.pokerTable.enableCheck();
            }
        }
    }

    public void startGame() throws InterruptedException {
        pokerTable.determineData();
        pokerTable.setSize(750, 600);
        String botLevel = pokerTable.getBotLevel();
        if (botLevel.equals("easy")) {
            bot.setStrategy(new EasyStrategy());
        } else {
            bot.setStrategy(new RandomStrategy());
        }
        pokerTable.displayTable();
        lockButtons(all_actions);
        setActionListeners();

        GameLogic.determineDealer(deck, pokerTable.getTable().getUser(), pokerTable.getTable().getBot(),
                pokerTable.getTable());

        refreshUI();

        preflopInit();
    }

    public void roundLoop() 
    {
        boolean actionOpen = GameLogic.actionOpen(table);

        if (actionOpen)
        {
            String goTo = "roundLoop";
            Player current_player = table.getCurrentPlayer();
            if (current_player == user) goTo = "wait for JButton";

            ArrayList<Action> available_actions = GameLogic.getAvailableActions(table);

            lockButtons(all_actions);
            int difference = Math.abs(table.getBotBet() - table.getUserBet());
            int max_bet = Integer.max(table.getUserBet(), table.getBotBet());

            Action selected_action;

            if (current_player == bot)
            {
                selected_action = current_player.act(available_actions, table);

                switch (selected_action)
                {
                    case FOLD:
                        table.pushToPot();
                        table.pushToWinner( table.getNonCurrentPlayer() );
                        table.getUser().getHand().clearHand();
                        table.getBot().getHand().clearHand();
                        table.clearCommunityCards();
                        refreshUI();
                        table.moveDealer();
                        goTo = "preFlopInit";
                        break;
                    case CHECK:
                        if ( GameLogic.actionOpen(table) ) bot.setActedThisRound(true);
                        else 
                        {
                            switch (table.getCurrentRound() )
                            {
                                case PREFLOP:
                                    goTo = "flopInit";
                                    break;
                                case FLOP:
                                    goTo = "turnInit";
                                    break;
                                case TURN:
                                    goTo = "riverInit";
                                    break;
                                case RIVER:
                                    goTo = "showdown";
                                    break;
                            }
                        }
                        break;

                    case CALL:
                        if ( GameLogic.actionOpen(table) ) 
                        {
                            difference = Math.abs(table.getBotBet() - table.getUserBet());
                            bot.setActedThisRound(true);
                            if (bot.getStack() > difference) 
                            {
                                current_player.removeFromStack(difference);
                                table.updateBetAmount(false, max_bet);
                            }

                            else // called all in
                            {
                                table.updateBetAmount(false, bot.getStack() );
                                current_player.removeFromStack( bot.getStack() ); 
                                table.setAllInCalled(true);
                            }

                            if (user.getStack() == 0) table.setAllInCalled(true);
                        }

                        else 
                        {
                            switch (table.getCurrentRound() )
                            {
                                case PREFLOP:
                                    goTo = "flopInit";
                                    break;
                                case FLOP:
                                    goTo = "turnInit";
                                    break;
                                case TURN:
                                    goTo = "riverInit";
                                    break;
                                case RIVER:
                                    goTo = "showdown";
                                    break;
                            }
                        }
                        break;

                    case RAISE:
                        current_player.removeFromStack( table.getRaiseAmount() - table.getBotBet());
                        table.updateBetAmount(false, table.getRaiseAmount() );
                        table.clearRaiseAmount();
                    
                        break;
                }

                bot.setActedThisRound(true);
                refreshUI();
                
                table.swapCurrentandNonCurrentPlayers();


                
            } 
            else unlockButtons(available_actions);

            switch (goTo)
            {
                case "preFlopInit":
                    preflopInit();
                    break;
                case "roundLoop": 
                    roundLoop();
                    break;
                case "flopInit":
                    flopInit();
                    break;
                case "turnInit":
                    turnInit();
                    break;
                case "riverInit":
                    riverInit();
                    break;
                case "showdown":
                    showDown();
                    break;
            }

            
        }
        
        else //action is closed
        {
            table.pushToPot();
            refreshUI();

            switch(table.getCurrentRound() )
            {
                case PREFLOP:
                    flopInit();
                    break;
                case FLOP:
                    turnInit();
                    break;
                case TURN:
                    riverInit();
                    break;
                case RIVER:
                    showDown();
                    break;
            }
        }


        refreshUI(); 
    }

    public void flopInit()
    {
        table = pokerTable.getTable();
        dealer = pokerTable.getTable().getDealer();
        non_dealer = pokerTable.getTable().getNonDealer();
        bot = pokerTable.getTable().getBot();
        user = pokerTable.getTable().getUser();

        table.setRound(Round.FLOP);
        dealer.resetActions();
        non_dealer.resetActions();

        table.setCurrentPlayer(non_dealer);
        table.setNonCurrentPlayer(dealer);
        
        deck.dealFlop(table);
        refreshUI();

        roundLoop();
    }

    public void turnInit()
    {
        table = pokerTable.getTable();
        dealer = pokerTable.getTable().getDealer();
        non_dealer = pokerTable.getTable().getNonDealer();
        bot = pokerTable.getTable().getBot();
        user = pokerTable.getTable().getUser();

        table.setRound(Round.TURN);
        dealer.resetActions();
        non_dealer.resetActions();

        table.setCurrentPlayer(non_dealer);
        table.setNonCurrentPlayer(dealer);
        
        deck.dealTurn(table);
        refreshUI();

        roundLoop();
    }

    public void riverInit()
    {
        table = pokerTable.getTable();
        dealer = pokerTable.getTable().getDealer();
        non_dealer = pokerTable.getTable().getNonDealer();
        bot = pokerTable.getTable().getBot();
        user = pokerTable.getTable().getUser();

        table.setRound(Round.RIVER);
        dealer.resetActions();
        non_dealer.resetActions();

        table.setCurrentPlayer(non_dealer);
        table.setNonCurrentPlayer(dealer);

        
        deck.dealRiver(table);
        refreshUI();

        roundLoop();
    }

    public void gameOver()
    {
        pokerTable.updateHandPanel(bot.getHand(), false, true);
        boolean playAgain;
        if (bot.getStack() == 0)
        {
            playAgain = pokerTable.playAgain(true);
        }
        else
        {
            playAgain = pokerTable.playAgain(false);
        }

        if (playAgain) 
        {
            user.reset();
            bot.reset();
            table.reset();
            
            pokerTable.displayTable();
            pokerTable.clearCommunityHand();

            lockButtons(all_actions);
            setActionListeners();

            deck = new Deck();
            deck.shuffle();

            GameLogic.determineDealer(deck, pokerTable.getTable().getUser(), pokerTable.getTable().getBot(), pokerTable.getTable());

            preflopInit();
            
        }
        else System.exit(0);
    }

    public void preflopInit() 
    {
        String num_hands_string = "0";
        try {
            File numHands = new File("numHands.txt");
            Scanner myReader = new Scanner(numHands);
            while (myReader.hasNextLine()) {
              num_hands_string = myReader.nextLine();
            }
            myReader.close();
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred reading.");
            e.printStackTrace();
          }
        
        int numHands_old = Integer.parseInt(num_hands_string);
        numHands_old++;
        String numHands_new = Integer.toString(numHands_old);
        

        try {
            FileWriter myWriter = new FileWriter("numHands.txt", false);
            myWriter.write(numHands_new);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } 

        catch (IOException e) {
            System.out.println("An error occurred writing to file.");
            e.printStackTrace();
        }


        if (bot.getStack() == 0 || user.getStack() == 0) 
        {
            refreshUI();
            pokerTable.updateHandPanel(bot.getHand(), false, true);
            gameOver();

        }
        

        else
        {
            table.clearCommunityCards();
            deck = new Deck();
            deck.shuffle();
            
            table.setAllInCalled(false);

            table = pokerTable.getTable();
            dealer = pokerTable.getTable().getDealer();
            non_dealer = pokerTable.getTable().getNonDealer();
            bot = pokerTable.getTable().getBot();
            user = pokerTable.getTable().getUser();
    
            table.setRound(Round.PREFLOP);
    
            if (dealer == bot) {
                table.setDealer(bot);
                table.setNonDealer(user);
            } else {
                table.setDealer(user);
                table.setNonDealer(bot);
            }
    
            dealer.resetActions();
            dealer.resetHand();
            non_dealer.resetActions();
            non_dealer.resetHand();
            table.clearCommunityCards();
            table.postBlinds();
    
            for (int i = 0; i < 2; i++) {
                deck.dealToPlayer(non_dealer);
                deck.dealToPlayer(dealer);
            }
    
            refreshUI();
    
            table.setCurrentPlayer(dealer);
            table.setNonCurrentPlayer(non_dealer);
    
            roundLoop();
        }
    }

    public void refreshUI()
    {
        updateStacks();
        updateHands();
        updateDealer();
    }

    public void updateStacks()
    {
        pokerTable.updateBotBet(pokerTable.getTable().getBotBet());
        pokerTable.updateUserBet(pokerTable.getTable().getUserBet());
        pokerTable.updateCommunityPot(pokerTable.getTable().getPot());
        pokerTable.updateBotPot(pokerTable.getTable().getBot().getStack());
        pokerTable.updateUserPot(pokerTable.getTable().getUser().getStack());
    }

    public void updateDealer()
    {
        pokerTable.updateDealerIcon();
    }

    public void updateHands()
    {
        pokerTable.updateHandPanel(user.getHand(), true, false);
        pokerTable.updateHandPanel(bot.getHand(), false, false);
        pokerTable.addToCommunityHand(pokerTable.getTable().getCommunityCards());
    }

    public void userFold()
    {
        table.pushToPot();
        table.pushToWinner( table.getNonCurrentPlayer() );
        //table.setHandComplete(true);
        table.getUser().getHand().clearHand();
        table.getBot().getHand().clearHand();
        refreshUI();
        table.moveDealer();

        preflopInit();
    }

    public void userCall()
    {

        int max_bet = Integer.max(table.getUserBet(), table.getBotBet());
        int difference = Math.abs(table.getBotBet() - table.getUserBet());

        if (user.getStack() > difference) 
        {
            user.removeFromStack(difference);
            table.updateBetAmount(true, max_bet);
        }

        else //all in called
        {
            user.removeFromStack( user.getStack() ); 
            table.updateBetAmount(true, user.getStack() );
            table.setAllInCalled(true);
        }

        if (bot.getStack() == 0) table.setAllInCalled(true);

        table.swapCurrentandNonCurrentPlayers();

        user.setActedThisRound(true);
        roundLoop();
    }

    public void userRaise()
    {
        pokerTable.determineRaiseAmount();
        table.getCurrentPlayer().removeFromStack( table.getRaiseAmount() - table.getUserBet());
        table.updateBetAmount(true, table.getRaiseAmount());
        table.clearRaiseAmount();

        user.setActedThisRound(true);

        table.swapCurrentandNonCurrentPlayers();
        roundLoop();
    }

    public void userCheck()
    {
        if ( GameLogic.actionOpen(table) ) 
        {
            user.setActedThisRound(true);
            table.swapCurrentandNonCurrentPlayers();
            roundLoop();
        }
        else 
        {
            switch (table.getCurrentRound() )
            {
                case PREFLOP:
                    flopInit();
                    break;
                case FLOP:
                    turnInit();
                    break;
                case TURN:
                    riverInit();
                    break;
                case RIVER:
                    showDown();
                    break;
            }
        }
    }

    public void showDown()
    {
        int winner = -1;

        HandClassifier user_hand = new HandClassifier(table.getUser().getHand().getCards(), table.getCommunityCards() );
        HandClassifier bot_hand = new HandClassifier(table.getBot().getHand().getCards(), table.getCommunityCards() );

        HandStrength user_strength = user_hand.classify();
        int user_strength_int = -1;
        switch (user_strength)
        {
            case HIGHCARD:
                user_strength_int = 0;
                break;
            case ONEPAIR:
                user_strength_int = 1;
                break;
            case TWOPAIR:
                user_strength_int = 2;
                break;
            case TRIPS:
                user_strength_int = 3;
                break;
            case STRAIGHT:
                user_strength_int = 4;
                break;
            case FLUSH:
                user_strength_int = 5;
                break;
            case FULLHOUSE:
                user_strength_int = 6;
                break;
            case QUADS:
                user_strength_int = 7; 
                break;
            case STRAIGHTFLUSH:
                user_strength_int = 8;
                break;
        }
        HandStrength bot_strength = bot_hand.classify();
        int bot_strength_int = -1;
        switch (bot_strength)
        {
            case HIGHCARD:
                bot_strength_int = 0;
                break;
            case ONEPAIR:
                bot_strength_int = 1;
                break;
            case TWOPAIR:
                bot_strength_int = 2;
                break;
            case TRIPS:
                bot_strength_int = 3;
                break;
            case STRAIGHT:
                bot_strength_int = 4;
                break;
            case FLUSH:
                bot_strength_int = 5;
                break;
            case FULLHOUSE:
                bot_strength_int = 6;
                break;
            case QUADS:
                bot_strength_int = 7; 
                break;
            case STRAIGHTFLUSH:
                bot_strength_int = 8;
                break;
        }

        if (user_strength_int > bot_strength_int)
        {
            table.pushToWinner(user);
            winner = 0;
        }
        else if (user_strength_int < bot_strength_int) 
        {
            table.pushToWinner(bot);
            winner = 1;
        }
        else 
        {
            int tiebreak = GameLogic.breakTie(user_hand, bot_hand);
            switch(tiebreak)
            {
                case 0: 
                    table.pushToWinner(user);
                    winner = 0;
                    break;
                case 1:
                    table.pushToWinner(bot);
                    winner = 1;
                    break;
                case 2:
                    table.pushToBoth(user, bot);
                    winner = 2;
                    break;
            }
        }

        if (user.getStack() == 0 || bot.getStack() == 0) gameOver();
        else
        {
            boolean playAgain = true;
            pokerTable.updateHandPanel(bot.getHand(), false, true);

            switch (winner)
            {
                case 0:
                    playAgain = pokerTable.handOver(0);
                    break;
                case 1:
                    playAgain = pokerTable.handOver(1);
                    break;
                case 2:
                    playAgain = pokerTable.handOver(2);
                    break;
            }

            if (playAgain) 
            {
                table.moveDealer();
                table.swapCurrentandNonCurrentPlayers();
                preflopInit();
            }
            else System.exit(0);
        }
    }

    public void updateTable()
    {

    }
}