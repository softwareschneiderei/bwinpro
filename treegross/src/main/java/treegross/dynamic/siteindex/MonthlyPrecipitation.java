package treegross.dynamic.siteindex;

import java.time.Month;

public class MonthlyPrecipitation {
    public final Month month;
    public final double precipitation;

    public MonthlyPrecipitation(Month month, double precipitation) {
        super();
        this.month = month;
        this.precipitation = precipitation;
    }
}
