import model.*;
import view.*;

import java.io.File;
import java.io.IOException;

import controller.*;


public class Driver {  
    public static void main(String args[]) {
      
      try {
        File numhands_file = new File("numHands.txt");
        if (numhands_file.createNewFile() ) {
          System.out.println("File created: " + numhands_file.getName());
        } else {
          System.out.println("File already exists.");
        }
      } catch (IOException e) {
        System.out.println("An error occurred writing file.");
      }
      
      Bot bot = new Bot();
      User user = new User();

      Table modelTable = new Table(user, bot);

      PokerTable tableUI = new PokerTable(modelTable);
      tableUI.createTable();
      PokerController game = new PokerController(tableUI);
    }
}