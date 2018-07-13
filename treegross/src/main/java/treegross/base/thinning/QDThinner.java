package treegross.base.thinning;

import treegross.base.OutType;
import treegross.base.Species;
import treegross.base.Stand;

/**
 * Thinning by QD
 *
 */
public class QDThinner implements Thinner {

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
     * @param stand the stand to work on
     * @param species thin only trees of the given species
     */
    @Override
    public void thin(Stand stand, Species species) {
        stand.forTreesMatching(tree -> tree.isLiving() && tree.crop && tree.isOf(species), tree -> {
            // we found a crop tree and remove now all other trees
            stand.forTreesMatching(competitor -> competitor.d > 7 && competitor.isLiving() && !competitor.crop && !competitor.habitat, competitor -> {
                double ent = Math.sqrt(Math.pow(tree.x - competitor.x, 2.0) + Math.pow(tree.y - competitor.y, 2.0));
                if (tree.h > tree.si * 0.8) {
                    if (ent < 0.9 * (tree.cw + competitor.cw) / 2.0 && competitor.h * 1.1 > tree.cb) {
                        competitor.takeOut(stand.year, OutType.THINNED);
                    }
                }
                if (tree.h > tree.si * 0.3 && tree.h < tree.si * 0.8) {
                    if (ent < 0.20 + (tree.cw + competitor.cw) / 2.0) {
                        competitor.takeOut(stand.year, OutType.THINNED);
                    }
                }
            });
        });
    }
}
