package forestsimulator.util;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatters {
    
    private NumberFormatters() {
        super();
    }
    
    public static NumberFormat integerFormat() {
        NumberFormat f = NumberFormat.getInstance(new Locale("en", "US"));
        f.setMaximumFractionDigits(0);
        f.setMinimumFractionDigits(0);
        f.setGroupingUsed(false);
        return f;
    }
}
