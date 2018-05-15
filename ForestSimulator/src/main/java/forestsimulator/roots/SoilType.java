package forestsimulator.roots;

import java.util.ResourceBundle;

public enum SoilType {
    Sand("SoilType.sand"),
    Loam("SoilType.loam"),
    Silt("SoilType.silt"),
    Clay("SoilType.clay"),
    Bedrock("SoilType.bedrock");

    private static final ResourceBundle bundle = ResourceBundle.getBundle("forestsimulator/gui");
    private final String resourceKey;
    
    private SoilType(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    @Override
    public String toString() {
        return bundle.getString(resourceKey);
    }
}
