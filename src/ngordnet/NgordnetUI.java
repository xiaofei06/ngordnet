/** Provides a simple user interface for exploring WordNet and NGram data.
 *  @author Xiaofei
 */

package ngordnet;

import java.util.Arrays;
import java.util.Set;
import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.In;

public class NgordnetUI {
    public static void main(String[] args) {
        In in = new In("../ngordnetui.config");
        System.out.println("Reading ngordnetui.config...");

        String wordFile = in.readString();
        String countFile = in.readString();
        String synsetFile = in.readString();
        String hyponymFile = in.readString();
        System.out.println("\nBased on ngordnetui.config, using the following: "
                           + wordFile + ", " + countFile + ", " + synsetFile +
                           ", and " + hyponymFile + ".");
        NGramMap ngm = new NGramMap(wordFile, countFile);
        WordNet wn = new WordNet(synsetFile, hyponymFile);
        YearlyRecordProcessor yrp = new WordLengthProcessor();
        int startDate = 1;
        int endDate = 2015;
        while(true){
            System.out.print("> ");
            String line = StdIn.readLine();
            String[] rawTokens = line.split(" ");
            String command = rawTokens[0];
            String[] tokens = new String[rawTokens.length - 1];
            System.arraycopy(rawTokens, 1, tokens, 0, rawTokens.length - 1);
            switch(command){
                case "quit":
                    return;
                case "help":
                    In inhelp = new In("../help.txt");
                    String helpStr = inhelp.readAll();
                    System.out.println(helpStr);
                    break;
                case "range":
                    startDate = Integer.parseInt(tokens[0]);
                    endDate = Integer.parseInt(tokens[1]);
                    System.out.println("Start date: " + startDate);
                    System.out.println("End date: " + endDate);
                    break;
                case "count":
                    String word = tokens[0];
                    int year = Integer.parseInt(tokens[1]);
                    int count = ngm.countInYear(word, year);
                    System.out.println(count);
                    break;
                case "hyponyms":
                    String s = tokens[0];
                    Set<String> hypos = wn.hyponyms(s);
                    String[] hyposList = hypos.toArray(new String[hypos.size()]);
                    System.out.println(Arrays.toString(hyposList));
                    break;
                case "history":
                    Plotter.plotAllWords(ngm, tokens, startDate, endDate);
                    break;
                case "hypohist":
                    Plotter.plotCategoryWeights(ngm, wn, tokens, startDate, endDate);
                    break;
                case "wordLength":
                    Plotter.plotProcessedHistory(ngm, startDate, endDate, yrp);
                    break;
                case "zipf":
                    int y = Integer.parseInt(tokens[0]);
                    System.out.println(y);
                    Plotter.plotZipfsLaw(ngm, y);
                    break;
                default:
                    System.out.println("Invalid command.");
                    break;
            }

        }

    }
}
