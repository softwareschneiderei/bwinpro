package treegross.base;

import java.util.Optional;

public class StandMetaData {
    public final String name;
    public final Optional<Integer> year;
    public final Optional<Double> area;

    public StandMetaData(String name, Optional<Integer> year, Optional<Double> area) {
        super();
        this.name = name;
        this.year = year;
        this.area = area;
    }
}
