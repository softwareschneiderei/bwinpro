package treegross.dynamic.siteindex;

import java.util.ArrayList;
import java.util.List;

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
    
    private static void readClimaticAndEnvironmentalData(List<Double> monthlyMeanTemperature, List<Double> monthlyPrecipitation, List<Double> annualNitrogenDeposition) {
        int year;
    }

    public static void main(String[] args) {
        List<Double> monthlyMeanTemperature = new ArrayList<>();
        List<Double> monthlyPrecipitation = new ArrayList<>();
        List<Double> annualNitrogenDeposition = new ArrayList<>();
        
        DynamicSiteIndex dsi = new DynamicSiteIndex();
        Regression dsiFunction = new Regression();
        EnvironmentVariables dsiEnvironment = new EnvironmentVariables();
        
        Projection dsiProjection = basicInfoSpecification();
        readClimaticAndEnvironmentalData(monthlyMeanTemperature, monthlyPrecipitation, annualNitrogenDeposition);
    }

}
