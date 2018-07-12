package treegross.base;

import java.util.NoSuchElementException;
import java.util.ResourceBundle;

public enum Layer {
    NONE("Layer.none", 0),
    UPPERSTORY("Layer.upperstory", 1),
    ZWISCHENSTAND("Layer.middle", 2),
    UNDERSTORY("Layer.understory", 3);
    
    private static final ResourceBundle bundle = ResourceBundle.getBundle("treegross/treegross");
    private final String resourceKey;
    private final int value;

    private Layer(String resourceKey, int value) {
        this.resourceKey = resourceKey;
        this.value = value;
    }
    
    public static Layer fromInt(int value) {
        for (Layer layer : values()) {
            if (layer.value == value) {
                return layer;
            }
        }
        throw new NoSuchElementException("No layer found for " + value);
    }
    
    public int toInt() {
        return value;
    }
     
    @Override
    public String toString() {
        return bundle.getString(resourceKey);
    }    
}
