package treegross.base;

public enum Layer {
    NONE(0),
    UPPERSTORY(1),
    ZWISCHENSTAND(2),
    UNDERSTORY(3);
    
    private final int value;

    private Layer(int value) {
        this.value = value;
    }
    
}
