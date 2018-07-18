package treegross.dynamic.siteindex;

import java.util.LinkedList;

public class DataAccumulation {

    private static final int windowSize = 5;
    private final LinkedList<Double> yearlyValues;

    public DataAccumulation() {
        this.yearlyValues = new LinkedList<>();
    }
    
    public DataAccumulation add(double value) {
        yearlyValues.add(value);
        while (yearlyValues.size() > windowSize) { yearlyValues.remove(); }
        return this;
    }
    
    public double weighted5YearMean() {
        if (yearlyValues.isEmpty()) {
            return 0d;
        }
        double result = 0d;
            
        int totalWeight = 0;
        for (int pastYear = 0; pastYear < yearlyValues.size(); pastYear++) {
            int weight = pastYear + 1;
            
            totalWeight += weight;
            result += weight * yearlyValues.get(pastYear);
        }
        return result /= totalWeight;
    }
}
