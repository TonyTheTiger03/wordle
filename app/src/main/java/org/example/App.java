
package org.example;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class App {
    public static void main(String[] args) {
        String cword = "";
        String uword = "";

        Scanner scan = new Scanner(System.in);
        List<String> words = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(Paths.get("words.txt"));

            for (String line : lines) {
                String w = line.trim().toLowerCase();
                if (!w.isEmpty()) {
                    words.add(w);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading words.txt: " + e.getMessage());
            return; 
        }
            if (words.isEmpty()) {
                System.out.println("No words found in words.txt");
                return;
        }

        Random rand = new Random();
        cword = words.get(rand.nextInt(words.size()));
        
        if (words.isEmpty()) {
            System.out.println("No words found in words.txt");
            return;
        }
        
        System.out.println("This is wordle you have 5 tries to guess the 5 letter word correctly:");
        System.out.println(". mean wrong place wrong letter and * mean wrong place right letter");
        
        Integer guessCount = 5;
        
        while (!uword.equals(cword)) {

            uword = scan.next();
            
            
            if (uword.equals(cword)) {
                System.out.println("You win! The word was " + cword);
                System.exit(0);
            } else {
                guessCount -= 1;
                if(guessCount <= 5 && guessCount > 0){
                    HashMap<Character, Integer> counts = new HashMap();
                    for(int i = 0; i < cword.length(); i++) {
                        if (counts.containsKey(cword.charAt(i))) {
                            counts.put(cword.charAt(i), counts.get(cword.charAt(i)) + 1);
                        } else {
                            counts.put(cword.charAt(i), 1);
                        }
                    }

                    // +   = REPLACE ME
                    String display = "+++++";

                    // .   = wrong place wrong letter
                    // *   = wrong place right letter
                    // a-z = right place right letter
                    for(int i = 0; i < uword.length(); i++){
                        if(counts.get(uword.charAt(i)) == null){
                            // not in the word at all
                            display = display.substring(0, i) + '.' + display.substring(i + 1);
                        } else if(uword.charAt(i) == cword.charAt(i)){
                            // perfect spot
                            display = display.substring(0, i) + uword.charAt(i) + display.substring(i + 1);
                            counts.put(cword.charAt(i), counts.get(cword.charAt(i)) - 1);
                        }
                    }

                    for(int i = 0; i < uword.length(); i++){
                        if(display.charAt(i) == '+') {
                            if (counts.get((Character)uword.charAt(i)) > 0) {
                                display = display.substring(0, i) + '*' + display.substring(i + 1);
                                counts.put(uword.charAt(i), counts.get(uword.charAt(i)) - 1);
                            } else {
                                display = display.substring(0, i) + '.' + display.substring(i + 1);
                            }
                        }
                    } 
                    System.out.println(display);  
                    System.out.println("You have " + guessCount + " triess left"); 
                }
                else{
                    System.out.println("You lose! the word was " + cword);
                    System.exit(0);
                }

            }
        }
    }
}
