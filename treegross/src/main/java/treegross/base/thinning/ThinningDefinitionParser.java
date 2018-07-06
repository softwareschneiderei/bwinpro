package treegross.base.thinning;

import static java.lang.Double.parseDouble;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import static java.util.stream.Collectors.toList;

public class ThinningDefinitionParser<V> {
    public static final ThinningDefinitionParser<Double> thinningFactorParser = new ThinningDefinitionParser<>(s -> parseDouble(s));
    public static final ThinningDefinitionParser<ThinningType> thinningTypeParser = new ThinningDefinitionParser<>(s -> ThinningType.forValue(0));

    private final Function<String, V> valueParser;
    
    public ThinningDefinitionParser(Function<String, V> valueParser) {
        this.valueParser = valueParser;
    }

    public List<ThinningValueRange<V>> parseDefinition(String thinningDefinition) throws NumberFormatException {
        return Arrays.stream(thinningDefinition.split(";"))
                .map(triple -> addThinningFactorRange(triple))
                .filter(range -> range.isPresent())
                .map(range -> range.get())
                .collect(toList());
    }

    private Optional<ThinningValueRange<V>> addThinningFactorRange(String triple) throws IllegalArgumentException, NumberFormatException {
        if (triple.isEmpty()) {
            return Optional.empty();
        }
        String[] values = triple.split("\\/");
        
        if (values.length != 3) {
            throw new IllegalArgumentException("Illegal thinning factor triple. We need exactly 3 decimal numbers separated by /. Got: " + triple);
        }
        return Optional.of(new ThinningValueRange(
                parseDouble(values[0]),
                parseDouble(values[2]),
                valueParser.apply(values[1])));
    }
}
