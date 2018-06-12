/* 
 * @(#) Stand.java  
 *  (c) 2002-2010 Juergen Nagel, Northwest German Research Station,
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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static treegross.base.ScaleManager.SCALE_AUTO;
import treegross.random.RandomNumber;

/**
 * TreeGrOSS : Stand.java version 7.5 18-Mar-2010 author	Juergen Nagel
 *
 * This is the major class for simulating forest stands with the treegross
 * packge. The class Stand.java holds all the information of the stand and
 * provides the routines for creation, growth, mortality and treatment. The
 * default growth model is for the region Northwest Germany and includes the
 * growth model "BWIN" you can often find cited in the literature.
 *
 * The model is limited to the simulation of 8000 trees at the moment, but that
 * number can be increased.
 *
 * Important: 1) The Stand needs to be linked to the file Species.txt which
 * holds the information about how to handle each species. You can use the
 * ForestSimulator to set up the Species.txt file. 2) The Stand needs to have at
 * least three corner points. If your Stand area is circular use about 20 to 40
 * corner points for create the circular Stand area.
 *
 *
 *
 * http://www.nw-fva.de/~nagel/treegross/
 *
 */
public class Stand {

    public boolean debug = true;

    /**
     * maximum number of trees a stand can handle
     */
    public final int maxStandTrees = 8500;
    /**
     * maximum number of species a stand can handle
     */
    public final int maxStandSpecies = 30;
    /**
     * maximum number of corner points a stand can handle
     */
    public final int maxStandCorners = 200;
    /**
     * Standortskennziffer
     */
    public String standortsKennziffer = "";
    /**
     * Program Directory provides the Path to settings file
     */
    private File programDir;
    /**
     * added by jhansen pointer to extended HashMap with all SpeciesDefinitions
     */
    private SpeciesDefMap sdm = null;
    /**
     * Model PlugIn default = Northwest Germany
     */
    public String modelRegion = "default";
    // stand variables
    /**
     * number of trees in the model
     */
    public int ntrees = 0;
    /**
     * number of living trees in the model
     */
    public int nTreesAlive= 0;
    /**
     * number existing species
     */
    public int nspecies = 0;
    /**
     * number of corner points
     */
    public int ncpnt = 0;
    /**
     * name of the forest stand
     */
    public String standname = "no name";
    /**
     * stand size
     */
    public double size = 0.00;
    /**
     * stand size enterprise: the area reperented by this stand
     */
    public double size_ep = 0;
    /**
     * Number of trees per hectare
     */
    public double nha;
    /**
     * Basal per hectare
     */
    public double bha;
    /**
     * Number of trees per hectare thinned
     */
    public double nhaout;
    /**
     * Basal area per hectare thinned
     */
    public double bhaout;
    /**
     * total number of stems, basal area
     */
    public double nhatotal, bhatotal;
    /**
     * Degree of Density
     */
    public double degreeOfDensity;
    /**
     * starting year of simulation
     */
    public int year = 2009;
    /**
     * starting months of simulation
     */
    public int monat = 3;
    /**
     * Stand Type code
     */
    public int bt = 0;
    /**
     * Ageclass
     */
    public int ageclass = 0;
    /**
     * Random growth effects
     */ //public int randomGrowthEffects = RandomNumber.PSEUDO; 
    public RandomNumber random;
    /**
     * distance dependent growth competition
     */
    public boolean distanceDependent = true;
    /**
     * ingrowth model active
     */
    public boolean ingrowthActive = false;
    /**
     * risk model active
     */
    public boolean riskActive = false;
    /**
     * Deadwood
     */
    public double volumeOfDeadwood = 0.0;
    /**
     * Deadwood modul
     */
    public boolean deadwoodModul = false;
    /**
     * Deadwood modul
     */
    public String deadwoodModulName = "none";
    /**
     * Sorting modul
     */
    public String sortingModul = "none";
    /**
     * Biomass modul
     */
    public String biomassModul = "none";
    /**
     * ID
     */
    public String id = "";
    /**
     * DatenSource
     */
    public String datenHerkunft = "";
    /**
     * Site
     */
    public String standort = "";
    /**
     * Rechtswert_m
     */
    public double rechtswert_m = 0.0;
    /**
     * Hochwert_m
     */
    public double hochwert_m = 0.0;
    /**
     * Height_uNN_m
     */
    public double hoehe_uNN_m = -99.9;
    /**
     * Exposition_Gon
     */
    public int exposition_Gon = -99;
    /**
     * Hangneigung_Prozent
     */
    public double hangneigungProzent = -99.9;
    /**
     * Wuchsgebiet
     */
    public String wuchsgebiet = "";
    /**
     * Wuchsbezirk
     */
    public String wuchsbezirk = "";
    /**
     * FileXMLSettings
     */
    public String FileXMLSettings = "ForestSimulatorSettings.xml";
    /**
     * Transport of Integer for variouns uses
     */
    public int temp_Integer;

    public double dg, hg, dgout, d100, h100;
    //warum ne schleifen variable global deklarieren? führt zu fehlern!
    //private int i;

    /**
     * array for all trees of Stand
     */
    public Tree tr[] = new Tree[maxStandTrees];
    /**
     * array for species information
     */
    public Species sp[] = new Species[maxStandSpecies];
    /**
     * array for the corner points of the stand area
     */
    public Corners cpnt[] = new Corners[maxStandCorners];
    /**
     * Center point of area to switch from xy to polar coordinate
     */
    public Corners center = new Corners();
    /**
     * Stand status 0 = initialized, 1 = growing, 2 to 90 = step of harvesting,
     * 99 = clear cut
     */
    public int status = 0;
    /**
     * TimeStep is the number of years the model is calibrated for
     */
    public int timeStep = 5;
    /**
     * stand related treatment information
     */
    public TreatmentRuleStand trule = new TreatmentRuleStand();

    public int nRegeneration = 0;

    int sizeoftr = 0;

    /*added by jhansen*/
    /* a container to store all added StandChangeListeners*/
    private final List<StandChangeListener> StandChangeListeners = new ArrayList<>();
    private boolean notifyListeners = true;
    public double ed = 0;
    public double pd = 0;
    public int water = -99;
    
    /**
     * parallel Competion Update mechanism
     */
    private final ScaleManager scaleMan;

    /**
     * stopping hook for time consuming loop/calculation
     */
    boolean stop = false;

    private final static Logger LOGGER = Logger.getLogger(Stand.class.getName());

    public Stand() {
        this(SCALE_AUTO);
    }

    /**
     * Creates a new stand with given scaling method.
     * Scaling is usefull to decrease computation time.
     * Applications that works with only one stand should use method SCALE_FIXED (1).
     * Applications which implents already parallelisation mechanisms shoud use SCALE_AUTO or SCALE_NOT.
     * 
     * @param scaleMethod set the scaling method: 0 = auto, 1 = fixed, 2 = use no scaling
     */
    public Stand(int scaleMethod) {
        super();
        random = new RandomNumber(RandomNumber.PSEUDO);
        scaleMan = new ScaleManager(this);
        scaleMan.setScaleMethod(scaleMethod);
    }

    public ScaleManager getScaleManager() {
        return scaleMan;
    }

    /**
     * This method stopps all running loops with stop hook yet implemented by
     * method grow2
     */
    public void stop() {
        stop = true;
    }

    /**
     * This method clears the information of Stand.java for reading in a new one
     */
    public void newStand() {
        ntrees = 0;
        nspecies = 0;
        ncpnt = 0;
        distanceDependent = false;
        //randomGrowthEffects=RandomNumber.PSEUDO;
        random = new RandomNumber(RandomNumber.PSEUDO);
        ingrowthActive = true;
        trule.treatmentStep = 5;
        trule.lastTreatment = 0;
        status = 0;
    }

    // in Stand ????
    /**
     * For simple stand creation. The method does all necessary settings to
     * start simulation.
     *
     * @param localPath = path of the xml file with simulator settings, if null
     * load std. model
     * @param xmlSettingsFile = name of file with simulator settings
     * @param standname = stand name
     * @param standsize = stand size [ha]
     * @param year = year that the simulation starts The methods create a square
     * stand area of the given site. If you need an different area shape or
     * circular plot you have to overwrite the cornerpoints, the stand.center
     * information.
     */
    public void easyStart(File localPath, String xmlSettingsFile, String standname, double standsize, int year) {
        SpeciesDefMap SDM = new SpeciesDefMap();

        LOGGER.log(Level.INFO, "SDM loaded from {0}\\{1}", new Object[]{localPath, xmlSettingsFile});

        if (localPath != null && xmlSettingsFile != null) {
            try {
                SDM.readFromPath(localPath + "\\" + xmlSettingsFile);
                //SDM.readFromURL(new URL(localPath, st.FileXMLSettings));
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "loading species definitions", ex);
            }
        } else {
            //load sdm from internal package
            SDM.readInternal(null);
        }
        setSDM(SDM);
        setProgramDir(localPath);
        addName(standname);
        addsize(standsize);
        this.year = year;
        // Flächengrenzen festlegen
        double l = Math.sqrt(10000.0 * standsize);
        ncpnt = 0;
        addcornerpoint("ECK1", 0.0, 0.0, 0.0);
        addcornerpoint("ECK2", 0.0, l, 0.0);
        addcornerpoint("ECK3", l, l, 0.0);
        addcornerpoint("ECK4", l, 0, 0.0);
        center.no = "polygon";
        center.x = l * 0.5;
        center.y = l * 0.5;
        center.z = 0.0;
        scaleMan.setScaleMethod(ScaleManager.SCALE_FIXED);
    }

    public void loadSDM(String path) {
        sdm = new SpeciesDefMap();
        sdm.readFromPath(path);
    }

    /**
     * set the SpececiesDefMap this is the recommend method to set a pointer to
     * an instance of SpeciesDefMap the SpeciesDefMap should be loaded only once
     * at startup or after changing the model file
     *
     * @param amap
     */
    public void setSDM(SpeciesDefMap amap) {
        sdm = amap;
    }

    public SpeciesDefMap getSDM() {
        return sdm;
    }

    /**
     * added by jhansen
     *
     * clones a tree and returns the index of the clone in tree array or -9 if
     * cloning fails
     *
     * @param index the index of tree to clone
     * @return index
     *
     */
    public int addCloneTree(int index) {
        Tree t = tr[index];
        int zb = 0;
        int tzb = 0;
        int hb = 0;
        if (t.crop) {
            zb = 1;
        }
        if (t.tempcrop) {
            tzb = 1;
        }
        if (t.habitat) {
            hb = 1;
        }
        try {
            addtreefac(t.code, t.no.trim() + "clone", t.age, t.out, t.d, t.h, t.cb, t.cw, t.si, t.x, t.y, t.z, zb, tzb, hb, t.fac);
            tr[ntrees - 1].v = t.v;
            // check for other parameters to set to clone
            return ntrees - 1;
        } catch (SpeciesNotDefinedException e) {
            if (debug) {
                LOGGER.log(Level.WARNING, null, e);
            }
        }
        return -9;
    }

    /**
     * add a tree to the stand
     *
     * @param co = species code
     * @param num = tree no or name
     * @param age = age of the tree
     * @param out = if living -1, else the year when died or taken out
     * @param d = dbh cm
     * @param h = height m
     * @param cb = crown base m
     * @param cw = crown width
     * @param si = site index
     * @param x = x-coordinate
     * @param y = y = coordinate
     * @param z = z - coordinate
     * @param zb = is tree a crop tree: z =1 else 0
     * @param tzb = is tree a temporary crop tree
     * @param hb = is tree a habitat tree
     * @return true if the tree has been added to the stand otherwise false
     * @throws treegross.base.SpeciesNotDefinedException
     */
    public boolean addtree(int co, String num, int age, int out, double d, double h, double cb, double cw,
            double si, double x, double y, double z, int zb, int tzb, int hb) throws SpeciesNotDefinedException {
        //Könnte fehlerhaft sein
        // if (sizeoftr<=ntrees) { tr[ntrees]=new tree();sizeoftr=sizeoftr+1;}
        // added by jhansen -> +1 -1 empty array place needed
        if (ntrees + 1 >= maxStandTrees - 1) {
            if (debug) {
                LOGGER.log(Level.WARNING, "Maximum tree number reached! Tree not added! {0} {1} d={2} species={3} height={4}", new Object[]{standname, trule.targetType, d, co, h});
            }
            return false;
        }

        tr[ntrees] = new Tree();
        tr[ntrees].code = co;
        tr[ntrees].no = num;
        tr[ntrees].age = age;
        tr[ntrees].out = out;
        tr[ntrees].crop = false;
        tr[ntrees].tempcrop = false;
        tr[ntrees].habitat = false;
        tr[ntrees].origin = 0;
        tr[ntrees].year = this.year;
        tr[ntrees].d = d;
        tr[ntrees].h = h;
        tr[ntrees].cb = cb;
        tr[ntrees].cw = cw;
        tr[ntrees].x = x;
        tr[ntrees].y = y;
        if (z < 0.0) {
            z = 0.0;
        }
        tr[ntrees].z = z;
        tr[ntrees].outtype = 0;
        tr[ntrees].fac = 1.0;
        tr[ntrees].si = si; // no site index set at this point
        tr[ntrees].group = -1;

        tr[ntrees].crop = (zb > 0);
        tr[ntrees].tempcrop = (tzb > 0);
        tr[ntrees].habitat = (hb > 0);
        tr[ntrees].sp = addspecies(tr[ntrees]);
        tr[ntrees].st = this;
        //               if (tr[ntrees].cw <= 0.0) tr[ntrees].generatecw();
        ntrees++;
        return true;
    }

    /**
     * add a tree to the stand
     *
     * @param co = species code
     * @param num = tree no or name
     * @param age = age of the tree
     * @param out = if living -1, else the year when died or taken out
     * @param d = dbh cm
     * @param h = height m
     * @param cb = crown base m
     * @param cw = crown width
     * @param si = site index
     * @param x = x-coordinate
     * @param y = y = coordinate
     * @param z = z - coordinate
     * @param zb = is tree a crop tree: z =1 else 0
     * @param tzb = is tree a temporary crop tree
     * @param hb = is tree a habitat tree
     * @return true if the tree has been added to the stand otherwise false
     * @throws treegross.base.SpeciesNotDefinedException
     */
    public boolean addTreeFromPlanting(int co, String num, int age, int out, double d, double h, double cb, double cw,
            double si, double x, double y, double z, int zb, int tzb, int hb) throws SpeciesNotDefinedException {
        boolean result;
        result = addtree(co, num, age, out, d, h, cb, cw, si, x, y, z, zb, tzb, hb);
        tr[ntrees - 1].origin = 1;
        return result;
    }

    /**
     * add a tree to the stand
     *
     * @param co = species code
     * @param num = tree no or name
     * @param age = age of the tree
     * @param out = if living -1, else the year when died or taken out
     * @param d = dbh cm
     * @param h = height m
     * @param cb = crown base m
     * @param cw = crown width
     * @param si = site index
     * @param x = x-coordinate
     * @param y = y = coordinate
     * @param z = z - coordinate
     * @param zb = is tree a crop tree: z =1 else 0
     * @param tzb = is tree a temporary crop tree
     * @param hb = is tree a habitat tree
     * @throws treegross.base.SpeciesNotDefinedException
     */
    public void addTreeFromNaturalIngrowth(int co, String num, int age, int out, double d, double h, double cb, double cw,
            double si, double x, double y, double z, int zb, int tzb, int hb) throws SpeciesNotDefinedException {
        addtree(co, num, age, out, d, h, cb, cw, si, x, y, z, zb, tzb, hb);
        tr[ntrees - 1].origin = 2;
        tr[ntrees - 1].layer = 3;
    }

    public void addTreeFromDB(int co, String num, double fac, int age, int out, int outtype, double d, double h, double v, double cb, double cw,
            double si, double x, double y, double z, int zb, int tzb, int hb, int layer) throws SpeciesNotDefinedException {

        //if (sizeoftr<=ntrees) { tr[ntrees]=new tree();sizeoftr=sizeoftr+1;} 
        tr[ntrees] = new Tree();
        tr[ntrees].code = co;
        tr[ntrees].no = num;
        tr[ntrees].fac = fac;
        tr[ntrees].age = age;
        tr[ntrees].out = out;
        tr[ntrees].outtype = outtype;
        tr[ntrees].crop = false;
        tr[ntrees].tempcrop = false;
        tr[ntrees].habitat = false;
        tr[ntrees].origin = 0;
        tr[ntrees].year = this.year;
        tr[ntrees].d = d;
        tr[ntrees].h = h;
        tr[ntrees].v = v;
        tr[ntrees].cb = cb;
        tr[ntrees].cw = cw;
        tr[ntrees].x = x;
        tr[ntrees].y = y;
        tr[ntrees].z = z;
        tr[ntrees].si = si; // no site index set at this point
        tr[ntrees].group = -1; // no site index set at this point
        /*if (zb>0) tr[ntrees].crop=true; else tr[ntrees].crop=false;
         if (tzb>0) tr[ntrees].tempcrop=true; else tr[ntrees].tempcrop=false;                
         if (hb>0) tr[ntrees].habitat=true; else tr[ntrees].habitat=false;*/
        tr[ntrees].crop = (zb > 0);
        tr[ntrees].tempcrop = (tzb > 0);
        tr[ntrees].habitat = (hb > 0);

        tr[ntrees].sp = addspecies(tr[ntrees]);
        tr[ntrees].st = this;
        tr[ntrees].layer = layer;
        //             if (tr[ntrees].cw <= 0.0) tr[ntrees].generatecw();
        ntrees++;
    }

    /* add a tree to the stand including factor*/
    public void addtreefac(int co, String num, int age, int out, double d, double h, double cb, double cw,
            double si, double x, double y, double z, int zb, int tzb, int hb, double fac) throws SpeciesNotDefinedException {

        if (ntrees + 1 >= maxStandTrees - 1) {
            if (debug) {
                LOGGER.log(Level.WARNING, "Maximum tree number reached! Tree not added! {0} {1} d={2} species={3} height={4}", new Object[]{standname, trule.targetType, d, co, h});
            }
            return;
        }
        //if (sizeoftr<=ntrees) { tr[ntrees]=new tree();sizeoftr=sizeoftr+1;} 
        tr[ntrees] = new Tree();
        tr[ntrees].code = co;
        tr[ntrees].no = num;
        tr[ntrees].age = age;
        tr[ntrees].out = out;
        tr[ntrees].crop = false;
        tr[ntrees].tempcrop = false;
        tr[ntrees].habitat = false;
        tr[ntrees].d = d;
        tr[ntrees].h = h;
        tr[ntrees].cb = cb;
        tr[ntrees].cw = cw;
        tr[ntrees].origin = 0;
        tr[ntrees].year = this.year;
        tr[ntrees].x = x;
        tr[ntrees].y = y;
        if (z < 0.0) {
            z = 0.0;
        }
        tr[ntrees].z = z;
        tr[ntrees].outtype = 0;
        tr[ntrees].fac = fac;
        tr[ntrees].si = si; // no site index set at this point
        tr[ntrees].group = -1; // no site index set at this point
        tr[ntrees].crop = zb > 0;
        tr[ntrees].tempcrop = tzb > 0;
        tr[ntrees].habitat = hb > 0;
        tr[ntrees].sp = addspecies(tr[ntrees]);
        tr[ntrees].st = this;
        //              if (tr[ntrees].cw <= 0.0) tr[ntrees].generatecw();
        ntrees++;
    }

    /* add a tree to the stand including factor*/
    public void addXMLTree(int codenumber, String number, int age, int out, int outtype,
            double d, double h, double cb, double cw,
            double si, double fac, double x, double y, double z, boolean zb, boolean tzb, boolean hb,
            int layer, double volumeDeadwood, String remarks) throws SpeciesNotDefinedException {
        tr[ntrees] = new Tree(codenumber, number, age, out, outtype, d, h, cb, cw, si, fac, x, y, z, zb, tzb, hb, layer,
                volumeDeadwood, remarks);
        tr[ntrees].sp = addspecies(tr[ntrees]);
        tr[ntrees].st = this;
        ntrees++;
    }

    /**
     * Set the name of the Stand
     *
     * @param na
     */
    public void addName(String na) {
        standname = na;
    }

    /**
     * Set the year of the simulation
     *
     * @param yr
     */
    public void addYear(Integer yr) {
        year = yr;
    }

    /**
     * initialize stand area size (ha)
     *
     * @param ha
     */
    public void addsize(double ha) {
        size = ha;
        // hat hier nichts zu suchen        
        // trule.selectHabiatPart=selHabitat.selectHabitatPartByStandSize(this);        
    }

    /**
     * addcorner points
     *
     * @param no
     * @param x
     * @param y
     * @param z
     */
    public void addcornerpoint(String no, double x, double y, double z) {
        cpnt[ncpnt] = new Corners();
        cpnt[ncpnt].no = no;
        cpnt[ncpnt].x = x;
        cpnt[ncpnt].y = y;
        cpnt[ncpnt].z = z;
        ncpnt = ncpnt + 1;
    }

    /* add a tree from Dbase NFV*/
    public void addtreeNFV(int co, String num, int age, int out, double d, double h, double cb, double cw,
            double si, double x, double y, double z, int zb, int tzb, int hb, int ou, double fac, String rm) throws SpeciesNotDefinedException {

        //if (sizeoftr<=ntrees) { tr[ntrees]=new tree();sizeoftr=sizeoftr+1;} 
        tr[ntrees] = new Tree();
        tr[ntrees].code = co;
        tr[ntrees].no = num;
        tr[ntrees].age = age;
        tr[ntrees].out = out;
        tr[ntrees].crop = false;
        tr[ntrees].d = d;
        tr[ntrees].h = h;
        tr[ntrees].cb = cb;
        tr[ntrees].cw = cw;
        tr[ntrees].x = x;
        tr[ntrees].y = y;
        tr[ntrees].z = z;
        tr[ntrees].outtype = 0;
        tr[ntrees].fac = fac;
        tr[ntrees].origin = 0;
        tr[ntrees].year = this.year;
        tr[ntrees].layer = ou;  //0=no layer, 1 upperstory, 2= understory
        tr[ntrees].ou = ou;
        tr[ntrees].remarks = rm;
        tr[ntrees].si = si; // no site index set at this point
        tr[ntrees].group = -1; // no site index set at this point
        tr[ntrees].crop = zb > 0;
        tr[ntrees].tempcrop = tzb > 0;
        tr[ntrees].habitat = hb > 0;
        tr[ntrees].sp = addspecies(tr[ntrees]);
        tr[ntrees].st = this;
        //              if (tr[ntrees].cw <= 0.0) tr[ntrees].generatecw();
        ntrees++;
    }

    /**
     * sort trees by d
     */
    public void sortbyd() {
        sortUsing((Tree t1, Tree t2) -> {
            if (t1.d > t2.d) {
                return -1;
            }
            if (t1.d == t2.d) {
                return 0;
            }
            return 1;
        });
    }
    
    /**
     * sort trees by h
     */
    public void sortbyh() {
        sortUsing((Tree t1, Tree t2) -> {
            if (t1.h > t2.h) {
                return -1;
            }
            if (t1.h == t2.h) {
                return 0;
            }
            return 1;
        });
    }

    /**
     * sort trees by y-coordinate, this is for the graphical display
     */
    public void sortbyy() {
        sortUsing((Tree t1, Tree t2) -> {
            if (t1.y > t2.y) {
                return -1;
            }
            if (t1.y == t2.y) {
                return 0;
            }
            return 1;
        });
    }

    /**
     * sort trees by number
     */
    public void sortbyNo() {
        sortUsing((Tree t1, Tree t2) -> {
            return t1.no.compareTo(t2.no);
        });
    }

    private void sortUsing(Comparator<Tree> comparator) {
        Arrays.sort(tr, 0, ntrees, comparator);
    }

    /**
     * total stand information resp. total stand values
     */
    public void standinfo() {
        bha = 0.0;
        nha = 0;
        bhaout = 0.0;
        nhaout = 0.0;
        nhatotal = 0.0;
        bhatotal = 0.0;
        for (int i = 0; i < ntrees; i++) {
            if (tr[i].out < 0) {
                nha = nha + 1;
                if (tr[i].d >= 7.0) {
                    bha = bha + tr[i].fac * Math.PI * Math.pow((tr[i].d / 200), 2.0);
                }
            }
            if (tr[i].out == year) {
                nhaout = nhaout + 1;
                if (tr[i].d >= 7.0) {
                    bhaout = bhaout + tr[i].fac * Math.PI * Math.pow((tr[i].d / 200), 2.0);
                }
            }
            if (tr[i].out > 0 && tr[i].out < year) {
                nhatotal = nhatotal + tr[i].fac;
                if (tr[i].d >= 7.0) {
                    bhatotal = bhatotal + tr[i].fac * Math.PI * Math.pow((tr[i].d / 200), 2.0);
                }
            }
        }
        bhatotal = (bhatotal + bha + bhaout) / size;
        nhatotal = (nhatotal + nhaout + nha) / size;
        bha = bha / size;
        nha = nha / size;
        bhaout = bhaout / size;
        nhaout = nhaout / size;
    }

    /**
     * get volume per hectare of the total volume (spe=0) or species volume
     * (spe=code)
     *
     * @param spe
     * @return a volume
     */
    public double getVha(int spe) {
        double vha = 0.0;
        for (int i = 0; i < ntrees; i++) {
            if ((tr[i].code == spe) || (spe == 0)) {
                vha = vha + tr[i].fac * tr[i].v;
            }
        }
        return vha / size;
    }

    /**
     * get volume harvested per hectare by target diameter of this year of the
     * total volume (spe=0) or species volume (spe=code)
     *
     * @param spe
     * @return a volume
     */
    public double getVhaTargetDiameter(int spe) {
        double vha = 0.0;
        for (int i = 0; i < ntrees; i++) {
            if (tr[i].outtype == 3 && tr[i].out == year
                    && ((tr[i].code == spe) || (spe == 0))) {
                vha = vha + tr[i].fac * tr[i].v;
            }
        }
        return vha / size;
    }

    /**
     * get thinning volume per hectare of this year of the total volume (spe=0)
     * or species volume (spe=code)
     *
     * @param spe
     * @return a volume
     */
    public double getVhaThinning(int spe) {
        double vha = 0.0;
        for (int i = 0; i < ntrees; i++) {
            if (tr[i].outtype == 2 && tr[i].out == year
                    && ((tr[i].code == spe) || (spe == 0))) {
                vha += +tr[i].fac * tr[i].v;
            }
        }
        return vha / size;
    }

    /**
     * Returns the number of missing heights, for a species (spe=code) or
     * (spe=0) for all trees
     *
     * @param spe
     * @return a number
     */
    public int getMissingHeight(int spe) {
        int miss = 0;
        for (int j = ntrees - 1; j >= 0; j--) {
            if (tr[j].d >= 7 && tr[j].h < 1.3 && (tr[j].code == spe || spe == 0)) {
                miss++;
            }
        }
        return miss;
    }

    /**
     * get volume by of residual tree
     *
     * @param spe
     * @return a volume
     */
    public double getVhaResidual(int spe) {
        double vha = 0.0;
        for (int i = 0; i < ntrees; i++) {
            if (tr[i].out < 0
                    && ((tr[i].code == spe) || (spe == 0))) {
                vha += tr[i].fac * tr[i].v;
            }
        }
        return vha / size;
    }

    /**
     * get the maximum diameter of all living trees (spe = 0) or of a species
     * (spe = sp.code)
     *
     * @param spe
     * @return a diameter
     */
    public double getDmax(int spe) {
        double dmax = 0.0;
        for (int i = 0; i < ntrees; i++) {
            if (tr[i].d > dmax && tr[i].out < 0
                    && ((tr[i].code == spe) || (spe == 0))) {
                dmax = tr[i].d;
            }
        }
        return dmax;
    }

    /**
     * detect species and initialize tree species
     */
    public void initspecies() {
        int i, j, ic;
        nspecies = 0;
        sp[nspecies] = new Species();
        if (ntrees > 0) {
            sp[0].code = tr[0].code;
            nspecies = nspecies + 1;
            for (i = 1; i < ntrees; i++) {
                ic = 0;
                for (j = 0; j < nspecies; j++) {
                    if (tr[i].code == sp[j].code) {
                        ic = 1;
                    }
                }

                if (ic == 0) {
                    sp[nspecies] = new Species();
                    sp[nspecies].code = tr[i].code;
                    nspecies++;
                }
            }
        }
    }

    public void grow2(int period, boolean naturalIngrowth) {
        //long ts = System.currentTimeMillis();
        executeMortality();
        //System.out.println("mortality: "+ntrees+":"+(System.currentTimeMillis()-ts));
        //ts = System.currentTimeMillis();
        grow(period, naturalIngrowth);
        //System.out.println("growth: "+ntrees+":"+(System.currentTimeMillis()-ts));
    }

    /**
     * st.grow starts a growth cycle for the class stand, expects an integer for
     * the number of years of one growing cycle. Should be between 1 -5. The
     * second parameter controlls the regenration growth and can be set as
     * either true or false
     *
     * @param period
     * @param naturalIngrowth
     */
    public void grow(int period, boolean naturalIngrowth) {
        // Stand is 1=growing, or 2....9 = Period of harvest, or 99=final clear cut
        if (status == 0) {
            status = 1;
        }
        // Start risk model
        if (riskActive) {
            try {
                String modelPlugIn = "treegross.base.Risk";
                PlugInRisk risk = (PlugInRisk) Class.forName(modelPlugIn).newInstance();
                risk.applyRisk(this);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                if (debug) {
                    LOGGER.log(Level.WARNING, null, e);
                }
            }
        }

        /*for (i = 0; i < ntrees && !stop; i++) {
            if (tr[i].out < 0) {
                tr[i].updateCompetition();
            }
        }*/
        scaleMan.updateCompetition();
        /*for (i = 0; i < ntrees && !stop; i++) {
            if (tr[i].out < 0) {
                tr[i].grow(period, random);
            }
        }*/
        scaleMan.growTrees(period, random);
        /*for (i = 0; i < ntrees && !stop; i++) {
            if (tr[i].out < 0) {
                tr[i].updateCompetition();
            }
        }*/
        scaleMan.updateCompetition();

        year += period;
        descspecies();         // new spcies description, dl00, h100 etc.

        /*for (i = 0; i < ntrees && !stop; i++) {
            if (tr[i].out < 0) {
                tr[i].updateCrown();
            }
        }*/
        scaleMan.updateCrown();

        if (nspecies > 0 && naturalIngrowth) {
            try {
                String modelPlugIn = "treegross.base." + sp[0].spDef.ingrowthXML;
                PlugInIngrowth ig = (PlugInIngrowth) Class.forName(modelPlugIn).newInstance();
                ig.predictIngrowth(this);
            } catch (Exception e) {
                if (debug) {
                    LOGGER.log(Level.WARNING, null, e);
                }
            } finally {
                descspecies();
            }
        }

        // delete dead regeneration trees
        cleanTreeArrayReg();
        notifyStandChanged("growing", this);
    }

    /**
     * estimates the stand situattion x years ago, under proer treatment
     */
    //@SuppressWarnings("empty-statement")
    /*public void growBack(int years, boolean gd){
     // System.out.println("grow back stand "+ntrees);
     for (int i=0;i<ntrees;i++)
     if (tr[i].out < 0 || tr[i].out==tr[i].st.year)  {
     tr[i].growBack(years);
     }
     for (int i=0;i<ntrees;i++)
     for (int ik=0;ik<ntrees;ik++) if (tr[ik].d < 7.0 || tr[ik].h < 5.0 || tr[ik].age <= 10) removeTree(ik);
     year=year-years;
     sortbyd();
     descspecies();         // new spcies description, dl00, h100 etc.
     for (int i=0;i<ntrees;i++)
     if ( tr[i].out < 0) {
     tr[i].cw=tr[i].calculateCw();
     tr[i].cb=tr[i].calculateCb();
     //System.out.println("grow back stand "+tr[i].h+"  "+tr[i].cb);
     };
     descspecies();         // new spcies description, dl00, h100 etc.


     // Bestand auf Sollgrundfläche anreichern
     if(gd)
     for (int i=0;i<nspecies;i++){
     Tree atree = new Tree();
     atree.st=this;
     atree.sp=this.sp[i];
     atree.d =this.sp[i].d100;
     atree.h =this.sp[i].h100;
     atree.age =(int) Math.round(this.sp[i].h100age);
     atree.code=this.sp[i].code;
     atree.cw=atree.calculateCw();
     double maxStandBasalArea = atree.calculateMaxBasalArea()*atree.getModerateThinningFactor()*
     (this.sp[i].percCSA/100.0);
     double ghaToGen = maxStandBasalArea-this.sp[i].gha;
     if (ghaToGen > 0 && sp[i].dg > 7.0 && sp[i].h100age > 10 && sp[i].d100 > 9.0){
     try {
     System.out.println("Es werden gha generiert: "+ghaToGen);
     GenDistribution gdb = new GenDistribution();
     gdb.weibull(this, sp[i].code, (int)Math.round(sp[i].h100age), sp[i].dg, sp[i].hg, sp[i].d100, ghaToGen * this.size);
     double si_soll =-9;
     for (int j = 0; j < this.ntrees; j++) if (this.tr[j].si > 0.0 && this.tr[j].code==sp[i].code) si_soll=this.tr[j].si;
     for (int j = 0; j < this.ntrees; j++) if (this.tr[j].si <= -9) this.tr[j].si=si_soll;
     GenerateXY gxy = new GenerateXY();
     gxy.zufall(this);
     } catch (Exception ex) { }
     }
     }
     }*/
    /**
     * estimates the stand situattion x years ago, under prior treatment
     *
     * @param years = number of years to grow back max. 5
     * @param db (true)= fill in new trees, that were virtually thinned to level
     * of desired basal area
     */
    @SuppressWarnings("empty-statement")
    public void growBack(int years, boolean db) {
        // System.out.println("grow back stand "+ntrees);
        for (int i = 0; i < ntrees; i++) {
            if (tr[i].out < 0 || tr[i].out == tr[i].st.year) {
                tr[i].growBack(years);
            }
        }
        for (int i = 0; i < ntrees; i++) {
            for (int ik = 0; ik < ntrees; ik++) {
                if (tr[ik].d < 7.0 || tr[ik].h < 5.0 || tr[ik].age <= 10) {
                    removeTree(ik);
                }
            }
        }
        year -= years;
        sortbyd();
        descspecies();         // new spcies description, dl00, h100 etc.
        for (int i = 0; i < ntrees; i++) {
            if (tr[i].out < 0) {
                tr[i].cw = tr[i].calculateCw();
                tr[i].cb = tr[i].calculateCb();
                //System.out.println("grow back stand "+tr[i].h+" "+tr[i].cb);
            }
        };
        descspecies();         // new spcies description, dl00, h100 etc.
        // Bestand auf Sollgrundfläche anreichern
        double dgGen, dMaxGen;
        if (db) {
            for (int i = 0; i < nspecies; i++) {
                if (sp[i].dg > 7.0 && sp[i].h100age > 10 && sp[i].d100 > 9.0) {
                    Tree atree = new Tree();
                    atree.st = this;
                    atree.sp = this.sp[i];
                    atree.d = this.sp[i].d100;
                    atree.h = this.sp[i].h100;
                    atree.age = (int) Math.round(this.sp[i].h100age);
                    atree.code = this.sp[i].code;
                    atree.cw = atree.calculateCw();
                    double maxStandBasalArea = atree.calculateMaxBasalArea()
                            * atree.getModerateThinningFactor() * (this.sp[i].percCSA / 100.0);

                    double ghaToGen = maxStandBasalArea - this.sp[i].gha;
                    if (ghaToGen > 0 /*&& sp[i].dg > 7.0 && sp[i].h100age > 10 && sp[i].d100 > 9.0*/) {
                        if(sp[i].dg > 150 || sp[i].d100 > 150){
                            LOGGER.log(Level.SEVERE, "treegross", "species dg or d100 too heigh! "+sp[i]);
                        }
                        try {
                            //System.out.println("Es werden gha generiert:
                            //"+ghaToGen+" "+sp[i].code+" "+sp[i].hg+" "+sp[i].dg+" "+sp[i].d100+"
                            //"+Math.round(sp[i].h100age));
                            FunctionInterpreter fi = new FunctionInterpreter();
                            //NormalDistributed nd = new NormalDistributed();
                            GenDistribution gdb = new GenDistribution();
                            
                            // limit dmax to dg * 1.8
                            dgGen =  sp[i].dg;
                            dMaxGen = sp[i].d100;
                            if(dMaxGen / dgGen > 1.8){
                                dMaxGen = dgGen * 1.8;
                            }
                            
                            gdb.weibull(this, sp[i].code, (int) Math.round(sp[i].h100age),
                                    dgGen, sp[i].hg, dMaxGen, ghaToGen * size, true);
                            double si_soll = -9;
                            for (int j = 0; j < this.ntrees; j++) {
                                if (this.tr[j].si > 0.0 && this.tr[j].code == sp[i].code) {
                                    si_soll = this.tr[j].si;
                                }
                            }
                            for (int j = 0; j < this.ntrees; j++) {
                                if (this.tr[j].si <= -9) {
                                    this.tr[j].si = si_soll;
                                }
                            }
                            for (int j = 0; j < this.ntrees; j++) {
                                if (this.tr[j].h == 0.0) {
                                    this.tr[j].h = fi.getValueForTree(this.tr[j],
                                            this.tr[j].sp.spDef.uniformHeightCurveXML)
                                            + fi.getValueForTree(this.tr[j], this.tr[j].sp.spDef.heightVariationXML)
                                            * /*nd.value(3.0)*/ random.nextNormal(3);
                                }
                            }
                            for (int j = 0; j < this.ntrees; j++) {
                                this.tr[j].setMissingData();
                            }
                            GenerateXY gxy = new GenerateXY();
                            gxy.zufall(this);
                        } catch (SpeciesNotDefinedException ex) {
                            LOGGER.log(Level.SEVERE, "", ex);
                        }
                    }
                }
            }
        }
    }

    /**
     * removeTree
     */
    private void removeTree(int m) {
        if (ntrees > 1) {
            for (int i = m; i < ntrees - 1; i++) {
                tr[i] = tr[i + 1];
            }
            ntrees--;
        } else {
            ntrees = 0;
        }
    }

    /**
     * st.area calculates the stand size according to the corner points
     *
     * @return stand size [ha]
     */
    public double area() {
        double size_m = 0.0;
        int i;
        if (ncpnt > 1) {
            for (i = 0; i < ncpnt - 1; i++) {
                size_m += (cpnt[i].x - cpnt[i + 1].x) * (cpnt[i].y + cpnt[i + 1].y);
            }
            size_m += (cpnt[ncpnt - 1].x - cpnt[0].x) * (cpnt[ncpnt - 1].y + cpnt[0].y);
            size_m = Math.abs(size_m / 20000.0);
        }
        //System.out.println("Flächengröße "+size);
        return size_m;
    }

    /**
     * This routine should always be called, before analyzing and growing the
     * Stand. It re-calculates also the height curves, tree volume, and stand
     * and species values which might have changed tree array must be sorted by
     * dbh!!
     */
    public void descspecies() {
        // calculate total basal area and number of stems
        nha = 0.0;
        nhaout = 0.0;
        bha = 0.0;
        bhaout = 0.0;

        int startindex = ntrees - 1; // last tree in array has index ntrees-1
        int ndh = 0; // number of diameter and height values
        nTreesAlive = 0;
        // by jhansen: to minimize number of loops calculate
        // nha, bha, nhaout, bhout and ndh in one loop
        // note:
        // a backward loop (j--) is faster because the computation time for j<=0 is much faster
        // then for checking j>= a int number, java has a special algorithm to compare numbers with 0.
        for (int j = startindex; j >= 0; j--) {
            if (tr[j].out < 1) {
                nTreesAlive++;
            }
            if (tr[j].d >= 7.0) {
                if (tr[j].out < 1) {
                    nha += tr[j].fac;
                    bha += tr[j].fac * Math.PI * (tr[j].d / 200.0) * (tr[j].d / 200.0);
                    if (tr[j].h >= 1.3) {
                        ndh++;
                    }
                }
                if (tr[j].out == year) {
                    nhaout += tr[j].fac;
                    bhaout += tr[j].fac * Math.PI * (tr[j].d / 200.0) * (tr[j].d / 200.0);
                }
            }
        }         
        nha = nha / size;
        bha = bha / size;
        nhaout = nhaout / size;
        bhaout = bhaout / size;

        if (nha > 0.0) {
            dg = 200 * Math.sqrt(bha / (Math.PI * nha));   //Dg
        }
        if (nhaout > 0.0) {
            dgout = 200 * Math.sqrt(bhaout / (Math.PI * nhaout));   //Dgout
        }
        for (int j = 0; j < nspecies; j++) {
            sp[j].updateSpecies(this);
        }

        // average and dominant stand height
        d100 = 0.0;
        double n100 = size * 100.0;  // n100 according to stand size

        // tree array must be sorted by dbh!! or we can not loop descanding
        if (n100 > 0) {
            double jj = 0;
            int k = 0;
            while (jj < n100 && k < ntrees) {
                if (tr[k].d >= 7.0 && tr[k].out < 1) {
                    d100 = d100 + tr[k].fac * Math.PI * (tr[k].d / 200.0) * (tr[k].d / 200.0);
                    jj += tr[k].fac;
                }
                k++;
            }
            d100 = 200 * Math.sqrt(d100 / (Math.PI * jj));
        }

        // calculate diameter-height curve  
        int missingheights = getMissingHeight(0);

        // number of diameter-height values > 5 then height curve
        if (ndh > 4) {
            int k = ndh / 1000; //if there are more than 1000 ndh then use only every k th tree
            k++;
            ndh = 0;

            HeightCurve m = new HeightCurve();
            m.heightcurve();

            for (int j = 0; j < ntrees; j = j + k) {
                if (tr[j].d >= 7.0 && tr[j].h >/*=*/ 1.3 && tr[j].out < 1) {
                    ndh++;
                }
            }
            for (int j = 0; j < ntrees; j = j + k) {
                if (tr[j].d >= 7.0 && tr[j].h >/*=*/ 1.3 && tr[j].out < 1) {
                    m.adddh(sp[0].spDef.heightCurve, ndh, tr[j].d, tr[j].h);
                }
            }

            m.start();

            if (dg > 0) {
                hg = m.hwert(sp[0].spDef.heightCurve, dg);
            }
            if (d100 > 0) {
                h100 = m.hwert(sp[0].spDef.heightCurve, d100);
            }

            //check if height values are ok
            double h_d = hg / dg;
            double h_d_100 = h100 / d100;

            if (h_d > 5 || h_d_100 > 5) {
                ndh = 1; // not ok-> use uniform height curve
            } // are h/d-values ok use normal height curve
            else if (missingheights > 0) {
                for (int j = startindex; j >= 0; j--) {
                    if (tr[j].h < 1.3) {
                        tr[j].h = m.hwert(sp[0].spDef.heightCurve, tr[j].d);
                    }
                }
            }
        }

        // Alternative if there are only a few  1 to 4 heights
        // Use uniform height curve 
        double dk = 0.0;
        double hk = 0.0;

        if (ndh > 0 && ndh <= 4) {
            for (int j = startindex; j >= 0; j--) {
                if (tr[j].d >= 7.0 && tr[j].h > 1.3 && tr[j].out < 1 && hk < tr[j].h) {
                    dk = tr[j].d;
                    hk = tr[j].h;
                }
            }
            double dgmerk = sp[0].dg;
            double hgmerk = sp[0].hg;
            Tree tree = new Tree();
            tree.d = d100;
            tree.sp = sp[0];
            tree.sp.dg = dk;
            tree.sp.hg = hk;
            FunctionInterpreter fi = new FunctionInterpreter();
            h100 = fi.getValueForTree(tree, tree.sp.spDef.uniformHeightCurveXML);
            tree.d = dg;
            hg = fi.getValueForTree(tree, tree.sp.spDef.uniformHeightCurveXML);

            // UniformHeight ufh = new UniformHeight();
            // hg=ufh.height(sp[0],dg,dk,hk, this);
            // h100=ufh.height(sp[0],d100,dk,hk, this);
            if (missingheights > 0) {
                for (int j = 0; j < ntrees; j++) {
                    if (tr[j].h < 1.3) {
                        tree.d = tr[j].d;
                        tr[j].h = fi.getValueForTree(tree, tree.sp.spDef.uniformHeightCurveXML);
                        // tr[j].h=ufh.height(tr[j].sp,tr[j].d,dk,hk, this);
                    }
                }
            }
            sp[0].dg = dgmerk;
            sp[0].hg = hgmerk;
        }

        // if sp.h100 = 0.0 then it is set to h100 of total stand
        for (int j = 0; j < nspecies; j++) {
            if (sp[j].h100 <= 0.0) {
                sp[j].updateSpecies(this);
            }
        }
        // no height existing then try to handle problem later       
        if (ndh <= 0) {
            hg = 0.0;
            h100 = 0.0;
            if (ntrees != 0 && debug) {
                LOGGER.log(Level.WARNING, /*"ERROR : */ "missing heights");
            }
        }
        //       
        // generate missing data of trees
        //
        for (int j = 0; j < ntrees; j++) {
            tr[j].setMissingData();
        }
        // neighbor trees
        if (ntrees > 0) {
            selectNeighborTrees();
        }
        // competition of trees
        /*for (int j = 0; j < ntrees; j++) {
            tr[j].updateCompetition();
        }*/
        scaleMan.updateCompetition();

        // calculate percentage of basal area and crown surface area for each species
        double sumBA = 0.0;
        double sumCSA = 0.0;
        for (int i = 0; i < ntrees; i++) {
            if (tr[i].d >= 7.0 && tr[i].out < 0) {
                sumBA = sumBA + tr[i].fac * Math.PI * Math.pow(tr[i].d / 200.0, 2.0);
                sumCSA = sumCSA + tr[i].fac * Math.PI * Math.pow(tr[i].cw / 2.0, 2.0);
            }
        }
        volumeOfDeadwood = 0.0;
        for (int i = 0; i < nspecies; i++) {
            sp[i].percBA = 0.0;
            sp[i].percCSA = 0.0;
            volumeOfDeadwood += sp[i].volumeOfDeadwood;
            for (int j = 0; j < ntrees; j++) {
                if (tr[j].code != sp[i].code) {
                    continue;
                }
                if (tr[j].d >= 7.0 && tr[j].out < 0) {
                    sp[i].percBA += tr[j].fac * Math.PI * Math.pow(tr[j].d / 200.0, 2.0);
                    sp[i].percCSA += tr[j].fac * Math.PI * Math.pow(tr[j].cw / 2.0, 2.0);
                }
            }
            if (sumBA > 0 && sp[i].percBA > 0) {
                sp[i].percBA = 100.0 * sp[i].percBA / sumBA;
            } else {
                sp[i].percBA = 0;
            }
            if (sumCSA > 0 && sp[i].percCSA > 0) {
                sp[i].percCSA = 100.0 * sp[i].percCSA / sumCSA;
            } else {
                sp[i].percCSA = 0;
            }
        }

        // warum ==1000.0
        // adjust the crownpercent in treatment rule so that there is only 100% in total
        double sumPercent = 0.0;

        for (int i = 0; i < nspecies; i++) {
            if (sp[i].trule.targetCrownPercent == 1000.0) {
                sp[i].trule.targetCrownPercent = sp[i].percCSA;
            }
        }

        for (int i = 0; i < nspecies; i++) {
            sumPercent = sumPercent + sp[i].trule.targetCrownPercent;
        }

        for (int i = 0; i < nspecies; i++) {
            if (sumPercent > 0) {
                sp[i].trule.targetCrownPercent = 100.0 * sp[i].trule.targetCrownPercent / sumPercent;
            } else {
                sp[i].trule.targetCrownPercent = 0;
            }
        }

        for (int i = 0; i < nspecies; i++) {
            sp[i].vol = 0.0;
            sp[i].vhaout = 0.0;
            for (int j = startindex; j >= 0; j--) {
                if (tr[j].code != sp[i].code) {
                    continue;
                }
                if (tr[j].d >= 7.0) {
                    if (tr[j].out < 1 || tr[j].out == year - 1) {
                        sp[i].vol += tr[j].fac * tr[j].v;
                    }
                    if (tr[j].out == year) {
                        sp[i].vhaout += tr[j].fac * tr[j].v;
                    }
                }
            }
            sp[i].vol = sp[i].vol / size;
            sp[i].vhaout = sp[i].vhaout / size;
        }
        //Für Arten mit nur einem Baum
        for (int n = 0; n < nspecies; n++) {
            if (sp[n].d100 <= 0) {
                sp[n].d100 = sp[n].dg;
            }
            if (sp[n].h100 <= 0) {
                sp[n].h100 = sp[n].hg;
            }
            if (sp[n].h100age <= 0) {
                for (int j = startindex; j >= 0; j--) {
                    if (tr[j].d >= 7.0 && tr[j].code == sp[n].code) {
                        sp[n].h100age = tr[j].age;
                    }
                }
            }
        }
        if (nspecies > 0 /*&& sp[0].hg > 1.3*/) {
            calculateDegreeOfStockingAndSpeciesPercentage();
        }
        // layer.setAssmannConfiguratedtoTrees(this);
    }

    /**
     * this routine fills in the missing data, it should be started, after all
     * trees are added to the stand
     */
    public void missingData() {
        // missing cornerpoints
        if (ncpnt <= 0) {
            double l = Math.sqrt(size * 10000.0);
            addcornerpoint("E1", 0.0, l, 0.0);
            addcornerpoint("E2", l, l, 0.0);
            addcornerpoint("E3", l, 0.0, 0.0);
            addcornerpoint("E1", 0.0, 0.0, 0.0);
            center.no = "polygon";
            center.x = l / 2.0;
            center.y = l / 2.0;
            center.z = 0.0;
        }
        // missing center point, calculate a center
        if (!center.no.contains("polygon") && !center.no.contains("circle")) {
            double xm = 0.0;
            double ym = 0.0;
            double zm = 0.0;
            for (int i = 0; i < ncpnt; i++) {
                xm = xm + cpnt[i].x;
                ym = ym + cpnt[i].y;
                zm = zm + cpnt[i].z;
            }
            center.x = xm / ncpnt;
            center.y = ym / ncpnt;
            center.z = zm / ncpnt;
            center.no = "polygon";
        }

        // 0. update st,bha die Bestandesgrundfläche
        bha = 0.0;
        for (int i = 0; i < ntrees; i++) {
            if (tr[i].out < 1 && tr[i].d >= 7.0) {
                bha = bha + tr[i].fac * Math.PI * (tr[i].d / 200.0) * (tr[i].d / 200.0);
            }
        }

        // 1. check if all trees have a height, if not generate height diameter curve 
        int missingHeight = getMissingHeight(0);

        if (missingHeight >= 0) {
            for (int i = 0; i < nspecies; i++) {
                sp[i].updateSpecies(this);
            }
        }

        missingHeight = getMissingHeight(0);

        if (missingHeight >= 0) {
            //
            // average and dominant stand height
            double n100 = size * 100.0;  // n100 according to stand size

            if (n100 > 0) {
                double jj = 0;
                int k = 0;
                while (jj < n100 && k < ntrees) {
                    if (tr[k].out < 1 && tr[k].d >= 7.0) {
                        d100 = d100 + tr[k].fac * Math.PI * (tr[k].d / 200.0) * (tr[k].d / 200.0);
                        jj = jj + tr[k].fac;
                    }
                    k++;
                }
                d100 = 200 * Math.sqrt(d100 / (Math.PI * jj));
            }

            // calculate diameter-height curve
            int ndh = 0; // number of diameter and height values

            for (int j = 0; j < ntrees; j++) {
                if (tr[j].h >= 1.3 && tr[j].out < 1) {
                    ndh++;
                }
            }

            // unnütig??  vgl missingHeight weiter oben!!!
            int missingheights = getMissingHeight(0);

            // number of diameter-height values > 5 then height curve
            if (ndh > 5) {
                int k = ndh / 1000; //if there are more than 1000 ndh then use only every k th tree
                k = k + 1;
                ndh = 0;
                for (int j = 0; j < ntrees; j = j + k) {
                    if (tr[j].h >/*=*/ 1.3 && tr[j].out < 1) {
                        ndh++;
                    }
                }

                HeightCurve m = new HeightCurve();
                m.heightcurve();

                for (int j = 0; j < ntrees; j = j + k) {
                    if (tr[j].h >/*=*/ 1.3 && tr[j].out < 1) {
                        m.adddh(sp[0].spDef.heightCurve, ndh, tr[j].d, tr[j].h);
                    }
                }

                m.start();

                if (dg > 0) {
                    hg = m.hwert(sp[0].spDef.heightCurve, dg);
                }
                if (d100 > 0) {
                    h100 = m.hwert(sp[0].spDef.heightCurve, d100);
                }

                if (missingheights > 0) {
                    for (int j = 0; j < ntrees; j++) {
                        if (tr[j].h < 1.3) {
                            tr[j].h = m.hwert(sp[0].spDef.heightCurve, tr[j].d);
                        }
                    }
                }
            }

            // Alternative if there are only a few  1 to 4 heights
            // Use uniform height curve 
            double dk = 0.0;
            double hk = 0.0;
            if (ndh > 0 && ndh <= 5) {
                for (int j = 0; j < ntrees; j++) {
                    if (tr[j].h > 1.3 && tr[j].out < 1) {
                        dk = tr[j].d;
                        hk = tr[j].h;
                    }
                }
                double dgmerk = sp[0].dg;
                double hgmerk = sp[0].hg;
                Tree tree = new Tree();
                tree.d = d100;
                tree.sp = sp[0];
                tree.sp.dg = dk;
                tree.sp.hg = hk;
                FunctionInterpreter fi = new FunctionInterpreter();
                h100 = fi.getValueForTree(tree, tree.sp.spDef.uniformHeightCurveXML);
                tree.d = dg;
                hg = fi.getValueForTree(tree, tree.sp.spDef.uniformHeightCurveXML);

//               UniformHeight ufh = new UniformHeight();
//               hg=ufh.height(sp[0],dg,dk,hk, this);
//               h100=ufh.height(sp[0],d100,dk,hk, this);
                if (missingheights > 0) {
                    for (int j = 0; j < ntrees; j++) {
                        if (tr[j].h < 1.3) {
                            tree.d = tr[j].d;
                            tr[j].h = fi.getValueForTree(tree, tree.sp.spDef.uniformHeightCurveXML);
                            //tr[j].h=ufh.height(tr[j].sp,tr[j].d,dk,hk, this);
                        }
                    }
                }
                sp[0].dg = dgmerk;
                sp[0].hg = hgmerk;

            }
        }
        //
        // In case a site index is given to one or more trees, than all trees without
        // site index of that species will get the average site index
        for (int jj = 0; jj < nspecies; jj++) {
            double avSiteIndex = 0.0;
            double nSiteIndex = 0.0;
            for (int j = 0; j < ntrees; j++) {
                if (tr[j].code == sp[jj].code && tr[j].si > 0) {
                    avSiteIndex = avSiteIndex + tr[j].si * tr[j].fac;
                    nSiteIndex = nSiteIndex + tr[j].fac;
                }
            }
            if (nSiteIndex > 0) {
                avSiteIndex = avSiteIndex / nSiteIndex;
                for (int j = 0; j < ntrees; j++) {
                    if (tr[j].code == sp[jj].code && tr[j].si < 0) {
                        tr[j].si = avSiteIndex;
                    }
                }
                sp[jj].hbon = avSiteIndex;
            }
        }
        //       
        // generate missing data of trees, sorting by height for dynamical crown base
        //
        sortbyh();
        for (int j = 0; j < ntrees; j++) {
            tr[j].setMissingData();
        }
        sortbyd();
        selectNeighborTrees();
        // competition of trees
        /*for (int j = 0; j < ntrees; j++) {
            tr[j].updateCompetition();
        }*/
        scaleMan.updateCompetition();
    }

    /**
     * detect species and initialize tree species addspecies is used to add a
     * species container if a tree is added and the species is not existing this
     * routine gets called by addtree
     *
     * @param t
     * @return a Species object
     * @throws treegross.base.SpeciesNotDefinedException
     */
    public Species addspecies(Tree t) throws SpeciesNotDefinedException {
        int merk = -9;  //species does not exists
        if (ntrees == 0 && nRegeneration == 0) {
            nspecies = 0;
        }
        for (int i = 0; i < nspecies; i++) {
            if (sp[i].code == t.code) {
                merk = i;
            }
        }
        if (merk == -9) {
            sp[nspecies] = new Species();
            sp[nspecies].code = t.code;
            sp[nspecies].size = size;
            sp[nspecies].setZero();

            sp[nspecies].spDef = sdm.getByCode(t.code);

            //if (sp[nspecies].spDef.defined==false) {
            if (sp[nspecies].spDef == null) {
                throw new SpeciesNotDefinedException(t, sdm);
            }
            sp[nspecies].h100 = 0.0;
            sp[nspecies].hg = 0.0;
            sp[nspecies].spDef.modelRegion = modelRegion;
            sp[nspecies].loadTrRuleDefault();
            merk = nspecies;
            nspecies++;
        }
        return sp[merk];
    }

    public void setProgramDir(File dir) {
        programDir = dir;
    }

    public void setModelRegion(String s) {
        modelRegion = s;
    }

    /**
     * Return true or false if the species is already defined
     *
     * @return true is the species is defined
     */
    public boolean getSpeciesDefinedTrue() {
        for (int i = 0; i < nspecies; i++) {
            if (sp[i].spDef.defined == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return the undefined species code as string devided by a blank
     *
     * @return a string containig all undefined species codes
     */
    public String getSpeciesUndefinedCode() {
        String str = "Species code:";
        for (int i = 0; i < nspecies; i++) {
            if (sp[i].spDef.defined == false) {
                str += " " + sp[i].code;
            }
        }
        return str;
    }

    /*added by jhansen*/
    public void notifyStandChanged(String action, Object sender) {
        if (!notifyListeners) {
            return;
        }
        if (StandChangeListeners.isEmpty()) {
            return;
        }
        StandChangeEvent sce = new StandChangeEvent(this, "StandChangeEvent", action, sender);
        for (Object vtemp1 : StandChangeListeners.toArray()) {
            StandChangeListener target;
            target = (StandChangeListener) vtemp1;
            target.StandChanged(sce);
        }
    }

    /*added by jhansen*/
    public void addStandChangeListener(StandChangeListener scl) {
        if (StandChangeListeners.contains(scl)) {
            return;
        }
        StandChangeListeners.add(scl);
    }

    /*added by jhansen*/
    public void removeAllStandChangeListeners() {
        StandChangeListeners.clear();
    }

    /**
     * select the neighbor trees to speed up competition
     */
    public void selectNeighborTrees() {
        double eA[] = null;
        if (ntrees > 0) {
            eA = new double[tr[0].maxNeighbor];
        }
        for (int i = 0; i < ntrees; i++) {
            //if (tr[i].no.indexOf("413") >-1)
            //System.out.println("das ist er");
            if (tr[i].out < 0 || tr[i].out == year) {
                for (int k = 0; k < tr[i].maxNeighbor; k++) {
                    eA[k] = 9999.9;
                    tr[i].neighbor[k] = -9;
                }
                for (int j = 0; j < ntrees; j++) {
                    if ((tr[j].out < 0 || tr[j].out == year) && (i != j)) {
                        double entf = Math.sqrt(Math.pow((tr[i].x - tr[j].x), 2.0) + Math.pow((tr[i].y - tr[j].y), 2.0));
                        //The influence zone should be at least 2m
                        double minimumRadius = 2.0 * tr[i].cw;
                        if (minimumRadius < 2) {
                            minimumRadius = 2.0;
                        }

                        if (entf < minimumRadius && tr[j].h > tr[i].cb) {
                            int merk = -9; // hier misnus 9
                            for (int k = 0; k < tr[i].maxNeighbor; k++) {
                                if (eA[k] > entf && merk < 0) {
                                    merk = k;
                                }
                            }
                            if (merk > -1) { // hier -1??
                                for (int k = tr[i].maxNeighbor - 2; k >= merk; k--) {
                                    tr[i].neighbor[k + 1] = tr[i].neighbor[k];
                                    eA[k + 1] = eA[k];
                                }
                                tr[i].neighbor[merk] = j;
                                eA[merk] = entf;
                            }
                        }
                    }
                }
                tr[i].nNeighbor = 0;
                for (int k = 0; k < tr[i].maxNeighbor; k++) {
                    if (eA[k] < 9999.0) {
                        tr[i].nNeighbor++;
                    }
                }
            }
        }
    }

    /**
     * Calculates the degree of stocking by the measured value and the desired
     * value for basal area of all species species. The desired values come from
     * the growth modell setting of maximum density and the recommended thinning
     * factor. See also: KRAMER, H.; AKCA, A. (1982): Leitfaden für Dendrometrie
     * und Bestandesinventur. J.D. Sauerlaender's Verlag, Frankfurt, page 155.
     */
    public void calculateDegreeOfStockingAndSpeciesPercentage() {
        degreeOfDensity = 0.0;
        for (int i = 0; i < nspecies; i++) {
            if (sp[i].h100 > 1.3) {
                Tree atree = new Tree();
                atree.code = sp[i].code;
                atree.d = sp[i].d100;
                atree.h = sp[i].h100;
                atree.sp = sp[i];
                atree.no = "dummy tree for species " + sp[i].code;
                atree.st = this;
                atree.cw = atree.calculateCw();
                double gha_model = atree.calculateMaxBasalArea() * atree.getModerateThinningFactor();
                if (gha_model <= 0.0) {
                    gha_model = 1.0;
                }
                sp[i].percBA = sp[i].gha / gha_model;
                degreeOfDensity += sp[i].percBA;
            } // added by jhabsen: if h100<1.3 use hg) ???????
            else {
                Tree atree = new Tree();
                atree.code = sp[i].code;
                atree.d = sp[i].dg;
                atree.h = sp[i].hg;
                atree.sp = sp[i];
                atree.no = "dummy tree for species " + sp[i].code;
                atree.st = this;
                atree.cw = atree.calculateCw();
                double gha_model = atree.calculateMaxBasalArea() * atree.getModerateThinningFactor();
                if (gha_model <= 0.0) {
                    gha_model = 1.0;
                }
                sp[i].percBA = sp[i].gha / gha_model;
                degreeOfDensity += sp[i].percBA;
            }
        }
        if (degreeOfDensity > 0) {
            for (int i = 0; i < nspecies; i++) {
                sp[i].percBA = 100.0 * sp[i].percBA / degreeOfDensity;
            }
        }
    }

    /**
     * added by jhansen delete regeneration trees with d<7cm and out>-1
     *
     * @return number of regeneration trees deleted
     */
    public int cleanTreeArrayReg() {
        Collection trees = getDeadRegTrees();
        List<Tree> treelist = new ArrayList<>();
        for (int i = 0; i < ntrees; i++) {
            treelist.add(tr[i]);
        }
        if (treelist.removeAll(trees)) {
            ntrees = ntrees - trees.size();
            tr = (Tree[]) treelist.toArray(new Tree[this.maxStandTrees]);
            descspecies();
            //System.out.println("N trees removed: "+ trees.size());
        }
        return trees.size();
    }
    
    /**
     *
     * @param index - the index of tree to remove
     * @param decspecies if true call method descspecies(...)
     * @return true if tree was deleted
     */
    public boolean deleteTree(int index, boolean decspecies) {
        int oldN = ntrees;
        List<Tree> treelist = new ArrayList<>();
        for (int i = 0; i < ntrees; i++) {
            if (i != index) {
                treelist.add(tr[i]);
            }
        }
        ntrees = treelist.size();
        tr = (Tree[]) treelist.toArray(new Tree[this.maxStandTrees]);
        if (decspecies) {
            descspecies();
        }
        return (oldN - ntrees == 1);
    }

    private Collection getDeadRegTrees() {
        List<Tree> treestoremove = new ArrayList<>();
        for (int i = 0; i < ntrees; i++) {
            if (tr[i].d < 7 && tr[i].out > -1) {
                treestoremove.add(tr[i]);
            }
        }
        return treestoremove;
    }

    /**
     * function allows to set the site index for all species (treeCode=0) or for
     * a specific species with the given code. SiteIndex is expressed in [m] at
     * age 100
     *
     * @param treeCode
     * @param siteIndex
     */
    public void setSiteIndex(int treeCode, double siteIndex) {
        for (int i = 0; i < ntrees; i++) {
            if (tr[i].d >= 7.0 && tr[i].out < 0 && (tr[i].code == treeCode || treeCode == 0)) {
                tr[i].si = siteIndex;
            }
        }
    }

    /**
     * function return the mean crown width for all trees (treeCode=0) or a
     * given species
     *
     * @param treeCode
     * @return avg. crown width
     */
    public double getMeanCrownWidth(int treeCode) {
        double mean = 0.0;
        int n = 0;
        for (int i = 0; i < ntrees; i++) {
            if (tr[i].d >= 7.0 && tr[i].out < 0 && (tr[i].code == treeCode || treeCode == 0)) {
                mean += tr[i].cw;
                n++;
            }
        }
        if (n > 0) {
            mean = mean / n;
        }
        return mean;
    }

    /**
     * function return the mean crown base for all trees (treeCode=0) or a given
     * species
     *
     * @param treeCode
     * @return avg. crown base
     */
    public double getMeanCrownBase(int treeCode) {
        double mean = 0.0;
        int n = 0;
        for (int i = 0; i < ntrees; i++) {
            if (tr[i].d >= 7.0 && tr[i].out < 0 && (tr[i].code == treeCode || treeCode == 0)) {
                mean += tr[i].cb;
                n++;
            }
        }
        if (n > 0) {
            mean = mean / n;
        }
        return mean;
    }

    public void executeMortality() {
        try {
            String modelPlugIn = "treegross.base.Mortality";
            PlugInMortality mo = (PlugInMortality) Class.forName(modelPlugIn).newInstance();
            mo.mortalityByInfluenceZone(this);   // check for mortality
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            LOGGER.log(Level.SEVERE, "PlugIn Mortality", e);
        }
        for (int i = 0; i < ntrees; i++) {
            if (tr[i].out < 0) {
                tr[i].ageBasedMortality();
            }
        }
    }
    
    public void notificationsEnabled(boolean on) {
        this.notifyListeners = on;
    }
}
