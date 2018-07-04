package treegross.treatment;

import java.util.Arrays;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.data.Offset;
import treegross.base.Species;
import treegross.base.Stand;
import treegross.base.TGTextFunction;
import treegross.base.thinning.HeightBasedThinning;
import treegross.base.thinning.ThinningFactorRange;

public class TreatmentElements2Test {
    private static final Offset<Double> delta = Offset.offset(0.01d);
    
    /*
     * #Pinning
    */
    @Test
    public void maxBasalAreaWithoutModerateThinning() {
        TreatmentElements2 te = new TreatmentElements2();
        Stand st = new Stand();
        addSpeciesTo(st);
        assertThat(te.getMaxStandBasalArea(st, false)).isCloseTo(21.44486, delta);
    }

    /*
     * #Pinning
    */
    @Test
    public void maxBasalAreaWithModerateThinning() {
        TreatmentElements2 te = new TreatmentElements2();
        Stand st = new Stand();
        addSpeciesTo(st);
        assertThat(te.getMaxStandBasalArea(st, true)).isCloseTo(17.15589, delta);
    }

    private void addSpeciesTo(Stand st) {
        final Species species = new Species();
        species.percCSA = 70d;
        species.spDef.crownwidthXML = new TGTextFunction("(2.6618+0.1152*t.d)*(1.0-exp(-exp(ln(t.d/8.3381)*1.4083))) /* Eiche (DÖBBELER ET. AL. 2001) */");
        species.spDef.maximumDensityXML = new TGTextFunction("0.0001*3.141592/(16*0.000002807*0.5814*(t.h^(0.9082-1.1830))) /* Eiche Nordwest (DÖBBELER 2004) */");
        species.spDef.moderateThinning = new HeightBasedThinning("", Arrays.asList(new ThinningFactorRange(20, 35, 0.8)));
        species.d100 = 2d;
        species.h100 = 30d;
        st.sp[0] = species;
        st.nspecies++;
    }
}
