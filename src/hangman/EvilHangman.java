package hangman;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

public class EvilHangman {

    public static void main(String[] args) throws Exception {
        if (args[0] == null || args[1] == null || args[2] == null) {
            throw new Exception("Invalid arguments");
        }

        File file = new File(args[0]);
        int wordLength = Integer.parseInt(args[1]);
        int guesses = Integer.parseInt(args[2]);
        if (guesses < 1) {
            throw new Exception("Invalid arguments");
        }
        EvilHangmanGame evilHangmanGame = new EvilHangmanGame();
        SortedSet<Character> guessedLetters = new TreeSet<>();
        try {
            evilHangmanGame.startGame(file, wordLength);
        }
        catch (IOException | EmptyDictionaryException e) {
            e.getMessage();
            return;
        }



        while (guesses > 0) {
            System.out.println("You have " + guesses + " guesses left");
            System.out.println("Used letters: " + evilHangmanGame.getGuessedLetters());
            System.out.println("Word: " + evilHangmanGame.getWord());
            System.out.println("Enter guess: ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine().toLowerCase();
            if (input.length() != 1) {
                System.out.println("Invalid input!\n");
                continue;
            }
            char guess = input.toLowerCase().charAt(0);
            guessedLetters = evilHangmanGame.getGuessedLetters();
            if (guessedLetters.contains(guess)) {
                System.out.println("You already used that letter");
                continue;
            } else if (guess < 'a' || guess > 'z') {
                System.out.println("Invalid input!\n");
                continue;
            }
            try {
                evilHangmanGame.makeGuess(guess);
            }
            catch (GuessAlreadyMadeException e) {
                e.getMessage();
                continue;
            }
            if (evilHangmanGame.getWord().contains(String.valueOf(guess))) {
                System.out.println("Yes, there is " + evilHangmanGame.guessedLetterCount(evilHangmanGame.getWord(),
                        guess) + " " + guess + "'s\n");
                if (!evilHangmanGame.containsDash(evilHangmanGame.getWord())) {
                    System.out.println("You win! You guessed the word: " + evilHangmanGame.getWord());
                    guesses = 0;
                }
            }
            else {
                System.out.println("Sorry, there are no " + guess + "'s\n");
                guesses--;
                if (guesses == 0 && evilHangmanGame.containsDash(evilHangmanGame.getWord())) {
                    System.out.println("Sorry, you lost! The word was: " + evilHangmanGame.getFinishedWord());
                    break;
                } else if (guesses != 0 && !evilHangmanGame.containsDash(evilHangmanGame.getWord())) {
                    System.out.println("You win! You guessed the word: " + evilHangmanGame.getWord());
                    break;
                }
            }
        }
    }
}
