package treegross.base.thinning;

import java.util.NoSuchElementException;
import java.util.ResourceBundle;

public enum ThinningType {
    SingleTreeSelection("ThinningType.singleTreeSelection", 0),
    ThinningFromAbove("ThinningType.thinningFromAbove", 1),
    ThinningFromBelow("ThinningType.thinningFromBelow", 2),
    ThinningQD("ThinningType.thinningQD", 3),
    ClearCut("ThinningType.clearCut", 9);
 
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
 
    @Override
    public String toString() {
        return bundle.getString(resourceKey);
    }    
}
