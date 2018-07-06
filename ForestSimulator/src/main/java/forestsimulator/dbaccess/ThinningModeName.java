package forestsimulator.dbaccess;

public enum ThinningModeName {
    HEIGHT("height"),
    AGE("age");
    
    private final String name;

    private ThinningModeName(String name) {
        this.name = name;
    }
   
    public String value() {
        return name;
    }
}
