/* 
 * @(#) mortality.java  
 *  (c) 2002 -2007 Juergen Nagel, Northwest German Forest Research Station , 
 *      Grätzelstr.2, 37079 Göttingen, Germany
 *      E-Mail: Juergen.Nagel@nw-fva.de
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 */
package treegross.plugin;

import java.util.logging.Logger;
import treegross.base.FunctionInterpreter;
import treegross.base.PlugInMortality;
import treegross.base.Stand;

/**
 * Mortality.java calculates the mortaliy for a given influence zone. It expects
 * a class stand and processes all living trees in that stand. The specific
 * values for the critical C66 index were derived by the growth and yield data
 * of the NW-FVA for Northwest Germany. It is supposed that a tree can not live
 * anymore if the calculated c66 value is greater than the critical value. The
 * critical values are set in this method directly. The mortality class can be
 * exchanged for different regions via the PlugInMortality class.
 */
public class LithuaniaMortality implements PlugInMortality {
    private final static Logger LOGGER = Logger.getLogger(LithuaniaMortality.class.getName());

    @Override
    public void mortalityByInfluenceZone(Stand st) {
        /* Order all internal tree numbers of st.tr[] to an array treeNo[] by c66xy, where the
         * c66xy is higher than the critical value
         */
        FunctionInterpreter fi = new FunctionInterpreter();
        double bhdinc = 0.0;
        for (int i = 0; i < st.ntrees; i++) {
            if (st.tr[i].out < 1) {
                bhdinc = fi.getValueForTree(st.tr[i], st.tr[i].sp.spDef.diameterIncrementXML)*10000;
                double f = 1.0 / (1.0 + Math.exp(-(0.4694 + 0.1123 * st.tr[i].d + 3.7130 * (bhdinc / st.tr[i].d) - 2.5575 * (st.tr[i].h / st.tr[i].d))));
                double prob = (100.2152 / (Math.exp(2.6909 * Math.pow(f, 1.6433))))/100.0;
                System.out.println("testing Mort 2 "+st.tr[i].d+"  "+prob);
                if (Math.random() < prob ){
                    st.tr[i].out = st.year;
                    st.tr[i].outtype = 1;
                }
            }
        }
    }
}
