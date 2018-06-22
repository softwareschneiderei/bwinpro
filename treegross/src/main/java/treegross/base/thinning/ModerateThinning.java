package treegross.base.thinning;

import treegross.base.Tree;


public interface ModerateThinning {
    
    double thinningFactorFor(Tree tree);
    
    boolean shouldReduce(double h100);

    String definition();

}
