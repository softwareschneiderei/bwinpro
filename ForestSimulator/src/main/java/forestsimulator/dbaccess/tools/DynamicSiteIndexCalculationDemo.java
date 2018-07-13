package forestsimulator.dbaccess.tools;

import forestsimulator.dbaccess.DatabaseEnvirionmentalDataProvider;
import java.time.Year;
import treegross.dynamic.siteindex.DynamicSiteIndex;
import treegross.dynamic.siteindex.DynamicSiteIndexCalculation;
import treegross.dynamic.siteindex.DynamicSiteIndexModel;
import treegross.dynamic.siteindex.EnvironmentVariables;
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
        
        dsiProjection.length = 5;
        Regression dsiFunction = new DynamicSiteIndexModel().modelParameters();
        DynamicSiteIndex dsi = new DynamicSiteIndexCalculation(new DynamicSiteIndex(initialSiteIndex())).recursiveProjection(dsiProjection, dsiFunction, dsiEnvironment);
        System.out.println("Dynamic site index: " + dsi.siIntermediates);
        System.out.println("Dynamic site index after " + dsiProjection.length + " years: " + dsi.endSiteIndex());
    }

    private static EnvironmentVariables loadEnvironmentData(String region, String subRegion, String scenario) {
        return new DatabaseEnvirionmentalDataProvider("data_standsimulation/climate_database.mdb").environmentalDataFor(region, subRegion, scenario);
    }
}
