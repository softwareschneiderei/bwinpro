package treegross.base.thinning;

import treegross.base.Stand;
import treegross.treatment.TreatmentElements2;

/**
 * Thinning from below
 *
 */
public class FromBelowThinner implements Thinner {

    @Override
    public void thin(TreatmentElements2 te, Stand stand) {
        te.thinFromBelow(stand);
    }
    
}
