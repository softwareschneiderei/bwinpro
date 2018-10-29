package treegross.base;

/**
 *  Wraps the site index value for type safety
 */
public class SiteIndex {
    public static final SiteIndex undefined = si(-9.0);

    public final double value;

    private SiteIndex(double value) {
        this.value = value;
    }
    
    public static SiteIndex si(double value) {
        return new SiteIndex(value);
    }

    public boolean undefined() {
        return value <= -9.0;
    }

    public boolean defined() {
        return value > 0;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.value) ^ (Double.doubleToLongBits(this.value) >>> 32));
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
        final SiteIndex other = (SiteIndex) obj;
        return Double.doubleToLongBits(this.value) == Double.doubleToLongBits(other.value);
    }

    @Override
    public String toString() {
        return "SiteIndex{" + "value=" + value + '}';
    }
}
