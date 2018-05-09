package forestsimulator.standsimulation;

import java.util.ResourceBundle;

public enum StandGraphicMode {
    G2D("StandGraphicMode.2d"),
    OpenGL3D("StandGraphicMode.opengl");

    private static final ResourceBundle bundle = ResourceBundle.getBundle("forestsimulator/gui");
    private final String resourceKey;
    
    private StandGraphicMode(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    @Override
    public String toString() {
        return bundle.getString(resourceKey);
    }
}
