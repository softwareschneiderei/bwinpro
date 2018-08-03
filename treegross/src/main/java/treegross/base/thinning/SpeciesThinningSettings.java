package treegross.base.thinning;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import treegross.base.Tree;
import static treegross.base.thinning.ThinningDefinitionParser.thinningFactorParser;

public class SpeciesThinningSettings {
    
    public static final SpeciesThinningSettings ageBasedScenarioSetting(ThinningType type, double intensity) {
        return new SpeciesThinningSettings(type, "", intensity, tree -> Double.valueOf(tree.age));
    }

    public static final SpeciesThinningSettings ageBasedScenarioSetting(ThinningType type, String intensityDefinition) {
        return new SpeciesThinningSettings(type, intensityDefinition, tree -> Double.valueOf(tree.age));
    }

    public static final SpeciesThinningSettings heightBasedScenarioSetting(ThinningType type, double intensity) {
        return new SpeciesThinningSettings(type, "", intensity, tree -> tree.h);
    }

    public static final SpeciesThinningSettings heightBasedScenarioSetting(ThinningType type, String intensityDefinition) {
        return new SpeciesThinningSettings(type, intensityDefinition, tree -> tree.h);
    }

    private final List<ThinningValueRange<Double>> intensityRanges;
    private final ThinningType type;
    private final double intensityDefault;
    private final Function<Tree, Double> attributeExtractor;

    private SpeciesThinningSettings(ThinningType type, String intensityDefinition, Function<Tree, Double> attributeExtractor) {
        this(type, intensityDefinition, ModerateThinning.defaultThinningFactor, attributeExtractor);
    }

    private SpeciesThinningSettings(ThinningType type, String intensityDefinition, double intensityDefault, Function<Tree, Double> attributeExtractor) {
        super();
        intensityRanges = thinningFactorParser.parseDefinition(intensityDefinition);
        this.type = type;
        this.intensityDefault = intensityDefault;
        this.attributeExtractor = attributeExtractor;
    }

    /**
     * Static thinning type for compatibility
     * @return the configured thinning type
     */
    public ThinningType type() {
        return type;
    }

    /**
     * TODO: Think about how to choose the thinning type in treatment to allow
     *       dynamic thinning depending on the stand 
     * @param normTree
     * @return 
     */
    public ThinningType typeFor(Tree normTree) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *  intensity Factor to normal stand density 1.0 = normal stand density, no thinning set intensity = 0.0
     * 
     * @param normTree
     * @return intensity for the given tree
     */
    public double intensityFor(Tree normTree) {
        return firstFactorFoundFor(normTree).orElse(intensityDefault);
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
}
