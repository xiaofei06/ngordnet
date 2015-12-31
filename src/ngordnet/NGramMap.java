/**
 * Class NGramMap
 * Provide various convenient methods for interacting with Google's NGrams dataset.
 * author xiaofei
 */

package ngordnet;

import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

public class NGramMap {
    /** Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME. */
    HashMap<Integer, YearlyRecord> yearlyRecords;
    HashMap<String, TimeSeries<Integer> > wordHistory;
    TimeSeries<Long> totalCounts;

    public NGramMap(String wordsFilename, String countsFilename){
        yearlyRecords = new HashMap<>();
        wordHistory = new HashMap<>();
        totalCounts = new TimeSeries<>();

        try(BufferedReader inWords = new BufferedReader(new FileReader(wordsFilename))){
            String line = null;
            while((line = inWords.readLine()) != null){
                String[] ar = line.split("\\t");
                String word = ar[0];
                int year = Integer.parseInt(ar[1]);
                int count = Integer.parseInt(ar[2]);

                if(yearlyRecords.containsKey(year)){
                    yearlyRecords.get(year).put(word, count);
                }else{
                    YearlyRecord temp = new YearlyRecord();
                    temp.put(word, count);
                    yearlyRecords.put(year, temp);
                }

                if(wordHistory.containsKey(word)){
                    wordHistory.get(word).put(year, count);
                }else{
                    TimeSeries<Integer> temp = new TimeSeries<>();
                    temp.put(year, count);
                    wordHistory.put(word, temp);
                }
            }
        }catch(IOException e){
            System.out.println("fail to read wordsfile");
        }

        try(BufferedReader inCounts = new BufferedReader(new FileReader(countsFilename))){
            String line = null;
            while((line = inCounts.readLine()) != null){
                String[] ar = line.split(",");
                totalCounts.put(Integer.parseInt(ar[0]), Long.parseLong(ar[1]));
            }
        }catch(IOException e){
            System.out.println("fail to read countsfile");
        }
    }

    /** Returns the absolute count of WORD in the given YEAR. If the word
      * did not appear in the given year, return 0. */
    public int countInYear(String word, int year){
        return yearlyRecords.get(year).count(word);
    }

    /** Returns a defensive copy of the YearlyRecord of YEAR. */
    public YearlyRecord getRecord(int year){
        return yearlyRecords.get(year).copy();
    }

    /** Returns the total number of words recorded in all volumes. */
    public TimeSeries<Long> totalCountHistory(){
        return totalCounts;
    }

    /** Provides the history of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Integer> countHistory(String word, int startYear, int endYear){
        TimeSeries<Integer> allHistory = wordHistory.get(word);
        return new TimeSeries<Integer>(allHistory, startYear, endYear);
    }

    /** Provides a defensive copy of the history of WORD. */
    public TimeSeries<Integer> countHistory(String word){
        TimeSeries<Integer> allHistory = wordHistory.get(word);
        return new TimeSeries<Integer>(allHistory);
    }

    /** Provides the relative frequency of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Double> weightHistory(String word, int startYear, int endYear){
        TimeSeries<Double> whistory = weightHistory(word);
        return new TimeSeries<Double>(whistory, startYear, endYear);
    }

    /** Provides the relative frequency of WORD. */
    public TimeSeries<Double> weightHistory(String word){
        return countHistory(word).dividedBy(totalCountHistory());
    }

    /** Provides the summed relative frequency of all WORDS between
      * STARTYEAR and ENDYEAR. If a word does not exist, ignore it rather
      * than throwing an exception. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words,
                              int startYear, int endYear){
        return new TimeSeries<Double>(summedWeightHistory(words), startYear, endYear);
    }

    /** Returns the summed relative frequency of all WORDS. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words){
        TimeSeries<Double> sum = new TimeSeries<>();
        for(String word : words){
            if(wordHistory.containsKey(word)){
                sum = weightHistory(word).plus(sum);
            }
        }
        return sum;
    }

    /** Provides processed history of all words between STARTYEAR and ENDYEAR as processed
      * by YRP. */

    public TimeSeries<Double> processedHistory(int startYear, int endYear,
                                               YearlyRecordProcessor yrp){
       TimeSeries<Double> proHistory = new TimeSeries<>();
       for(int year = startYear; year <= endYear; year++){
           if(yearlyRecords.containsKey(year)){
               proHistory.put(year, yrp.process(yearlyRecords.get(year)));
           }else{
               proHistory.put(year, 0.0);
           }
       }
       return proHistory;
    }

    /** Provides processed history of all words ever as processed by YRP. */
    public TimeSeries<Double> processedHistory(YearlyRecordProcessor yrp){
        TimeSeries<Double> proHistory = new TimeSeries<>();
        for(int year : yearlyRecords.keySet()){
            proHistory.put(year, yrp.process(yearlyRecords.get(year)));
        }
        return proHistory;
    }
}
