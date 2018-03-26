/**
 * TreeGrOSS : class taper function by Brink calculates volume and diameter
 * Version 11-NOV-2004
 */
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

import java.util.logging.Level;
import java.util.logging.Logger;

public class TaperFunction {

    /*double[] a; // array for parameter value
    double[] b;*/
    private String pluginClass = "treegross.base.TaperFunctionBySchmidt";
    //String info = "";

    private PlugInTaperFunction tapInstance;

    private final static Logger LOGGER = Logger.getLogger(TaperFunction.class.getName());

    public TaperFunction(String pluginClass) {
        initInstance(pluginClass);
    }

    private void initInstance(String pluginClass) {
        this.pluginClass = pluginClass;
        // jhansen: this is bad: so no real PlugIns are provided because every plugin has to be in treegross.base package
        String modelPlugIn = /*"treegross.base." + */pluginClass;
        try {
            tapInstance = (PlugInTaperFunction) Class.forName(modelPlugIn).newInstance();
            //LOGGER.log(Level.INFO, "TaperFunction created.");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            tapInstance = null;
            LOGGER.log(Level.SEVERE, "treegross", ex);
        }
    }

    public void setInstance(String pluginClass) {
        initInstance(pluginClass);
    }

    public PlugInTaperFunction getInstance() {
        return tapInstance;
    }

    /**
     * load Parameter values, spcode=species code according to Lower Saxony
     *
     * @param funNo
     */
    public void loadParameter(int funNo) {
        //System.out.println(" Laden der Parameter "+spcode+"  ");
        if (tapInstance != null) {
            tapInstance.loadParameter(funNo);
        } else {
            LOGGER.log(Level.SEVERE, "Plugin {0} for TaperFunction not instancinated!", pluginClass);
        }
        /*try {
         String modelPlugIn = "treegross.base." + modelRegion;
         PlugInTaperFunction tap = (PlugInTaperFunction) Class.forName(modelPlugIn).newInstance();
         tap.loadParameter(funNo);
         } catch (ClassNotFoundException e) {
         LOGGER.log(Level.SEVERE, "TaperFunction", e);
         } catch (IllegalAccessException e) {
         LOGGER.log(Level.SEVERE, "TaperFunction", e);
         } catch (InstantiationException e) {
         LOGGER.log(Level.SEVERE, "TaperFunction", e);
         }*/
    }

    public double barkreduce(int funNo, double d) {
        double bark = 0.0;
        if (tapInstance != null) {
            bark = tapInstance.barkreduce(funNo, d);
        } else {
            LOGGER.log(Level.SEVERE, "Plugin {0} for TaperFunction not instancinated!", pluginClass);
        }
        return bark;
        /*try {
         String modelPlugIn = "treegross.base." + modelRegion;
         PlugInTaperFunction tap = (PlugInTaperFunction) Class.forName(modelPlugIn).newInstance();
         bark = tap.barkreduce(funNo, d);
         } catch (ClassNotFoundException e) {
         LOGGER.log(Level.SEVERE, "TaperFunction", e);
         } catch (IllegalAccessException e) {
         LOGGER.log(Level.SEVERE, "TaperFunction", e);
         } catch (InstantiationException e) {
         LOGGER.log(Level.SEVERE, "TaperFunction", e);
         }
         return bark;*/
    }

    /**
     * finds the diameter at a given height Berechnet Schaftradius bei gegebener
     * stemheight h bei Rindabindex=1 wird die doppelte Rindenstärke vom
     * Durchmesser abgezogen, als Eingangsvariable für die Berechnung der
     * doppelten Rindenstärke wird der abgerundete Schaftdurchmesser benötigt
     * bei Forstindex=1 wird der Schaftdurchmesser mit Rinde auf ganze cm
     * abgerundet
     *
     * @param funNo
     * @param dbh
     * @param height
     * @param h
     * @param barkindex
     * @param sortindex
     * @return a diameter
     */
    public double getDiameterEst(int funNo, double dbh, double height, double h, int barkindex, int sortindex) {
        double diameter = 0.0;
        if (tapInstance != null) {
            diameter = tapInstance.getDiameterEst(funNo, dbh, height, h, barkindex, sortindex);
        } else {
            LOGGER.log(Level.SEVERE, "Plugin {0} for TaperFunction not instancinated!", pluginClass);
        }
        return diameter;
        /*try {
         String modelPlugIn = "treegross.base." + modelRegion;
         PlugInTaperFunction tap = (PlugInTaperFunction) Class.forName(modelPlugIn).newInstance();
         diameter = tap.getDiameterEst(funNo, dbh, height, h, barkindex, sortindex);
         } catch (ClassNotFoundException e) {
         LOGGER.log(Level.SEVERE, "TaperFunction", e);
         } catch (IllegalAccessException e) {
         LOGGER.log(Level.SEVERE, "TaperFunction", e);
         } catch (InstantiationException e) {
         LOGGER.log(Level.SEVERE, "TaperFunction", e);
         }
         return diameter;*/
    }

    /**
     * finds the height for a given diameter sucht Höhe zu einem vorgegebenen
     * Durchmesser: stemd
     *
     * @param funNo
     * @param dbh
     * @param height
     * @param stemd
     * @return height
     */
    public double getLengthEst(int funNo, double dbh, double height, double stemd) {
        double length = 0.0;
        if (tapInstance != null) {
            length = tapInstance.getLengthEst(funNo, dbh, height, stemd);
        } else {
            LOGGER.log(Level.SEVERE, "Plugin {0} for TaperFunction not instancinated!", pluginClass);
        }
        return length;
        /*try {
         String modelPlugIn = "treegross.base." + modelRegion;
         PlugInTaperFunction tap = (PlugInTaperFunction) Class.forName(modelPlugIn).newInstance();
         length = tap.getLengthEst(funNo, dbh, height, stemd);
         } catch (ClassNotFoundException e) {
         LOGGER.log(Level.SEVERE, "TaperFunction", e);
         } catch (IllegalAccessException e) {
         LOGGER.log(Level.SEVERE, "TaperFunction", e);
         } catch (InstantiationException e) {
         LOGGER.log(Level.SEVERE, "TaperFunction", e);
         }
         return length;*/
    }

    /**
     * calculates the taper functionvolume according to the modell of Pain and
     * Boyer 1996 / modell of Riemer et al. species code according to the code
     * of Lower Saxony
     *
     * @param funNo
     * @param dbh
     * @param height
     * @param h
     * @param barkindex
     * @param sortindex
     * @return a volume
     */
    public double getCumVolume(int funNo, double dbh, double height, double h, int barkindex, int sortindex) {
        double cumVolume = 0.0;
        if (tapInstance != null) {
            cumVolume = tapInstance.getCumVolume(funNo, dbh, height, h, barkindex, sortindex);
        } else {
            LOGGER.log(Level.SEVERE, "Plugin {0} for TaperFunction not instancinated!", pluginClass);
        }
        return cumVolume;
        /*try {
         String modelPlugIn = "treegross.base." + modelRegion;
         PlugInTaperFunction tap = (PlugInTaperFunction) Class.forName(modelPlugIn).newInstance();
         cumVolume = tap.getCumVolume(funNo, dbh, height, h, barkindex, sortindex);
         } catch (ClassNotFoundException e) {
         LOGGER.log(Level.SEVERE, "TaperFunction", e);
         } catch (IllegalAccessException e) {
         LOGGER.log(Level.SEVERE, "TaperFunction", e);
         } catch (InstantiationException e) {
         LOGGER.log(Level.SEVERE, "TaperFunction", e);
         }
         return cumVolume;*/
    }

    public int getNumberOfFunctions() {
        int nFunNr = 0;
        if (tapInstance != null) {
            nFunNr = tapInstance.getNumberOfFunctions();
        } else {
            LOGGER.log(Level.SEVERE, "Plugin {0} for TaperFunction not instancinated!", pluginClass);
        }
        return nFunNr;
        /*try {
         String modelPlugIn = "treegross.base." + modelRegion;
         PlugInTaperFunction tap = (PlugInTaperFunction) Class.forName(modelPlugIn).newInstance();
         nFunNr = tap.getNumberOfFunctions();
         } catch (ClassNotFoundException e) {
         LOGGER.log(Level.SEVERE, "TaperFunction", e);
         } catch (IllegalAccessException e) {
         LOGGER.log(Level.SEVERE, "TaperFunction", e);
         } catch (InstantiationException e) {
         LOGGER.log(Level.SEVERE, "TaperFunction", e);
         }
         return nFunNr;*/
    }

    public String getFunctionName(int funNo) {
        String funName = "not found";
        if (tapInstance != null) {
            funName = tapInstance.getFunctionName(funNo);
        } else {
            LOGGER.log(Level.SEVERE, "Plugin {0} for TaperFunction not instancinated!", pluginClass);
        }
        return funName;
        /*try {
         String modelPlugIn = "treegross.base." + modelRegion;
         PlugInTaperFunction tap = (PlugInTaperFunction) Class.forName(modelPlugIn).newInstance();
         funName = tap.getFunctionName(funNo);
         } catch (ClassNotFoundException e) {
         LOGGER.log(Level.SEVERE, "TaperFunction", e);
         } catch (IllegalAccessException e) {
         LOGGER.log(Level.SEVERE, "TaperFunction", e);
         } catch (InstantiationException e) {
         LOGGER.log(Level.SEVERE, "TaperFunction", e);
         }
         return funName;*/
    }

    public int getFunctionNumber(int speciesCode) {
        int funNo = 0;
        if (tapInstance != null) {
            funNo = tapInstance.getFunctionNumber(speciesCode);
        } else {
            LOGGER.log(Level.SEVERE, "Plugin {0} for TaperFunction not instancinated!", pluginClass);
        }
        return funNo;
        /*try {
         String modelPlugIn = "treegross.base." + modelRegion;
         PlugInTaperFunction tap = (PlugInTaperFunction) Class.forName(modelPlugIn).newInstance();
         funNo = tap.getFunctionNumber(speciesCode);
         } catch (ClassNotFoundException e) {
         LOGGER.log(Level.SEVERE, "TaperFunction", e);
         } catch (IllegalAccessException e) {
         LOGGER.log(Level.SEVERE, "TaperFunction", e);
         } catch (InstantiationException e) {
         LOGGER.log(Level.SEVERE, "TaperFunction", e);
         }
         return funNo;*/
    }
}
