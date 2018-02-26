/**
 * TreeGrOSS : class TgSaveStand saves the stand information in text file Same
 * format as the readstand format.
 *
 * Version 07-Jun-2006 (c) 2002-2006 Juergen Nagel, Northwest Forest Research
 * Station, Grätzelstr.2, 37079 Göttingen, Germany E-Mail:
 * Juergen.Nagel@nw-fva.de
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 */
package treegross.base;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import treegross.random.RandomNumber;

public class DataExchangeFormat {

    /**
     * write stand data format delimited by ; : 1. Row : stand name; 2. Row :
     * year of data measurement 3. Row : stand size in ha 4. ROW : number of
     * Corner Points ncp: 5. to ncp ROW: label,x-coordinate, y-coordinate,
     * z_koordinate Rest. Row to end : single tree values : species code Lower
     * Saxony, tree number (string), -1 = tree is alive otherwise the year of
     * mortality or cut, dbh (cm), height (m), crown base (m), crown width (m),
     * x-coordinate, y-coordinate, z-coordinate (all in m and should be positive
     * or zero)
     */

    private final static Logger LOGGER = Logger.getLogger(DataExchangeFormat.class.getName());

    public void save(Stand st, String fn) {
        try {
            int i;
            int pm = 0;
            NumberFormat f;
            f = NumberFormat.getInstance(new Locale("en", "US"));
            f.setMaximumFractionDigits(2);
            f.setMinimumFractionDigits(2);
            OutputStream os = new FileOutputStream(fn);
            PrintWriter out = new PrintWriter(new OutputStreamWriter(os));
            Date datum = new Date();
            out.println("TreeGrOSS Exchange Data :" + datum + " by Waldplaner2");
            out.println(st.standname);
            out.println(st.year + ";Jahr");
            out.println(st.size + ";Flaechengroesse ha");
            out.println(st.ncpnt + ";Eckpunkte");
            out.println("No;x;y;z;");
            for (i = 0; i < st.ncpnt; i++) {
                out.println(st.cpnt[i].no + ";" + f.format(st.cpnt[i].x) + ";"
                        + f.format(st.cpnt[i].y) + ";" + f.format(st.cpnt[i].z) + ";");
            }

            //Save simulation options
            int rge;
            //if (st.randomGrowthEffects==true) rge=1;
            rge = st.random.getRandomType();
            out.println(rge + ";Zufallseffekte Wachstum");

            int dd = 0;
            if (st.distanceDependent == true) {
                dd = 1;
            }
            out.println(dd + ";Distanzabhängigkeit");

            int ia = 0;
            if (st.ingrowthActive == true) {
                ia = 1;
            }
            out.println(ia + ";Einwuchs an");

            //Save Treatment Options (Stand -> TreatmentRulesStand)
            out.println(st.trule.treatmentType + ";Behandlungstyp");
            out.println(st.trule.maxHarvestVolume + ";Maximales Erntevolumen");
            out.println(st.trule.minHarvestVolume + ";Minimales Erntevolumen");
            out.println(st.trule.maxThinningVolume + ";Maximales Durchforstungsvolumen");
            out.println(st.trule.minThinningVolume + ";Minimales Durchforstungsvolumen");
            out.println(st.trule.minOutVolume + ";Minimales Entnahmevolumen (Ernte+Durchforstung)");
            out.println(st.trule.maxOutVolume + ";Maximales Entnahmevolumen (Ernte+Durchforstung)");
            out.println(st.trule.standType + ";StandType");
            out.println(st.trule.targetType + ";TargetType");
            out.println(st.trule.nHabitat + ";Anzahl der Habitatbäume");
            out.println(st.trule.treatmentStep + ";Jahre zwischen den Eingriffen");
            out.println(st.trule.harvestingYears + ";Endnutzung startete vor x Jahren");
            out.println(st.trule.maxHarvestingPeriode + ";Endnutzungszeitraum maximal X Jahre");
            if (st.trule.harvestLayerFromBelow == true) {
                pm = 1;
            }
            out.println(pm + ";1-> Endnutzung einer zielstarken schicht von unten, 0-> Endnutzung einer zielstarken schicht von oben");
            if (st.trule.selectCropTrees == true) {
                pm = 1;
            }
            out.println(pm + ";1-> Z-Baumauswahl an, 0-> Z-Baumauswahl aus");
            if (st.trule.reselectCropTrees == true) {
                pm = 1;
            }
            out.println(pm + ";1-> Z-Baumnachwahl an, 0-> Z-Baumnachauswahl aus");
            if (st.trule.selectCropTreesOfAllSpecies == true) {
                pm = 1;
            }
            out.println(pm + ";1-> Z-Bäume auch von Arten, die nicht dem Zieltyp entsprechen, 0-> Z-Bäume nur von Arten, die dem Zieltyp entsprechen");
            if (st.trule.releaseCropTrees == true) {
                pm = 1;
            }
            out.println(pm + ";1-> Z-Baumfreistellung an, 0-> Z-Baumfreistellung aus");
            if (st.trule.releaseCropTreesSpeciesDependent == true) {
                pm = 1;
            }
            out.println(pm + ";1-> Z-Baumfreistellung entsprechend des Zieltyps, 0-> Z-Baumfreistellung entsprechend der Bedrängungssituation");
            if (st.trule.cutCompetingCropTrees == true) {
                pm = 1;
            }
            out.println(pm + ";1-> Entnahme von sich bedrängenden Z-Bäumen, 0-> keine Entnahme von sich bedrängenden Z-Bäumen");
            if (st.trule.thinArea == true) {
                pm = 1;
            }
            out.println(pm + ";1-> Durchforstung der Zwischenräume an, 0-> Durchforstung der Zwischenräume aus");
            if (st.trule.thinAreaSpeciesDependent == true) {
                pm = 1;
            }
            out.println(pm + ";1-> Durchforstung der Zwischenräume entsprechend des Zieltyps an, 0-> Durchforstung der Zwischenräume entsprechend der Bedr�ngungssituation");
            out.println(st.trule.thinningIntensityArea + ";Freistellungsgrad der Zwischenräume");
            out.println(st.trule.typeOfHarvest + ";Art der Endnutzung (0=Zielstärke, 1=Dg, 2=Alter");
            out.println(st.trule.lastTreatment + ";Die letzte Maßnahme wurde im Jahr X ausgeführt");
            if (st.trule.selectHabiatPart == true) {
                pm = 1;
            }
            out.println(pm + "; für eine Resthabitatbaumanzahl <0 wird ein weiterer Habitatbaum selektiert");
            if (st.trule.protectMinorities == true) {
                pm = 1;
            }
            out.println(pm + ";Schutz von Minderheiten");

            //Save treatment options for target main and secondary species
            // Sort species that the species with highest Targetpercent will be first
            out.println(st.nspecies + ";Anzahl Arten");
            Species sptemp;
            for (int a = 0; a < st.nspecies - 1; a++) {
                for (int b = a + 1; b < st.nspecies; b++) {
                    if (st.sp[a].trule.targetCrownPercent < st.sp[b].trule.targetCrownPercent) {
                        sptemp = st.sp[a];
                        st.sp[a] = st.sp[b];
                        st.sp[b] = sptemp;
                    }
                }
            }

            for (int n = 0; n < st.nspecies; n++) {
                out.println(st.sp[n].code + ";Baumart " + n);
                out.println(st.sp[n].trule.thinningIntensity + ";Freistellungsgrad Baumart " + n);
                out.println(st.sp[n].trule.targetDiameter + ";Zieldurchmesser Baumart " + n);
                out.println(st.sp[n].trule.targetDiameterLayer + ";Zieldurchmesser Baumschicht " + n);
                out.println(st.sp[n].trule.targetAgeLayer + ";Zielalter Baumschicht " + n);
                out.println(st.sp[n].trule.targetCrownPercent + ";Zielprozent Baumart " + n);
                out.println(st.sp[n].trule.minCropTreeHeight + ";Mindesthöhe Durchforstung Baumart " + n);
                out.println(st.sp[n].trule.maxAge + ";Maximales Alter Baumart " + n);
                out.println(st.sp[n].trule.numberCropTreesWanted + ";Anzahl Z-Bäume/ha" + n);
                out.println(st.sp[n].trule.targetRang + ";Rang der Art im Zielbestand" + n);
            }
            out.println("Code;N;No;Age;DBH;Height;Site index;Crown Base;Crown width;alive;"
                    + "Removal Code;x-Coord.;y-Coord.;z-Coord.;Crop tree; Temp Crop tree, Habitat tree");
            for (i = 0; i < st.ntrees; i++) {
                if ("".equals(st.tr[i].no)) {
                    st.tr[i].no = " ";
                }

                int zb = 0;
                int tzb = 0;
                int hb = 0;

                if (st.tr[i].crop == true) {
                    zb = 1;
                }
                if (st.tr[i].tempcrop == true) {
                    tzb = 1;
                }

                if (st.tr[i].habitat == true) {
                    hb = 1;
                }

                out.println(st.tr[i].code + ";1;" + st.tr[i].no + ";" + st.tr[i].age + ";"
                        + f.format(st.tr[i].d) + ";" + f.format(st.tr[i].h) + ";"
                        + f.format(st.tr[i].si) + ";"
                        + f.format(st.tr[i].cb) + ";" + f.format(st.tr[i].cw) + ";"
                        + +st.tr[i].out + ";" + st.tr[i].outtype + ";"
                        + f.format(st.tr[i].x) + ";" + f.format(st.tr[i].y) + ";" + f.format(st.tr[i].z)
                        + ";" + zb + ";" + tzb + ";" + hb);
            }

            out.close();
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "treegross", e);
        }
    }

    public void read(Stand st, String fn) {
        try {
            int pm;
            String s;
            st.newStand();
            BufferedReader in
                    = new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(fn)));

            //s=in.readLine();
            in.readLine();
            st.addName(in.readLine());
            s = in.readLine();

            //			System.out.println("Test  :"+s);
            //			Boolean B = new Boolean();
            //			boolean b=new Boolean(s).booleanValue();
            //			System.out.println(s+" Test2  :"+b);
            //			
            StringTokenizer stx;
            String delim = ";";
            stx = new StringTokenizer(s, delim);
            st.year = Integer.parseInt(stx.nextToken());
            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.addsize(Double.parseDouble(stx.nextToken()));

            // read line with corner coordinates, if not existant than first number =0	
            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            int ii, nx;
            nx = Integer.parseInt(stx.nextToken());
            /*s=*/
            in.readLine(); //read over Header
            //            System.out.println("Eckpunkte  "+nx);
            for (ii = 0; ii < nx; ii++) {
                s = in.readLine();
                stx = new StringTokenizer(s, delim);
                st.addcornerpoint(stx.nextToken(), Double.parseDouble(stx.nextToken()), Double.parseDouble(stx.nextToken()),
                        Double.parseDouble(stx.nextToken()));
            }

            //read simulation options
            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            int rge = Integer.parseInt(stx.nextToken());

            if (rge == 1) {
                st.random.setRandomType(RandomNumber.PSEUDO);
            } else if (rge == 1) {
                st.random.setRandomType(RandomNumber.OFF);
            } else {
                st.random.setRandomType(rge);
            }

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            int dd = Integer.parseInt(stx.nextToken());
            st.distanceDependent = dd == 1;

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            int ia = Integer.parseInt(stx.nextToken());
            st.ingrowthActive = ia == 1;

            //read treatment options 
            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.treatmentType = Integer.parseInt(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.maxHarvestVolume = Double.parseDouble(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.minHarvestVolume = Double.parseDouble(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.maxThinningVolume = Double.parseDouble(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.minThinningVolume = Double.parseDouble(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.minOutVolume = Double.parseDouble(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.maxOutVolume = Double.parseDouble(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.standType = Integer.parseInt(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.targetType = Integer.parseInt(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.nHabitat = Double.parseDouble(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.treatmentStep = Integer.parseInt(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.harvestingYears = Integer.parseInt(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.maxHarvestingPeriode = Integer.parseInt(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            pm = Integer.parseInt(stx.nextToken());
            st.trule.harvestLayerFromBelow = pm == 1;

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            pm = Integer.parseInt(stx.nextToken());
            if (pm == 1) {
                st.trule.selectCropTrees = true;
            } else {
                st.trule.harvestLayerFromBelow = false;
            }

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            pm = Integer.parseInt(stx.nextToken());
            if (pm == 1) {
                st.trule.reselectCropTrees = true;
            } else {
                st.trule.harvestLayerFromBelow = false;
            }

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            pm = Integer.parseInt(stx.nextToken());
            if (pm == 1) {
                st.trule.selectCropTreesOfAllSpecies = true;
            } else {
                st.trule.harvestLayerFromBelow = false;
            }

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            pm = Integer.parseInt(stx.nextToken());
            if (pm == 1) {
                st.trule.releaseCropTrees = true;
            } else {
                st.trule.harvestLayerFromBelow = false;
            }

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            pm = Integer.parseInt(stx.nextToken());
            if (pm == 1) {
                st.trule.releaseCropTreesSpeciesDependent = true;
            } else {
                st.trule.harvestLayerFromBelow = false;
            }

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            pm = Integer.parseInt(stx.nextToken());
            if (pm == 1) {
                st.trule.cutCompetingCropTrees = true;
            } else {
                st.trule.harvestLayerFromBelow = false;
            }

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            pm = Integer.parseInt(stx.nextToken());
            if (pm == 1) {
                st.trule.thinArea = true;
            } else {
                st.trule.harvestLayerFromBelow = false;
            }

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            pm = Integer.parseInt(stx.nextToken());
            if (pm == 1) {
                st.trule.thinAreaSpeciesDependent = true;
            } else {
                st.trule.harvestLayerFromBelow = false;
            }

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.thinningIntensityArea = Double.parseDouble(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.typeOfHarvest = Integer.parseInt(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.lastTreatment = Integer.parseInt(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            pm = Integer.parseInt(stx.nextToken());
            if (pm == 1) {
                st.trule.selectHabiatPart = true;
            } else {
                st.trule.harvestLayerFromBelow = false;
            }

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            pm = Integer.parseInt(stx.nextToken());
            st.trule.protectMinorities = pm == 1;

            //get number of species
            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            int nspecies = Integer.parseInt(stx.nextToken());

            //Read treatment options for species            
            int codeSp[] = new int[nspecies];
            double tISp[] = new double[nspecies];
            double tdSp[] = new double[nspecies];
            double tdlSp[] = new double[nspecies];
            double talSp[] = new double[nspecies];
            double tcpSp[] = new double[nspecies];
            double cthSp[] = new double[nspecies];
            double maxAgeSp[] = new double[nspecies];
            int nCT[] = new int[nspecies];
            int tR[] = new int[nspecies];

            //Read treatment options for target main species  
            for (int i = 0; i < nspecies; i++) {
                s = in.readLine();
                stx = new StringTokenizer(s, delim);
                codeSp[i] = Integer.parseInt(stx.nextToken());

                s = in.readLine();
                stx = new StringTokenizer(s, delim);
                tISp[i] = Double.parseDouble(stx.nextToken());

                s = in.readLine();
                stx = new StringTokenizer(s, delim);
                tdSp[i] = Double.parseDouble(stx.nextToken());

                s = in.readLine();
                stx = new StringTokenizer(s, delim);
                tdlSp[i] = Double.parseDouble(stx.nextToken());

                s = in.readLine();
                stx = new StringTokenizer(s, delim);
                talSp[i] = Double.parseDouble(stx.nextToken());

                s = in.readLine();
                stx = new StringTokenizer(s, delim);
                tcpSp[i] = Double.parseDouble(stx.nextToken());

                s = in.readLine();
                stx = new StringTokenizer(s, delim);
                cthSp[i] = Double.parseDouble(stx.nextToken());

                s = in.readLine();
                stx = new StringTokenizer(s, delim);
                maxAgeSp[i] = Double.parseDouble(stx.nextToken());

                s = in.readLine();
                stx = new StringTokenizer(s, delim);
                nCT[i] = Integer.parseInt(stx.nextToken());

                s = in.readLine();
                stx = new StringTokenizer(s, delim);
                tR[i] = Integer.parseInt(stx.nextToken());
            }

            /*s=*/            in.readLine();
            while (true) {
                s = in.readLine();
                //System.out.println("DataReadIn"+s);                
                if (s == null) {
                    break;
                }

                stx = new StringTokenizer(s, delim);
                int code = Integer.parseInt(stx.nextToken());
                double numberoftrees = Double.parseDouble(stx.nextToken());
                //int numberoftrees = Integer.parseInt(stx.nextToken());
                String no = stx.nextToken();
                int age = Integer.parseInt(stx.nextToken());
                double dbh = Double.parseDouble(stx.nextToken());
                double height = Double.parseDouble(stx.nextToken());
                double site = Double.parseDouble(stx.nextToken());
                double crbase = Double.parseDouble(stx.nextToken());
                double crwidth = Double.parseDouble(stx.nextToken());
                int out = Integer.parseInt(stx.nextToken());
                int outtype = Integer.parseInt(stx.nextToken());
                double x = Double.parseDouble(stx.nextToken());
                double y = Double.parseDouble(stx.nextToken());
                double z = Double.parseDouble(stx.nextToken());
                int zb = Integer.parseInt(stx.nextToken());
                int tzb = Integer.parseInt(stx.nextToken());
                int hb = Integer.parseInt(stx.nextToken());
                int ntimes = (int) numberoftrees;
                //if (z < 0.0 || z == null) z=0.0;
                // Check if weighted tree or single trees; weighted trees are i.e. from inventory plots
                // with concentric circles or from gaugle sampling
                if ((numberoftrees - ntimes) > 1) {
                    st.addtreefac(code, no, age, out, dbh, height, crbase, crwidth,
                            site, x, y, z, zb, tzb, hb, numberoftrees);
                } else {
                    for (int i = 0; i < numberoftrees; i++) {
                        st.addtree(code, no, age, out, dbh, height, crbase, crwidth,
                                site, x, y, z, zb, tzb, hb);
                    }

                }
                // Tree which only add a height value, but are not on the plot                         
                if (no.compareTo("nurH") == 0 && numberoftrees == 0) {
                    st.addtree(code, no, age, out, dbh, height, crbase, crwidth,
                            site, x, y, z, zb, tzb, hb);
                }
            }

            for (int i = 0; i < st.nspecies; i++) {
                for (int j = 0; j < nspecies; j++) {
                    if (st.sp[i].code == codeSp[j]) {
                        st.sp[i].trule.targetDiameter = tdSp[j];
                        st.sp[i].trule.thinningIntensity = tISp[j];
                        st.sp[i].trule.targetDiameterLayer = tdlSp[j];
                        st.sp[i].trule.targetAgeLayer = talSp[j];
                        st.sp[i].trule.targetCrownPercent = tcpSp[j];
                        st.sp[i].trule.minCropTreeHeight = cthSp[j];
                        st.sp[i].trule.maxAge = maxAgeSp[j];
                        st.sp[i].trule.numberCropTreesWanted = nCT[j];
                        st.sp[i].trule.targetRang = tR[j];
                    }

                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "treegross", e);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "treegross", e);
        } catch (SpeciesNotDefinedException e) {
            LOGGER.log(Level.SEVERE, "treegross", e);
        }
    }

    public double targetDiameter;
    /**
     * diameter for start of harvesting periode of layer (cm)
     */
    public double targetDiameterLayer;
    /**
     * age for start of harvesting periode of layer (years)
     */
    public double targetAgeLayer;
    /**
     * target percentage of crown surface of living trees (%)
     */
    public double targetCrownPercent;
    /**
     * Min height for croptreeselection (m)
     */
    public double minCropTreeHeight;
    /**
     * max age before harvested (years)
     */
    public double maxAge;
    /**
     * number of desired crop trees [st/ha]
     */
    public int numberCropTreesWanted;
    /**
     * shows the rang of this species in target stand 1= main species in target
     * stand type 2= secondary species 1 in target stand type 3= secondary
     * species 2 in target stand type 0= not relevant in target stand type
     */
    public int targetRang;

    /**
     * 0= normal, 0.125 high, -0.25= low influeces definition of crop tree
     * competion: 0: tree can be a taken out as competitor, if its crown is
     * tangent or overlapping to a crop trees crown >0: (temp)crop tree will be
     * is freed from competitors in a radius around its crown
     * (radius=thinningIntensity*crownwidth of crop tree) (1= (temp)crop tree
     * will be is freed from competitors in a radius of its crownwidth around
     * its crown)
     * <0: tree can be a taken out as competitor, if its crown is overlapping to
     * a crop trees crown more than thinningIntensity*crownwidth of pressed crop
     * tree (-1= full overlapp is allowed -> no tree will be taken out)
     * !thinning Intensity can only be show variation in ammont of thinned
     * trees, if amount for thinng is high enough (maxThinningVolume and
     * maxOutVolume)!
     */
    public double thinningIntensity;

    public void readOldFormat3(Stand st, String fn) {
        try {

            String s;
            st.newStand();
            BufferedReader in
                    = new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(fn)));

            /*s=*/            in.readLine();
            st.addName(in.readLine());
            s = in.readLine();

            //			System.out.println("Test  :"+s);
            //			Boolean B = new Boolean();
            //			boolean b=new Boolean(s).booleanValue();
            //			System.out.println(s+" Test2  :"+b);
            //			
            StringTokenizer stx;
            String delim = ";";
            stx = new StringTokenizer(s, delim);
            st.year = Integer.parseInt(stx.nextToken());
            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.addsize(Double.parseDouble(stx.nextToken()));

            // read line with corner coordinates, if not existant than first number =0	
            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            int ii, nx;
            nx = Integer.parseInt(stx.nextToken());
            /*s=*/
            in.readLine(); //read over Header
            //            System.out.println("Eckpunkte  "+nx);
            for (ii = 0; ii < nx; ii++) {
                s = in.readLine();
                stx = new StringTokenizer(s, delim);
                st.addcornerpoint(stx.nextToken(), Double.parseDouble(stx.nextToken()), Double.parseDouble(stx.nextToken()),
                        Double.parseDouble(stx.nextToken()));
            }

            //read simulation options
            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            int rge = Integer.parseInt(stx.nextToken());

            //if (rge==1) st.randomGrowthEffects=true; else st.randomGrowthEffects=false;
            if (rge == 1) {
                st.random.setRandomType(RandomNumber.PSEUDO);
            } else if (rge == 1) {
                st.random.setRandomType(RandomNumber.OFF);
            } else {
                st.random.setRandomType(rge);
            }

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            int dd = Integer.parseInt(stx.nextToken());
            st.distanceDependent = dd == 1;

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            int ia = Integer.parseInt(stx.nextToken());
            st.ingrowthActive = ia == 1;

            //read treatment options
            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.treatmentType = Integer.parseInt(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.maxHarvestVolume = Double.parseDouble(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.minHarvestVolume = Double.parseDouble(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.maxThinningVolume = Double.parseDouble(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.minThinningVolume = Double.parseDouble(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.minOutVolume = Double.parseDouble(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.maxOutVolume = Double.parseDouble(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.nHabitat = Double.parseDouble(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            int pm = Integer.parseInt(stx.nextToken());
            st.trule.protectMinorities = pm == 1;

            //get number of species
            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            int nspecies = Integer.parseInt(stx.nextToken());

            //Read treatment options for species            
            int codeSp[] = new int[nspecies];
            double tdSp[] = new double[nspecies];
            double tcpSp[] = new double[nspecies];
            double cthSp[] = new double[nspecies];
            double maxAgeSp[] = new double[nspecies];
            double thIntensSp[] = new double[nspecies];

            //Read treatment options for target main species  
            for (int i = 0; i < nspecies; i++) {
                s = in.readLine();
                stx = new StringTokenizer(s, delim);
                codeSp[i] = Integer.parseInt(stx.nextToken());

                s = in.readLine();
                stx = new StringTokenizer(s, delim);
                tdSp[i] = Double.parseDouble(stx.nextToken());

                s = in.readLine();
                stx = new StringTokenizer(s, delim);
                tcpSp[i] = Double.parseDouble(stx.nextToken());

                s = in.readLine();
                stx = new StringTokenizer(s, delim);
                cthSp[i] = Double.parseDouble(stx.nextToken());

                s = in.readLine();
                stx = new StringTokenizer(s, delim);
                maxAgeSp[i] = Double.parseDouble(stx.nextToken());

                s = in.readLine();
                stx = new StringTokenizer(s, delim);
                thIntensSp[i] = Double.parseDouble(stx.nextToken());
            }

            /*s=*/            in.readLine();

            while (true) {
                s = in.readLine();
                //                          System.out.println("DataReadIn"+s);

                if (s == null) {
                    break;
                }

                stx = new StringTokenizer(s, delim);
                int code = Integer.parseInt(stx.nextToken());
                double numberoftrees = Double.parseDouble(stx.nextToken());
                //			  int numberoftrees = Integer.parseInt(stx.nextToken());
                String no = stx.nextToken();
                int age = Integer.parseInt(stx.nextToken());
                double dbh = Double.parseDouble(stx.nextToken());
                double height = Double.parseDouble(stx.nextToken());
                double site = Double.parseDouble(stx.nextToken());
                double crbase = Double.parseDouble(stx.nextToken());
                double crwidth = Double.parseDouble(stx.nextToken());
                int out = Integer.parseInt(stx.nextToken());
                int outtype = Integer.parseInt(stx.nextToken());
                double x = Double.parseDouble(stx.nextToken());
                double y = Double.parseDouble(stx.nextToken());
                double z = Double.parseDouble(stx.nextToken());
                int zb = Integer.parseInt(stx.nextToken());
                int tzb = Integer.parseInt(stx.nextToken());
                int hb = Integer.parseInt(stx.nextToken());
                int ntimes = (int) numberoftrees;
                // Check if weighted tree or single trees; weighted trees are i.e. from inventory plots
                // with concentric circles or from gaugle sampling
                if ((numberoftrees - ntimes) > 1) {
                    st.addtreefac(code, no, age, out, dbh, height, crbase, crwidth,
                            site, x, y, z, zb, tzb, hb, numberoftrees);
                } else {
                    for (int i = 0; i < numberoftrees; i++) {
                        st.addtree(code, no, age, out, dbh, height, crbase, crwidth,
                                site, x, y, z, zb, tzb, hb);
                    }

                }
                // Tree which only add a height value, but are not on the plot                         
                if (no.compareTo("nurH") == 0 && numberoftrees == 0) {
                    st.addtree(code, no, age, out, dbh, height, crbase, crwidth,
                            site, x, y, z, zb, tzb, hb);
                }
            }

            for (int i = 0; i < st.nspecies; i++) {
                for (int j = 0; j < nspecies; j++) {
                    if (st.sp[i].code == codeSp[j]) {
                        st.sp[i].trule.targetDiameter = tdSp[j];
                        st.sp[i].trule.targetCrownPercent = tcpSp[j];
                        st.sp[i].trule.minCropTreeHeight = cthSp[j];
                        st.sp[i].trule.maxAge = maxAgeSp[j];
                        st.sp[i].trule.thinningIntensity = thIntensSp[j];
                    }

                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "treegross", e);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "treegross", e);
        } catch (SpeciesNotDefinedException e) {
            LOGGER.log(Level.SEVERE, "treegross", e);
        }
    }

    public void readOldFormat2(Stand st, String fn) {
        try {

            String s;
            st.ntrees = 0;
            st.nspecies = 0;
            st.ncpnt = 0;

            BufferedReader in
                    = new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(fn)));

            /*s=*/            in.readLine();
            st.addName(in.readLine());
            s = in.readLine();

            //			System.out.println("Test  :"+s);
            //			Boolean B = new Boolean();
            //			boolean b=new Boolean(s).booleanValue();
            //			System.out.println(s+" Test2  :"+b);
            //			
            StringTokenizer stx;
            String delim = ";";
            stx = new StringTokenizer(s, delim);
            st.year = Integer.parseInt(stx.nextToken());
            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.addsize(Double.parseDouble(stx.nextToken()));

            // read line with corner coordinates, if not existant than first number =0	
            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            int ii, nx;
            nx = Integer.parseInt(stx.nextToken());
            /*s=*/
            in.readLine(); //read over Header
            //            System.out.println("Eckpunkte  "+nx);
            for (ii = 0; ii < nx; ii++) {
                s = in.readLine();
                stx = new StringTokenizer(s, delim);
                st.addcornerpoint(stx.nextToken(), Double.parseDouble(stx.nextToken()), Double.parseDouble(stx.nextToken()),
                        Double.parseDouble(stx.nextToken()));
            }

            //read simulation options
            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            int rge = Integer.parseInt(stx.nextToken());
            //if (rge==1) st.randomGrowthEffects=true; else st.randomGrowthEffects=false;
            if (rge == 1) {
                st.random.setRandomType(RandomNumber.PSEUDO);
            } else if (rge == 1) {
                st.random.setRandomType(RandomNumber.OFF);
            } else {
                st.random.setRandomType(rge);
            }

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            int dd = Integer.parseInt(stx.nextToken());
            st.distanceDependent = dd == 1;

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            int ia = Integer.parseInt(stx.nextToken());
            st.ingrowthActive = ia == 1;

            //read treatment options
            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.treatmentType = Integer.parseInt(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.maxHarvestVolume = Double.parseDouble(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.minHarvestVolume = Double.parseDouble(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.maxThinningVolume = Double.parseDouble(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.minThinningVolume = Double.parseDouble(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.minOutVolume = Double.parseDouble(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.maxOutVolume = Double.parseDouble(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            for (int i = 0; i < st.nspecies; i++) {
                st.sp[i].trule.thinningIntensity = Double.parseDouble(stx.nextToken());
            }

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.trule.nHabitat = Double.parseDouble(stx.nextToken());

            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            int pm = Integer.parseInt(stx.nextToken());
            st.trule.protectMinorities = pm == 1;

            //get number of species
            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            int nspecies = Integer.parseInt(stx.nextToken());

            //Read treatment options for species            
            int codeSp[] = new int[nspecies];
            double tdSp[] = new double[nspecies];
            double tcpSp[] = new double[nspecies];
            double cthSp[] = new double[nspecies];
            double maxAgeSp[] = new double[nspecies];

            //Read treatment options for target main species  
            for (int i = 0; i < nspecies; i++) {
                s = in.readLine();
                stx = new StringTokenizer(s, delim);
                codeSp[i] = Integer.parseInt(stx.nextToken());

                s = in.readLine();
                stx = new StringTokenizer(s, delim);
                tdSp[i] = Double.parseDouble(stx.nextToken());

                s = in.readLine();
                stx = new StringTokenizer(s, delim);
                tcpSp[i] = Double.parseDouble(stx.nextToken());

                s = in.readLine();
                stx = new StringTokenizer(s, delim);
                cthSp[i] = Double.parseDouble(stx.nextToken());

                s = in.readLine();
                stx = new StringTokenizer(s, delim);
                maxAgeSp[i] = Double.parseDouble(stx.nextToken());
            }

            /*s=*/            in.readLine();

            while (true) {
                s = in.readLine();
                //System.out.println("DataReadIn"+s);                
                if (s == null) {
                    break;
                }

                stx = new StringTokenizer(s, delim);
                int code = Integer.parseInt(stx.nextToken());
                double numberoftrees = Double.parseDouble(stx.nextToken());
                //int numberoftrees = Integer.parseInt(stx.nextToken());
                String no = stx.nextToken();
                int age = Integer.parseInt(stx.nextToken());
                double dbh = Double.parseDouble(stx.nextToken());
                double height = Double.parseDouble(stx.nextToken());
                double site = Double.parseDouble(stx.nextToken());
                double crbase = Double.parseDouble(stx.nextToken());
                double crwidth = Double.parseDouble(stx.nextToken());
                int out = Integer.parseInt(stx.nextToken());
                int outtype = Integer.parseInt(stx.nextToken());
                double x = Double.parseDouble(stx.nextToken());
                double y = Double.parseDouble(stx.nextToken());
                double z = Double.parseDouble(stx.nextToken());
                int zb = Integer.parseInt(stx.nextToken());
                int tzb = Integer.parseInt(stx.nextToken());
                int hb = Integer.parseInt(stx.nextToken());
                int ntimes = (int) numberoftrees;
                // Check if weighted tree or single trees; weighted trees are i.e. from inventory plots
                // with concentric circles or from gaugle sampling
                if ((numberoftrees - ntimes) > 1) {
                    st.addtreefac(code, no, age, out, dbh, height, crbase, crwidth,
                            site, x, y, z, zb, tzb, hb, numberoftrees);
                } else {
                    for (int i = 0; i < numberoftrees; i++) {
                        st.addtree(code, no, age, out, dbh, height, crbase, crwidth,
                                site, x, y, z, zb, tzb, hb);
                    }

                }
                // Tree which only add a height value, but are not on the plot                         
                if (no.compareTo("nurH") == 0 && numberoftrees == 0) {
                    st.addtree(code, no, age, out, dbh, height, crbase, crwidth,
                            site, x, y, z, zb, tzb, hb);
                }
            }

            for (int i = 0; i < st.nspecies; i++) {
                for (int j = 0; j < nspecies; j++) {
                    if (st.sp[i].code == codeSp[j]) {
                        st.sp[i].trule.targetDiameter = tdSp[j];
                        st.sp[i].trule.targetCrownPercent = tcpSp[j];
                        st.sp[i].trule.minCropTreeHeight = cthSp[j];
                        st.sp[i].trule.maxAge = maxAgeSp[j];
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "treegross", e);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "treegross", e);
        } catch (SpeciesNotDefinedException e) {
            LOGGER.log(Level.SEVERE, "treegross", e);
        }
    }

    public void readOldFormat1(Stand st, String fn) {
        try {
            String s;
            st.ntrees = 0;
            st.nspecies = 0;
            st.ncpnt = 0;

            BufferedReader in
                    = new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(fn)));

            /*s=*/            in.readLine();
            st.addName(in.readLine());
            s = in.readLine();

            //			System.out.println("Test  :"+s);
            //			Boolean B = new Boolean();
            //			boolean b=new Boolean(s).booleanValue();
            //			System.out.println(s+" Test2  :"+b);
            //			
            StringTokenizer stx;
            String delim = ";";
            stx = new StringTokenizer(s, delim);
            st.year = Integer.parseInt(stx.nextToken());
            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            st.addsize(Double.parseDouble(stx.nextToken()));

            // read line with corner coordinates, if not existant than first number =0	
            s = in.readLine();
            stx = new StringTokenizer(s, delim);
            int ii, nx;
            nx = Integer.parseInt(stx.nextToken());
            in.readLine(); //read over Header
            // System.out.println("Eckpunkte  "+nx);
            for (ii = 0; ii < nx; ii++) {
                s = in.readLine();
                stx = new StringTokenizer(s, delim);
                st.addcornerpoint(stx.nextToken(), Double.parseDouble(stx.nextToken()), Double.parseDouble(stx.nextToken()),
                        Double.parseDouble(stx.nextToken()));
            }

            in.readLine();
            while ((s = in.readLine())!=null) {                

                stx = new StringTokenizer(s, delim);

                int code = Integer.parseInt(stx.nextToken());
                double numberoftrees = Double.parseDouble(stx.nextToken());
                //  int numberoftrees = Integer.parseInt(stx.nextToken());
                String no = stx.nextToken();
                int age = Integer.parseInt(stx.nextToken());
                double dbh = Double.parseDouble(stx.nextToken());
                double height = Double.parseDouble(stx.nextToken());
                double site = Double.parseDouble(stx.nextToken());
                double crbase = Double.parseDouble(stx.nextToken());
                double crwidth = Double.parseDouble(stx.nextToken());
                int out = Integer.parseInt(stx.nextToken());
                int outtype = Integer.parseInt(stx.nextToken());
                double x = Double.parseDouble(stx.nextToken());
                double y = Double.parseDouble(stx.nextToken());
                double z = Double.parseDouble(stx.nextToken());
                int zb = Integer.parseInt(stx.nextToken());
                int ntimes = (int) numberoftrees;

                // Check if weighted tree or single trees; weighted trees are i.e. from inventory plots
                // with concentric circles or from gaugle sampling
                if ((numberoftrees - ntimes) > 1) {
                    st.addtreefac(code, no, age, out, dbh, height, crbase, crwidth,
                            site, x, y, z, zb, 0, 0, numberoftrees);
                } else {
                    for (int i = 0; i < numberoftrees; i++) {
                        st.addtree(code, no, age, out, dbh, height, crbase, crwidth,
                                site, x, y, z, zb, 0, 0);
                    }
                }

                // Tree which only add a height value, but are not on the plot                         
                if (no.compareTo("nurH") == 0 && numberoftrees == 0) {
                    st.addtree(code, no, age, out, dbh, height, crbase, crwidth,
                            site, x, y, z, zb, 0, 0);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "treegross", e);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "treegross", e);
        } catch (SpeciesNotDefinedException e) {
            LOGGER.log(Level.SEVERE, "treegross", e);
        }
    }

    public void writeTreeTable(Stand st) {
        PrintWriter out = null;
        try {
            NumberFormat f;
            f = NumberFormat.getInstance(new Locale("en", "US"));
            f.setMaximumFractionDigits(2);
            f.setMinimumFractionDigits(2);
            out = new PrintWriter(new OutputStreamWriter(new FileOutputStream("baumtabelle.txt")));
            for (int i = 0; i < st.ntrees; i++) {
                st.tr[i].updateCrown();
            }
            for (int i = 0; i < st.ntrees; i++) {
                st.tr[i].updateCompetition();
            }
            for (int i = 1; i < st.ntrees; i++) {
                out.println(st.standname
                        + "," + f.format(st.size) + "," + st.year + "," + st.tr[i].no + "," + st.tr[i].code + "," + st.tr[i].age
                        + "," + f.format(st.tr[i].d) + "," + f.format(st.tr[i].h) + "," + f.format(st.tr[i].cb)
                        + "," + f.format(st.tr[i].cw) + "," + f.format(st.tr[i].cbLightCrown) + "," + f.format(st.tr[i].cwLightCrown)
                        + "," + f.format(st.tr[i].c66) + "," + f.format(st.tr[i].c66c) + "," + f.format(st.tr[i].x) + "," + f.format(st.tr[i].y)
                );
            }
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "treegross", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
