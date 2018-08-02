package treegross.dynamic.siteindex;

import java.util.LinkedList;
import java.util.function.Function;
import treegross.util.MeanCalculator;

public class Weighted5YearMeanCalculator<E> implements MeanCalculator<E>{

    private static final int windowSize = 5;
    private final LinkedList<E> yearlyValues;

    public Weighted5YearMeanCalculator() {
        this.yearlyValues = new LinkedList<>();
    }
    
    @Override
    public void add(E value) {
        yearlyValues.add(value);
        while (yearlyValues.size() > windowSize) { yearlyValues.remove(); }
    }
    
    @Override
    public double meanOf(Function<E, Double> extractor) {
        if (yearlyValues.isEmpty()) {
            return 0d;
        }
        double result = 0d;
            
        int totalWeight = 0;
        for (int pastYear = 0; pastYear < yearlyValues.size(); pastYear++) {
            int weight = pastYear + 1;
            
            totalWeight += weight;
            result += weight * extractor.apply(yearlyValues.get(pastYear));
        }
        return result /= totalWeight; //NOSONAR, false positive! We have at least one value here, so weight is also at least 1!
    }

    @Override
    public int windowSize() {
        return windowSize;
    }
}
