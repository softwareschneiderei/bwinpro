package forestsimulator.standsimulation;

import java.util.ResourceBundle;

public enum StandShape {
    Square("StandShape.square"),
    Circular("StandShape.circular");

    private static final ResourceBundle bundle = ResourceBundle.getBundle("forestsimulator/gui");
    private final String resourceKey;
    
    private StandShape(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    @Override
    public String toString() {
        return bundle.getString(resourceKey);
    }
}
