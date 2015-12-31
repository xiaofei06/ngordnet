/**
 * Class Timeseries
 * author xiaofei
 */

package ngordnet;

import java.util.TreeMap;
import java.util.TreeSet;
import java.util.NavigableSet;
import java.util.Collection;
import java.util.ArrayList;


public class TimeSeries<T extends Number> extends TreeMap<Integer, T> {
    /** Constructs a new empty TimeSeries. */
    public TimeSeries(){
        super();
    }

    /** Creates a copy of TS, but only between STARTYEAR and ENDYEAR.
     * inclusive of both end points. */
    public TimeSeries(TimeSeries<T> ts, int startYear, int endYear){
        super();
        for(int k : ts.keySet()){
            if(k >= startYear && k <= endYear){
                this.put(k, ts.get(k));
            }
        }
    }

    /** Creates a copy of TS. */
    public TimeSeries(TimeSeries<T> ts){
        super(ts);
    }

    /** Returns the quotient of this time series divided by the relevant value in ts.
      * If ts is missing a key in this time series, return an IllegalArgumentException. */
    public TimeSeries<Double> dividedBy(TimeSeries<? extends Number> ts){
        TimeSeries<Double> quotient = new TimeSeries<>();
        for(int k : this.keySet()){
            if(!ts.containsKey(k)){
                throw new IllegalArgumentException();
            }
            double quote = this.get(k).doubleValue() /  ts.get(k).doubleValue();
            quotient.put(k, quote);
        }
        return quotient;
    }

    /** Returns the sum of this time series with the given ts. The result is a
      * a Double time series (for simplicity). */
    public TimeSeries<Double> plus(TimeSeries<? extends Number> ts){
        TimeSeries<Double> sumSeries = new TimeSeries<>();
        TreeSet<Integer> allYears = new TreeSet<>();
        allYears.addAll(this.keySet());
        allYears.addAll(ts.keySet());
        for(int k : allYears){
            double sumValue = this.containsKey(k)? this.get(k).doubleValue() : 0;
            sumValue += ts.containsKey(k)? ts.get(k).doubleValue() : 0;
            sumSeries.put(k, sumValue);
        }
        return sumSeries;
    }

    /** Returns all years for this time series (in any order). */
    public Collection<Number> years(){
        ArrayList<Number> yearList = new ArrayList<>();
        for(int k : this.keySet()){
            yearList.add(k);
        }
        return yearList;
    }

    /** Returns all data for this time series.
      * Must be in the same order as years(). */
    public Collection<Number> data(){
        ArrayList<Number> dataList = new ArrayList<>();
        for(int k : this.keySet()){
            dataList.add(this.get(k));
        }
        return dataList;
    }
}
