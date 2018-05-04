package forestsimulator.standsimulation;

import java.util.ResourceBundle;

public enum ThinningType {
    SingleTreeSelection("ThinningType.singleTreeSelection"),
    ThinningFromAbove("ThinningType.thinningFromAbove"),
    ThinningFromBelow("ThinningType.thinningFromBelow"),
    ThinningQD("ThinningType.thinningQD");
 
    private static final ResourceBundle bundle = ResourceBundle.getBundle("forestsimulator/gui");
    private final String resourceKey;
     
    private ThinningType(String resourceKey) {
        this.resourceKey = resourceKey;
    }
 
    @Override
    public String toString() {
        return bundle.getString(resourceKey);
    }    
}
