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
    
    @Ignore("Spruce fails expectations. Formula needs to be checked.")
    @Test
    public void climateSensitiveSiteIndexForSpruce() {
        DynamicSiteIndexCalculator dsiCalculator = calculatorFor(511);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2010), SiteIndex.si(35.15d), normalized).value).isCloseTo(35.18038, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2011), SiteIndex.si(35.18038d), normalized).value).isCloseTo(35.14579, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2012), SiteIndex.si(35.14579d), normalized).value).isCloseTo(35.11146, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2013), SiteIndex.si(35.11146d), normalized).value).isCloseTo(35.09742, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2014), SiteIndex.si(35.09742d), normalized).value).isCloseTo(35.08035, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2015), SiteIndex.si(35.08035d), normalized).value).isCloseTo(35.02104, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2016), SiteIndex.si(35.02104d), normalized).value).isCloseTo(34.99873, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2017), SiteIndex.si(34.99873d), normalized).value).isCloseTo(34.97877, delta);
    }

    @Test
    public void climateSensitiveSiteIndexForDouglasFir() {
        DynamicSiteIndexCalculator dsiCalculator = calculatorFor(611);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2010), SiteIndex.si(45.58d), normalized).value).isCloseTo(45.57345, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2011), SiteIndex.si(45.57345d), normalized).value).isCloseTo(45.58264, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2012), SiteIndex.si(45.58264d), normalized).value).isCloseTo(45.60227, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2013), SiteIndex.si(45.60227d), normalized).value).isCloseTo(45.61692, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2014), SiteIndex.si(45.61692), normalized).value).isCloseTo(45.60105, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2015), SiteIndex.si(45.60105d), normalized).value).isCloseTo(45.52026, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2016), SiteIndex.si(45.52026d), normalized).value).isCloseTo(45.52244, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2017), SiteIndex.si(45.52244d), normalized).value).isCloseTo(45.49415, delta);
    }

    @Test
    public void climateSensitiveSiteIndexForOak() {
        DynamicSiteIndexCalculator dsiCalculator = calculatorFor(110);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2010), SiteIndex.si(31.04d), normalized).value).isCloseTo(31.05169, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2011), SiteIndex.si(31.05169d), normalized).value).isCloseTo(31.06264, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2012), SiteIndex.si(31.06264d), normalized).value).isCloseTo(31.0794, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2013), SiteIndex.si(31.0794d), normalized).value).isCloseTo(31.10689, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2014), SiteIndex.si(31.10689d), normalized).value).isCloseTo(31.12663, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2015), SiteIndex.si(31.12663d), normalized).value).isCloseTo(31.12987, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2016), SiteIndex.si(31.12987d), normalized).value).isCloseTo(31.16279, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2017), SiteIndex.si(31.16279d), normalized).value).isCloseTo(31.1842, delta);
    }

    @Test
    public void climateSensitiveSiteIndexForBeech() {
        DynamicSiteIndexCalculator dsiCalculator = calculatorFor(211);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2010), SiteIndex.si(30.88d), normalized).value).isCloseTo(30.86176, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2011), SiteIndex.si(30.86176d), normalized).value).isCloseTo(30.816, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2012), SiteIndex.si(30.816d), normalized).value).isCloseTo(30.77718, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2013), SiteIndex.si(30.77718d), normalized).value).isCloseTo(30.74927, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2014), SiteIndex.si(30.74927d), normalized).value).isCloseTo(30.72149, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2015), SiteIndex.si(30.72149d), normalized).value).isCloseTo(30.68367, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2016), SiteIndex.si(30.68367d), normalized).value).isCloseTo(30.66013, delta);
        Assertions.assertThat(dsiCalculator.computeSiteIndex(Year.of(2017), SiteIndex.si(30.66013d), normalized).value).isCloseTo(30.6324, delta);
    }

    private DynamicSiteIndexCalculator calculatorFor(int speciesCode) {
        SpeciesDefMap speciesMap = new SpeciesDefMap();
        speciesMap.readFromPath(new File("user/models/ForestSimulatorSettingsBW.xml"));
        DynamicSiteIndexCalculator dsiCalculator = speciesMap.getByCode(speciesCode).dsiCalculator;
        System.out.println("Formula for : " + speciesMap.getByCode(speciesCode).shortName + "=" + dsiCalculator.functionText());
        return dsiCalculator;
    }
}
