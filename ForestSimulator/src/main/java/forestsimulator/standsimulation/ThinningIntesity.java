package forestsimulator.standsimulation;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public enum ThinningIntesity {
    None(0.0, "ThinningIntesity.none"),
    Low(0.8, "ThinningIntesity.low"),
    Moderate(1.0, "ThinningIntesity.moderate"),
    Heavy(1.2, "ThinningIntesity.heavy"),
    VeryHeavy(1.5, "ThinningIntesity.veryheavy");

    private static final ResourceBundle bundle = ResourceBundle.getBundle("forestsimulator/gui");
    private final double value;
    private final String resourceKey;
    
    private ThinningIntesity(double value, String resourceKey) {
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
