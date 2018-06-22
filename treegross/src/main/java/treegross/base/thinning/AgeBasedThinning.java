package treegross.base.thinning;

import treegross.base.Tree;

public class AgeBasedThinning implements ModerateThinning {

    private final String thinningDefinition;

    public AgeBasedThinning(String thinningDefinition) {
        this.thinningDefinition = thinningDefinition;
    }

    @Override
    public double thinningFactorFor(Tree tree) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean shouldReduce(double h100) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String definition() {
        return thinningDefinition;
    }
}
