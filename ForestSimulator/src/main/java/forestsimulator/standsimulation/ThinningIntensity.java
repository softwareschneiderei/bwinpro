package forestsimulator.standsimulation;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public enum ThinningIntensity {
    None(0.0, "ThinningIntensity.none"),
    Low(0.8, "ThinningIntensity.low"),
    Moderate(1.0, "ThinningIntensity.moderate"),
    Heavy(1.2, "ThinningIntensity.heavy"),
    VeryHeavy(1.5, "ThinningIntensity.veryheavy");

    private static final ResourceBundle bundle = ResourceBundle.getBundle("forestsimulator/gui");
    private final double value;
    private final String resourceKey;
    
    private ThinningIntensity(double value, String resourceKey) {
        this.value = value;
        this.resourceKey = resourceKey;
    }
    
    public String entry() {
        return DecimalFormat.getNumberInstance(Locale.ENGLISH).format(value) + " " + toString();
    }

    @Override
    public String toString() {
        return bundle.getString(resourceKey);
    }
}
