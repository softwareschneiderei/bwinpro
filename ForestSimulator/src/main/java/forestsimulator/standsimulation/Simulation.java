package forestsimulator.standsimulation;

import forestsimulator.dbaccess.DatabaseEnvirionmentalDataProvider;
import forestsimulator.util.StopWatch;
import java.time.Year;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import treegross.base.SiteIndex;
import treegross.base.Stand;
import treegross.dynamic.siteindex.EnvironmentVariables;
import treegross.dynamic.siteindex.EnvironmentalDataProvider;
import treegross.treatment.Treatment2;

public class Simulation {
    public static final Consumer<Stand> publishNothing = stand -> {};
    private static final Logger logger = Logger.getLogger(Simulation.class.getSimpleName());
    
    private final Stand st;
    private final Treatment2 treat = new Treatment2();
    private final boolean applyTreatment;
    private final boolean executeMortality;
    private final boolean calculateDynamicSiteIndex;
    private EnvironmentVariables environmentVariables;

    public Simulation(Stand st, boolean applyTreatment, boolean executeMortality, boolean calculateDynamicSiteIndex, EnvironmentalDataProvider environmentProvider, String climateScenario) {
        super();
        this.st = st;
        this.applyTreatment = applyTreatment;
        this.executeMortality = executeMortality;
        this.calculateDynamicSiteIndex = calculateDynamicSiteIndex;
        if (calculateDynamicSiteIndex) {
            this.environmentVariables = environmentProvider.environmentalDataFor(st.location, climateScenario);
        }
    }

    public void executeStep(int numberOfYears, Consumer<Stand> publishStand) {
        if (applyTreatment) {
            StopWatch treatment = new StopWatch("Applying treatment").start();
            treat.executeManager2(st);
            treatment.printElapsedTime();
        }
        if (executeMortality) {
            StopWatch mortality = new StopWatch("Executing mortality").start();
            st.descspecies();
            st.executeMortality();
            mortality.printElapsedTime();
        }
        st.descspecies();
        StopWatch grow = new StopWatch("Growing").start();
        // first year of dsi calculation is the next year
        int startYear = st.year + 1;
        st.grow(numberOfYears, st.ingrowthActive);
        grow.printElapsedTime();
        StopWatch dsi = new StopWatch("Dynamic site index").start();
        if (calculateDynamicSiteIndex && environmentVariables != null) {
            IntStream.range(startYear, startYear + numberOfYears).mapToObj(year -> Year.of(year)).forEachOrdered(year -> {
                st.forAllTrees(tree -> {
                    tree.dsi = SiteIndex.si(tree.sp.spDef.dsiCalculator.computeSiteIndex(year, tree.dsi.value, environmentVariables));
                });
            });
        }
        dsi.printElapsedTime();
        StopWatch save = new StopWatch("Publishing stand").start();
        publishStand.accept(st);
        save.printElapsedTime();
    }
}
