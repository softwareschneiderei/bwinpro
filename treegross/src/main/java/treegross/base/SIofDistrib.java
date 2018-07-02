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

public class SIofDistrib {
    private final FunctionInterpreter fi = new FunctionInterpreter();

    /**
     * calcultes the site index of a generated a diameter distribution from
     * stand values it uses all trees with no site index information at this
     * point, parameters passed: the stand, species code, age, mean quadratic
     * diameter [cm] and height [m] of dg
     *
     * @param st
     * @param code
     * @param alter
     * @param dg
     * @param hg
     */
    public void si(Stand st, int code, int alter, double dg, double hg) {
        double d100;//=0;
        double h100;//=0;
        // Sort trees by diameter      
        st.sortbyd();
        double size;// = 0.0;
        size = st.area();
        int n100 = (int) (100 * size); // number of trees of the 100 strongest
                                       // depends on the size of the stand
        n100 = Math.max(n100, 1);      // if very small take at least on tree
        double gsum = 0.0; // sum of basal area
        int n = 0; // counter
        int i = 0;
        do //only if the same species code, all trees with site index -9, and living
        // the restiction to only taking trees with site index -9 is because in SimWald
        // several different stands are generated
        {
            if (st.tr[i].code == code && st.tr[i].si == -9 && st.tr[i].isLiving()) {
                n++;
                gsum += Math.PI * Math.pow(st.tr[i].d / 200, 2);
            }
            i++;
        } while (i < st.ntrees && n < n100);
        d100 = 200 * Math.sqrt(gsum / (Math.PI * n));  // calculate d100 of those trees 
        Species species = st.speciesFor(code).orElse(st.sp[0]);
        Tree tree = new Tree();
        tree.d = d100;
        tree.sp = species;
        tree.sp.dg = dg;
        tree.sp.hg = hg;
        tree.age = alter;
        h100 = fi.getValueForTree(tree, tree.sp.spDef.uniformHeightCurveXML);
        tree = new Tree();
        tree.sp = species;
        tree.sp.h100 = h100;
        tree.age = alter;
        double siteindex = fi.getValueForTree(tree, species.spDef.siteindexXML);
        // assign all trees an individual site index
        for (i = 0; i < st.ntrees; i++) {
            if (st.tr[i].si == -9 && st.tr[i].code == code) {
                st.tr[i].si = siteindex;
            }
        }
    }
}
