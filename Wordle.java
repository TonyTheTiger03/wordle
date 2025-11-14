import java.util.Scanner;
import java.util.HashMap;

public class Wordle {
    public static void main(String[] args) {
        String cword = "apple";
        String uword = "";

        Scanner scan = new Scanner(System.in);

        while (!uword.equals(cword)) {
            System.out.print("Give me a word: ");
            uword = scan.next();

            if (uword.equals(cword)) {
                System.out.println("You win! The word was " + cword);
                System.exit(0);
            } else {
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
                        } else {
                            display = display.substring(0, i) + '.' + display.substring(i + 1);
                        }
                    }
                }

                System.out.println(display);
            }
        }
    }
}