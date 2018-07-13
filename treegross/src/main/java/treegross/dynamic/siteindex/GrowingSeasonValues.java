package treegross.dynamic.siteindex;

import java.time.Year;

/**
 * Environmental information of the growing seasion for a year
 * @author mmv
 */
public class GrowingSeasonValues {
    public final Year year;
    public final double meanTemperature;
    public final double meanPrecipitationSum;
    public final AnnualNitrogenDeposition nitrogenDeposition;

    public GrowingSeasonValues(Year year, double meanTemperature, double meanPrecipitationSum, AnnualNitrogenDeposition nitrogenDeposition) {
        super();
        this.year = year;
        this.meanTemperature = meanTemperature;
        this.meanPrecipitationSum = meanPrecipitationSum;
        this.nitrogenDeposition = nitrogenDeposition;
    }
}
