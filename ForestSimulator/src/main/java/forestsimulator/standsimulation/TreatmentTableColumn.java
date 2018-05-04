package forestsimulator.standsimulation;

import java.util.ResourceBundle;

public enum TreatmentTableColumn {
    Species("TreatmentTableColumn.species"),
    Code("TreatmentTableColumn.code"),
    ThinnigHeight("TreatmentTableColumn.thinningHeight"),
    TargetD("TreatmentTableColumn.targetD"),
    CropTrees("TreatmentTableColumn.croptrees"),
    Mixture("TreatmentTableColumn.mixture");

    private static final ResourceBundle bundle = ResourceBundle.getBundle("forestsimulator/gui");
    private final String resourceKey;
     
    private TreatmentTableColumn(String resourceKey) {
        this.resourceKey = resourceKey;
    }
 
    @Override
    public String toString() {
        return bundle.getString(resourceKey);
    }    
}
