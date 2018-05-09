package forestsimulator.standsimulation;

import java.util.ResourceBundle;

public enum CoordinateType {
    Random("CoordinateType.random"),
    Raster("CoordinateType.raster");

    private static final ResourceBundle bundle = ResourceBundle.getBundle("forestsimulator/gui");
    private final String resourceKey;
    
    private CoordinateType(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    @Override
    public String toString() {
        return bundle.getString(resourceKey);
    }
}
