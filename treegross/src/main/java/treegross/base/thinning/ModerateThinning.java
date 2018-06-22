package treegross.base.thinning;

import treegross.base.Tree;


public interface ModerateThinning {
    static final double defaultThinningFactor = 1d;
    
    double thinningFactorFor(Tree tree);
    
    boolean shouldReduce(double h100);

    String definition();

}
