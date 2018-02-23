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

public class ElSaltoIndexheight implements PlugInFunctionClass {

    double hbon = 0.0;
    double hb0 = .0;
    double hb1 = 0.0;
    double hb2 = 0.0;
    double hb3 = 0.0;
    double hb4 = 0.0;
    double hb5 = 0.0;
    double ehoch = 0.0;
    String rtyp = " ";
    double ihpot = 0.0;
    String info = "unknown";
    int numberOfFunctions = 7;

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
        double indexheight = 0.0;
        int codeNo = t.sp.code;
        initFunction(codeNo);

        if (t.age != 0) {
            if (rtyp.equals("H")) // Hossfeld IV Function
            {
                double Asi = 20.0;
                double d = Math.pow(hb1 / Asi, hb0);
                double r = Math.sqrt((Math.pow((t.sp.h100 - d), 2.0) + (4 * hb1 * t.sp.h100) / Math.pow(t.age, hb0)));
                indexheight = (t.si + d + r) / (2 + (4 * hb1 / (Math.pow((t.age + 5), hb0))) / (t.si - d + r));
                if (indexheight <= 0.0) {
                    indexheight = 0.01;
                }
            }
        } else {
            indexheight = 0.01;
        }
        //System.out.println("ElSaltoIndexHeight   "+indexheight);
        return indexheight;
    }

    /**
     * set the coefficients for the species
     */
    private void initFunction(int funNo) {
        if (funNo >= 20) {
            info = "1:cooperi Corral et al 2004";
            hb0 = 1.4328;
            hb1 = 2012.406;
            rtyp = "H";
        } else {
            switch (funNo) {
                case 1:
                    info = "1:cooperi Corral et al 2004";
                    hb0 = 1.4328;
                    hb1 = 2012.406;
                    rtyp = "H";
                    break;
                case 2:
                    info = "2:durangensis Corral et al 2004";
                    hb0 = 1.3900;
                    hb1 = 2066.069;
                    rtyp = "H";
                    break;
                case 3:
                    info = "3:engelmannii Corral et al 2004";
                    hb0 = 1.4125;
                    hb1 = 1926.363;
                    rtyp = "H";
                    break;
                case 4:
                    info = "4:leiophylla Corral et al 2004";
                    hb0 = 1.3873;
                    hb1 = 1751.79;
                    rtyp = "H";
                    break;
                case 5:
                    info = "5:teocote";
                    hb0 = 1.4653;
                    hb1 = 2164.62;
                    rtyp = "H";
                    break;
                case 6:
                    info = "6:herrerae Corral et al 2004";
                    hb0 = 1.4653;
                    hb1 = 2164.62;
                    rtyp = "H";
                    break;
                case 8:
                    info = "8:ayacahuite";
                    hb0 = 1.4020;
                    hb1 = 2420.60;
                    rtyp = "H";
                    break;
            }
        }
    }

    @Override
    public double getValueForSpecies(Species sp, String s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
