package treegross.dynamic.siteindex;

import java.time.Year;
import java.util.Iterator;
import java.util.stream.IntStream;

public class Projection implements Iterable<Year>{
    
    public String standName;
    public String treeSpecies;
    public String rcp;
    public int length;
    private final Year start;

    public Projection(Year start) {
        super();
        this.start = start;
    }

    @Override
    public Iterator<Year> iterator() {
        return IntStream.range(start.getValue(), start.getValue() + length).mapToObj(number -> Year.of(number)).iterator();
    }
}
