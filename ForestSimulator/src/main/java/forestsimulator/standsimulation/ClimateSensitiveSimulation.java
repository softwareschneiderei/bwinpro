package forestsimulator.standsimulation;

import forestsimulator.util.StopWatch;
import java.text.MessageFormat;
import java.time.Year;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import static treegross.base.SiteIndex.si;
import treegross.base.Stand;
import static treegross.dynamic.siteindex.EnvironmentStandardizer.standardize;
import treegross.dynamic.siteindex.EnvironmentVariables;
import treegross.dynamic.siteindex.EnvironmentalDataProvider;

public class ClimateSensitiveSimulation extends Simulation {
    private final EnvironmentVariables environmentVariables;
    private final String climateScenario;
   
    public ClimateSensitiveSimulation(Stand st, boolean applyTreatment, boolean executeMortality, EnvironmentalDataProvider environmentProvider, String climateScenario) {
        super(st, applyTreatment, executeMortality);
        this.climateScenario = climateScenario;
        this.environmentVariables = standardize(environmentProvider.environmentalDataFor(st.location, climateScenario));
    }

    @Override
    protected void actionsAfterGrowing(int startYear, int numberOfYears) {
        if (environmentVariables == null) {
            return;
        }
        StopWatch dsi = new StopWatch("Dynamic site index").start();
        IntStream.range(startYear, startYear + numberOfYears).mapToObj(Year::of).forEachOrdered(year -> {
                if (!environmentVariables.hasDataFor(year)) {
                    logToBatchLog(MessageFormat.format("No climate data for year {0} in scenario {1}. Using previous site index.", year, climateScenario));
                }
                // TODO: check if computing for each species is enough
                getStand().forAllTrees(tree -> 
                    tree.dsi = si(tree.sp.spDef.dsiCalculator.computeSiteIndex(year, tree.dsi.value, environmentVariables))
                );
        });
        dsi.printElapsedTime();
    }
    
    private void logToBatchLog(String message) {
        ForestSimulator.batchLogger.log(Level.INFO, message);
    }
}
