package treegross.base.thinning;

import treegross.base.Stand;
import treegross.treatment.TreatmentElements2;

/**
 * Thinning by QD
 *
 */
public class QDThinner implements Thinner {

    @Override
    public void thin(TreatmentElements2 te, Stand stand) {
        te.thinByQD(stand);
    }
}
