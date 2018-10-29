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

    @Override
    public String toString() {
        return "SeasonMeanValues{" + "year=" + year + ", meanTemperature=" + meanTemperature + ", meanPrecipitationSum=" + meanPrecipitationSum + ", aridityIndex=" + aridityIndex + ", nitrogenDeposition=" + nitrogenDeposition + '}';
    }
    
    
}
