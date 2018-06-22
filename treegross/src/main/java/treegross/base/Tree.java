/** TreeGrOSS : class Tree defines the trees for class stand
 * http://treegross.sourceforge.net
 * Version 05-Apr-2005 */
/*   (c) 2002-2008 Juergen Nagel, Northwest German Forest Research Station , 
       Grätzelstr.2, 37079 Göttingen, Germany
       E-Mail: Juergen.Nagel@nw-fva.de
 
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
 */
package treegross.base;

import java.util.logging.Level;
import java.util.logging.Logger;
import treegross.base.thinning.HeightBasedThinning;
import treegross.random.RandomNumber;

/**
 * TreeGrOSS : class tree defines the trees for class stand
 */
public class Tree implements Cloneable {

    public long universalID = 0;
    /**
     * species Code according to Lower Saxony
     */
    public int code;
    /**
     * tree number
     */
    public String no;
    /**
     * age
     */
    public int age;
    /**
     * if living -1, else the year when died or taken out
     */
    public int out;
    /**
     * 0=standing, 1= fallen, 2=thinned, 3=harvested
     */
    public OutType outtype;
    /**
     * diameter, height, volume
     */
    public double d, h, v;
    /**
     * crown base and crown width
     */
    public double cb, cw;
    /**
     * relative tree position coordinates , z=vertical height of ground
     */
    public double x, y, z;
    /**
     * crop tree
     */
    public boolean crop;
    /**
     * temporary crop tree
     */
    public boolean tempcrop;
    /**
     * habitat tree
     */
    public boolean habitat;
    /**
     * competition index c66 c66xy c66c and c66cxy
     */
    public double c66, c66c, c66xy, c66cxy;
    /**
     * weight factor for concentric sample plots
     */
    public double fac;
    /**
     * site index
     */
    public double si;
    /**
     * reference to species
     */
    public Species sp;
    /**
     * reference to stand
     */
    public Stand st;
    /**
     * tree layer 1=upper, 2=middel, 3=lower
     */
    public int layer;
    /**
     * last diameter amd height increment
     */
    public double bhdinc, hinc;
    /**
     * year, tree grew into stand
     */
    public int year;
    /**
     * 0= unknown, 1=planting, 2=natural regeneration
     */
    public int origin;
    /**
     * remarks
     */
    public String remarks;
    public double hMeasuredValue;
    /**
     * Erlös pro Baum in Euro: getProceeds (get from auxilliaries.SingletreeAsset
     */
    public double proceeds;
    /**
     * erntekostenfreier Erlös pro Baum in Euro: getPwoh (get from auxilliaries.TreeValudation
     */
    public double pwohc;
    /**
     * Stammholzanteil in EFm: getSharelog (get from auxilliaries.TreeValudation
     */
    public double sharelog;
    /**
     * Kosten pro Baum in Euro: getCosts (get from auxilliaries.TreeValudation
     */
    public double costs;
    /**
     * X-Holzanteil: getShareXtimber (get from auxilliaries.TreeValudation
     */
    public double shareXtimber;
    /**
     * Totholz Zersetzungsgrad
     */
    public int zGrad = 0;
    /**
     * Volume of deadwood
     */
    public double volumeDeadwood = 0.0;
    /**
     * Volume of deadwood nature conservation
     */
    public double volumeDeadwoodConservation = 0.0;
    /**
     * Volume of harvested
     */
    public double volumeHarvested = 0.0;
    /**
     * Degree of Decay
     */
    public double degreeOfDecay = 0;
    /**
     * maximum number of neighbor trees
     */
    public final int maxNeighbor = 15;
    /**
     * neighbor tree indices in radius of 2*crowthwidth
     */
    public int[] neighbor = new int[maxNeighbor];
    /**
     * number of neighbor trees
     */
    public int nNeighbor;
    /**
     * group
     */
    public int group;
    public int ihpot;
// für experimetal version
    /**
     * width and height of light crown
     */
    public double cbLightCrown = -9;
    /**
     * width and height of light crown
     */
    public double cwLightCrown = -9;
    /**
     * year of removal in reality
     */
    int yearOfRemovalinReality = 0;
// for Viswin 
    /* Ober- u. Unterstand */ public int ou = 0;
    /**
     * mortality reason 1= thinned or harvested, 2= dry and standing, 3= wind
     * throw, 4= other
     */
    public int mortalityReason = 0;
    //for ried
    /**
     * index of vitality
     */    public double vitality = 1;

    public boolean outBySkidtrail = false;
    public int bioMass = 0; // kg   

    private final static Logger LOGGER = Logger.getLogger(Tree.class.getName());

    /**
     * empty tree contructor
     */
    public Tree() {
    }

    /**
     * Fill tree with data, fille missing information with negative value
     *
     * @param codex
     * @param nox
     * @param cbx
     * @param outx
     * @param outtypex
     * @param dx
     * @param agex
     * @param cwx
     * @param hx
     * @param six
     * @param facx
     * @param xx
     * @param tempCropTreex
     * @param zx
     * @param cropTreex
     * @param yx
     * @param habitatTreex
     * @param treeLayerx
     * @param volumeDeadwoodx
     * @param remarksx
     */
    public Tree(int codex, String nox, int agex, int outx, OutType outtypex, double dx, double hx, double cbx, double cwx,
            double six, double facx, double xx, double yx, double zx, boolean cropTreex, boolean tempCropTreex,
            boolean habitatTreex, int treeLayerx, double volumeDeadwoodx, String remarksx) {
        code = codex;
        no = nox;
        out = outx;
        outtype = outtypex;
        d = dx;
        h = hx;
        age = agex;
        cb = cbx;
        cw = cwx;
        si = six;
        fac = facx;
        x = xx;
        y = yx;
        z = zx;
        habitat = habitatTreex;
        crop = cropTreex;
        tempcrop = tempCropTreex;
        layer = treeLayerx;
        volumeDeadwood = volumeDeadwoodx;
        remarks = remarksx;
    }

    /**
     * this function clones the Tree the fields Stand and SpeciesDef will not be
     * cloned!!! The Stand and SpeciesDef must be added after cloning a Tree. if
     * Tree.clone() is used in Stand.clone() the new cloned Stand must be added
     * to the new cloned tree by writing adiditonal code!
     *
     * @return clone of this tree
     */
    @Override
    public Tree clone() {
        Tree clone = new Tree();
        clone.universalID = this.universalID;
        clone.age = this.age;
        clone.bhdinc = this.bhdinc;
        clone.c66 = this.c66;
        clone.c66c = this.c66c;
        clone.c66cxy = this.c66cxy;
        clone.c66xy = this.c66xy;
        clone.cb = this.cb;
        clone.cbLightCrown = this.cbLightCrown;
        //clone.ci= new Double(this.ci);
        clone.code = this.code;
        clone.costs = this.costs;
        clone.crop = this.crop;
        clone.cw = this.cw;
        clone.cwLightCrown = this.cwLightCrown;
        clone.d = this.d;
        clone.fac = this.fac;
        clone.h = this.h;
        clone.hMeasuredValue = this.hMeasuredValue;
        clone.habitat = this.habitat;
        clone.hinc = this.hinc;
        clone.layer = this.layer;
        clone.no = this.no;
        clone.origin = this.origin;
        clone.ou = this.ou;
        clone.out = this.out;
        clone.outtype = this.outtype;
        clone.proceeds = this.proceeds;
        clone.pwohc = this.pwohc;
        if (remarks != null) {
            clone.remarks = this.remarks;
        } else {
            clone.remarks = "";
        }
        clone.shareXtimber = this.shareXtimber;
        clone.sharelog = this.sharelog;
        clone.si = this.si;
        clone.sp = sp.clone();
        //clone.st has to be added in the super clone call
        clone.v = this.v;
        clone.x = this.x;
        clone.y = this.y;
        clone.year = this.year;
        clone.yearOfRemovalinReality = this.yearOfRemovalinReality;
        clone.z = this.z;
        clone.zGrad = this.zGrad;
        clone.volumeDeadwood = this.volumeDeadwood;
        clone.degreeOfDecay = this.degreeOfDecay;
        clone.vitality = vitality;
        clone.outBySkidtrail = this.outBySkidtrail;
        clone.bioMass = this.bioMass;
        clone.group = this.group;
        return clone;
    }

    public double calculateCw() {
        double cwerg;
        if (sp.spDef.crownwidthXML.getFunctionText().length() > 2 && d >= 7.0) {
            FunctionInterpreter fi = new FunctionInterpreter();
            cwerg = fi.getValueForTree(this, sp.spDef.crownwidthXML);
        } else {
            cwerg = 0.01;
        }
        //regeneration placeholder 
        if (d < 7.0) {
            double dmerk = this.d;
            double hmerk = this.h;
            double cbmerk = this.cb;
            this.d = 7.0;
            this.h = 8.0;
            this.cb = 2.0;
            FunctionInterpreter fi = new FunctionInterpreter();
            cwerg = fi.getValueForTree(this, sp.spDef.crownwidthXML);
            this.d = dmerk;
            this.h = hmerk;
            this.cb = cbmerk;
        }
        if (cwerg > 50) {
            LOGGER.log(Level.SEVERE,
                    "Calculate crown width ({0}/{1}/{2}): cw unrealistic {3} using old cw: {4}",
                    new Object[]{st.standname, no, d, cwerg, cw});
            cwerg = cw;
        }
        return cwerg;
    }

    public double calculateCwAtHeight(double hx) {
        double erg;
        if (d >= 7.0) {
            double cwAtHx = 0.0;
            double h66 = h - 2.0 * (h - cb) / 3.0;
            if (hx < h && hx > h66) {
                cwAtHx = Math.sqrt(1.5 * Math.pow((cw / 2.0), 2.0) * (h - hx) / (h - cb));
            }
            if (hx <= h66 && hx > cb) {
                cwAtHx = cw / 2.0;
            }
            erg = 2.0 * cwAtHx;
        } // else we are looking at the small trees representing a layer so they get crown width
        else {
            erg = cw;
        }
        return erg;
    }

    public double calculateCb() {
        double cberg = 0.0;
        if (sp.spDef.crownbaseXML.getFunctionText().length() > 4 && d >= 7) {
            FunctionInterpreter fi = new FunctionInterpreter();
            if (sp.h100 < 1.3 || Double.isNaN(sp.h100)) {
                sp.h100 = h;
            }
            cberg = fi.getValueForTree(this, sp.spDef.crownbaseXML);
        }
        if (d < 7.0) {
            cberg = h / 2.0;
        }
        if (cberg < 0.01) {
            cberg = 0.01;
        }
        if (Double.isNaN(cberg)) {
            boolean errout = false;
            if (st != null) {
                if (st.debug) {
                    errout = true;
                }
            }
            if (errout) {
                String msg = "calculated crown base is NaN for:" + st.standname + "/" + no + "\n\t"
                        + sp.spDef.crownbaseXML + " dbh: " + d + " height: " + h + " sp.h100: " + sp.h100;
                LOGGER.log(Level.WARNING, msg);
            }
            cberg = h / 2.0;
        }
        return cberg;
    }

    public double calculateVolume() {
        double erg = 0.0;
        if (d > 3.0 && h > 3.0) {
            FunctionInterpreter fi = new FunctionInterpreter();
            erg = fi.getValueForTree(this, sp.spDef.volumeFunctionXML);
            if (erg < 0.0) {
                erg = 0.0;
            }
            if (Double.isNaN(erg)) {
                erg = 0.0;
            }
        }
        return erg;
    }

    public double calculateDecay() {
        double erg;
        FunctionInterpreter fi = new FunctionInterpreter();
        erg = fi.getValueForTree(this, sp.spDef.decayXML);
        if (erg < 0.0) {
            erg = 0.0;
        }
        if (erg < 1.0) {
            volumeDeadwood = v * erg;
        }
        return erg;
    }

    public double calculateSiteIndex() {
        double erg;
        FunctionInterpreter fi = new FunctionInterpreter();
        erg = fi.getValueForTree(this, sp.spDef.siteindexXML);
        return erg;
    }

    public double calculateMaxBasalArea() {
        return new FunctionInterpreter().getValueForTree(this, sp.spDef.maximumDensityXML);
    }

    public void ageBasedMortality() {
        double ageindex = (1.0 * age / (1.0 * sp.spDef.maximumAge)) - 1.0;
        if (ageindex > st.random.nextUniform()) {
            out = st.year;
            outtype = OutType.FALLEN;
        }
    }

    /**
     * get moderate thinning factor for maximum basal area from setting
     *
     * @return the moderate thinning fctor
     */
    public double getModerateThinningFactor() {
        return new HeightBasedThinning(moderateThinning()).thinningFactorFor(this);
    }

    protected String moderateThinning() {
        return sp.spDef.moderateThinning;
    }

    public void setMissingData() {
        //if the first tree of a species is added there is no site index
        if (si <= -9.0 && sp.hbon <= 0.0) {
            if (sp.h100 <= 1.3 || Double.isNaN(sp.h100)) {
                sp.h100 = st.h100;
            }
            si = calculateSiteIndex();
        }
        if (si <= -9.0) {
            si = sp.hbon;
        }
        if (cb < 0.01) {
            cb = calculateCb();
        }
        if (cw < 0.01) {
            cw = calculateCw();
        }
        if (v <= 0.0) {
            v = calculateVolume();
        }
    }

    /**
     * grows a single tree for the length of up to 5 years, with and without
     * random effects. If the years is < 5 then the increment is set to
     * increment*years/5
     */
    void grow(int years, /*boolean randomEffects*/ RandomNumber rn) {
        FunctionInterpreter fi = new FunctionInterpreter();
        // Jugendwachstum, solange der BHD < 7.0 oder h < 1.3
        if (d < 7) {
            double jh1;
            double jh2;
            Tree atree = new Tree();
            atree.sp = sp;
            atree.code = sp.code;
            atree.si = si;
            atree.age = 30;
            jh1 = fi.getValueForTree(atree, sp.spDef.siteindexHeightXML);
            jh2 = jh1;
            jh1 = jh1 * ((Math.exp(age / 30.0) - 1.0) / (Math.exp(1.0) - 1.0));
            jh2 = jh2 * ((Math.exp((age + 5) / 30.0) - 1.0) / (Math.exp(1.0) - 1.0));
            hinc = jh2 - jh1;
            if (hinc < 0) {
                hinc = 0;
            }
            bhdinc = 0.0;
            double hd = 1.0;
            double dneu;
            if (sp.code < 200) {
                hd = 1.28;
            } else if (sp.code >= 200 && sp.code < 300) {
                hd = 1.40;
            } else if (sp.code >= 300 && sp.code < 400) {
                hd = 1.80;
            } else if (sp.code >= 400 && sp.code < 500) {
                hd = 1.20;
            } else if (sp.code >= 500 && sp.code < 600) {
                hd = 0.95;
            } else if (sp.code >= 600 && sp.code < 700) {
                hd = 0.85;
            } else if (sp.code >= 700 && sp.code < 800) {
                hd = 1.1;
            } else if (sp.code >= 800 && sp.code < 900) {
                hd = 0.95;
            }
            dneu = (h + (years * hinc / 5.0)) / hd;
            if (dneu > d) {
                d = dneu;
            }
        } else {
            bhdinc = fi.getValueForTree(this, sp.spDef.diameterIncrementXML);
            if (Double.isInfinite(bhdinc) || Double.isNaN(bhdinc) || bhdinc < 0) {
                boolean errout = false;
                if (st != null) {
                    st.debug = errout;
                }
                if (errout) {
                    LOGGER.log(Level.WARNING, "dbh increment is < 0, NaN or infinite for: {0}/{1}\n\tbhdinc: {2}\n\tc66cxy: {3} c66xy: {4}crown width {5}\n\tcrown base {6} species: {7} h100 for species: {8}", new Object[]{st.standname, no, bhdinc, c66cxy, c66xy, cw, cb, code, sp.h100});
                }
                bhdinc = 0;
            }

            //NormalDistributed ndis = new NormalDistributed();
            double effect;
            if (/*randomEffects*/rn.randomOn) {
                if (bhdinc <= 0.0) {
                    bhdinc = 0.0001;
                }
                //effect = sp.spDef.diameterIncrementError*ndis.value(1.0);
                effect = sp.spDef.diameterIncrementError * rn.nextNormal(1);
                bhdinc = Math.exp(Math.log(bhdinc) + effect);
            }
            if (bhdinc < 0.0) {
                bhdinc = 0.0;
            }

            bhdinc = 2.0 * Math.sqrt((Math.PI * Math.pow((d / 2.0), 2.0) + bhdinc * 10000.0) / Math.PI) - d;

            // grow height (hinc????):
            double ihpot_l = fi.getValueForTree(this, sp.spDef.potentialHeightIncrementXML);
            if (h / sp.h100 >= 1.0) {
                hinc = 1.0;
            } else {
                hinc = sp.h100 / h;
            }

            hinc = fi.getValueForTree(this, sp.spDef.heightIncrementXML);
            if (rn.randomOn) {
                effect = sp.spDef.heightIncrementError;
                hinc += effect * rn.nextNormal(1);
            }

            if (hinc > ihpot_l * 1.2) {
                hinc = ihpot_l * 1.2;   //ihpot*1.2 is max
            }
        }

        //no negative height growth allowed
        if (hinc < 0) {
            hinc = 0.0;
        }

        double ts = 0.2;
        if (st.timeStep > 0) {
            ts = 1.0 / st.timeStep;
        }

        h += years * hinc * ts;
        d += years * bhdinc * ts;
        age += years;
        v = calculateVolume();
    }

    void growBack(int years) {
        //System.out.println("grow back Tree");
        double ts = st.timeStep;
        if (out == st.year) {
            out = -1;
            outtype = OutType.STANDING;
        }
        RandomNumber rnOff = new RandomNumber(RandomNumber.OFF);
        if (out < 0) {
            Tree xtree;
            //explicitly set stand (not included in Tree.clone() -> Tree.grow(...) refers Stand.timeStep)
            xtree = this.clone();
            xtree.st = this.st;
            xtree.grow(years, rnOff);
            double dzvor = xtree.bhdinc;
            double hzvor = xtree.hinc;
            //System.out.println("grow back: d,h "+dzvor+"  "+hzvor);
            xtree = this.clone();
            xtree.st = this.st;
            xtree.h = xtree.h - years * hzvor / ts;
            xtree.d = xtree.d - years * dzvor / ts;
            xtree.age = xtree.age - years;
            FunctionInterpreter fi = new FunctionInterpreter();
            xtree.sp.h100 = xtree.sp.h100 - fi.getValueForTree(xtree, sp.spDef.potentialHeightIncrementXML);
            if (xtree.h < 1.3) {
                h = 4;
            }
            if (xtree.d < 1.0) {
                d = 1.0;
            }
            xtree.cw = xtree.calculateCw();
            xtree.cb = xtree.calculateCb();

            xtree.grow(years, rnOff);
            //System.out.println("grow back: d,h "+xtree.bhdinc+"  "+xtree.hinc);
            double newH = h - years * xtree.hinc / ts;
            if (newH < h) {
                h = newH;
            }
            double newD = d - years * xtree.bhdinc / ts;
            if (newD < d) {
                d = newD;
            }
            if (d < 7.0 || h < 5.0) {
                d = 3.0;
                h = 4.0;
                cw = 2.0;
                cb = 2.0;
            }
            if (cb >= h) {
                cb = h - 0.2;
            }
            //System.out.println("grow back: cb,h "+cb+"  "+h);
            age -= years;
            v = calculateVolume();
        }
    }

    /**
     * update the crown base and width i.e. after growing of the tree
     */
    public void updateCrown() {
        if (d >= 7.0) {
            double cbnew;
            cbnew = calculateCb();
            if (cbnew > cb) {
                cb = cbnew;
            }
            // Crown base is not allowed to get a lower value
            cw = calculateCw();
        } //the little trees
        else {
            cb = h / 2.0;
        }
    }

    /**
     * update the competition indices, should be called after growing, thinning,
     * and mortality
     */
    public void updateCompetition() {
        if (sp.spDef.competitionXML.length() > 1) {
            try {
                String modelPlugIn = "treegross.base." + sp.spDef.competitionXML;
                PlugInCompetition comp = (PlugInCompetition) Class.forName(modelPlugIn).newInstance();
                if (fac > 0.0 && out < 1) {
                    c66 = comp.getc66(this);
                    c66c = comp.getc66c(this);
                    comp.replaceC66xyAndC66cxy(this, cw);
                } else {
                    c66 = -99;
                    c66c = -99.0;
                    c66xy = -99;
                    c66cxy = -99;
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                LOGGER.log(Level.WARNING, "ERROR in Class tree updateCompetition!", e);
            }
        }
    }

    /**
     * setGroup to assign the tree to an individual group
     *
     * @param group
     */
    public void setGroup(int group) {
        this.group = group;
    }

    boolean isLiving() {
        return out < 0;
    }
}
