package treegross.treatment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.data.Offset;
import treegross.base.Species;
import treegross.base.TGTextFunction;
import treegross.base.thinning.HeightBasedThinning;
import treegross.base.thinning.SpeciesThinningSettings;
import treegross.base.thinning.ThinningValueRange;

public class TreatmentElements2Test {
    private static final Offset<Double> delta = Offset.offset(0.01d);
    
    /*
     * #Pinning
    */
    @Test
    public void maxBasalAreaWithoutModerateThinning() {
        assertThat(TreatmentElements2.getMaxStandBasalArea(exampleSpecies(), false)).isCloseTo(31.03320, delta);
    }

    /*
     * #Pinning
    */
    @Test
    public void maxBasalAreaWithModerateThinning() {
        assertThat(TreatmentElements2.getMaxStandBasalArea(exampleSpecies(), true)).isCloseTo(23.86773, delta);
    }

    private Iterable<Species> exampleSpecies() {
        List<Species> result = new ArrayList<>();
        final Species oak = new Species();
        oak.percCSA = 70d;
        oak.spDef.crownwidthXML = new TGTextFunction("(2.6618+0.1152*t.d)*(1.0-exp(-exp(ln(t.d/8.3381)*1.4083))) /* Eiche (DÖBBELER ET. AL. 2001) */");
        oak.spDef.maximumDensityXML = new TGTextFunction("0.0001*3.141592/(16*0.000002807*0.5814*(t.h^(0.9082-1.1830))) /* Eiche Nordwest (DÖBBELER 2004) */");
        oak.spDef.moderateThinning = new HeightBasedThinning("", Arrays.asList(new ThinningValueRange(20, 35, 0.8)));
        oak.d100 = 2d;
        oak.h100 = 30d;
        oak.trule.thinningSettings = thinningSettings();
        result.add(oak);
        final Species spruce = new Species();
        spruce.percCSA = 30d;
        spruce.spDef.crownwidthXML = new TGTextFunction("(2.79563+0.07358*t.d)*(1.0-exp(-exp(ln(t.d/5.43234)*1.34187))) /* Fichte (Albrecht 2010) FVA-BaWü */");
        spruce.spDef.maximumDensityXML = new TGTextFunction("0.0001*3.141592/(16*0.000002807*0.5814*(t.h^(0.9082-1.1830))) /* Eiche Nordwest (DÖBBELER 2004) */");
        spruce.spDef.moderateThinning = new HeightBasedThinning("", Arrays.asList(new ThinningValueRange(20, 37, 0.7)));
        spruce.d100 = 1.5d;
        spruce.h100 = 35d;
        spruce.trule.thinningSettings = thinningSettings();
        result.add(spruce);
        return result;
    }

    private static SpeciesThinningSettings thinningSettings() {
        return SpeciesThinningSettings.heightBasedScenarioSetting("10.0/sts/22.0;22.0/sts/28.0;28.0/sts/100.0", 1);
    }
}
