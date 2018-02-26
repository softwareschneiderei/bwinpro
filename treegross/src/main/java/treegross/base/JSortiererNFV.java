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

import java.beans.*;
import java.io.Serializable;

/**
 * @author J. Nagel
 */
public class JSortiererNFV extends Object implements Serializable {

    /**
     * number of avaiable functions
     */
    int numberOfFunctions;
    /**
     * function information
     */
    String funInfo;
    /**
     * function number NFV
     */
    int funNo;
    /**
     * diamter at breast height [cm]
     */
    double dbh;
    /**
     * mh height where dbh is measured [m]
     */
    double mh;
    /**
     * dTop upper diameter (optional, if > dbh) [cm]
     */
    double dTop;
    /**
     * mhTop height where dTop is measured [m]
     */
    double mhTop;
    /**
     * height total tree height [m]
     */
    double height;

    /**
     * Assortment: start at height [m]
     */
    double asStarth;
    /**
     * Assortment: end at height [m]
     */
    double asEndh;
    /**
     * Assortment: minimum average diameter of piece [cm]
     */
    double asMinAvD;
    /**
     * Assortment: maximum average diameter of piece [cm]
     */
    double asMaxAvD;
    /**
     * Assortment: minimum top diameter of piece [cm]
     */
    double asMinTopD;
    /**
     * Assortment: maximum top diameter of piece [cm]
     */
    double asMaxTopD;
    /**
     * Assortment: minimum lengths of piece [m]
     */
    public double asMinLen;
    /**
     * Assortment: maximum lengths of piece [m]
     */
    public double asMaxLen;
    /**
     * Assortment: intervall [m]
     */
    public double asIntervall;
    /**
     * Assortment: additional Length Percent
     */
    public double asAddLenPer;
    /**
     * Assortment: additional Length absolut [cm]
     */
    public double asAddLenAbs;

    /**
     * logFound control if assortment is in log
     */
    public boolean logFound;
    /**
     * Length of log
     */
    public double logLength;
    /**
     * top diameter of log
     */
    public double logTopD;
    /**
     * middle diameter of log
     */
    public double logMidD;
    /**
     * additional length
     */
    public double logAddLength;
    /**
     * regional model plugIn
     */
    String modelRegion = "default";

    TaperFunction tp = null;

    public static final String PROP_SAMPLE_PROPERTY = "sampleProperty";

    private String sampleProperty;

    private final PropertyChangeSupport propertySupport;

    public JSortiererNFV(String region) {
        propertySupport = new PropertyChangeSupport(this);
        modelRegion = region;
        tp = new TaperFunction(modelRegion);
    }

    public String getSampleProperty() {
        return sampleProperty;
    }

    public void setSampleProperty(String value) {
        String oldValue = sampleProperty;
        sampleProperty = value;
        propertySupport.firePropertyChange(PROP_SAMPLE_PROPERTY, oldValue, sampleProperty);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    /**
     * getAssortment, defines the assortment to look for in the tree Parameters
     * to pass: ba = species code NFV du_cm = diameter at breast height [cm]
     * hu_m = dbh measured at height [m] (is 13 m) do_cm = upper diameter [cm]
     * (optional, if < dbh) ho_m = dTop measured at height [m] hoehe_m = tree
     * height [m]
     * @param function
     * @param du_cm
     * @param hu_m
     * @param do_cm
     * @param ho_m
     * @param hoehe_m
     * @param hs_m
     * @param he_m
     * @param dmmin_cm
     * @param dmmax_cm
     * @param dzmin_cm
     * @param dzmax_cm
     * @param laengmin_m
     * @param laengmax_m
     * @param intervall_m
     * @param laengzpro
     * @param laengzab_m
     * @return 
     */
    public boolean getAssortment(int function, double du_cm, double hu_m, double do_cm, double ho_m, double hoehe_m,
            double hs_m, double he_m, double dmmin_cm, double dmmax_cm, double dzmin_cm,
            double dzmax_cm, double laengmin_m, double laengmax_m, double intervall_m,
            double laengzpro, double laengzab_m) {
        funNo = function;
        dbh = du_cm;
        mh = hu_m;
        dTop = do_cm;
        mhTop = ho_m;
        height = hoehe_m;
        if (dTop > dbh) {
            dTop = 0.0;
            mhTop = 0.0;
        }
        asStarth = hs_m;
        asEndh = he_m;
        asMinAvD = dmmin_cm;
        asMaxAvD = dmmax_cm;
        asMinTopD = dzmin_cm;
        asMaxTopD = dzmax_cm;
        asMinLen = laengmin_m;
        asMaxLen = laengmax_m;
        asIntervall = intervall_m;
        asAddLenPer = laengzpro;
        asAddLenAbs = laengzab_m;
        return findLog();

    }

    /*
     * Sort Routine which finds log which fullfills 
     * all criteria and is as long as possible
     */
    boolean findLog() {
        logFound = true;
        logTopD = asMinTopD;
        double ho;//=0.0;
        logMidD = 0.0;
        logLength = 0.0;
        double hu = asStarth;
        // if assortment minimum top diameter less than dbh than logFound = False
        if (asMinTopD > dbh || asStarth > asEndh) {
            logFound = false;
        } else {
            // find height of assortment minimum top diameter and store to ho               
            ho = tp.getLengthEst(funNo, dbh, height, asMinTopD);
            // correct height if asEndh is smaller as ho
            if (ho >= asEndh) {
                ho = asEndh;
            }
            logLength = ho - asStarth;
            // correct for additional length
            if (asAddLenPer > 0.0) {
                logAddLength = logLength * (asAddLenPer / 100.0);
            } else {
                logAddLength = asAddLenAbs;
            }
            // check assortment mid diameter and store to dmid               
            logMidD = tp.getDiameterEst(funNo, dbh, height, (asStarth + (logLength / 2.0)), 0, 0);
            // if dmid is below the assortment minimum diameter then adjust and look for height
            // of that required minimum mid diameter
            if (logMidD < asMinAvD) {
                logMidD = asMinAvD;
                ho = tp.getLengthEst(funNo, dbh, height, asMinAvD);
                logLength = (ho - asStarth) * 2.0;
                logTopD = tp.getDiameterEst(funNo, dbh, height, asStarth + logLength, 0, 0);
                if (asAddLenPer > 0.0) {
                    logAddLength = logLength * (asAddLenPer / 100.0);
                } else {
                    logAddLength = asAddLenAbs;
                }
            }
            // now dmid and dTop should fit the minimum requirement and we have to check if
            // the length is to long, if true we reduce the length to asMaxLen 
            if (logLength > asMaxLen) {
                logLength = asMaxLen;
                ho = asStarth + logLength;
                logTopD = tp.getDiameterEst(funNo, dbh, height, ho, 0, 0);
                logMidD = tp.getDiameterEst(funNo, dbh, height, (asStarth + (logLength / 2.0)), 0, 0);
                if (asAddLenPer > 0.0) {
                    logAddLength = logLength * (asAddLenPer / 100.0);
                } else {
                    logAddLength = asAddLenAbs;
                }
            }
            // I think there are no further adjustments possible if we assume that the stem taper is
            // regular and the diameter gets always smaller if we move up the stem
            if (logTopD < asMinTopD || logTopD > asMaxTopD) {
                logFound = false;
            }
            if (logMidD < asMinAvD || logMidD > asMaxAvD) {
                logFound = false;
            }
            if (logLength < asMinLen || logLength > asMaxLen) {
                logFound = false;
            }
        }
        return logFound;
    }

    /**
     * Middle diameter of log without bark
     *
     * @return a diameter
     */
    public double getADm_cm() {
        return tp.getDiameterEst(funNo, dbh, height, asStarth + logLength / 2.0, 1, 0);
    }

    /**
     * Top diameter of log without bark
     *
     * @return a diameter
     */
    public double getADz_cm() {
        return tp.getDiameterEst(funNo, dbh, height, asStarth + logLength, 1, 0);
    }

    /**
     * Bottom diameter of log without bark
     *
     * @return a diameter
     */
    public double getADb_cm() {
        return tp.getDiameterEst(funNo, dbh, height, asStarth, 1, 0);
    }

    /**
     * Length of log without bark
     *
     * @return a length
     */
    public double getALae_m() {
        return logLength;
    }

    /**
     * Position of log from ground
     *
     * @return height above ground
     */
    public double getAPos_m() {
        return asStarth;
    }

    /**
     * Volume of log with bark
     *
     * @return a volume
     */
    public double getAVolmR_m3() {
        return tp.getCumVolume(funNo, dbh, height, asStarth + logLength, 0, 0)
                - tp.getCumVolume(funNo, dbh, height, asStarth, 0, 0);
    }

    /**
     * Volume of log without bark
     *
     * @return a volume
     */
    public double getAVoloR_m3() {
        return tp.getCumVolume(funNo, dbh, height, asStarth + logLength, 1, 0)
                - tp.getCumVolume(funNo, dbh, height, asStarth, 1, 0);
    }

    /**
     * absolut additional length for log
     *
     * @return a length
     */
    public double getALaengzug_cm() {
        return logAddLength * 100.0;
    }

    /**
     * Volume below the beginning of the log
     *
     * @return a volume
     */
    public double getARestU_m3() {
        return tp.getCumVolume(funNo, dbh, height, asStarth, 0, 0);
    }

    /**
     * Volume below the beginning of the log
     *
     * @return a volume
     */
    public double getARestO_m3() {
        return tp.getCumVolume(funNo, dbh, height, height, 0, 0)
                - tp.getCumVolume(funNo, dbh, height, asStarth + logLength + logAddLength, 0, 0);
    }

    /**
     * Exact top diameter of log with bark added by jhansen
     *
     * @return a diameter
     */
    public double getADzB_cm() {
        if (logFound) {
            return logTopD;
        } else {
            return 0.0;
        }
    }

    /**
     * Exact middle diameter of log with bark added by jhansen
     *
     * @return a diameter
     */
    public double getADmB_cm() {
        if (logFound) {
            return logMidD;
        } else {
            return 0.0;
        }
    }

    /* Functions avaible */
    private void loadFunctionInfo(int funNo) {
        numberOfFunctions = tp.getNumberOfFunctions();
        funInfo = tp.getFunctionName(funNo);
        /*    if (funNo==0) { funInfo="beech , Brink (Schmidt 2001)";}
         if (funNo==1) { funInfo="oak , Brink (Schmidt 2001)";}
         if (funNo==2) { funInfo="spruce , Brink (Schmidt 2001)";}
         if (funNo==3) { funInfo="pine , Brink (Schmidt 2001)";}
         if (funNo==4) { funInfo="Douglas fir , Brink (Schmidt 2001)";}  */
    }

    /**
     * number of avaible functions
     *
     * @return number of functions
     */
    public int getNumberOfFunctions() {
        loadFunctionInfo(funNo);
        return numberOfFunctions;
    }

    /**
     * get function name
     *
     * @param funNo
     * @return the name of the function
     */
    public String getFunctionName(int funNo) {
        loadFunctionInfo(funNo);
        return funInfo;
    }

    /**
     * set model Region
     *
     * @param region
     */
    public void setModelRegion(String region) {
        modelRegion = region;
    }

    /**
     * volume by Huber, this is how the wood is sold
     *
     * @param d_cm
     * @param l_m
     * @return a volume
     */
    public double getVolumeHuber(double d_cm, double l_m) {
        double v = Math.PI * Math.pow((d_cm / 200.0), 2.0) * l_m;
        return v;
    }

    public int getFunNumber(int speciesCodeNds) {
        return tp.getFunctionNumber(speciesCodeNds);
    }
}
