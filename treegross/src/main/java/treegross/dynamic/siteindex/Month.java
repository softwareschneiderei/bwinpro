package treegross.dynamic.siteindex;

public class Month {
    /**
     * Month number from 1 to 12
     */
    public final int number;
    
    public Month(int number) {
        super();
        if (number < 1 || number > 12) {
            throw new IllegalArgumentException("Month must have a number between 1 and 12, given: " + number);
        }
        this.number = number;
    }
}
