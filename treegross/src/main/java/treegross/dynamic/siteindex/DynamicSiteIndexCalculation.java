package treegross.dynamic.siteindex;

import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicSiteIndexCalculation {

    private static Projection basicInfoSpecification() {
        Projection result = new Projection();
        result.standName = "fi 150/   b";
        result.treeSpecies = "Fi";
        result.vegetationBeginMonth = 3;
        result.vegetationEndMonth = 8;
        result.rcp = "rcp85";
        return result;
    }
    
    /*
     * TODO: Read data from CSV or database
    */
    private static void readClimaticAndEnvironmentalData(
            Map<Year, List<MonthlyMeanTemperature>> monthlyMeanTemperature,
            Map<Year, List<MonthlyPrecipitation>> monthlyPrecipitation,
            List<AnnualNitrogenDeposition> annualNitrogenDeposition) {
        final int currentYear = 1;
        for (int year = currentYear; year < currentYear + 10; year++) {
            monthlyMeanTemperature.put(Year.of(year), new ArrayList<>());
            monthlyMeanTemperature.get(Year.of(year)).add(new MonthlyMeanTemperature(Month.MARCH, 18d));
            monthlyMeanTemperature.get(Year.of(year)).add(new MonthlyMeanTemperature(Month.APRIL, 19d));
            monthlyMeanTemperature.get(Year.of(year)).add(new MonthlyMeanTemperature(Month.MAY, 20d));
            monthlyMeanTemperature.get(Year.of(year)).add(new MonthlyMeanTemperature(Month.JUNE, 21d));
            monthlyMeanTemperature.get(Year.of(year)).add(new MonthlyMeanTemperature(Month.JULY, 22d));
            monthlyMeanTemperature.get(Year.of(year)).add(new MonthlyMeanTemperature(Month.AUGUST, 21d));
            
            monthlyPrecipitation.put(Year.of(year), new ArrayList<>());
            monthlyPrecipitation.get(Year.of(year)).add(new MonthlyPrecipitation(Month.MARCH, 1d));
            monthlyPrecipitation.get(Year.of(year)).add(new MonthlyPrecipitation(Month.APRIL, 2d));
            monthlyPrecipitation.get(Year.of(year)).add(new MonthlyPrecipitation(Month.MAY, 3d));
            monthlyPrecipitation.get(Year.of(year)).add(new MonthlyPrecipitation(Month.JUNE, 4d));
            monthlyPrecipitation.get(Year.of(year)).add(new MonthlyPrecipitation(Month.JULY, 5d));
            monthlyPrecipitation.get(Year.of(year)).add(new MonthlyPrecipitation(Month.AUGUST, 6d));
            
            // TODO: read annual NO deposition data
        }
    }

    /**
     * Multi-step PreTreatment of environmental variables
     */
    private static void preTreatmentOfEnvironmentalData(
            Map<Year, List<MonthlyMeanTemperature>> meanTemperatures,
            Map<Year, List<MonthlyPrecipitation>> meanPrecipitations,
            List<AnnualNitrogenDeposition> annualNitrogenDepositions,
            EnvironmentVariables dsiEnvironment) {
        climateDataCombination(meanTemperatures, meanPrecipitations, annualNitrogenDepositions, dsiEnvironment);
    }

    private static void climateDataCombination(
            Map<Year, List<MonthlyMeanTemperature>> meanTemperatures,
            Map<Year, List<MonthlyPrecipitation>> meanPrecipitations,
            List<AnnualNitrogenDeposition> annualNitrogenDepositions,
            EnvironmentVariables dsiEnvironment) {
        for (Map.Entry<Year, List<MonthlyMeanTemperature>> entry : meanTemperatures.entrySet()) {
            Year key = entry.getKey();
            List<MonthlyMeanTemperature> monthlyMeans = entry.getValue();
            // TODO: sum up the vegetation month mean temperatures and calculate arithmetic mean
        }
        // TODO: sum up the vegetation month precipitations and calculate arithmetic mean
    }

    public static void main(String[] args) {
        Map<Year, List<MonthlyMeanTemperature>> monthlyMeanTemperature = new HashMap<>();
        Map<Year, List<MonthlyPrecipitation>> monthlyPrecipitation = new HashMap<>();
        List<AnnualNitrogenDeposition> annualNitrogenDeposition = new ArrayList<>();
        
        DynamicSiteIndex dsi = new DynamicSiteIndex();
        Regression dsiFunction = new Regression();
        EnvironmentVariables dsiEnvironment = new EnvironmentVariables();
        
        Projection dsiProjection = basicInfoSpecification();
        readClimaticAndEnvironmentalData(monthlyMeanTemperature, monthlyPrecipitation, annualNitrogenDeposition);
        preTreatmentOfEnvironmentalData(monthlyMeanTemperature, monthlyPrecipitation, annualNitrogenDeposition, dsiEnvironment);
    }

}
