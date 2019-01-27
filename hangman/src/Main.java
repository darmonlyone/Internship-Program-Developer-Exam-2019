package src;

import src.HangmanGame.Hangman;
import src.HangmanGame.WordGuess;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main class for playing hangman game
 * This application use for Internship Exam 2019
 * @author Manusporn Fukkham
 * @since 27/01/2019
 */
class Main {

    private static Scanner scanner = new Scanner(System.in);

    /**
     * This is the main method which run the hangman game on the Terminal/Console (Command Line Interface)
     */
    public static void main(String[] args) {
        while (true) {
            File[] hangmanFiles = readHangManFile("hangman/category/");
            String[] hangmanCategories = new String[hangmanFiles.length];

            for (int i = 0; i < hangmanFiles.length; i++) {
                hangmanCategories[i] = getFileHangmanName(hangmanFiles[i]);
                println(i + 1 + ") " + hangmanCategories[i]);
            }

            Object category = readInput("Select Category: ");

            if (isInteger(category) && (int) category <= hangmanFiles.length && (int) category > 0) {
                File hangmanFile = hangmanFiles[(int) category - 1];
                Hangman hangmanGame = readFileToHangman(hangmanFile);
                assert hangmanGame != null;
                println("\nCategory: " + hangmanGame.getTopic());
                hangmanGame.randomHangmanWord();
                println("Hint: \"" + hangmanGame.getCorrectWord().getHint() + "\"\n");
                println(hangmanGame.status());
                println("*You can type correct word for win*");

                while (!hangmanGame.isEnd()) {
                    String guess = readInput(": ").toString().toLowerCase();
                    if (guess.equals(hangmanGame.getCorrectWord().getWord().toLowerCase())) hangmanGame.setScore(100);
                    else hangmanGame.guess(guess.substring(0, 1));
                }

                String loseWin = (hangmanGame.isWin() ? "Win" : (hangmanGame.isLose()) ? "Lose" : "Non");
                println("\nYour correct word is " + hangmanGame.getCorrectWord().getWord());
                System.out.printf("Scores: %.0f %nStatus: %s with %d guessing count left%n", hangmanGame.getScore(), loseWin, hangmanGame.getGuessCount());
                println("Thanks for playing our hangman game");
                break;

            } else println("Please input the No. of category (As integer type)\n");
        }
    }

    /**
     * Change the hangman file to title of hangman game
     * @param file hang man file that contain the word and hint of those word which
     *             file name should have "hangman" on it, also should be '.txt' file
     * @return the realistic name of hangman file
     */
    private static String getFileHangmanName(File file){
        return file.getName().replaceAll("Hangman", "").replaceAll("(.)([A-Z])", "$1 $2").replaceAll(".txt", "");
    }

    /**
     * Change the hangman file to Hangman from hangman class which this
     * file should contain the word and hint of those word which
     * file name should have "hangman" on it, also should be '.txt' file
     * @param file Hang man file can see more information on readHangManFile function
     * @return the hangman game from hangman class
     */
    private static Hangman readFileToHangman(File file){
        try {
            Scanner fileScanner = new Scanner(new FileReader(file));
            ArrayList<WordGuess> words = new ArrayList<>();
            while (fileScanner.hasNext()){
                String line = fileScanner.nextLine();
                if (!line.replaceAll(" ", "").isEmpty() && !line.startsWith("#")) {
                    String[] splitedLine = line.split(": ");
                    words.add(new WordGuess(splitedLine[0].trim(), splitedLine[1].trim()));
                }
            }
            return new Hangman(getFileHangmanName(file),words);
        } catch (FileNotFoundException e) {
            println("Cannot find this hang man file");
            return null;
        }
    }

    /**
     * Read all hangman file form the parameter directory.
     * Hangman file is the file that contain the word and hint of those word which use ": " to separate
     * word and hint, the title of those word will come File name which contain Hangman word.
     * @param dir directory to find hangman file
     * @return all the file that have hangman on it name, file should be `.txt` file
     */
    private static File[] readHangManFile(String dir){
        File f = new File(dir);
        return f.listFiles((dirrectory, name) -> name.contains("Hangman") && name.endsWith(".txt"));
    }

    /**
     * Shortcut function of print line
     * @param str String to print auto new line
     */
    private static void println(Object str){
        System.out.println(str);
    }

    /**
     * Shortcut function to scan the input which print the string for asking the input
     * @param str string for asking the input
     * @return the input and parse it to string or integer automatically
     */
    private static Object readInput(String str){
        System.out.print(str);
        String temp = scanner.nextLine();
        if (isInteger(temp)){
            return Integer.parseInt(temp);
        } else {
            return temp;
        }
    }

    /**
     * Boolean function of asking is those obj is integer of not
     * @param obj object that could be integer
     * @return true if obj is integer and false when it's not
     */
    private static boolean isInteger(Object obj) {
        try {
            Integer.parseInt(obj.toString());
        } catch(NumberFormatException | ClassCastException| NullPointerException e ) {
            return false;
        }
        return true;
    }
}
