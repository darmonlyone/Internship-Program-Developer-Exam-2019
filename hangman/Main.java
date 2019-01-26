import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

class Main {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            File[] hangmanFiles = readHangManFile();
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

                String loseWin = (hangmanGame.isWin() ? "Win" : "Lose");
                println("\nYour correct word is " + hangmanGame.getCorrectWord().getWord());
                System.out.printf("Scores: %.0f %nStatus: %s with %d guessing count left%n", hangmanGame.getScore(), loseWin, hangmanGame.getGuessCount());
                println("Thanks for playing our hangman game");
                break;

            } else println("Please input the No. of category (As integer type)\n");
        }
    }

    private static String getFileHangmanName(File file){
        return file.getName().replaceAll("Hangman", "").replaceAll("(.)([A-Z])", "$1 $2").replaceAll(".txt", "");
    }

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

    private static File[] readHangManFile(){
        File f = new File("hangman/category/");
        return f.listFiles((dir, name) -> name.contains("Hangman") && name.endsWith(".txt"));
    }

    private static void println(Object str){
        System.out.println(str);
    }

    private static void print(Object str){
        System.out.print(str);
    }

    private static Object readInput(String str){
        print(str);
        String temp = scanner.nextLine();
        if (isInteger(temp)){
            return Integer.parseInt(temp);
        } else {
            return temp;
        }
    }

    private static boolean isInteger(Object str) {
        try {
            Integer.parseInt(str.toString());
        } catch(NumberFormatException | ClassCastException| NullPointerException e ) {
            return false;
        }
        return true;
    }
}

class Hangman {
    private ArrayList<WordGuess> words;
    private double score;
    private String hangmanWord;
    private int guessCount;
    private WordGuess correctWord;
    private String topic;
    private List<String> usedChar;

    public Hangman(String topic, ArrayList<WordGuess> words) {
        this.topic = topic;
        this.words = words;
        score = 0.00;
        guessCount = 10;
        usedChar = new ArrayList<>();
    }

    public void randomHangmanWord(){
        if (hangmanWord == null){
            Random random = new Random();
            WordGuess word = words.get(random.nextInt(words.size()));
            hangmanWord = word.getWord().replaceAll("([A-z])", "_ ");
            correctWord = word;
        }
    }

    public void guess(String str){
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
                System.out.println(status() + ", guess correct: " + str);
            } else {
                guessCount--;
                System.out.println(status() + ", guess wrong: " + str);
            }
        }else
            System.out.println("This character is used please try again");

    }

    public String getTopic() {
        return topic;
    }

    public String status(){
        return String.format("%s  score %.0f, remaining wrong guess %d", hangmanWord, getScore(), guessCount);
    }

    public boolean isWin(){
        return score >= 100;
    }

    public boolean isLose(){
        return guessCount == 0;
    }

    public boolean isEnd(){
        return isLose() || isWin();
    }

    public double getScore(){
        if (Math.ceil(score) >= 100) score = 100;
        return score;
    }

    public int getGuessCount() {
        return guessCount;
    }

    public WordGuess getCorrectWord(){
        return correctWord;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

class WordGuess{
    private String word;
    private String hint;

    public WordGuess(String word, String hint) {
        this.word = word;
        this.hint = hint;
    }

    public String getWord() {
        return word;
    }

    public String getHint() {
        return hint;
    }
}
