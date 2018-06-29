package treegross.dynamic.siteindex;

import java.time.Month;
import java.time.Year;
import java.util.Iterator;
import java.util.stream.IntStream;

public class Projection implements Iterable<Year>{
    
    public String standName;
    public String treeSpecies;
    public final Month vegetationBeginMonth;
    public final Month vegetationEndMonth;
    public String rcp;
    public int length;

    public Projection() {
        this(Month.MARCH, Month.AUGUST);
    }
    
    public Projection(Month vegetationBegin, Month vegetationEnd) {
        super();
        this.vegetationBeginMonth = vegetationBegin;
        this.vegetationEndMonth = vegetationEnd;
    }

    @Override
    public Iterator<Year> iterator() {
        return IntStream.range(1, length).mapToObj(number -> Year.of(number)).iterator();
    }
}
