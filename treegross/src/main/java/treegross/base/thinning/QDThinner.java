package treegross.base.thinning;

import treegross.base.OutType;
import treegross.base.Stand;

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
 */
public class QDThinner implements Thinner {

    @Override
    public void thin(Stand stand) {
        stand.forTreesMatching(tree -> tree.isLiving() && tree.crop, (treegross.base.Tree tree1) -> {
            // we found a crop tree and remove now all other trees
            for (int j = 0; j < stand.ntrees; j++) {
                if (stand.tr[j].d > 7 && stand.tr[j].isLiving() && !stand.tr[j].crop && !stand.tr[j].habitat) {
                    double ent = Math.sqrt(Math.pow(tree1.x - stand.tr[j].x, 2.0) + Math.pow(tree1.y - stand.tr[j].y, 2.0));
                    if (tree1.h > tree1.si.value * 0.8) {
                        if (ent < 0.9 * (tree1.cw + stand.tr[j].cw) / 2.0 && stand.tr[j].h * 1.1 > tree1.cb) {
                            stand.tr[j].takeOut(stand.year, OutType.THINNED);
                        }
                    }
                    // TODO: What is this condition? Should it be moved into SiteIndex?
                    if (tree1.h > tree1.si.value * 0.3 && tree1.h < tree1.si.value * 0.8) {
                        if (ent < 0.20 + (tree1.cw + stand.tr[j].cw) / 2.0) {
                            stand.tr[j].takeOut(stand.year, OutType.THINNED);
                        }
                    }
                }
            }
        });
    }
}
