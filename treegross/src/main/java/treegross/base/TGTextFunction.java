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

    private String function;
    
    public TGTextFunction() {
        this("");
    }

    public TGTextFunction(String function) {
        super();
        this.function = function;
    }

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
        return new TGTextFunction(function);
    }

    @Override
    public String toString() {
        return function;
    }

    @Override
    public boolean undefined() {
        return this.function.isEmpty();
    }
}
