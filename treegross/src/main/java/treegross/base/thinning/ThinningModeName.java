package treegross.base.thinning;


import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;
import treegross.base.Tree;

public enum ThinningModeName {
    HEIGHT("ThinningModeName.height", "height", tree -> tree.h),
    AGE("ThinningModeName.age", "age", tree -> Double.valueOf(tree.age));
    
    private static final ResourceBundle bundle = ResourceBundle.getBundle("treegross/treegross");
    private final String name;
    private final String resourceKey;
    private final Function<Tree, Double> attributeExtractor;

    private ThinningModeName(String resourceKey, String name, Function<Tree, Double> attributeExtractor) {
        this.name = name;
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

    public <T> Optional<T> firstValueFoundFor(DefinedRanges<T> ranges, Tree tree) {
        return ranges.firstValueFoundFor(criterionValueOf(tree));
    }
    
    public <T> T bestValueFor(List<ThinningValueRange<T>> ranges, Tree tree) {
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
