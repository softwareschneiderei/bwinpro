package treegross.dynamic.siteindex;

import java.time.Month;

public class MonthlyMeanTemperature {
    public final Month month;
    public final double meanTemperature;

    public MonthlyMeanTemperature(Month month, double meanTemperature) {
        super();
        this.month = month;
        this.meanTemperature = meanTemperature;
    }
}
