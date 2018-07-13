package treegross.dynamic.siteindex;

public class MonthlyValues {
    
    public final double temperatureMean;
    public final double precipitationSum;
    public final AnnualNitrogenDeposition nitrogenDeposition;

    public MonthlyValues(double temperatureMean, double precipitationSum, AnnualNitrogenDeposition nitrogenDeposition) {
        this.temperatureMean = temperatureMean;
        this.precipitationSum = precipitationSum;
        this.nitrogenDeposition = nitrogenDeposition;
    }
}
