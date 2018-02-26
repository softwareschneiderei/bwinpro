/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package treegross.base;

/**
 *
 * @author jhansen
 */
public class TGTextFunction implements TGFunction {

    private String function = "";

    @Override
    public void init(String xmlText) {
        function = xmlText;
    }

    @Override
    public int getType() {
        return TGFunction.TEXT_FUNCTION;
    }

    @Override
    public String getFunctionText() {
        return function;
    }

    @Override
    public TGFunction clone() {
        TGTextFunction clone = new TGTextFunction();
        clone.init(function);
        return clone;
    }

    @Override
    public String toString() {
        return function;
    }
}
