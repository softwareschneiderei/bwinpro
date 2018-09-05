package treegross.base.thinning;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import treegross.base.OutType;
import treegross.base.Species;
import treegross.base.Stand;
import treegross.base.Tree;
import treegross.treatment.TreatmentElements2;

/**
 * Thinning by releasing the crop trees
 *
 */
public class SingleTreeSelectionThinner extends AreaThinner {
    private static final Logger logger = Logger.getLogger(SingleTreeSelectionThinner.class.getName());
    
    private int competitorsTakenOut = 0;
    private Optional<Double> competitorFactor;
    
    SingleTreeSelectionThinner(boolean cropTreesOnly, double volumeAlreadyOut) {
        super(!cropTreesOnly, volumeAlreadyOut);
    }

    @Override
    public void thin(Stand stand, Species species) {
        thinCropTreeCompetition(stand, species);
        thinArea(stand);
    }
    
    /**
     * Check all trees if they are no crop tree, if they are competing with a
     * crop tree Mesurement: is the overlap of the crown This void needs
     * CropTreeSelection
     *
     * @param st stand object
     * @param species the species of the crop trees
     */
    public void thinCropTreeCompetition(Stand st, Species species) {
        logger.info("Thinning crop tree competition");
        //set max thinning volume (vmaxthinning) if outaken amount (vout) 
        //has not reached max allowed amount for stand (st.size*st.trule.maxThinningVolume)
        double vmaxthinning = st.size * st.trule.maxThinningVolume - thinned;

        //reduce max thinning if max allowed amount for stand (st.size*st.trule.maxThinningVolume)
        // minus outaken amount (vout) is less than set max thinning volume (vmaxthinning)
        if ((st.size * st.trule.maxOutVolume - vout) < vmaxthinning) {
            vmaxthinning = st.size * st.trule.maxOutVolume - vout;
        }

//        if (vmaxthinning <= 0) {
//            return;
//        }
        List<ThinningValueRange<Double>> valueRanges = ThinningDefinitionParser.thinningFactorParser.parseDefinition(species.trule.competitorTakeOutDefinition);
        competitorFactor = species.trule.thinningSettings.getMode().firstFactorFoundFor(valueRanges, species.referenceTree());

        // Thinning is done iteratively tree by tree
        // 1. Calculate the overlap of all crop trees
        // 2. Calculate tolerable overlap of crop tree according to Spellmann et al,
        //    Heidi Doebbeler and crown width functions
        // 3. Find tree with the highest differenz in overlap - tolerable overlap
        // 4. Remove for the crop tree of 3.) the tree with the greates overlap area
        // 5. Start with 1. again
        double intensity = 2.0 - species.thinningIntensity();
        if (intensity == 0.0) {
            intensity = 1.0;
        }

        double maxBasalAreaOut = TreatmentElements2.reduceBaOut(st);

//        if (maxBasalAreaOut <= 0.0) {
//            return;
//        }
        while (continueThinning(species, vmaxthinning, maxBasalAreaOut)) { //stop if max thinning amount is reached
            // update competition overlap for crop trees
            st.forTreesMatching(isCropTreeOf(species), Tree::updateCompetition);
            Optional<Tree> cropTree = findCropTreeWithMostCompetition(st, species);
            // release the crop tree with indexOfCropTree and take out neighbor, which comes closest with the
            // crown to the crop tree's crown at height crown base. Neighbors are taken out only if they come
            // into the limit of twice the crown radius of the crop tree size
            if (!cropTree.isPresent()) {
                logger.log(Level.INFO, "No crop tree for species: {0}", species.code);
                break;
            }
            // Find neighbor who comes closest
            Optional<Tree> closestNeighbor = findClosestNeighbor(st, cropTree.get(), intensity);
            if (!closestNeighbor.isPresent()) {
                logger.log(Level.INFO, "No closest neighbour.", species.code);
                break;
            }
            Tree competitor = closestNeighbor.get();
            competitor.takeOut(st.year, OutType.THINNED);
            thinned += (competitor.fac * competitor.v);
            competitorsTakenOut++;
            maxBasalAreaOut -= (competitor.fac * Math.PI * Math.pow(competitor.d / 200.0, 2.0)) / st.size;
        }
    }

    private boolean continueThinning(Species species, double vmaxthinning, double maxBasalAreaOut) {
        if (species.trule.competitorTakeOutDefinition.isEmpty()) {
            return thinned < vmaxthinning && maxBasalAreaOut > 0;
        }
        // TODO: http://issuetracker.intranet:20002/browse/BWIN-89
        int competitorsToTakeOut = (int) (species.trule.numberCropTreesWanted * competitorFactor.orElse(1d));
        logger.log(Level.INFO, "Competitors taken out: {0} of {1}", new Object[]{ competitorsTakenOut, competitorsToTakeOut });
        return competitorsTakenOut < competitorsToTakeOut;
    }

    private Optional<Tree> findCropTreeWithMostCompetition(Stand st, Species species) {
        // find crop with most competition, defined as that tree with greates ratio of
        // actual c66xy divided by maximum c66
        Optional<Tree> cropTree = Optional.empty();
        double maxCompetition = -Double.MAX_VALUE;
        for (Tree tree : st.trees()) {
            if (isCropTreeOf(species).test(tree)) {
                double c66Ratio = TreatmentElements2.calculateC66Ratio(tree, species.thinningIntensity());
                // remember tree if c66Ratio is greater than maxCompetition
                if (c66Ratio > maxCompetition) {
                    cropTree = Optional.of(tree);
                    maxCompetition = c66Ratio;
                }
            }
        }
        return cropTree;
    }

    private Optional<Tree> findClosestNeighbor(Stand st, Tree cropTree, double intensity) {
        double dist = 9999.0;
        Optional<Tree> closestNeighbor = Optional.empty();
        double h66 = cropTree.cb;
        for (int i = 0; i < cropTree.nNeighbor; i++) {
            Tree neighbor = st.tr[cropTree.neighbor[i]];
            if (neighbor.d > 7
                    && neighbor.isLiving()
                    && !neighbor.crop
                    && !neighbor.habitat) {
                double radius = neighbor.calculateCwAtHeight(h66) / 2.0;
                double ent = Math.sqrt(Math.pow(cropTree.x - neighbor.x, 2.0)
                        + Math.pow(cropTree.y - neighbor.y, 2.0));
                if ((ent - radius < cropTree.cw * (0.75 / intensity)) && dist > (ent - radius)) {
                    closestNeighbor = Optional.of(neighbor);
                    dist = ent - radius;
                }
            }
        }
        return closestNeighbor;
    }
    
    private Predicate<Tree> isCropTreeOf(Species species) {
        return tree -> tree.isLiving() && tree.crop && tree.isOf(species);
    }
}
