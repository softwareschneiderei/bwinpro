package forestsimulator.standsimulation;

import java.util.ResourceBundle;

public enum CreationType {
    Distribution("CreationType.distribution"),
    Tree("CreationType.tree");

    private static final ResourceBundle bundle = ResourceBundle.getBundle("forestsimulator/gui");
    private final String resourceKey;
    
    private CreationType(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    @Override
    public String toString() {
        return bundle.getString(resourceKey);
    }
}
