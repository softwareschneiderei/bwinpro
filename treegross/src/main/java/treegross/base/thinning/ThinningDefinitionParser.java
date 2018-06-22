package treegross.base.thinning;

import java.util.ArrayList;
import java.util.List;

public class ThinningDefinitionParser {

    public List<ThinningFactorRange> parseDefinition(String thinningDefinition) throws NumberFormatException {
        List<ThinningFactorRange> result = new ArrayList<>();
        if (thinningDefinition.length() > 4) {
            String[] tokens = thinningDefinition.split(";");
            // added by jhansen
            int end = tokens.length / 3;

            for (int i = 0; i < end; i++) {
                ThinningFactorRange range = new ThinningFactorRange(
                        Double.parseDouble(tokens[i * 3]),
                        Double.parseDouble(tokens[i * 3 + 2]),
                        Double.parseDouble(tokens[i * 3 + 1]));
                result.add(range);
            }
        }
        return result;
    }
}
