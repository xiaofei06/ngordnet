/**
 * Class YearlyRcord
 * author xiaofei
 */

package ngordnet;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Collection;


public class YearlyRecord{
    /** a hashmap maps word to count */
    private HashMap<String, Integer> wordToCount;
    /** a treemap maps count to words */
    private TreeMap<Integer, ArrayList<String>> countToWords;
    /** a hashmap maps word to rank */
    private HashMap<String, Integer> wordToRank;
    /** number of words in YearlyRecord */
    private int size;
    /** a boolean indicate whether ranking has performed */
    private boolean ranked;

    /** Creates a new empty YearlyRecord. */
    public YearlyRecord(){
        wordToCount = new HashMap<>();
        countToWords = new TreeMap<>();
        wordToRank = new HashMap<>();
        size = 0;
        ranked = false;
    }

    /** Creates a YearlyRecord using the given data. */
    public YearlyRecord(HashMap<String, Integer> otherCountMap){
        this();
        for(String word : otherCountMap.keySet()){
            this.put(word, otherCountMap.get(word));
        }
    }

    public YearlyRecord copy(){
        YearlyRecord newYR = new YearlyRecord(this.wordToCount);
        newYR.ranking();
        return newYR;
    }

    /** Returns the number of times WORD appeared in this year. */
    public int count(String word){
        if(wordToCount.containsKey(word)){
            return wordToCount.get(word);
        }
        return 0;
    }

    /** Records that WORD occurred COUNT times in this year. */
    public void put(String word, Integer count){
        // update wordToCount map
        wordToCount.put(word, count);
        // update countToWords map
        ArrayList<String> temp;
        if(countToWords.containsKey(count)){
            temp = countToWords.get(count);
            temp.add(word);
        }else{
            temp = new ArrayList<>();
            temp.add(word);
        }
        countToWords.put(count, temp);
        // update size
        size++;
        //do not update wordToRank map
        ranked = false;
    }

    /** Returns the number of words recorded this year. */
    public int size(){
        return this.size();
    }

    /** Returns all words in ascending order of count. */
    public Collection<String> words(){
        ArrayList<String> words = new ArrayList<>();
        for(int num : countToWords.keySet()){
            words.addAll(countToWords.get(num));
        }
        return words;
    }

    /** Returns all counts in ascending order of count. */
    public Collection<Integer> counts(){
        ArrayList<Integer> counts = new ArrayList<>();
        for(String s : this.words()){
            counts.add(wordToCount.get(s));
        }
        return counts;
    }

    /** Returns rank of WORD. Most common word is rank 1.
      * If two words have the same rank, break ties arbitrarily.
      * No two words should have the same rank.
      */
    public int rank(String word){
        if(!ranked){
            ranking();
        }
        return wordToRank.get(word);
    }

    /** construct the ranking map */
    private void ranking(){
        wordToRank = new HashMap<>();
        if(ranked){
            return;
        }
        int r = 1; // rank
        for(int num : countToWords.keySet()){
            for(String word : countToWords.get(num)){
                wordToRank.put(word, r);
                r++;
            }
        }
        ranked = true;
    }
}
