/**
 * TreeGrOSS : class siteindex defines the forest site index information
 * http://treegross.sourceforge.net Version 07-DEZ-2012
 */
package treegross.plugin;

import treegross.base.*;

/**
 * TreeGrOSS : class contains the regional site index curves for ElSalto Mexico
 *
 * @author Juergen Nagel
 *
 */
//  public class ElSaltoSiteindex implements PlugInFunctionClass
public class ElSaltoTreeHeightIncrement implements PlugInFunctionClass {

    double hz1 = 0.0;
    double hz2 = 0.0;
    double hse = 0.0;
    double hb4 = 0.0;
    double hb5 = 0.0;
    double ehoch = 0.0;
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
        double hsx = 0.0;
        funNo = t.sp.spDef.code;
        initFunction(funNo);

        double kr = t.cw / 2.0; //crown radius
        double kl = t.h - t.cb; //crown length
        double cr = kl / t.cb; // crown ratio

        if (kl < 0.05) {
            kl = 0.05;
        }
        // caculate crown surface area
        double km = (Math.PI * kr / (6 * kl * kl)) * (Math.pow((4.0 * kl * kl + kr * kr), 1.5) - kr * kr * kr);
        double effect = hse;
//        if (randomEffect == false) effect=0.0;
//         Siteindex si= new Siteindex(t.st.modelRegion);
//         if (t.si>-9) t.sp.h100=si.indexheight(t.age,t.si,t.sp.spDef.siteIndex);
//         double ihpot=si.potheightgrowth(t.age,t.sp.h100,t.si,t.sp.spDef.siteIndex);

        if (funNo <= 5) {
            hsx = hz1 / (1.0 - ((1.0 - (hz1 / t.h)) * (Math.pow(t.d / (t.d + t.bhdinc), hz2)))) - t.h;
        }
        if (funNo == 6) {
            hsx = hz1 * (Math.pow(t.h / hz1, Math.pow(t.d / (t.d + t.bhdinc), hz2))) - t.h;
        }
        if (funNo == 7) {
            hsx = hz1 / (1.0 - ((1.0 - (hz1 / t.h)) * (Math.pow(t.d / (t.d + t.bhdinc), hz2)))) - t.h;
        }
        if (funNo == 8) {
            hsx = hz1 * (Math.pow(t.h / hz1, Math.pow(t.d / (t.d + t.bhdinc), hz2))) - t.h;
        }
        if (hsx < 0) {
            hsx = 0.0;
        }          //no negative height growth allowed
        // add growth to old values and adjust age
        return hsx;
    }

    /**
     * set the coefficients for the species
     */
    private void initFunction(int funNo) {
        numberOfFunctions = 9;
        if (funNo == 1) {
            info = "Pinus cooperi";
            hz1 = 28.2808;
            hz2 = 1.20086;
            hse = 0.068;
        }

        if (funNo == 2) {
            info = "Pinus durangensis";
            hz1 = 33.2986;
            hz2 = 1.0222;
            hse = 0.068;
        }

        if (funNo == 3) {
            info = "Pinus engelmanni";
            hz1 = 33.2986;
            hz2 = 1.0222;
            hse = 0.068;
        }

        if (funNo == 4) {
            info = "Pinus leiophylla";
            hz1 = 50.267;
            hz2 = 0.753;
            hse = 0.068;
        }

        if (funNo == 5) {
            info = "Pinus teocote";
            hz1 = 36.256;
            hz2 = 1.0213;
            hse = 0.068;
        }

        if (funNo == 6) {
            info = "Pinus herrerae";
            hz1 = 36.256;
            hz2 = 1.0213;
            hse = 0.068;
        }

        if (funNo == 8) {
            info = "Pinus ayacahuite";
            hz1 = 40.493;
            hz2 = 0.54;
            hse = 0.068;
        }

        if (funNo >= 30 && funNo < 40) {
            info = "Quercus spp";
            hz1 = 29.6333;
            hz2 = 1.1616;
            hse = 0.068;
        }

        if (funNo == 20 || funNo == 21) {
            info = "otras latifoliadas";
            hz1 = 26.2414;
            hz2 = 0.7629;
            hse = 0.068;
        }
        if (funNo >= 50 && funNo < 60) {
            info = "otras latifoliadas";
            hz1 = 26.2414;
            hz2 = 0.7629;
            hse = 0.068;
        }
    }

    @Override
    public double getValueForSpecies(Species sp, String s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
