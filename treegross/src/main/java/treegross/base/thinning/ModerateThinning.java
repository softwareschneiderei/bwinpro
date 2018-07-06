package treegross.base.thinning;

import treegross.base.Tree;


public interface ModerateThinning {
    static final double defaultThinningFactor = 1d;
    
    /**
     * A norm tree is used to select the thinning factor based on certain attributes
     * like height or age. Usually they are based on the tree's species.
     * 
     * @param tree norm tree for a species
     * @return thinning factor for certain attributes of the norm tree
     */
    double thinningFactorFor(Tree tree);
    
    String definition();

}
