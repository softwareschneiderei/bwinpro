package forestsimulator.dbaccess.tools;

import treegross.base.TGFunction;
import treegross.dynamic.siteindex.DynamicSiteIndexProgression;
import treegross.dynamic.siteindex.DynamicSiteIndexCalculator;
import treegross.dynamic.siteindex.EnvironmentVariables;
import treegross.dynamic.siteindex.Projection;

public class DynamicSiteIndexCalculation {

    private final DynamicSiteIndexProgression dsi;

    public DynamicSiteIndexCalculation(DynamicSiteIndexProgression dsi) {
        super();
        this.dsi = dsi;
    }

    public DynamicSiteIndexProgression recursiveProjection(Projection projection, DynamicSiteIndexModel dsiFunction, EnvironmentVariables dsiEnvironment) {
        TGFunction model = dsiFunction.parametersForSpecies(projection.treeSpecies);
        DynamicSiteIndexCalculator calculator = new DynamicSiteIndexCalculator(model);
        projection.forEach(year -> {
            final double siteIndex;
                siteIndex = calculator.computeSiteIndex(year, dsi.endSiteIndex(), dsiEnvironment);
                dsi.siIntermediates.put(year, siteIndex);
        });
        return dsi;
    }
}
