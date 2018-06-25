package treegross.base.thinning;

import treegross.base.Species;
import treegross.base.Tree;


public interface ModerateThinning {
    static final double defaultThinningFactor = 1d;
    
    double thinningFactorFor(Tree tree);
    
    boolean shouldReduce(Species h100);

    String definition();

}
