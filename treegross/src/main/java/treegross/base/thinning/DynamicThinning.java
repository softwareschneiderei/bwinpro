package treegross.base.thinning;

import treegross.base.Species;
import treegross.base.Tree;


public interface DynamicThinning {
    static final double defaultThinningFactor = 1d;
    
    double thinningFactorFor(Tree tree);
    
    boolean shouldReduce(Species species);

    String moderateThinningDefinition();

}
