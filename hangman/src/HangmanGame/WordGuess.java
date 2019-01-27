package src.HangmanGame;

/**
 * Word Guess class, containing the the word and hint
 * @author Manusporn Fukkham
 * @since 27/01/2019
 */
public class WordGuess{
    private String word;
    private String hint;

    /**
     * Constructor of WordGuess class with word and hint
     * @param word String of the word
     * @param hint String of hint of WordGuess word
     */
    public WordGuess(String word, String hint) {
        this.word = word;
        this.hint = hint;
    }

    /**
     * Word getter
     * @return String of word
     */
    public String getWord() {
        return word;
    }

    /**
     * Hint getter
     * @return String of hint of WordGuess word
     */
    public String getHint() {
        return hint;
    }
}
