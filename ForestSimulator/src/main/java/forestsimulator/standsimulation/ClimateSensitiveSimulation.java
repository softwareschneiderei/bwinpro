package forestsimulator.standsimulation;

import forestsimulator.util.StopWatch;
import java.text.MessageFormat;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.IntStream;
import treegross.base.SiteIndex;
import treegross.base.Stand;
import treegross.base.Tree;
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
                Map<Integer, SiteIndex> speciesSi = new HashMap<>();
                getStand().forAllTrees(tree -> {
                    if (!speciesSi.containsKey(tree.sp.code)) {
                        speciesSi.put(tree.sp.code, dsiFor(tree, year));
                    }
                    tree.si = speciesSi.get(tree.sp.code);
                });
        });
        dsi.printElapsedTime();
    }
    
    private SiteIndex dsiFor(Tree tree, Year year) {
        return tree.sp.spDef.dsiCalculator.computeSiteIndex(year, tree.si, environmentVariables);
    }
    
    private void logToBatchLog(String message) {
        ForestSimulator.batchLogger.log(Level.INFO, message);
    }
}
