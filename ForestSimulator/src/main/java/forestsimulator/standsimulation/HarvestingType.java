package forestsimulator.standsimulation;

import java.util.ResourceBundle;

public enum HarvestingType {
    SingleTreeSelection("HarvestingType.targetDiameter"),
    ThinningFromAbove("HarvestingType.shelter"),
    ThinningFromBelow("HarvestingType.clearCut"),
    ThinningQD("HarvestingType.clearGaps");
 
    private static final ResourceBundle bundle = ResourceBundle.getBundle("forestsimulator/gui");
    private final String resourceKey;
     
    private HarvestingType(String resourceKey) {
        this.resourceKey = resourceKey;
    }
 
    @Override
    public String toString() {
        return bundle.getString(resourceKey);
    }    
}
