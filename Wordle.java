import java.util.Scanner;
import java.util.Random;

/**
 * Project 2: Wordle
 *
 * This project has you create a text based version of Wordle
 * (https://www.nytimes.com/games/wordle/index.html). Wordle is a word guessing
 * game in which you have 6 tries to guess a 5-letter word. You are told whether
 * each letter of your guess is in the word and in the right position, in the
 * word but in the wrong position, or not in the word at all.
 *
 * Some key differences in our version are:
 *
 * - Text menu based with no grid. Players have to scroll up to see their
 * previous guesses.
 *
 * - Support for 4, 5, or 6 letter words
 *
 * - We don't check for whether a guess is a valid word or not. Players can
 * guess anything they want (of the correct length).
 *
 * Fun facts: The original Wordle was developed by Josh Wardle. Wardle's wife
 * chose the official word list for the game.
 *
 * 500.112 Gateway Computing: Java Spring 2022
 * 
 * @author Mark Faust (mfaust4 - 10/5/22)
 */
public class Wordle {

   /**
    * Defining the only Random variable you may (and must) use. DO NOT CHANGE
    * THIS LINE OF CODE.
    */
   static Random gen = new Random(0);

   /**
    * Defines the number of guesses the player starts with for each word. DO NOT
    * CHANGE THIS LINE OF CODE.
    */
   static final int MAX_GUESSES = 6;
   /**
    * Defines the number of hints the player starts with for each word. DO NOT
    * CHANGE THIS LINE OF CODE.
    */
   static final int MAX_HINTS = 2;
   /**
    * The main method. This is where most of your menu logic and game logic
    * (i.e. implementation of the rules of the game ) will end up. Feel free to
    * move logic in to smaller subroutines as you see fit.
    *
    * @param args commandline args
    */
    
   public static void main(String[] args) {
      Scanner kb = new Scanner(System.in);
      
      int guesses = MAX_GUESSES;
      int hints = MAX_HINTS;
      String word = newWord();
      String guess;
      String text = prompt(kb);

      while (true) {
         //checks if entered text is 'E'
         if (text.charAt(0) == 'E') {
            System.exit(0);
         }
         //checks if entered text is 'N'
         if (text.charAt(0) == 'N') {
            word = newWord();
            guesses = MAX_GUESSES;
            hints = MAX_HINTS;
            text = prompt(kb); 
            continue;
         }
         //checks if entered text is 'H'.
         if (text.charAt(0) == 'H') {
            //checks if any hints are left
            if (hints > 0) {
               giveHint(word);
               --hints;
               //prints number of hints left
               if (hints == 1) {
                  System.out.println("You have " + hints + " hint remaining.");
               }
               else {
                  System.out.println("You have " + hints + " hints remaining.");
               }
               text = prompt(kb); 
               continue;
            }
            else {
               System.out.println("Sorry, you're out of hints!");
               text = prompt(kb); 
               continue;
            }
         }
         //checks if entered text is 'G'
         if (text.charAt(0) == 'G') {
            //checks if any guesses are left
            if (guesses > 0) {
               System.out.println("Enter your guess:");
               guess = kb.nextLine();
               //makes sure that guess is valid
               if (validateGuess(word.length(), guess)) {
                  --guesses;
                  //check to see if the correct word was guessed
                  if (checkGuess(word, guess)) {
                     System.out.println("Congrats! You won!");
                     guesses = 0;
                     text = prompt(kb); 
                     continue;
                  }
                  //prints how many guesses remaining if guess isn't right
                  else {
                     if (guesses == 1) {
                        System.out.print("You have ");
                        System.out.println(guesses + " guess remaining.");
                     }
                     else if (guesses != 0) {
                        System.out.print("You have ");
                        System.out.println(guesses + " guesses remaining.");
                     }
                     else {
                        System.out.print("Sorry, you're out of guesses! ");
                        System.out.print("The word was " + word + ". ");
                        System.out.print("Use the \"n\"/\"N\" command ");
                        System.out.println("to play again.");
                     }
                     text = prompt(kb); 
                     continue;
                  }
               }
               else {
                  text = prompt(kb); 
                  continue;
               }
            }
            else {
               System.out.print("Sorry, you're out of guesses! ");
               System.out.println("Use the \"n\"/\"N\" command to play again.");
               text = prompt(kb); 
               continue;
            }  
         }
      }
   }

   /**
    * Prints "HINT! The word contains the letter: X" where X is a randomly
    * chosen letter in the word parameter.
    *
    * @param word The word to give a hint for.
    */
   static void giveHint(String word) {
      int i = gen.nextInt(word.length());
      System.out.print("HINT! The word contains the letter: ");
      System.out.println(word.charAt(i));
   }

   /**
    * Checks the players guess for validity. We define a valid guess as one that
    * is the correct length and contains only lower case letters and upper case
    * letters. If either validity condition fails, a message is printed 
    * indicating which condition(s) failed.
    *
    * @param length The length of the current word that the player is trynig to
    *               guess.
    * @param guess  The guess that the player has entered.
    * @return true if the guess is of the correct length and contains only valid
    * characters, otherwise false.
    */
   static boolean validateGuess(int length, String guess) {
      //makes sure guess is one of the accepted values (letter and length)
      for (int i = 0; i < guess.length(); ++i) {
         if (guess.length() != length && 
            (guess.charAt(i) < 65 || guess.charAt(i) > 122)) {
            System.out.println("You must enter a guess of length " + length);
            System.out.print("Your guess must only contain upper case ");
            System.out.println("letters and lower case letters");
            return false;
         }
      }
      //makes sure guess is of the right length
      if (guess.length() != length) {
         System.out.println("You must enter a guess of length " + length);
         return false;
      }
      //makes sure guess only contains letters
      for (int i = 0; i < guess.length(); ++i) {
         if (guess.charAt(i) < 65 || guess.charAt(i) > 122) {
            System.out.print("Your guess must only contain upper case ");
            System.out.println("letters and lower case letters");
            return false;
         }
      }
      return true;
   }

   /**
    * Checks the player's guess against the current word. Capitalization is
    * IGNORED for this comparison. This function also prints a string
    * corresponding to the player's guess. A ? indicates a letter that isn't in
    * the word at all. A lower case letter indicates that the letter is in the
    * word but not in the correct position. An upper case letter indicates a
    * correct letter in the correct position. Example:
    *
    * SPLINE (the correct word)
    *
    * SPEARS (the player's guess)
    *
    * SPe??s (the output printed by this function)
    *
    * Suggestion 1: Convert guess to upper case before doing anything else. This
    * can help simplify later logic.
    *
    * Suggestion 2: Consider using String.indexOf
    *
    * @param word  The current word the player is trying to guess.
    * @param guess The guess that a player has entered.
    * @return true if the word and guess match IGNORING CASE, otherwise false.
    */
   static boolean checkGuess(String word, String guess) {
      word = word.toUpperCase();
      guess = guess.toUpperCase();
      
      if (word.equals(guess)) {
         System.out.println(word);
         return true;
      }
      
      int i = 0;
      //checks each letter of user's guess with each letter of the actual word
      if (word.length() > 0 && word.length() > 0) {
         for (int k = 0; k < word.length(); ++k) {
            for (i = 0; i < word.length(); ++i) {
               if (guess.charAt(k) == word.charAt(i)) {
                  if (k == i) {
                     System.out.print(guess.charAt(k));
                  }
                  else {
                     System.out.print(guess.toLowerCase().charAt(k));
                  }
                  break;
               }
            }
            if (i == word.length()) {
               System.out.print("?");
            }
         }
      }
      System.out.println();
      return false;
   }

   /**
    * Chooses a random word using WordProvider.getWord(int length). This should
    * print "New word length: X" where x is the length of the word.
    *
    * @return newWord the randomly chosen word
    */
   static String newWord() {
      int random = gen.nextInt(3) + 4;
      String newWord = WordProvider.getWord(random);
      System.out.println("New word length: " + newWord.length());
      return newWord;
   }

   /**
    * Prints menu options.
    */
   static void printMenu() {
      System.out.println("n/N: New word");
      System.out.println("h/H: Get a hint");
      System.out.println("g/G: Make a guess");
      System.out.println("e/E: Exit");
      System.out.println("-------------");
   }
   
   /**
    * Prompts the user to input text used to select 
    * one of the four differet menu options.
    *
    * @param kb    The Scanner object that is passed in from the main method
    * @return text The String literal that is being checked with each menu print
    */ 
   static String prompt(Scanner kb) {
      printMenu();
      System.out.print("Please enter a choice: ");
      String text = kb.nextLine().toUpperCase();
      
      //checks if user input is one of the valid options and no more than 1 char
      if ((text.charAt(0) != 'E' && text.charAt(0) != 'H' &&
          text.charAt(0) != 'N' && text.charAt(0) != 'G') || 
          text.length() > 1) {
         System.out.println("Invalid option! Try again!");
         text = prompt(kb);
      }
      return text;
   }
}