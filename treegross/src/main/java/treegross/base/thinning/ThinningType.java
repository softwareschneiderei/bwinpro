package treegross.base.thinning;

import java.util.NoSuchElementException;
import java.util.ResourceBundle;

public enum ThinningType {
    SingleTreeSelection("ThinningType.singleTreeSelection", "sts") {
        @Override
        public Thinner thinner(boolean cropTreesOnly, double volumeAlreadyOut) {
            return new SingleTreeSelectionThinner(cropTreesOnly, volumeAlreadyOut);
        }
    },
    ThinningFromAbove("ThinningType.thinningFromAbove", "tfa") {
        @Override
        public Thinner thinner(boolean cropTreesOnly, double volumeAlreadyOut) {
            return new FromAboveThinner(volumeAlreadyOut);
        }
    },
    ThinningFromBelow("ThinningType.thinningFromBelow", "tfb") {
        @Override
        public Thinner thinner(boolean cropTreesOnly, double volumeAlreadyOut) {
            return new FromBelowThinner(volumeAlreadyOut);
        }
    },
    ThinningQD("ThinningType.thinningQD", "qd") {
        @Override
        public Thinner thinner(boolean cropTreesOnly, double volumeAlreadyOut) {
            return new QDThinner(cropTreesOnly);
        }
    },
    ClearCut("ThinningType.clearCut", "cc") {
        @Override
        public Thinner thinner(boolean cropTreesOnly, double volumeAlreadyOut) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
 
    private static final ResourceBundle bundle = ResourceBundle.getBundle("treegross/treegross");

    private final String resourceKey;
    private final String value;
     
    private ThinningType(String resourceKey, String value) {
        this.resourceKey = resourceKey;
        this.value = value;
    }
    
    public static ThinningType forShortName(String shortName) {
        for (ThinningType type : values()) {
            if (type.shortName().equals(shortName)) {
                return type;
            }
        }
        throw new NoSuchElementException("No thinning type found for short name:" + shortName);
    }

    public String shortName() {
        return value;
    }
    
    public abstract Thinner thinner(boolean cropTreesOnly, double volumeAlreadyOut);
    
    @Override
    public String toString() {
        return bundle.getString(resourceKey);
    }    
}
