package treegross.base.thinning;

import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import treegross.base.TreatmentRuleStand;

public enum ThinningType {
    SingleTreeSelection("ThinningType.singleTreeSelection", 0) {
        @Override
        public void applyTo(TreatmentRuleStand rules, boolean cropTreesOnly) {
            super.applyTo(rules, cropTreesOnly);
            rules.thinArea = !cropTreesOnly;
            rules.selectCropTrees = true;
            rules.reselectCropTrees = true;
            rules.releaseCropTrees = true;
            rules.cutCompetingCropTrees = true;
            rules.releaseCropTreesSpeciesDependent = true;
        }

        @Override
        public Thinner thinner(double volumeAlreadyOut) {
            return new SingleTreeSelectionThinner(volumeAlreadyOut);
        }
    },
    ThinningFromAbove("ThinningType.thinningFromAbove", 1) {
        @Override
        public Thinner thinner(double volumeAlreadyOut) {
            return new FromAboveThinner(volumeAlreadyOut);
        }
    },
    ThinningFromBelow("ThinningType.thinningFromBelow", 2) {
        @Override
        public Thinner thinner(double volumeAlreadyOut) {
            return new FromBelowThinner(volumeAlreadyOut);
        }
    },
    ThinningQD("ThinningType.thinningQD", 3) {
        @Override
        public void applyTo(TreatmentRuleStand rules, boolean cropTreesOnly) {
            super.applyTo(rules, cropTreesOnly);
            rules.thinArea = false;
            if (cropTreesOnly) {
                rules.thinArea = false; // oder doch true
            }
            rules.selectCropTrees = true;
            rules.reselectCropTrees = true;
            rules.releaseCropTrees = true;
            rules.cutCompetingCropTrees = true;
            rules.releaseCropTreesSpeciesDependent = true;
        }

        @Override
        public Thinner thinner(double volumeAlreadyOut) {
            return new QDThinner();
        }
    },
    ClearCut("ThinningType.clearCut", 9) {
        @Override
        public Thinner thinner(double volumeAlreadyOut) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
 
    private static final ResourceBundle bundle = ResourceBundle.getBundle("treegross/treegross");

    private final String resourceKey;
    private final int value;
     
    private ThinningType(String resourceKey, int value) {
        this.resourceKey = resourceKey;
        this.value = value;
    }
    
    public static ThinningType forValue(int typeNumber) {
        for (ThinningType type : values()) {
            if (type.value() == typeNumber) {
                return type;
            }
        }
        throw new NoSuchElementException("No thinning type found for number:" + typeNumber);
    }

    public int value() {
        return value;
    }
    
    public abstract Thinner thinner(double volumeAlreadyOut);
    
    public void applyTo(TreatmentRuleStand rules, boolean cropTreesOnly) {
        rules.typeOfThinning = this;
    }
 
    @Override
    public String toString() {
        return bundle.getString(resourceKey);
    }    
}
