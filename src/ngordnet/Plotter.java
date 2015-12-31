/**
 * Utility class for generating plots.
 * author xiaofei */

package ngordnet;

import java.util.ArrayList;
import java.util.Set;
import java.util.Collection;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.StyleManager.ChartTheme;
import com.xeiam.xchart.ChartBuilder;


public class Plotter {
    /** Creates a plot of the TimeSeries TS. Labels the graph with the
      * given TITLE, XLABEL, YLABEL, and LEGEND. */
    public static void plotTS(TimeSeries<? extends Number> ts, String title,
                              String xlabel, String ylabel, String legend){
        ArrayList<Number> xValues = new ArrayList<Number>();
        ArrayList<Number> yValues = new ArrayList<Number>();
        for(Number year : ts.years()){
            xValues.add(year);
            yValues.add(ts.get(year));
        }
        Chart chart = QuickChart.getChart(title, ylabel, xlabel, legend, xValues, yValues);
        new SwingWrapper(chart).displayChart();
    }

    /** Creates a plot of the TimeSeries TS. */
    public static void plotTS(TimeSeries<? extends Number> ts){
        String title = "time series plot";
        String xlabel = "year";
        String ylabel = "y";
        String legend = "time series";
        plotTS(ts, title, xlabel, ylabel, legend);
    }

    /** Creates a plot of the absolute word counts for WORD from STARTYEAR
      * to ENDYEAR, using NGM as a data source. */
    public static void plotCountHistory(NGramMap ngm, String word,
                                      int startYear, int endYear){
        TimeSeries<Integer> ts = ngm.countHistory(word, startYear, endYear);
        plotTS(ts);
    }

    /** Creates a plot of the normalized weight counts for WORD from STARTYEAR
      * to ENDYEAR, using NGM as a data source. */
    public static void plotWeightHistory(NGramMap ngm, String word,
                                       int startYear, int endYear){
        TimeSeries<Double> ts = ngm.weightHistory(word, startYear, endYear);
        plotTS(ts);
    }

    /** Creates a plot of the processed history from STARTYEAR to ENDYEAR, using
      * NGM as a data source, and the YRP as a yearly record processor. */

    public static void plotProcessedHistory(NGramMap ngm, int startYear, int endYear,
                                            YearlyRecordProcessor yrp){
        TimeSeries<Double> ts = ngm.processedHistory(startYear, endYear, yrp);
        plotTS(ts);
    }

    /** Creates a plot of the total normalized count of WN.hyponyms(CATEGORYLABEL)
      * from STARTYEAR to ENDYEAR using NGM and WN as data sources. */
    public static void plotCategoryWeights(NGramMap ngm, WordNet wn, String categoryLabel,
                                            int startYear, int endYear){
        Set<String> words = wn.hyponyms(categoryLabel);
        TimeSeries<Double> ts = ngm.summedWeightHistory(words, startYear, endYear);
        plotTS(ts);
    }

    /** Creates overlaid category weight plots for each category label in CATEGORYLABELS
      * from STARTYEAR to ENDYEAR using NGM and WN as data sources. */

    public static void plotCategoryWeights(NGramMap ngm, WordNet wn, String[] categoryLabels,
                                            int startYear, int endYear){
        String xlabel = "year";
        String ylabel = "weights";
        Chart chart = new ChartBuilder().width(800).height(600).xAxisTitle(xlabel).yAxisTitle(ylabel).build();
        ArrayList xValues = upRange(startYear, endYear);
        for(String label : categoryLabels){
            String legend = label;
            Set<String> words = wn.hyponyms(label);
            TimeSeries<Double> ts = ngm.summedWeightHistory(words, startYear, endYear);
            ArrayList<Number> yValues = generateValues(ts, startYear, endYear);
            chart.addSeries(legend, xValues, yValues);
        }
        new SwingWrapper(chart).displayChart();
    }

    /** Makes a plot showing overlaid individual normalized count for every word in WORDS
      * from STARTYEAR to ENDYEAR using NGM as a data source. */
    public static void plotAllWords(NGramMap ngm, String[] words, int startYear, int endYear){
        String xlabel = "year";
        String ylabel = "count";
        Chart chart = new ChartBuilder().width(800).height(600).xAxisTitle(xlabel).yAxisTitle(ylabel).build();
        ArrayList xValues = upRange(startYear, endYear);
        for(String word : words){
            String legend = word;
            TimeSeries<Integer> ts = ngm.countHistory(word, startYear, endYear);
            ArrayList<Number> yValues = generateValues(ts, startYear, endYear);
            chart.addSeries(legend, xValues, yValues);
        }
        new SwingWrapper(chart).displayChart();
    }

    /** Returns the numbers from start to end, inclusive in decreasing order. */
    private static ArrayList<Number> upRange(int start, int end){
        ArrayList<Number> nums = new ArrayList<>();
        for(int i = start; i <= end; i++){
            nums.add(i);
        }
        return nums;
    }

    /** Returns the list of values given a time series and start year and end year. */
    private static ArrayList<Number> generateValues(TimeSeries<? extends Number> ts, int start, int end){
        ArrayList<Number> nums = new ArrayList<>();
        for(int i = start; i <= end; i++){
            if(ts.containsKey(i)){
                nums.add(ts.get(i));
            }else{
                nums.add(0);
            }
        }
        return nums;
    }

    /** Plots the count (or weight) of every word against the rank of every word on a
      * log-log plot. Uses data from YEAR, using NGM as a data source. */
    public static void plotZipfsLaw(NGramMap ngm, int year){
        YearlyRecord yr = ngm.getRecord(year);
        ArrayList<Number> xValues = new ArrayList<>();
        ArrayList<Number> yValues = new ArrayList<>();
        for(String word : yr.words()){
            xValues.add(yr.rank(word));
            yValues.add(yr.count(word));
        }
        String title = "count vs. rank";
        String ylabel = "count (log)";
        String xlabel = "rank (log)";
        String legend = year + "";
        Chart chart = new ChartBuilder().width(800).height(600).xAxisTitle(ylabel).yAxisTitle(xlabel).build();
        chart.getStyleManager().setYAxisLogarithmic(true);
        chart.getStyleManager().setXAxisLogarithmic(true);
        chart.addSeries(legend, xValues, yValues);
        new SwingWrapper(chart).displayChart();
    }
}
