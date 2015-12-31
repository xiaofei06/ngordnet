/**
 * unit tests for NGramMap
 * author xiaofei
 */

package ngordnet;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

public class NGramMapTest{
    NGramMap ngm = new NGramMap("../p1data/ngrams/words_that_start_with_q.csv",
                        "../p1data/ngrams/total_counts.csv");

    @Test
    public void testCountInYear(){
        assertEquals(ngm.countInYear("quantity", 1736), 139);
    }

    @Test
    public void testGetRecord(){
        YearlyRecord yr = ngm.getRecord(1736);
        assertEquals(yr.count("quantity"), 139);
    }

    @Test
    public void testCountHistory(){
        TimeSeries<Integer> countHistory = ngm.countHistory("quantity");
        assertEquals(countHistory.get(1736).longValue(), 139);
    }

    @Test
    public void testTotalCountHistory(){
        TimeSeries<Long> totalCountHistory = ngm.totalCountHistory();
        assertEquals(totalCountHistory.get(1736).longValue(), 8049773);
    }

    @Test
    public void testWeightHistory(){
        TimeSeries<Double> weightHistory = ngm.weightHistory("quantity");
        assertEquals(weightHistory.get(1736), 1.7267E-5, 1E-7);
    }

    @Test
    public void testSummedWeightHistory(){
        ArrayList<String> words = new ArrayList<>();
        words.add("quantity");
        words.add("quality");
        TimeSeries<Double> sum = ngm.summedWeightHistory(words);
        assertEquals(sum.get(1736), 3.875E-5, 1E-7);
    }

    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(NGramMapTest.class);
    }
}
