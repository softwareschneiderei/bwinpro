package treegross.base.thinning;


import java.util.ResourceBundle;

public enum ThinningModeName {
    HEIGHT("ThinningModeName.height", "height"),
    AGE("ThinningModeName.age", "age");
    
    private static final ResourceBundle bundle = ResourceBundle.getBundle("treegross/treegross");
    private final String name;
    private final String resourceKey;

    private ThinningModeName(String resourceKey, String name) {
        this.name = name;
        this.resourceKey = resourceKey;
    }
    
    public static ThinningModeName fromName(String name) {
        for (ThinningModeName mode : values()) {
            if (mode.name.equalsIgnoreCase(name)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("No thinning mode for name '" + name + "'");
    }
   
    @Override
    public String toString() {
        return bundle.getString(resourceKey);
    }    
}
