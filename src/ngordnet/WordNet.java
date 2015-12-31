/**
 * Class WordNet
 * Read input files and store them for rapid queries
 * auther xiaofei
 */

package ngordnet;

import edu.princeton.cs.algs4.Digraph;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.TreeSet;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class WordNet{
    Digraph myGraph;
    int numSynset;
    Set<String> wordSet;
    Map<Integer, Set<String>> myMap;

    /** Creates a WordNet using files form SYNSETFILENAME and HYPONYMFILENAME */
    public WordNet(String synsetFilename, String hyponymFilename){
        wordSet = new TreeSet<String>();
        myMap = new HashMap<Integer, Set<String>>();
        try(BufferedReader inSynset = new BufferedReader(new FileReader(synsetFilename)))
        {
            String line = null;
            while((line = inSynset.readLine()) != null){
                String[] ar = line.split(",");
                int key = Integer.parseInt(ar[0]);
                numSynset += 1;
                Set<String> mySet = new TreeSet<String>();
                String[] words = ar[1].split(" ");
                for(String word : words){
                    mySet.add(word);
                    wordSet.add(word);
                }
                myMap.put(key, mySet);
            }
        } catch(IOException e){
            System.out.println("Fail to read synset file");
        }
        myGraph = new Digraph(numSynset); // create new graph
        try(BufferedReader inHyponyms = new BufferedReader(new FileReader(hyponymFilename)))
        {
            String line = null;
            while((line = inHyponyms.readLine()) != null){
                String[] ids = line.split(",");
                int start = Integer.parseInt(ids[0]);
                for(int i=1;i<ids.length;i++){
                    myGraph.addEdge(start, Integer.parseInt(ids[i]));
                }
            }
        }catch(IOException e){
            System.out.println("Fail to read hyponyms file");
        }
    }

    /* Returns true if NOUN is a word in some synset. */
    public boolean isNoun(String noun){
        return wordSet.contains(noun);
    }

    /* Returns the set of all nouns. */
    public Set<String> nouns(){
        return wordSet;
    }

    /** Returns the set of all hyponyms of WORD as well as all synonyms of
      * WORD. If WORD belongs to multiple synsets, return all hyponyms of
      * all of these synsets. See http://goo.gl/EGLoys for an example.
      * Do not include hyponyms of synonyms.
      */
    public Set<String> hyponyms(String word){
        Set<String> result = new TreeSet<>();
        Set<Integer> setId = new TreeSet<>();
        for(Integer key : myMap.keySet()){
            Set<String> currentSet = myMap.get(key);
            if(currentSet.contains(word)){
                // add words in currentSet into result
                result.addAll(currentSet);
                setId.add(key);
            }
        }
        // find the hypos and add into result
        Set<Integer> descentSet = GraphHelper.descendants(myGraph, setId);
        for(Integer i : descentSet){
            result.addAll(myMap.get(i));
        }
        return result;
    }
}
