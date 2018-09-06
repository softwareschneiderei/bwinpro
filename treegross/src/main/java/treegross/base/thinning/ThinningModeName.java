package treegross.base.thinning;


import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;
import treegross.base.Tree;

public enum ThinningModeName {
    HEIGHT(100, "ThinningModeName.height", "height", tree -> tree.h),
    AGE(200, "ThinningModeName.age", "age", tree -> Double.valueOf(tree.age));
    
    private static final ResourceBundle bundle = ResourceBundle.getBundle("treegross/treegross");
    public final double min = 0d;
    private final String name;
    public final double max;
    private final String resourceKey;
    private final Function<Tree, Double> attributeExtractor;

    private ThinningModeName(double max, String resourceKey, String name, Function<Tree, Double> attributeExtractor) {
        this.name = name;
        this.max = max;
        this.resourceKey = resourceKey;
        this.attributeExtractor = attributeExtractor;
    }
    
    public static ThinningModeName fromName(String name) {
        for (ThinningModeName mode : values()) {
            if (mode.name.equalsIgnoreCase(name)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("No thinning mode for name '" + name + "'");
    }

    public <T> boolean coverageComplete(DefinedRanges<T> ranges) {
        return ranges.cover(min, max);
    }

    public <T> Optional<T> firstValueFoundFor(DefinedRanges<T> ranges, Tree tree) {
        return ranges.firstValueFoundFor(criterionValueOf(tree));
    }
    
    public <T> T bestValueFor(DefinedRanges<T> ranges, Tree tree) {
        // If value is below defined ranges use value of first range
        
        // if value is above defined ranges use value of last range
        return null;
    }
    
    private double criterionValueOf(Tree tree) {
        return attributeExtractor.apply(tree);
    }
   
    @Override
    public String toString() {
        return bundle.getString(resourceKey);
    }    
}
