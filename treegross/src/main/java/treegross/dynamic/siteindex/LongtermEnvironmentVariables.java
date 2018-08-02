package treegross.dynamic.siteindex;

import java.time.Year;

class LongtermEnvironmentVariables extends EnvironmentVariables {
    
    public LongtermEnvironmentVariables(EnvironmentVariables mean5Year) {
        super(mean5Year);
    }

    @Override
    public double growingSeasonMeanTemperatureOf(Year year) {
        return 0.9859 * super.growingSeasonMeanTemperatureOf(year) - 0.8488;
    }

    @Override
    public double growingSeasonPrecipitationSumOf(Year year) {
        return 1.0799 * super.growingSeasonPrecipitationSumOf(year) - 10.691;
    }

    @Override
    public AnnualNitrogenDeposition nitrogenDepositionOf(Year year) {
        return new AnnualNitrogenDeposition(0.8947 * super.nitrogenDepositionOf(year).value - 4.355);
    }

    @Override
    public double aridityIndexOf(Year year) {
        return 1.1187 * super.aridityIndexOf(year) - 5.7999;
    }

    public EnvironmentVariables standardized(EnvironmentVariables environment) {
        EnvironmentVariables result = new EnvironmentVariables();
        for (GrowingSeasonValues yearlyValues : environment) {
            double longtermTemperatureMean = growingSeasonMeanTemperatureOf(yearlyValues.year);
            double longtermPrecipitationMean = growingSeasonPrecipitationSumOf(yearlyValues.year);
            AnnualNitrogenDeposition longtermNitrogenDeposition = nitrogenDepositionOf(yearlyValues.year);
            result.addGrowingSeason(new GrowingSeasonValues(
                    yearlyValues.year,
                    standardizeValues(yearlyValues.meanTemperature, longtermTemperatureMean),
                    standardizeValues(yearlyValues.meanPrecipitationSum, longtermPrecipitationMean),
                    new AnnualNitrogenDeposition(standardizeValues(yearlyValues.nitrogenDeposition.value, longtermNitrogenDeposition.value))));
        }
        return result;
    }

    private double standardizeValues(double yearlyMean, double longtermMean) {
        return (yearlyMean - longtermMean) / longtermMean;
    }
}
