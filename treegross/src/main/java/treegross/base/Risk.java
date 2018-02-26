/* 
 * @(#) Risk.java
 *  (c) 2010 Juergen Nagel, Northwest German Forest Research Station ,
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
package treegross.base;

/**
 * Risk model to a stand via PlugInRisk Adapter
 *
 * @author nagel
 */
class Risk implements PlugInRisk {

    /**
     * Applies the risk model to a stand
     *
     * @param st Waldzustandsbericht 2009 Hessen, speziell das Hess.Ried
     */
    @Override
    public void applyRisk(Stand st) {
        double rate;// = 0.002;
        for (int i = 0; i < st.ntrees; i++) {
            if (st.tr[i].out < 0 && st.tr[i].d >= 7.0) {
                rate = 0.002;
                if (st.tr[i].code < 113) {
                    rate = 0.002;
                }
                if (st.tr[i].code == 211) {
                    rate = 0.001;
                }
                if (st.tr[i].code == 511) {
                    rate = 0.004;
                }
                if (st.tr[i].code == 711) {
                    rate = 0.005;
                }
                if (st.random.nextUniform() <= rate * 5) {
                    st.tr[i].out = st.year;
                    st.tr[i].outtype = 1;
                }
            }
        }
    }
}
