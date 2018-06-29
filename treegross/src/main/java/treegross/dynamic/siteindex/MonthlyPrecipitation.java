package treegross.dynamic.siteindex;

import java.time.Month;

public class MonthlyPrecipitation implements MonthlyValue<Double>{
    private final Month month;
    private final double precipitation;

    public MonthlyPrecipitation(Month month, double precipitation) {
        super();
        this.month = month;
        this.precipitation = precipitation;
    }

    @Override
    public Month month() {
        return month;
    }

    @Override
    public Double value() {
        return precipitation;
    }
}
