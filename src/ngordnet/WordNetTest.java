/**
 * unit tests for WordNet
 * author xiaofei
 */

 
package ngordnet;

import java.util.Set;
import java.util.TreeSet;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class WordNetTest{
    WordNet wn;

    @Before
    public void initialize(){
        wn = new WordNet("../p1data/wordnet/synsets11.txt", "../p1data/wordnet/hyponyms11.txt");
    }

    @Test
    public void testIsNoun(){
        assertTrue(wn.isNoun("jump"));
        assertTrue(wn.isNoun("leap"));
        assertTrue(wn.isNoun("nasal_decongestant"));
    }

    @Test
    public void testNouns(){
        Set<String> allNouns = new TreeSet<>();
        String[] nouns = {"augmentation", "nasal_decongestant", "change", "action",
            "actifed", "antihistamine", "increase", "descent", "parachuting", "leap", "demotion", "jump"};
        for(int i = 0; i < nouns.length; i++){
            allNouns.add(nouns[i]);
        }
        assertEquals(allNouns, wn.nouns());
    }
    @Test
    public void testHyponyms(){
        Set<String> hyponyms = new TreeSet<>();
        hyponyms.add("augmentation");
        hyponyms.add("leap");
        hyponyms.add("increase");
        hyponyms.add("jump");
        assertEquals(hyponyms, wn.hyponyms("increase"));
    }

    public static void main(String[] args){

        jh61b.junit.textui.runClasses(WordNetTest.class);
    }
}
