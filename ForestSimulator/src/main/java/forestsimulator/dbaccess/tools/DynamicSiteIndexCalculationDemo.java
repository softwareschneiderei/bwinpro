package forestsimulator.dbaccess.tools;

import forestsimulator.dbaccess.DatabaseEnvironmentalDataProvider;
import java.io.File;
import java.time.Year;
import treegross.base.SiteIndex;
import static treegross.base.SiteIndex.si;
import treegross.base.StandLocation;
import treegross.dynamic.siteindex.DynamicSiteIndexProgression;
import treegross.dynamic.siteindex.EnvironmentStandardizer;
import treegross.dynamic.siteindex.EnvironmentVariables;
import treegross.dynamic.siteindex.Projection;

public class DynamicSiteIndexCalculationDemo {
    public static Projection basicInfoSpecification() {
        Projection result = new Projection(Year.of(2006));
        result.standName = "fi 150/   b";
        result.treeSpecies = "Fi";
        result.rcp = "misc";
        return result;
    }
    
    public static SiteIndex initialSiteIndex() {
        return si(39.80825);
    }
    
    public static void main(String[] args) {
        Projection dsiProjection = basicInfoSpecification();
        EnvironmentVariables dsiEnvironment = loadEnvironmentData("BW", "1/01", dsiProjection.rcp);
        
        dsiProjection.length = 5;
        DynamicSiteIndexModel dsiFunction = new DynamicSiteIndexModel();
        DynamicSiteIndexProgression dsi = new DynamicSiteIndexCalculation(new DynamicSiteIndexProgression(initialSiteIndex())).recursiveProjection(dsiProjection, dsiFunction, EnvironmentStandardizer.standardize(dsiEnvironment));
        System.out.println("Dynamic site index: " + dsi.siIntermediates);
        System.out.println("Dynamic site index after " + dsiProjection.length + " years: " + dsi.endSiteIndex());
    }

    private static EnvironmentVariables loadEnvironmentData(String federalState, String subRegion, String scenario) {
        return new DatabaseEnvironmentalDataProvider(new File("data_standsimulation/climate_data.mdb")).environmentalDataFor(new StandLocation(federalState, StandLocation.regionFrom(subRegion), subRegion), scenario);
    }

}
