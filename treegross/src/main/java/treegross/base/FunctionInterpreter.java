/* http://www.nw-fva.de
 Version 07-11-2008

 (c) 2002 Juergen Nagel, Northwest German Forest Research Station, 
 Grätzelstr.2, 37079 Göttingen, Germany
 E-Mail: Juergen.Nagel@nw-fva.de
 
 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation.

 This program is distributed in the hope that it will be useful,
 but WITHOUT  WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 */
package treegross.base;

import org.nfunk.jep.*;
import treegross.random.RandomNumber;

/**
 *
 * @author nagel, modyfied jhansen
 */
public class FunctionInterpreter {

    public double getValueForTree(Tree t, TGFunction func) {
        if (func.getType() == TGFunction.CLASS_FUCNTION) {
            TGClassFunction cf = (TGClassFunction) func;
            return cf.getFunctionClass().getValueForTree(t, func.getFunctionText());
        } else {
            String function = func.getFunctionText();
            return getValueForTree(t, function);
        }
    }

    public double getValueForTree(Tree t, String func) {
        String function = func;
        JEP jep = new JEP();
        if (function.contains("t.d")) {
            jep.addVariable("t.d", t.d);
        }
        if (function.contains("t.h")) {
            jep.addVariable("t.h", t.h);
        }
        if (function.contains("t.age")) {
            jep.addVariable("t.age", t.age);
        }
        if (function.contains("t.cb")) {
            jep.addVariable("t.cb", t.cb);
        }
        if (function.contains("t.cw")) {
            jep.addVariable("t.cw", t.cw);
        }
        if (function.contains("t.c66c")) {
            jep.addVariable("t.c66c", t.c66c);
        }
        if (function.contains("t.c66")) {
            jep.addVariable("t.c66", t.c66);
        }
        if (function.contains("t.c66cxy")) {
            jep.addVariable("t.c66cxy", t.c66cxy);
        }
        if (function.contains("t.c66xy")) {
            jep.addVariable("t.c66xy", t.c66xy);
        }
        if (function.contains("t.out")) {
            jep.addVariable("t.out", t.out);
        }
        if (function.contains("t.si")) {
            jep.addVariable("t.si", t.si);
        }
        if (function.contains("t.ihpot")) {
            jep.addVariable("t.ihpot", t.ihpot);
        }
        if (function.contains("t.hinc")) {
            jep.addVariable("t.hinc", t.hinc);
        }
        if (function.contains("sp.hg")) {
            jep.addVariable("sp.hg", t.sp.hg);
        }
        if (function.contains("sp.dg")) {
            jep.addVariable("sp.dg", t.sp.dg);
        }
        if (function.contains("sp.h100")) {
            jep.addVariable("sp.h100", t.sp.h100);
        }
        if (function.contains("sp.year")) {
            jep.addVariable("sp.year", t.st.year);
        }
        if (function.contains("sp.BHD_STD")) {
            jep.addVariable("sp.BHD_STD", getBHD_STD(t));
        }
        if (function.contains("sp.Cw_dg")) {
            jep.addVariable("sp.Cw_dg", getCw_dg(t));
        }
        if (function.contains("sp.gha")) {
            jep.addVariable("sp.gha", t.sp.gha);
        }
        if (function.contains("st.gha")) {
            jep.addVariable("st.gha", t.st.bha);
        }
        jep.addStandardFunctions();
        jep.parseExpression(function);
        return jep.getValue();
    }

    public double getValueForSpecies(Species sp, TGFunction func, RandomNumber rn) {
        if (func.getType() == TGFunction.CLASS_FUCNTION) {
            TGClassFunction cf = (TGClassFunction) func;
            return cf.getFunctionClass().getValueForSpecies(sp, func.getFunctionText());
        } else {
            String function = func.getFunctionText();
            //RandomNumber zz = new RandomNumber();
            JEP jep = new JEP();

            if (function.contains("sp.dg")) {
                jep.addVariable("sp.dg", sp.dg);
            }
            if (function.contains("sp.h100")) {
                jep.addVariable("sp.h100", sp.h100);
            }
            if (function.contains("dmax")) {
                jep.addVariable("dmax", sp.dmax);
            }
            if (function.contains("random")) {
                jep.addVariable("random", rn.nextUniform());
            }

            jep.addStandardFunctions();
            jep.parseExpression(function);
            /*value = jep.getValue();
             return value;*/
            return jep.getValue();
        }
    }

    // calculates the Standard Error of the DBH of a species
    private double getBHD_STD(Tree t) {
        double sError = 0.0;
        double dm = 0.0;
        int ndg = 0;
        for (int i = t.st.ntrees - 1; i >= 0; i--) {
            if (t.code == t.st.tr[i].code && t.st.tr[i].out < 0) {
                dm += t.st.tr[i].d;
                ndg++;
            }
        }
        if (ndg > 0) {
            dm = dm / ndg;
            double sum = 0.0;
            for (int i = t.st.ntrees - 1; i >= 0; i--) {
                if (t.code == t.st.tr[i].code && t.st.tr[i].out < 0) {
                    sum += Math.pow((t.st.tr[i].d - dm), 2.0);
                }
            }
            if (ndg > 1) {
                sError = Math.sqrt(sum / (ndg - 1)) / Math.sqrt(ndg);
            } else {
                sError = 1;
            }
        }
        return sError;
    }

    /**
     * calculates the Standard Error of the DBH of a species
     */
    private double getCw_dg(Tree t) {
        Tree atree = new Tree();
        atree.sp = t.sp;
        atree.code = t.code;
        atree.d = t.sp.dg;
        atree.h = t.sp.hg;
        atree.cb = atree.calculateCb();
        atree.cw = atree.calculateCw();
        return atree.cw;
    }
}
