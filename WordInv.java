import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.TreeMap;


public class WordInv {

    // instance variables
    private TreeMap<String, Integer> words;
    private ArrayList<Pair> ranks;
    
    public WordInv(Scanner sc) {
        // look at each "word" in file
        // add to map if first occurrence
        // otherwise update frequency
        words = new TreeMap<String, Integer>();
        while(sc.hasNext()) {
            String word = sc.next().toLowerCase();
            if(words.containsKey(word) == false)
                words.put(word, 1);
            else
                words.put(word, words.get(word) + 1);
        } 
        // word no longer exists
//        System.out.println(words.size());
//        for(String word : words.keySet())
//            System.out.println(word + " " + words.get(word));
        ranks = new ArrayList<Pair>();
        for(String word : words.keySet()) {
            ranks.add(new Pair(words.get(word), word));
        }
        
        Collections.sort(ranks);
    }
    
    public void showSome(int amount) {
        int index = 0;
        while(index < amount && index < ranks.size()) {
            System.out.println("rank: " + (index + 1) 
                    + " " + ranks.get(index));
            index++;
        }
    }
    
    public void showError(int amount) {
        int aveConstant = getExpectedConstant(amount);
        System.out.println(aveConstant);
        int index = 0;
        while(index < amount && index < ranks.size()) {
            // expected freq = constant / rank
            int expectedFreq = aveConstant / (index + 1);
            int actualFreq = ranks.get(index).count;
            double error = (expectedFreq - actualFreq) / (1.0 * expectedFreq) * 100;
            System.out.println("rank: " + (index + 1) + " word and actual freq: "
                    + ranks.get(index) + ", expected freq: "
                    + expectedFreq + ", error: " + error);
            index++; // shorthand for index = index + 1
        }
    }
    
    
    private int getExpectedConstant(int amount) {
        double total = 0;
        for(int i = 0; i < amount; i++) {
            // constant should equal freq * rank
            double oneConstant = ranks.get(i).count * (i + 1);  
            total +=  oneConstant;
//            System.out.println(total + " " + oneConstant);
        }
        return (int) (total / amount);
    }


    private static class Pair implements Comparable<Pair> {
        
        private int count;
        private String word;
        
        public Pair(int c, String s) {
            count = c;
            word = s;
        }
        
        public String toString() {
            return word + " " + count;
        }
        
        public int compareTo(Pair other) {
            int result = other.count - this.count;
            if(result == 0)
                result = this.word.compareTo(other.word);
            return result;
        }
        
       
    }
}
