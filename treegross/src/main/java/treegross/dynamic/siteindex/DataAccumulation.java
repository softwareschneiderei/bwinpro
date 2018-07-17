package treegross.dynamic.siteindex;

import java.util.List;

public class DataAccumulation {
    
    public double[] weighted5YearMean(List<Double> yearlyValues, int projectionLength) {
        double[] result = new double[projectionLength];
        for (int i = 0; i < result.length; i++) {
            result[i] = 0d;
        }
        int lag = 5;
        for (int year = 1; year <= projectionLength; year++) {
            int period = lag;
            if (year <= lag) {
                period = year;
            }
            
            int sYear = 0;
            for (int jYear = 1; jYear <= period; jYear++) {
                int dYear = year - jYear + 1;
                int weight = (period - jYear + 1);
                if (dYear > 0) {
                    sYear = sYear + weight;
                    result[year - 1] = result[year - 1] + weight * yearlyValues.get(dYear - 1);
                }
            }
            if (sYear > 0) {
                result[year - 1] = result[year - 1] / sYear;
            }
        }
        return result;
    }
}
