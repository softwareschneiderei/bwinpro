package treegross.base.thinning;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import treegross.base.Tree;
import static treegross.base.thinning.ThinningDefinitionParser.thinningFactorParser;
import static treegross.base.thinning.ThinningDefinitionParser.thinningTypeParser;

public class SpeciesThinningSettings {
    
    public static final SpeciesThinningSettings ageBasedScenarioSetting(String typeDefinition, double intensity) {
        return new SpeciesThinningSettings(ThinningModeName.AGE, typeDefinition, "", intensity, tree -> Double.valueOf(tree.age));
    }

    public static final SpeciesThinningSettings ageBasedScenarioSetting(String typeDefinition, String intensityDefinition) {
        return new SpeciesThinningSettings(ThinningModeName.AGE, typeDefinition, intensityDefinition, tree -> Double.valueOf(tree.age));
    }

    public static final SpeciesThinningSettings heightBasedScenarioSetting(String typeDefinition, double intensity) {
        return new SpeciesThinningSettings(ThinningModeName.HEIGHT, typeDefinition, "", intensity, tree -> tree.h);
    }

    public static final SpeciesThinningSettings heightBasedScenarioSetting(String typeDefinition, String intensityDefinition) {
        return new SpeciesThinningSettings(ThinningModeName.HEIGHT, typeDefinition, intensityDefinition, tree -> tree.h);
    }

    private final List<ThinningValueRange<Double>> intensityRanges;
    private final List<ThinningValueRange<ThinningType>> typeRanges;
    private final ThinningModeName mode;
    private final double intensityDefault;
    private final Function<Tree, Double> attributeExtractor;
    private final String intensityDefinition;
    private final String typeDefinition;

    private SpeciesThinningSettings(ThinningModeName mode, String typeDefinition, String intensityDefinition, Function<Tree, Double> attributeExtractor) {
        this(mode, typeDefinition, intensityDefinition, ModerateThinning.defaultThinningFactor, attributeExtractor);
    }

    private SpeciesThinningSettings(
            ThinningModeName mode,
            String typeDefinition,
            String intensityDefinition,
            double intensityDefault,
            Function<Tree, Double> attributeExtractor) {
        super();
        this.mode = mode;
        this.intensityDefinition = intensityDefinition;
        this.typeDefinition = typeDefinition;
        this.intensityRanges = thinningFactorParser.parseDefinition(intensityDefinition);
        this.typeRanges = thinningTypeParser.parseDefinition(typeDefinition);
        this.intensityDefault = intensityDefault;
        this.attributeExtractor = attributeExtractor;
    }

    public SpeciesThinningSettings with(ThinningModeName thinningModeName, String newTypeDefinition, String newIntensityDefinition) {
        return new SpeciesThinningSettings(thinningModeName, newTypeDefinition, newIntensityDefinition, intensityDefault, attributeExtractor);
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
        return firstFactorFoundFor(typeRanges, referenceTree).orElse(ThinningType.SingleTreeSelection);
    }

    /**
     *  intensity Factor to normal stand density 1.0 = normal stand density, no thinning set intensity = 0.0
     * 
     * @param referenceTree
     * @return intensity for the given tree
     */
    public double intensityFor(Tree referenceTree) {
        return firstFactorFoundFor(intensityRanges, referenceTree).orElse(intensityDefault);
    }

    private <T> Optional<T> firstFactorFoundFor(List<ThinningValueRange<T>> ranges, Tree tree) {
        return ranges.stream()
                .map(range -> range.factorFor(attributeExtractor.apply(tree)))
                .filter(Optional::isPresent).findFirst().orElse(Optional.empty());
    }

    @Override
    public String toString() {
        return "SpeciesThinningSettings{" + "mode=" + mode + ", intensityDefinition=" + intensityDefinition + ", typeDefinition=" + typeDefinition + '}';
    }
}
