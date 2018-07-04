package treegross.base.thinning;

import treegross.base.Species;
import treegross.base.Tree;


public interface DynamicThinning {
    static final double defaultThinningFactor = 1d;
    static final double defaultIntensity = 1d;
    
    double thinningFactorFor(Tree tree);
    
    double intensityFor(Tree tree);
    
    boolean shouldReduce(Species species);

    String moderateThinningDefinition();

}
