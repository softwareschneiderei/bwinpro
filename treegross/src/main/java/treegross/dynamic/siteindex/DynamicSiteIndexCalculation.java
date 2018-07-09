package treegross.dynamic.siteindex;

import java.time.Year;

public class DynamicSiteIndexCalculation {

    private final DynamicSiteIndex dsi;

    public DynamicSiteIndexCalculation(DynamicSiteIndex dsi) {
        super();
        this.dsi = dsi;
    }

    public DynamicSiteIndex recursiveProjection(Projection projection, Regression dsiFunction, EnvironmentVariables dsiEnvironment) {
        ModelParameters model = dsiFunction.parametersForSpecies(projection.treeSpecies);
        projection.forEach(year -> {
            final double siteIndex = computeSiteIndex(year, model, dsiEnvironment, dsi.endSiteIndex());
            dsi.siIntermediates.put(year, siteIndex);
        });
        return dsi;
    }

    private static double computeSiteIndex(Year year, ModelParameters model, EnvironmentVariables environment, double previousSiteIndex) {
        System.out.println("Previous site index: " + previousSiteIndex);
        System.out.println("Mean teamperature: " + environment.growingSeasonMeanTemperatureOf(year));
        System.out.println("Mean precipitation sum: " + environment.growingSeasonPrecipitationSumOf(year));
        System.out.println("Annual nitrogen deposition: " + environment.nitrogenDepositionOf(year).value);
        System.out.println("Aridity index: " + environment.aridityIndexOf(year));
        return model.parameter1 * Math.pow(previousSiteIndex, model.parameter2)
                * Math.exp(model.parameter3 * environment.growingSeasonMeanTemperatureOf(year)
                        + model.parameter4 * environment.growingSeasonPrecipitationSumOf(year)
                        + model.parameter5 * environment.aridityIndexOf(year)
                        + model.parameter6 * environment.nitrogenDepositionOf(year).value
                        + model.parameter7 * environment.growingSeasonPrecipitationSumOf(year)
                                * environment.nitrogenDepositionOf(year).value);
    }
}
