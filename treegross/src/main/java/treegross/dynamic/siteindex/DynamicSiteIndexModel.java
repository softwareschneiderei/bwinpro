package treegross.dynamic.siteindex;

public class DynamicSiteIndexModel {
    public Regression modelParameters() {
        Regression result = new Regression();
        result.addParametersFor("Fi", new ModelParameters(
                1.00003571186121,
                0.99998833695419,
                -9.02561907643062E-03,
                4.76764480882665E-04,
                -2.57562425678908E-03,
                1.00564825578297E-03,
                1.37988034418277E-02
        ));
        return result;
    }
}
