package treegross.base.thinning;

import treegross.base.OutType;
import treegross.base.Stand;

/**
 * Thinning by QD
 *
 */
public class QDThinner implements Thinner {

    @Override
    public void thin(Stand stand) {
        thinByQD(stand);
    }

    /**
     * Thinning by Q-D-Rule is a specail thinning from Rheinland-Pfalz The
     * selected crop trees are released so that there will be no crown contact
     * after one growing cyle. In this routine it is simlypied the crop trees
     * get 25 cm distance to the crown. This strong thinning is performed when
     * the height of the crop tree is between greater 35% or smalerequal 80% of
     * the site-index height at age 100. If the height is 100% only trees will
     * be removed which have contact to the crown of the crop tree. The thinning
     * method assumes that there is no competition of other crop trees and that
     * the number of crop trees is low
     *
     * @param st
     */
    public void thinByQD(Stand st) {
        st.forTreesMatching(tree -> tree.isLiving() && tree.crop, tree -> {
            // we found a crop tree and remove now all other trees
            for (int j = 0; j < st.ntrees; j++) {
                if (st.tr[j].d > 7 && st.tr[j].isLiving() && st.tr[j].crop == false && st.tr[j].habitat == false) {
                    double ent = Math.sqrt(Math.pow(tree.x - st.tr[j].x, 2.0) + Math.pow(tree.y - st.tr[j].y, 2.0));
                    if (tree.h > tree.si * 0.8) {
                        if (ent < 0.9 * (tree.cw + st.tr[j].cw) / 2.0 && st.tr[j].h * 1.1 > tree.cb) {
                            st.tr[j].out = st.year;
                            st.tr[j].outtype = OutType.THINNED;
                        }
                    }
                    if (tree.h > tree.si * 0.3 && tree.h < tree.si * 0.8) {
                        if (ent < 0.20 + (tree.cw + st.tr[j].cw) / 2.0) {
                            st.tr[j].out = st.year;
                            st.tr[j].outtype = OutType.THINNED;
                        }
                    }
                }
            }
        });
    }
}
