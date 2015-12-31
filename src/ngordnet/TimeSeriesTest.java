/**
 * unit tests for TimeSeries
 * author xiaofei
 */
 
package ngordnet;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class TimeSeriesTest{

    TimeSeries<Double> ts = new TimeSeries<Double>();
    TimeSeries<Integer> ts2 = new TimeSeries<Integer>();
    TimeSeries<Double> ts3 = new TimeSeries<Double>();

    @Before
    public void initialize(){
        ts.put(1992, 3.6);
        ts.put(1993, 9.2);
        ts.put(1994, 15.2);
        ts.put(1995, 16.1);
        ts.put(1996, -15.7);

        ts2.put(1991, 10);
        ts2.put(1992, -5);
        ts2.put(1993, 1);

        ts3.put(1991, 5.0);
        ts3.put(1992, 1.0);
        ts3.put(1993, 100.0);
    }

    @Test
    public void testPlus(){
        TimeSeries<Double> tSum = ts.plus(ts2);
        assertEquals(tSum.get(1991).doubleValue(), 10.0, 0.01);
        assertEquals(tSum.get(1992).doubleValue(), -1.4, 0.01);
        assertEquals(tSum.get(1994).doubleValue(), 15.2, 0.01);
    }

    @Test
    public void testDivide(){
        TimeSeries<Double> tQuotient = ts2.dividedBy(ts3);
        assertEquals(tQuotient.get(1991).doubleValue(), 2.0, 0.01);
        assertEquals(tQuotient.get(1992).doubleValue(), -5.0, 0.01);
    }

    public static void main(String[] args){
        jh61b.junit.textui.runClasses(TimeSeriesTest.class);
    }
}
