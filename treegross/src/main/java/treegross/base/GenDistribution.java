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
/**
 * TreeGrOSS : class gendistribution generates a diameter distribution from
 * stand values This is the major class for several other classes of this
 * package http://treegross.sourceforge.net
 * 
* @version 30-NOV-2004
 * @author	Juergen Nagel
 */
package treegross.base;

public class GenDistribution {

    public void noDist(Stand st, int art, int alter, double dg, double hg, double gfl, boolean correct) throws Exception {
        double dgen, gsum, g;
        String nr;//="";
        gsum = 0.0;

        dgen = dg;
        g = Math.PI * (dgen / 200.0) * (dgen / 200.0);
        boolean added;//=true;
        do {
            gsum += g;
            Integer nox = st.ntrees + 2;
            nr = nox.toString();
            added = st.addtree(art, nr, alter, -1, dgen, hg, 0.0, 0.0, -9.0, 0.0, 0.0, 0.0, 0, 0, 0);
            if (correct) {
                checkGAndCorrectFactor(gfl, gsum, st);
            }
        } while (gsum < gfl && added);
    }

    /**
     * generates a diameter distribution from stand values
     *
     * @param st
     * @param art
     * @param alter
     * @param dg
     * @param hg
     * @param gfl
     * @param dmax
     * @param correct
     * @throws treegross.base.SpeciesNotDefinedException
     */
    public void weibull(Stand st, int art, int alter, double dg, double hg, double dmax, double gfl, boolean correct) throws SpeciesNotDefinedException {
        double dgen, gsum;
        String nr;//="";
        Species spx = new Species();
        spx.spDef = st.getSDM().getByCode(art);
        if (spx.spDef == null) {
            System.err.println("species definition NULL for species " + art);
        } else {
            if (spx.spDef.diameterDistributionXML == null) {
                System.err.println("spDef.diameterDistributionXML NULL for species " + art);
            }
        }
        spx.dg = dg;
        if(dmax > dg){
            spx.dmax = dmax;
        } else {
            spx.dmax = dg * 1.2;
        }
        gsum = 0.0;
        // 1.c Bäume erzeugen         
        do {
            FunctionInterpreter fi = new FunctionInterpreter();
            dgen = fi.getValueForSpecies(spx, spx.spDef.diameterDistributionXML, st.random);
            if (dgen < 7.01) {
                dgen = 7.01;
            }
            gsum +=  Math.PI * (dgen / 200.0) * (dgen / 200.0);
            Integer nox = st.ntrees + 2;
            nr = nox.toString();
            st.addtree(art, nr, alter, -1, dgen, 0.0, 0.0, 0.0, -9.0, 0.0, 0.0, 0.0, 0, 0, 0);
            if (correct) {
                checkGAndCorrectFactor(gfl, gsum, st);
            }
        } while (gsum < gfl);
    }

    /**
     * generates a diameter distribution from stand values, if dg-(dmax-dg) <
     * 7.0 else the diameters are created using a normal distribution, where
     * dmax-dg) is 3 sigma @param st @param art @param alter @param dg @param hg
     *
     * @param st
     * @param art
     * @param alter
     * @param dg
     * @param hg
     * @param dmax
     * @param gfl
     * @param correct
     * @throws java.lang.Exception
     */
    public void weibullEven(Stand st, int art, int alter, double dg, double hg,
            double dmax, double gfl, boolean correct) throws Exception {
        double dgen;//=7.0;
        double gsum = 0.0;
        String nr = "";
        int ngen = 0;

        if (dg - (dmax - dg) > 7.0) {
            nr = "";
            Species spx = new Species();

            //spx.spDef.loadSpeciesDefXML(art,st.programDir,st.FileXMLSettings);
            spx.spDef = st.getSDM().getByCode(art);

            spx.dg = dg;
            spx.dmax = dmax;
            gsum = 0.0;
            //double ggenerated;
            int ncycle = 0;
            int anzahl;//=0;
            // 
            do {
                // calculate number of trees 
                ncycle = ncycle + 1;
                anzahl = (int) (((gfl - gsum) * 0.8) / (Math.PI * Math.pow((dg / 200.0), 2.0)));
                //         System.out.println(" generate Anzahl der gewünschten Bäume"+anzahl);
                // Intervalllänge delta
                if (anzahl > 0) {
                    //double delta=1.0/(anzahl);
                    //            System.out.println(" generate Intervalllänge"+delta);
                    // 1.c Bäume erzeugen         
                    for (int i = 0; i < anzahl; i++) {
                        FunctionInterpreter fi = new FunctionInterpreter();
                        dgen = fi.getValueForSpecies(spx, spx.spDef.diameterDistributionXML, st.random);
                        gsum += Math.PI * (dgen / 200.0) * (dgen / 200.0);
                        Integer nox = st.ntrees + 2;
                        nr = nox.toString();
                        st.addtree(art, nr, alter, -1, dgen, 0.0, 0.0, 0.0, -9.0, 0.0, 0.0, 0.0, 0, 0, 0);
                        if (correct) {
                            checkGAndCorrectFactor(gfl, gsum, st);
                        }
                        ngen++;
                    }
                    //            System.out.println(" generate gfl und gsum "+ncycle+" "+gfl+"  "+gsum);
                }
            } while ((Math.abs(gfl - gsum) > 0.05) && (ncycle < 20) && (anzahl > 0));

        } else {  //generate from normal distribution
            //NormalDistributed ndis = new NormalDistributed();  
            do {
                dgen = dg + ((dmax - dg) * st.random.nextNormal(3)/*ndis.value(3.0)*/);
                if (dgen >= 1.0) {
                    gsum += Math.PI * (dgen / 200.0) * (dgen / 200.0);
                    Integer nox = st.ntrees + 2;
                    nr = nox.toString();
                    st.addtree(art, nr, alter, -1, dgen, 0.0, 0.0, 0.0, -9.0, 0.0, 0.0, 0.0, 0, 0, 0);
                    if (correct) {
                        checkGAndCorrectFactor(gfl, gsum, st);
                    }
                    ngen++;
                }
            } while (gfl > gsum);

        }
        // generate at least one tree             
        if (ngen == 0) {
            st.addtree(art, nr, alter, -1, dg, hg, 0.0, 0.0, -9.0, 0.0, 0.0, 0.0, 0, 0, 0);
        }
    }

    /* this method reduces the tree factor to avoid too heigh basal area generated*/
    private void checkGAndCorrectFactor(double gsoll, double gist, Stand st) {
        //reduce factor of tree
        if (gist > gsoll && st.tr[st.ntrees - 1].d > 0) {
            st.tr[st.ntrees - 1].fac = 1 - ((gist - gsoll) / (Math.PI * Math.pow(st.tr[st.ntrees - 1].d / 200.0, 2)));
            if (st.tr[st.ntrees - 1].fac < 0.00000001) {
                st.tr[st.ntrees - 1].fac = 0.00000001;
            }
            //System.out.println("tree factor corrected: "+st.tr[st.ntrees-1].fac+ "gist/gsoll: "+gist+"/"+gsoll);
        }
    }
}
