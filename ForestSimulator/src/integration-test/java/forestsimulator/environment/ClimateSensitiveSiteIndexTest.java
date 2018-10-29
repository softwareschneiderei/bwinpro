package forestsimulator.environment;

import forestsimulator.dbaccess.DatabaseEnvironmentalDataProvider;
import java.io.File;
import java.time.Year;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import treegross.base.SiteIndex;
import treegross.base.SpeciesDefMap;
import treegross.base.StandLocation;
import treegross.dynamic.siteindex.DynamicSiteIndexCalculator;
import treegross.dynamic.siteindex.EnvironmentNormalizer;
import treegross.dynamic.siteindex.EnvironmentVariables;

public class ClimateSensitiveSiteIndexTest {
    private static final Offset<Double> delta = Offset.offset(0.001);
    private static EnvironmentVariables normalized;
    
    @BeforeClass
    public static void setup() {
        DatabaseEnvironmentalDataProvider dataProvider = new DatabaseEnvironmentalDataProvider(new File("data_standsimulation/climate_data.mdb"));
        normalized = EnvironmentNormalizer.normalize(dataProvider.environmentalDataFor(new StandLocation("1/01"), "RCP45"));
    }
    
    @Ignore("Ignored until we fix the aridity index calculation")
    @Test
    public void climateSensitiveSiteIndexForSpruce() {
        DynamicSiteIndexCalculator dsiCalculator = calculatorFor(511);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2009), SiteIndex.si(35.15d), normalized).value).isCloseTo(35.15, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2010), SiteIndex.si(35.171632089), normalized).value).isCloseTo(35.18038, delta);
//        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2011), SiteIndex.si(35.15d), normalized).value).isCloseTo(35.14579, delta);
//        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2009), SiteIndex.si(35.15d), normalized).value).isCloseTo(35.15, delta);
//        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2009), SiteIndex.si(35.15d), normalized).value).isCloseTo(35.15, delta);
//        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2009), SiteIndex.si(35.15d), normalized).value).isCloseTo(35.15, delta);
//        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2009), SiteIndex.si(35.15d), normalized).value).isCloseTo(35.15, delta);
    }

    @Ignore("Ignored until we fix the aridity index calculation")
    @Test
    public void climateSensitiveSiteIndexForDouglasFir() {
        DynamicSiteIndexCalculator dsiCalculator = calculatorFor(611);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2009), SiteIndex.si(35.15d), normalized).value).isCloseTo(35.15, delta);
    }

    @Ignore("Ignored until we fix the aridity index calculation")
    @Test
    public void climateSensitiveSiteIndexForOak() {
        DynamicSiteIndexCalculator dsiCalculator = calculatorFor(110);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2009), SiteIndex.si(35.15d), normalized).value).isCloseTo(35.15, delta);
    }

    @Ignore("Ignored until we fix the aridity index calculation")
    @Test
    public void climateSensitiveSiteIndexForBeech() {
        DynamicSiteIndexCalculator dsiCalculator = calculatorFor(211);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2009), SiteIndex.si(35.15d), normalized).value).isCloseTo(35.15, delta);
    }

    private DynamicSiteIndexCalculator calculatorFor(int speciesCode) {
        SpeciesDefMap speciesMap = new SpeciesDefMap();
        speciesMap.readFromPath(new File("user/models/ForestSimulatorSettingsBW.xml"));
        DynamicSiteIndexCalculator dsiCalculator = speciesMap.getByCode(speciesCode).dsiCalculator;
        System.out.println("Formula: " + dsiCalculator.functionText());
        return dsiCalculator;
    }
}
