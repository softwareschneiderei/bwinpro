/**
 * TreeGrOSS : class siteindex defines the forest site index information
 * http://treegross.sourceforge.net Version 07-DEZ-2012
 */
package treegross.plugin;

import treegross.base.*;
import treegross.random.RandomNumber;

/**
 * TreeGrOSS : class contains the regional site index curves for ElSalto Mexico
 *
 * @author Juergen Nagel
 *
 */
//  public class ElSaltoSiteindex implements PlugInFunctionClass
public class ElSaltoTreeDBHIncrement implements PlugInFunctionClass {

    double dz1 = 0.0;
    double dz2 = 0.0;
    double dse = 0.0;
    double dz0 = 0.0;
    double dz3 = 0.0;
    double dz5 = 0.0;
    double dz4 = 0.0;
    String rtyp = " ";
    String info = "unknown";
    int numberOfFunctions = 7;
    int funNo = 1;

    @Override
    public String getName(int funNo) {
        initFunction(funNo);
        return info;
    }

    /**
     * this returns the indexheight at the age of 100 years
     *
     * @param t
     * @param fun
     * @return a calculated value for functuin fun and tree t
     */
    @Override
    public double getValueForTree(Tree t, String fun) {
        double dsx;
        funNo = t.sp.spDef.code;
        initFunction(funNo);
         //RandomNumber zz = new RandomNumber();         
        //NormalDistributed ndis = new NormalDistributed();

        RandomNumber rn = new RandomNumber(RandomNumber.PSEUDO);

        double kr = t.cw / 2.0; //crown radius
        double kl = t.h - t.cb; //crown length
        double cr = kl / t.cb; // crown ratio

        // if crown length is set to a minimum of 5 cm
        if (kl < 0.05) {
            kl = 0.05;
        }

        // caculate crown surface area
        double km = (Math.PI * kr / (6 * kl * kl)) * (Math.pow((4.0 * kl * kl + kr * kr), 1.5) - kr * kr * kr);
        double effect = dse;
 //        if (randomEffect == false) effect=0.0;

         // if the variable st.distanceDependent is true, then the competition c66 and c66c is
        // caculated by the influence zone and not for the total area
        double xc66 = t.c66;
        double xc66c = t.c66c;
        if (t.st.distanceDependent == true) {
            xc66 = t.c66xy;
            xc66c = t.c66cxy;
        }
        // basal area increment, see Bwinpro; variables para El Salto
        double ant = t.sp.gha / t.st.bha;
        double H100 = t.sp.h100;

        dsx = Math.exp(dz0 + dz1 * Math.log(km) + dz2 * Math.log(H100) + dz3 * xc66
                + dz4 * xc66c + dz5 * ant + effect * rn.nextNormal(1)/*ndis.value(1.0)*/);
        dsx = dsx / 10000.0;
        if (dsx < 0) {
            dsx = 0; // no negative increment allowed
        }
        // convert basal area in diameter increment
        // dsx=(2.0*Math.sqrt((Math.PI*Math.pow((t.d/2.0),2.0)+dsx)/Math.PI) - t.d)*0.833;

        return dsx;
    }

    /**
     * set the coefficients for the species
     */
    private void initFunction(int funNo) {
        if (funNo == 1) {
            info = "cooperi";
            dz0 = 2.848;
            dz1 = 0.105;
            dz2 = 0.378;
            dz3 = -0.616;
            dz4 = -3.635;
            dz5 = 0.004;
            dse = 0.0;
        }

        if (funNo == 2) {
            info = "durangensis";
            dz0 = 1.174;
            dz1 = 0.333;
            dz2 = 0.575;
            dz3 = -0.358;
            dz4 = -1.701;
            dz5 = 0.006;
            dse = 0.0;
        }

        if (funNo == 3) {
            info = "engelmannii";
            dz0 = 1.174;
            dz1 = 0.333;
            dz2 = 0.575;
            dz3 = -0.358;
            dz4 = -1.701;
            dz5 = 0.006;
            dse = 0.0;
        }

        if (funNo == 4) {
            info = "leiophylla";
            dz0 = 0.775;
            dz1 = 0.374;
            dz2 = 0.878;
            dz3 = -0.865;
            dz4 = 0.0;
            dz5 = 0.0;
            dse = 0.0;
        }

        if (funNo == 5) {
            info = "teocote";
            dz0 = 2.774;
            dz1 = 0.004;
            dz2 = 0.059;
            dz3 = -0.388;
            dz4 = -1.598;
            dz5 = 0.007;
            dse = 0.0;
        }

        if (funNo == 6) {
            info = "herrerae";
            dz0 = 2.774;
            dz1 = 0.004;
            dz2 = 0.059;
            dz3 = -0.388;
            dz4 = -1.598;
            dz5 = 0.007;
            dse = 0.0;
        }

        if (funNo == 8) {
            info = "ayacahuite";
            dz0 = 2.774;
            dz1 = 0.004;
            dz2 = 0.059;
            dz3 = -0.388;
            dz4 = -1.598;
            dz5 = 0.007;
            dse = 0.0;
        }

        if (funNo == 18) {
            info = "Quercus sp";
            dz0 = 1.738;
            dz1 = 0.391;
            dz2 = 0.384;
            dz3 = -0.404;
            dz4 = 0.0;
            dz5 = 0.0;
            dse = 0.0;
        }

        if (funNo == 20 || funNo == 21) {
            info = "otras latifoliadas";
            dz0 = 1.595;
            dz1 = 0.439;
            dz2 = 0.342;
            dz3 = -0.074;
            dz4 = 0.0;
            dz5 = 0.0;
            dse = 0.0;
        }
        if (funNo >= 50 || funNo < 60) {
            info = "otras latifoliadas";
            dz0 = 1.595;
            dz1 = 0.439;
            dz2 = 0.342;
            dz3 = -0.074;
            dz4 = 0.0;
            dz5 = 0.0;
            dse = 0.0;
        }
        if (funNo >= 30 && funNo < 40) {
            info = "Quercus sp";
            dz0 = 1.738;
            dz1 = 0.391;
            dz2 = 0.384;
            dz3 = -0.404;
            dz4 = 0.0;
            dz5 = 0.0;
            dse = 0.0;
        }

    }

    @Override
    public double getValueForSpecies(Species sp, String s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
