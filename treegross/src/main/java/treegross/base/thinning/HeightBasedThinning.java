package treegross.base.thinning;

import treegross.base.Tree;

public class HeightBasedThinning implements ModerateThinning {

    private final String thinningDefinition;

    public HeightBasedThinning(String thinningDefinition) {
        super();
        this.thinningDefinition = thinningDefinition;
    }

    @Override
    public double thinningFactorFor(Tree tree) {
        double tfac = 1.0;
        if (thinningDefinition.length() > 4) {
            String zeile = thinningDefinition;
            String[] tokens;
            tokens = zeile.split(";");
            // added by jhansen
            int end = tokens.length / 3;

            for (int i = 0; i < end; i++) {
                double hu = Double.parseDouble(tokens[i * 3]);
                double f = Double.parseDouble(tokens[i * 3 + 1]);
                double ho = Double.parseDouble(tokens[i * 3 + 2]);
                if (tree.h >= hu && tree.h < ho) {
                    tfac = f;
                    break;
                }
            }
        }
        return tfac;
    }

    @Override
    public boolean shouldReduce(double h100) {
        return h100 >= startReducingAHeight();
    }

    @Override
    public String definition() {
        return thinningDefinition;
    }
    
    protected double startReducingAHeight() throws NumberFormatException {
        double heightStartReducing = Double.POSITIVE_INFINITY;
        String[] heightStartReducingA = thinningDefinition.split(";");
        if (heightStartReducingA.length > 2){
            heightStartReducing = Double.parseDouble(heightStartReducingA[2]);
        }
        return heightStartReducing;
    }
}
