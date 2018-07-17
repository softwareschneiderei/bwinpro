package forestsimulator.dbaccess.tools;

import forestsimulator.dbaccess.DatabaseEnvirionmentalDataProvider;
import java.time.Year;
import treegross.dynamic.siteindex.AnnualNitrogenDeposition;
import treegross.dynamic.siteindex.DynamicSiteIndex;
import treegross.dynamic.siteindex.DynamicSiteIndexCalculation;
import treegross.dynamic.siteindex.DynamicSiteIndexModel;
import treegross.dynamic.siteindex.EnvironmentVariables;
import treegross.dynamic.siteindex.GrowingSeasonValues;
import treegross.dynamic.siteindex.Projection;
import treegross.dynamic.siteindex.Regression;

public class DynamicSiteIndexCalculationDemo {
    public static Projection basicInfoSpecification() {
        Projection result = new Projection(Year.of(2006));
        result.standName = "fi 150/   b";
        result.treeSpecies = "Fi";
        result.rcp = "MISC";
        return result;
    }
    
    public static double initialSiteIndex() {
        return 39.80825;
    }
    
    public static void main(String[] args) {
        Projection dsiProjection = basicInfoSpecification();
        EnvironmentVariables dsiEnvironment = loadEnvironmentData("1", "1/01", dsiProjection.rcp);
        EnvironmentVariables mean5YearVariables = dsiEnvironment.calculate5YearMeans();
        LongtermEnvironmentVariables longTermVariables = new LongtermEnvironmentVariables(mean5YearVariables);
        
        EnvironmentVariables standardizedEnvironment = longTermVariables.standardized(dsiEnvironment);
        dsiProjection.length = 5;
        Regression dsiFunction = new DynamicSiteIndexModel().modelParameters();
        DynamicSiteIndex dsi = new DynamicSiteIndexCalculation(new DynamicSiteIndex(initialSiteIndex())).recursiveProjection(dsiProjection, dsiFunction, standardizedEnvironment);
        System.out.println("Dynamic site index: " + dsi.siIntermediates);
        System.out.println("Dynamic site index after " + dsiProjection.length + " years: " + dsi.endSiteIndex());
    }

    private static EnvironmentVariables loadEnvironmentData(String region, String subRegion, String scenario) {
        return new DatabaseEnvirionmentalDataProvider("data_standsimulation/climate_database.mdb").environmentalDataFor(region, subRegion, scenario);
    }

    private static class LongtermEnvironmentVariables extends EnvironmentVariables {

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
                        new AnnualNitrogenDeposition(
                                standardizeValues(yearlyValues.nitrogenDeposition.value, longtermNitrogenDeposition.value))));
            }
            return result;
        }

        private static double standardizeValues(double yearlyMean, double longtermMean) {
            return (yearlyMean - longtermMean) / longtermMean;
        }
    }
}
