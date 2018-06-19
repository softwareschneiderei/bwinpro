package treegross.base;

import java.util.ResourceBundle;

/**
 * DO NOT REORDER!
 * Some part of the code may rely on the ordinal or comparison functions
 * 
 * @author mmv
 */
public enum OutType {
    STANDING("OutType.standing", false),
    FALLEN("OutType.fallen", false),
    THINNED("OutType.thinned", true),
    HARVESTED("OutType.harvested", true);
    
    private static final ResourceBundle bundle = ResourceBundle.getBundle("treegross/treegross");
    private final String resourceKey;
    private final boolean treated;
     
    private OutType(String resourceKey, boolean treated) {
        this.resourceKey = resourceKey;
        this.treated = treated;
    }
    
    /**
     * TODO: What does this mean in practice?
     * 
     * @param type
     * @return 
     */
    public boolean atLeast(OutType type) {
        return ordinal() >= type.ordinal();
    }
    
    public boolean treated() {
        return treated;
    }
 
    @Override
    public String toString() {
        return bundle.getString(resourceKey);
    }    
}
