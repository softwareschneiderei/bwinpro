/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package treegross.base;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jhansen
 */
public class TGClassFunction implements TGFunction {
    private static final String prefix = "CLASS:";

    private PlugInFunctionClass fc;
    private String function;
    private final static Logger LOGGER = Logger.getLogger(TGClassFunction.class.getName());

    @Override
    public void init(String xmlText) {
        function = removePrefix(xmlText.trim());
        // so können die Plugins nur im Package treegross.plugin paziert werden
        String modelPlugIn = "treegross.plugin." + function;
        try {
            fc = (PlugInFunctionClass) Class.forName(modelPlugIn).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            LOGGER.log(Level.SEVERE, "function class loading: ", e);
        }
    }

    private String  removePrefix(String xmlText) {
        int m = xmlText.indexOf(prefix);
        if (m > -1) {
            return xmlText.substring(m + prefix.length());
        }
        return xmlText;
    }

    @Override
    public int getType() {
        return TGFunction.CLASS_FUCNTION;
    }

    public PlugInFunctionClass getFunctionClass() {
        return fc;
    }

    @Override
    public String getFunctionText() {
        return function;
    }

    @Override
    public TGFunction clone() {
        TGClassFunction clone = new TGClassFunction();
        clone.init(function);
        return clone;
    }

    @Override
    public String toString() {
        return "CLASS:" + function;
    }

    @Override
    public boolean undefined() {
        return function.isEmpty();
    }
}
