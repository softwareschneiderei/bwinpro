package treegross.dynamic.siteindex;

import java.time.Month;

public class MonthlyMeanTemperature implements MonthlyValue<Double>{
    private final Month month;
    private final double meanTemperature;

    public MonthlyMeanTemperature(Month month, double meanTemperature) {
        super();
        this.month = month;
        this.meanTemperature = meanTemperature;
    }

    @Override
    public Month month() {
        return month;
    }

    @Override
    public Double value() {
        return meanTemperature;
    }
}
