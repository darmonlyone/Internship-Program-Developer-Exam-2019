package src.HangmanGame;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Hangman class for playing hangman game
 * This game set maximum guess of 5 and word can be contain
 * number or special character but this class won't let those word to be hide
 * @author Manusporn Fukkham
 * @since 27/01/2019
 */
public class Hangman {
    private ArrayList<WordGuess> words;
    private double score;
    private int guessCount;
    private WordGuess correctWord;
    private String hangmanWord;
    private String topic;
    private List<String> usedChar;

    /**
     * Constructor of Hangman class with title of
     * hangman game and list of word to use on this game.
     * @param title parameter of Topic of of hangman game
     * @param words parameter of list of WordGuess class with containing
     *              the word to play on this hangman game
     */
    public Hangman(String title, ArrayList<WordGuess> words) {
        this.topic = title;
        this.words = words;
        score = 0.00;
        guessCount = 5;
        usedChar = new ArrayList<>();
    }

    /**
     * Random the word for playing this game
     * and set up the application to ready for hangman game.
     */
    public void randomHangmanWord(){
        if (hangmanWord == null){
            Random random = new Random();
            WordGuess word = words.get(random.nextInt(words.size()));
            hangmanWord = word.getWord().replaceAll("([A-z])", "_ ");
            correctWord = word;
        }
    }

    /**
     * Guess the of hangman game with a character and
     * run the hangman word and score automatically.
     * Calculate the score with total of 100 divide with each character each.
     * This method won't work if not calling randomHangmanWord.
     * @param str a character of string that use for guessing.
     */
    public void guess(String str){
        if (hangmanWord == null) return;
        Pattern pattern = Pattern.compile("[A-z]");

        if (!pattern.matcher(str).matches())
            System.out.println("This character is incorrect");

        else if (!usedChar.contains(str)){
            int pointer = 0;
            usedChar.add(str);
            StringBuilder hiddenWord = new StringBuilder(hangmanWord);
            String answer = correctWord.getWord();

            if (answer.toLowerCase().contains(str)) {
                int correctCount = 0;
                pointer += 1;
                char guessChar = str.charAt(0);
                for (int i = 0; i < answer.length(); i++) {
                    if (answer.charAt(i) == ' ' || !pattern.matcher(Character.toString(answer.charAt(i))).matches()) pointer += 1;
                    if (Character.toLowerCase(answer.charAt(i)) == Character.toLowerCase(guessChar)) {
                        correctCount++;
                        hiddenWord.setCharAt((((i + 1) * 2) - 1) - pointer, answer.charAt(i));
                    }
                }
                hangmanWord = hiddenWord.toString();
                score += correctCount*(100.0/(answer.length() - pointer + 1));
                // print the status with correct guss
                System.out.println(status() + ", guess correct: " + str);
            } else {
                guessCount--;
                // print the status with wrong guss
                System.out.println(status() + ", guess wrong: " + str);
            }
        }else
            System.out.println("This character is used please try again");

    }

    /**
     * Topic getter method
     * @return String of topic of this hangman game
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Show the overall status of this hangman game
     * @return String of overall status
     */
    public String status(){
        return String.format("%s  score %.0f, remaining wrong guess %d", hangmanWord, getScore(), guessCount);
    }

    /**
     * Boolean to check wining of this hangman game
     * @return Boolean of score more than or equal 100
     */
    public boolean isWin(){
        return score >= 100;
    }

    /**
     * Boolean to check loosing of this hangman game
     * @return Boolean of guessing count equal 0
     */
    public boolean isLose(){
        return guessCount == 0;
    }

    /**
     * Boolean to check that game is should be end already or not
     * @return Boolean isLose or isWin already or not
     */
    public boolean isEnd(){
        return isLose() || isWin();
    }

    /**
     * Score getter to get the score of your game
     * @return Double of score, score will floor down to 100
     *         when score is over 100
     */
    public double getScore(){
        if (Math.ceil(score) >= 100) score = 100;
        return score;
    }

    /**
     * Guess Counting getter
     * @return Integer of guess count left to use on this game
     */
    public int getGuessCount() {
        return guessCount;
    }

    /**
     * Answer getter
     * @return the correct answer for this hangman game as WordGeuss class
     */
    public WordGuess getCorrectWord(){
        return correctWord;
    }

    /**
     * Score setter for setting the score
     * @param score score to set the score of player playing this game
     */
    public void setScore(int score) {
        this.score = score;
    }
}