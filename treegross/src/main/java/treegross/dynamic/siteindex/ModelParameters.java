package treegross.dynamic.siteindex;

public class ModelParameters {
    public final double parameter1;
    public final double parameter2;
    public final double parameter3;
    public final double parameter4;
    public final double parameter5;
    public final double parameter6;
    public final double parameter7;

    public ModelParameters() {
        this(1d, 1d, 1d, 1d, 1d, 1d, 1d);
    }
    
    public ModelParameters(double parameter1, double parameter2, double parameter3, double parameter4, double parameter5, double parameter6, double parameter7) {
        super();
        this.parameter1 = parameter1;
        this.parameter2 = parameter2;
        this.parameter3 = parameter3;
        this.parameter4 = parameter4;
        this.parameter5 = parameter5;
        this.parameter6 = parameter6;
        this.parameter7 = parameter7;
    }
}
