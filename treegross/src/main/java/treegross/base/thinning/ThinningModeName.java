package treegross.base.thinning;


import java.util.ResourceBundle;

public enum ThinningModeName {
    HEIGHT("ThinningModeName.height","height"),
    AGE("ThinningModeName.age", "age");
    
    private static final ResourceBundle bundle = ResourceBundle.getBundle("treegross/treegross");
    private final String name;
    private final String resourceKey;

    private ThinningModeName(String resourceKey, String name) {
        this.name = name;
        this.resourceKey = resourceKey;
    }
   
    public String value() {
        return name;
    }
    
    @Override
    public String toString() {
        return bundle.getString(resourceKey);
    }    
}
