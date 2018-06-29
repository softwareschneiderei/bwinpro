package treegross.dynamic.siteindex;

import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DynamicSiteIndexCalculation {

    private static Projection basicInfoSpecification() {
        Projection result = new Projection();
        result.standName = "fi 150/   b";
        result.treeSpecies = "Fi";
        result.rcp = "rcp85";
        return result;
    }
    
    /*
     * TODO: Read data from CSV or database
    */
    private static void readClimaticAndEnvironmentalData(
            Map<Year, List<MonthlyMeanTemperature>> monthlyMeanTemperature,
            Map<Year, List<MonthlyPrecipitation>> monthlyPrecipitation,
            Map<Year, AnnualNitrogenDeposition> annualNitrogenDeposition) {
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
            Projection projection,
            Map<Year, List<MonthlyMeanTemperature>> meanTemperatures,
            Map<Year, List<MonthlyPrecipitation>> meanPrecipitations,
            Map<Year, AnnualNitrogenDeposition> annualNitrogenDepositions,
            EnvironmentVariables dsiEnvironment) {
        climateDataCombination(projection, meanTemperatures, meanPrecipitations, annualNitrogenDepositions, dsiEnvironment);
    }

    private static void climateDataCombination(
            Projection projection,
            Map<Year, List<MonthlyMeanTemperature>> meanTemperatures,
            Map<Year, List<MonthlyPrecipitation>> meanPrecipitations,
            Map<Year, AnnualNitrogenDeposition> annualNitrogenDepositions,
            EnvironmentVariables dsiEnvironment) {
        projection.forEach(year -> {
            List<MonthlyMeanTemperature> monthlyTemperatures = vegetationMonthsOf(meanTemperatures.get(year));
            dsiEnvironment.meanTemperature.put(year, meanValueOf(monthlyTemperatures));
            List<MonthlyPrecipitation> monthlyPrecipitations = vegetationMonthsOf(meanPrecipitations.get(year));
            dsiEnvironment.precipitationSum.put(year, meanValueOf(monthlyPrecipitations));
            dsiEnvironment.nitrogenDisposition.put(year, annualNitrogenDepositions.getOrDefault(year, new AnnualNitrogenDeposition(8d)));
        });
    }

    private static double meanValueOf(List<? extends MonthlyValue<Double>> monthlyMeans) {
        return monthlyMeans.stream().mapToDouble(monthlyMean -> monthlyMean.value()).sum() / monthlyMeans.size();
    }

    private static <T extends MonthlyValue<?>> List<T> vegetationMonthsOf(List<T> monthlyMeans) {
        return monthlyMeans.stream().filter(DynamicSiteIndexCalculation::isVegetationMonth).collect(Collectors.toList());
    }

    private static boolean isVegetationMonth(MonthlyValue<?> monthlyValue) {
        return monthlyValue.month().getValue() >= Month.MARCH.getValue() && monthlyValue.month().getValue() <= Month.AUGUST.getValue();
    }

    private static Regression readModelParameters() {
        Regression result = new Regression();
        result.addParametersFor("Fi", new ModelParameters(
                1.00003571186121,
                0.99998833695419,
                -9.02561907643062E-03,
                4.76764480882665E-04,
                -2.57562425678908E-03,
                1.00564825578297E-03,
                1.37988034418277E-02
        ));
        return result;
    }

    private static double initialSiteIndex() {
        return 39.80825;
    }

    private static void recursiveProjection(DynamicSiteIndex dsi, Projection projection, Regression dsiFunction, EnvironmentVariables dsiEnvironment) {
        ModelParameters model = dsiFunction.parametersForSpecies(projection.treeSpecies);
        projection.forEach(year -> {
            final double siteIndex = computeSiteIndex(year, model, dsiEnvironment, dsi.endSiteIndex());
            dsi.siIntermediates.put(year, siteIndex);
        });
    }

    private static double computeSiteIndex(Year year, ModelParameters model, EnvironmentVariables dsiEnvironment, double previousSiteIndex) {
        return Math.pow(model.parameter1 * previousSiteIndex, model.parameter2)
                * Math.exp(model.parameter3 * dsiEnvironment.meanTemperature.get(year)
                        + model.parameter4 * dsiEnvironment.precipitationSum.get(year)
                        + model.parameter5 * dsiEnvironment.aridityIndexFor(year)
                        + model.parameter6 * dsiEnvironment.nitrogenDisposition.get(year).value
                        + model.parameter7 * dsiEnvironment.precipitationSum.get(year)
                                * dsiEnvironment.nitrogenDisposition.get(year).value);
    }
    
    public static void main(String[] args) {
        Map<Year, List<MonthlyMeanTemperature>> monthlyMeanTemperature = new HashMap<>();
        Map<Year, List<MonthlyPrecipitation>> monthlyPrecipitation = new HashMap<>();
        Map<Year, AnnualNitrogenDeposition> annualNitrogenDeposition = new HashMap<>();
        
        DynamicSiteIndex dsi = new DynamicSiteIndex(initialSiteIndex());
        EnvironmentVariables dsiEnvironment = new EnvironmentVariables();
        
        Projection dsiProjection = basicInfoSpecification();
        readClimaticAndEnvironmentalData(monthlyMeanTemperature, monthlyPrecipitation, annualNitrogenDeposition);
        dsiProjection.length = monthlyMeanTemperature.size();
        preTreatmentOfEnvironmentalData(dsiProjection, monthlyMeanTemperature, monthlyPrecipitation, annualNitrogenDeposition, dsiEnvironment);
        Regression dsiFunction = readModelParameters();
        recursiveProjection(dsi, dsiProjection, dsiFunction, dsiEnvironment);
    }
}
