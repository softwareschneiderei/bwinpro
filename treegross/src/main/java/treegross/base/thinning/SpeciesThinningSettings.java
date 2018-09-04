package treegross.base.thinning;

import java.util.List;
import treegross.base.Tree;
import static treegross.base.thinning.ThinningDefinitionParser.thinningFactorParser;
import static treegross.base.thinning.ThinningDefinitionParser.thinningTypeParser;

public class SpeciesThinningSettings {
    public static final SpeciesThinningSettings defaultSettings = SpeciesThinningSettings.heightBasedScenarioSetting(
            "10.0/sts/22.0;22.0/sts/28.0;28.0/sts/100.0",
            "10.0/0.8/22.0;22.0/0.8/28.0;28.0/0.7/100.0");
    
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

    private final List<ThinningValueRange<Double>> intensityRanges;
    private final List<ThinningValueRange<ThinningType>> typeRanges;
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
    
    public ThinningType typeFor(Tree referenceTree) {
        return mode.firstFactorFoundFor(typeRanges, referenceTree).orElse(ThinningType.SingleTreeSelection);
    }

    /**
     *  intensity Factor to normal stand density 1.0 = normal stand density, no thinning set intensity = 0.0
     * 
     * @param referenceTree
     * @return intensity for the given tree
     */
    public double intensityFor(Tree referenceTree) {
        return mode.firstFactorFoundFor(intensityRanges, referenceTree).orElse(intensityDefault);
    }

    @Override
    public String toString() {
        return "SpeciesThinningSettings{" + "mode=" + mode + ", intensityDefinition=" + intensityDefinition + ", typeDefinition=" + typeDefinition + '}';
    }
}
