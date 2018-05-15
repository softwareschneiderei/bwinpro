package forestsimulator.standsimulation;

import java.util.ResourceBundle;

public enum MixtureType {
    Trunk("MixtureType.trunk"),
    Squad("MixtureType.squad"),
    Group("MixtureType.group"),
    Thicket("MixtureType.thicket");

    private static final ResourceBundle bundle = ResourceBundle.getBundle("forestsimulator/gui");
    private final String resourceKey;
    
    private MixtureType(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    @Override
    public String toString() {
        return bundle.getString(resourceKey);
    }
}
