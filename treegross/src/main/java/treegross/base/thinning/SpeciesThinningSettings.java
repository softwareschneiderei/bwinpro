package treegross.base.thinning;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import treegross.base.Tree;
import static treegross.base.thinning.ThinningDefinitionParser.thinningFactorParser;
import static treegross.base.thinning.ThinningDefinitionParser.thinningTypeParser;

public class SpeciesThinningSettings {
    public static final SpeciesThinningSettings defaultSettings = SpeciesThinningSettings.heightBasedScenarioSetting(
            "0.0/sts/22.0;22.0/sts/28.0;28.0/sts/100.0",
            "0.0/0.8/22.0;22.0/0.8/28.0;28.0/0.7/100.0");
    
    public static final SpeciesThinningSettings ageBasedScenarioSetting(String typeDefinition, double intensity) {
        return new SpeciesThinningSettings(ThinningModeName.AGE, typeDefinition, "", intensity);
    }

    public static final SpeciesThinningSettings ageBasedScenarioSetting(String typeDefinition, String intensityDefinition) {
        return new SpeciesThinningSettings(ThinningModeName.AGE, typeDefinition, intensityDefinition);
    }

    public static final SpeciesThinningSettings heightBasedScenarioSetting(String typeDefinition, double intensity) {
        return new SpeciesThinningSettings(ThinningModeName.HEIGHT, typeDefinition, "", intensity);
    }

    public static final SpeciesThinningSettings heightBasedScenarioSetting(String typeDefinition, String intensityDefinition) {
        return new SpeciesThinningSettings(ThinningModeName.HEIGHT, typeDefinition, intensityDefinition);
    }

    private final DefinedRanges<Double> intensityRanges;
    private final DefinedRanges<ThinningType> typeRanges;
    private final ThinningModeName mode;
    private final double intensityDefault;
    private final String intensityDefinition;
    private final String typeDefinition;

    private SpeciesThinningSettings(ThinningModeName mode, String typeDefinition, String intensityDefinition) {
        this(mode, typeDefinition, intensityDefinition, ModerateThinning.defaultThinningFactor);
    }

    private SpeciesThinningSettings(
            ThinningModeName mode,
            String typeDefinition,
            String intensityDefinition,
            double intensityDefault) {
        super();
        this.mode = mode;
        this.intensityDefinition = intensityDefinition;
        this.typeDefinition = typeDefinition;
        this.intensityRanges = thinningFactorParser.parseDefinition(intensityDefinition);
        this.typeRanges = thinningTypeParser.parseDefinition(typeDefinition);
        this.intensityDefault = intensityDefault;
    }

    public SpeciesThinningSettings with(ThinningModeName thinningModeName, String newTypeDefinition, String newIntensityDefinition) {
        return new SpeciesThinningSettings(
                thinningModeName,
                newTypeDefinition,
                newIntensityDefinition,
                intensityDefault);
    }

    public ThinningModeName getMode() {
        return mode;
    }
    
    public String typeDefinition() {
        return typeDefinition;
    }
    
    public String intensityDefinition() {
        return intensityDefinition;
    }
    
    public boolean typeCoverageComplete() {
        return mode.coverageComplete(typeRanges);
    }

    public ThinningType typeFor(Tree referenceTree) {
        return valueFor(typeRanges, referenceTree, ThinningType.SingleTreeSelection);
    }

    public boolean intensityCoverageComplete() {
        return mode.coverageComplete(intensityRanges);
    }

    /**
     *  intensity Factor to normal stand density 1.0 = normal stand density, no thinning set intensity = 0.0
     * 
     * @param referenceTree
     * @return intensity for the given tree
     */
    public double intensityFor(Tree referenceTree) {
        return valueFor(intensityRanges, referenceTree, intensityDefault);
    }

    private <T> T valueFor(DefinedRanges<T> ranges, Tree referenceTree, T fallBack) {
        Optional<T> value = mode.firstValueFoundFor(ranges, referenceTree);
        if (!value.isPresent()) {
            T bestValue = mode.bestValueFor(ranges, referenceTree, fallBack);
            logger().log(Level.INFO, "No value found for mode {0}. Using best value {1}.", new Object[]{mode, bestValue});
            return bestValue;
        }
        return value.get();
    }

    private static Logger logger() {
        return Logger.getLogger("BatchLogger");
    }

    @Override
    public String toString() {
        return "SpeciesThinningSettings{" + "mode=" + mode + ", intensityDefinition=" + intensityDefinition + ", typeDefinition=" + typeDefinition + '}';
    }
}
