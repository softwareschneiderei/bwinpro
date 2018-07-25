package forestsimulator.standsimulation;

import forestsimulator.util.StopWatch;
import java.time.Year;
import java.util.stream.IntStream;
import treegross.base.SiteIndex;
import treegross.base.Stand;
import static treegross.dynamic.siteindex.EnvironmentStandardizer.standardize;
import treegross.dynamic.siteindex.EnvironmentVariables;
import treegross.dynamic.siteindex.EnvironmentalDataProvider;

public class ClimateSensitiveSimulation extends Simulation {
    
    private final EnvironmentVariables environmentVariables;
   
    public ClimateSensitiveSimulation(Stand st, boolean applyTreatment, boolean executeMortality, EnvironmentalDataProvider environmentProvider, String climateScenario) {
        super(st, applyTreatment, executeMortality);
        this.environmentVariables = standardize(environmentProvider.environmentalDataFor(st.location, climateScenario));
    }

    @Override
    protected void actionsAfterGrowing(int startYear, int numberOfYears) {
        StopWatch dsi = new StopWatch("Dynamic site index").start();
        if (environmentVariables != null) {
            IntStream.range(startYear, startYear + numberOfYears).mapToObj(Year::of).forEachOrdered(year ->
                    // TODO: check if computing for each species is enough
                    getStand().forAllTrees(tree -> 
                        tree.dsi = SiteIndex.si(tree.sp.spDef.dsiCalculator.computeSiteIndex(year, tree.dsi.value, environmentVariables))
                    )
            );
        }
        dsi.printElapsedTime();
    }
}
