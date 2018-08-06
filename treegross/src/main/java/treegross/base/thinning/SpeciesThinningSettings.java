package treegross.base.thinning;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import treegross.base.Tree;
import static treegross.base.thinning.ThinningDefinitionParser.thinningFactorParser;

public class SpeciesThinningSettings {
    
    public static final SpeciesThinningSettings ageBasedScenarioSetting(ThinningType type, double intensity) {
        return new SpeciesThinningSettings(ThinningModeName.AGE, type, "", intensity, tree -> Double.valueOf(tree.age));
    }

    public static final SpeciesThinningSettings ageBasedScenarioSetting(ThinningType type, String intensityDefinition) {
        return new SpeciesThinningSettings(ThinningModeName.AGE, type, intensityDefinition, tree -> Double.valueOf(tree.age));
    }

    public static final SpeciesThinningSettings heightBasedScenarioSetting(ThinningType type, double intensity) {
        return new SpeciesThinningSettings(ThinningModeName.HEIGHT, type, "", intensity, tree -> tree.h);
    }

    public static final SpeciesThinningSettings heightBasedScenarioSetting(ThinningType type, String intensityDefinition) {
        return new SpeciesThinningSettings(ThinningModeName.HEIGHT, type, intensityDefinition, tree -> tree.h);
    }

    private final List<ThinningValueRange<Double>> intensityRanges;
    private final ThinningModeName mode;
    private final ThinningType type;
    private final double intensityDefault;
    private final Function<Tree, Double> attributeExtractor;
    private final String intensityDefinition;

    private SpeciesThinningSettings(ThinningModeName mode, ThinningType type, String intensityDefinition, Function<Tree, Double> attributeExtractor) {
        this(mode, type, intensityDefinition, ModerateThinning.defaultThinningFactor, attributeExtractor);
    }

    private SpeciesThinningSettings(
            ThinningModeName mode,
            ThinningType type,
            String intensityDefinition,
            double intensityDefault,
            Function<Tree, Double> attributeExtractor) {
        super();
        this.intensityDefinition = intensityDefinition;
        intensityRanges = thinningFactorParser.parseDefinition(intensityDefinition);
        this.mode = mode;
        this.type = type;
        this.intensityDefault = intensityDefault;
        this.attributeExtractor = attributeExtractor;
    }

    public SpeciesThinningSettings with(ThinningModeName thinningModeName, ThinningType thinningType, String newIntensityDefinition) {
        return new SpeciesThinningSettings(thinningModeName, thinningType, newIntensityDefinition, intensityDefault, attributeExtractor);
    }

    public ThinningModeName getMode() {
        return mode;
    }
    
    public ThinningType type() {
        return type;
    }

    public String intensityDefinition() {
        return intensityDefinition;
    }

    /**
     *  intensity Factor to normal stand density 1.0 = normal stand density, no thinning set intensity = 0.0
     * 
     * @param referenceTree
     * @return intensity for the given tree
     */
    public double intensityFor(Tree referenceTree) {
        return firstFactorFoundFor(referenceTree).orElse(intensityDefault);
    }

    private Optional<Double> firstFactorFoundFor(Tree tree) {
        return intensityRanges.stream()
                .map(range -> range.factorFor(attributeExtractor.apply(tree)))
                .filter(Optional::isPresent).findFirst().orElse(Optional.empty());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.intensityRanges);
        hash = 97 * hash + Objects.hashCode(this.type);
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.intensityDefault) ^ (Double.doubleToLongBits(this.intensityDefault) >>> 32));
        hash = 97 * hash + Objects.hashCode(this.attributeExtractor);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SpeciesThinningSettings other = (SpeciesThinningSettings) obj;
        if (Double.doubleToLongBits(this.intensityDefault) != Double.doubleToLongBits(other.intensityDefault)) {
            return false;
        }
        if (!Objects.equals(this.intensityRanges, other.intensityRanges)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        return Objects.equals(this.attributeExtractor, other.attributeExtractor);
    }

    @Override
    public String toString() {
        return "SpeciesThinningSettings{" + "mode=" + mode + ", type=" + type + ", intensityDefinition=" + intensityDefinition + '}';
    }
}
