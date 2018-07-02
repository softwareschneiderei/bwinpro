package treegross.base.thinning;

import static java.lang.Double.parseDouble;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static java.util.stream.Collectors.toList;

public class ThinningDefinitionParser {

    public List<ThinningFactorRange> parseDefinition(String thinningDefinition) throws NumberFormatException {
        return Arrays.stream(thinningDefinition.split(";"))
                .map(triple -> addThinningFactorRange(triple))
                .filter(range -> range.isPresent())
                .map(range -> range.get())
                .collect(toList());
    }

    private Optional<ThinningFactorRange> addThinningFactorRange(String triple) throws IllegalArgumentException, NumberFormatException {
        if (triple.isEmpty()) {
            return Optional.empty();
        }
        String[] values = triple.split("\\/");
        
        if (values.length != 3) {
            throw new IllegalArgumentException("Illegal thinning factor triple. We need exactly 3 decimal numbers separated by /. Got: " + triple);
        }
        return Optional.of(new ThinningFactorRange(
                parseDouble(values[0]),
                parseDouble(values[2]),
                parseDouble(values[1])));
    }
}
