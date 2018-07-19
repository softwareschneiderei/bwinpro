package treegross.dynamic.siteindex;

public class DynamicSiteIndexCalculation {

    private final DynamicSiteIndex dsi;

    public DynamicSiteIndexCalculation(DynamicSiteIndex dsi) {
        super();
        this.dsi = dsi;
    }

    public DynamicSiteIndex recursiveProjection(Projection projection, DynamicSiteIndexModel dsiFunction, EnvironmentVariables dsiEnvironment) {
        DynamicSiteIndexModelParameters model = dsiFunction.parametersForSpecies(projection.treeSpecies);
        DynamicSiteIndexCalculator calculator = new DynamicSiteIndexCalculator(model);
        projection.forEach(year -> {
            final double siteIndex = calculator.computeSiteIndex(year, dsi.endSiteIndex(), dsiEnvironment);
            dsi.siIntermediates.put(year, siteIndex);
        });
        return dsi;
    }
}
