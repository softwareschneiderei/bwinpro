package forestsimulator.standsimulation;

import java.util.ResourceBundle;

public enum WoodType {
    HardWood("WoodType.hardwood"),
    AllHardWood("WoodType.allhardwood"),
    AllSpecies("WoodType.allspecies");
 
    private static final ResourceBundle bundle = ResourceBundle.getBundle("forestsimulator/gui");
    private final String resourceKey;
     
    private WoodType(String resourceKey) {
        this.resourceKey = resourceKey;
    }
 
    @Override
    public String toString() {
        return bundle.getString(resourceKey);
    }    
}
