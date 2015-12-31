/**
 * Class WordLengthProcessor
 * An implementation of YearlyRecordProcessor
 * author Xiaofei
 */

package ngordnet;

import java.util.Collection;

public class WordLengthProcessor implements YearlyRecordProcessor{
    @Override
    public double process(YearlyRecord yearlyRecord){
        Collection<String> words = yearlyRecord.words();
        long volume = 0;
        long totalLength = 0;
        for(String word : words){
            long wordLength = word.length();
            long wordCount = yearlyRecord.count(word);
            totalLength += wordLength * wordCount;
            volume += wordCount;
        }
        double avgLength = (double) totalLength/volume;
        return avgLength;
    }
}
