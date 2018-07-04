package treegross.base;

/**
 *
 * @author jhansen
 */
public interface TGFunction {

    public static final int CLASS_FUCNTION = 1;
    public static final int TEXT_FUNCTION = 0;

    public void init(String xmlText);

    public String getFunctionText();

    public int getType();

    public TGFunction clone();

    @Override
    public String toString();
}
