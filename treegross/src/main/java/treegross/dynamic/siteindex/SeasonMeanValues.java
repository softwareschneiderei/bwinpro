package treegross.dynamic.siteindex;

import java.time.Year;

/**
 * Environmental information of the growing seasion for a year
 * @author mmv
 */
public class SeasonMeanValues {
    public final Year year;
    public final double meanTemperature;
    public final double meanPrecipitationSum;
    public final double aridityIndex;
    public final AnnualNitrogenDeposition nitrogenDeposition;

    public SeasonMeanValues(Year year, double meanTemperature, double meanPrecipitationSum, double aridityIndex, AnnualNitrogenDeposition nitrogenDeposition) {
        super();
        this.year = year;
        this.meanTemperature = meanTemperature;
        this.meanPrecipitationSum = meanPrecipitationSum;
        this.aridityIndex = aridityIndex;
        this.nitrogenDeposition = nitrogenDeposition;
    }
}
