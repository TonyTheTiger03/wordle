package org.example;

import java.util.HashMap;
import java.util.Random;
import java.util.List;

public class WordleGame {
    private List<String> words;
    private String word;
    private int guessesLeft;

    public WordleGame(List<String> words) {
        this.words = words;

        Random rand = new Random();
        this.word = words.get(rand.nextInt(words.size()));

        this.guessesLeft = 5;
    }

    public String getWord(){
        return this.word;
    }

    public int getGuessesLeft(){
        return this.guessesLeft;
    }

    public boolean isGuessValid(String guess) {
        if(guess.length() != 5){
            return false;
        }

        for (String w : words) {
            if (w.equals(guess)) {
                return true;
            }
        }

        return false;
    }

    public String checkGuess(String guess) {
        if (guess.equals(word)) {
            return word;
        } else {
            guessesLeft -= 1;

            HashMap<Character, Integer> counts = new HashMap<>();
            for(int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                counts.put(c, counts.getOrDefault(c, 0) + 1);
            }

            // +   = REPLACE ME
            String display = "+++++";

            // .   = wrong place wrong letter
            // *   = wrong place right letter
            // a-z = right place right letter
            for(int i = 0; i < guess.length(); i++){
                char g = guess.charAt(i);
                if(!counts.containsKey(g)){
                    display = display.substring(0, i) + '.' + display.substring(i + 1);
                } else if(g == word.charAt(i)){
                    display = display.substring(0, i) + g + display.substring(i + 1);
                    counts.put(g, counts.get(g) - 1);
                }
            }

            for(int i = 0; i < guess.length(); i++){
                char g = guess.charAt(i);
                if(display.charAt(i) == '+') {
                    if (counts.getOrDefault(g, 0) > 0) {
                        display = display.substring(0, i) + '*' + display.substring(i + 1);
                        counts.put(g, counts.get(g) - 1);
                    } else {
                        display = display.substring(0, i) + '.' + display.substring(i + 1);
                    }
                }
            }

            return display;
        }
    }
}