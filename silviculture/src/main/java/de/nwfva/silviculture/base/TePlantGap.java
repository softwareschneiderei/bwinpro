/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nwfva.silviculture.base;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Node;
import treegross.base.Competition;
import treegross.base.SpeciesDefMap;
import treegross.base.SpeciesNotDefinedException;
import treegross.base.Stand;
import treegross.treatment.TreatmentElements2;

/**
 *
 * @author jhansen
 */
public class TePlantGap implements TreatmentElement {

    private final String name = "Plant gaps";
    private final String label = "plant_gaps";
    private final String desc = "plant_gaps_info";

    private double lochdurchmesser;
    private String material;
    private double verbandx;
    private double verbandy;

    public TePlantGap(double lochdurchmesser, String material, double verbandx, double verbandy) {
        init(lochdurchmesser, material, verbandx, verbandy);
    }

    public TePlantGap() {
        this(10.0, "211=0.8=30.0;", 2.0, 3.0);
    }

    private void init(double lochdurchmesser, String material, double verbandx, double verbandy) {
        this.lochdurchmesser = lochdurchmesser;
        this.material = material;
        this.verbandx = verbandx;
        this.verbandy = verbandy;
    }

    //logic of treatment element:
    public void makePlanting(Stand st) {
        //Do nothing but printing line of text to stdout:
        String[] sMaterial = material.split(";");
        int[] nArt = new int[sMaterial.length];
        double[] nArtPro = new double[sMaterial.length];
        double[] nArtBon = new double[sMaterial.length];
        int nArten = 0;
        try {
            for (int i = 0; i < sMaterial.length; i++) {
                String[] txt = sMaterial[i].split("=");
                nArt[i] = Integer.parseInt(txt[0]);
                nArtPro[i] = Double.parseDouble(txt[1]);
                nArtBon[i] = Double.parseDouble(txt[2]);
                nArten = nArten + 1;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        for (int i = 1; i < nArten; i++) {
            nArtPro[i] = nArtPro[i] + nArtPro[i - 1];
        }

        if (st != null) {
// Create a grid
            double xmin = 999999.0;
            double xmax = 0.0;
            double ymin = 999999.0;
            double ymax = 0.0;
            for (int i = 0; i < st.ncpnt; i++) {
                if (st.cpnt[i].x < xmin) {
                    xmin = st.cpnt[i].x;
                }
                if (st.cpnt[i].y < ymin) {
                    ymin = st.cpnt[i].y;
                }
                if (st.cpnt[i].x > xmax) {
                    xmax = st.cpnt[i].x;
                }
                if (st.cpnt[i].y > ymax) {
                    ymax = st.cpnt[i].y;
                }
            }
            int nx = (int) Math.round((xmax - xmin) / verbandx);
            int ny = (int) Math.round((ymax - ymin) / verbandy);
            double sradius = lochdurchmesser / 2.0;
            double cover, xp, yp;
            TreatmentElements2 te2 = new TreatmentElements2();
            Competition comp = new Competition();

// make Grid 1 by 1 m and find position with less crown coverage                  
            for (int i = 0; i < nx; i++) {
                xp = (i + 1) * verbandx + xmin;
                for (int j = 0; j < ny; j++) {
                    yp = (j + 1) * verbandy + ymin;
// Abprüfen, ob der Punkt auf der Fläche ist sonst weiterspringen
                    if (comp.pnpoly(xp, yp, st) != 0) {
// Berechnen, wieviel Überdeckung und wieviel in der Fläche mittels 1x1 Raster
                        double percInStand = comp.getPercCircleInStand(sradius, xp, yp, st);
                        double overlapArea = 0.0;
                        for (int it = 0; it < st.ntrees; it++) {
                            if (st.tr[it].out < 0) {
                                double dist = Math.pow((xp - st.tr[it].x), 2.0) + Math.pow((yp - st.tr[it].y), 2.0);
                                if (dist < 0.1) {
                                    overlapArea = 1000.0;
                                    break;
                                }
                                if (dist > 0.1 && st.tr[it].age > 5) {
                                    dist = Math.sqrt(dist);
                                    overlapArea = overlapArea + te2.overlap(sradius, st.tr[it].cw / 2.0, dist);
                                }
                            }

                        }
                        double area = percInStand * Math.PI * sradius * sradius;
                        if (area > 0) {
                            cover = 100.0 * overlapArea / area;
                        } else {
                            cover = 100.0;
                        }
//Pflanzen wenn Cover leiner 30%                            
                        if (cover < 30.0) {
                            double ran = Math.random();
                            int choArt = 0;
                            double siteindex = 30.0;
                            if (ran < nArtPro[0]) {
                                choArt = nArt[0];
                            }
                            for (int kk = 1; kk < nArten; kk++) {
                                if (ran >= nArtPro[kk - 1] && ran < nArtPro[kk]) {
                                    choArt = nArt[kk];
                                    siteindex = nArtBon[kk];
                                }
                            }

                            try {
                                if (choArt > 0) {
                                    st.addTreeFromPlanting(choArt, "p" + st.ntrees + "_" + st.year, 1, -1, 0.25, 1.0, 0.1, 2.52, siteindex, xp, yp, 0, 0, 0, 0);
                                }
                            } catch (SpeciesNotDefinedException ex) {
                                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "", ex);
                            }
                        }

                    } // Punkt im Polygon

                }
            }

        }
        System.out.println(name + " executed");
    }

    @Override
    public String toString() {
        return getLabel();
    }

    // interface overrides: 
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLabel() {
        return RBHolder.getResourceBundle().getString(label);
    }

    @Override
    public String getDescription() {
        return RBHolder.getResourceBundle().getString(desc);
    }

    @Override
    public void execute(Stand st) {
        makePlanting(st);
    }

    @Override
    public ArrayList<TreatmentElementParameter> getRequiredParameters() {
        ArrayList<TreatmentElementParameter> params;
        params = new ArrayList<>(3);
        params.add(new TreatmentElementParameter("lochdurchmesser", RBHolder.getResourceBundle().getString("te_gap_diameter"), RBHolder.getResourceBundle().getString("te_gap_diameter_info"), lochdurchmesser));
        params.add(new TreatmentElementParameter("material", RBHolder.getResourceBundle().getString("te_plants"), RBHolder.getResourceBundle().getString("te_plants_info"), material));
        params.add(new TreatmentElementParameter("verbandx", RBHolder.getResourceBundle().getString("te_plant_spacing"), RBHolder.getResourceBundle().getString("te_plants_spacing_info1"), verbandx));
        params.add(new TreatmentElementParameter("verbandy", RBHolder.getResourceBundle().getString("te_plant_spacing"), RBHolder.getResourceBundle().getString("te_plants_spacing_info2"), verbandy));
        return params;
    }

    @Override
    public void parse(Node n) {
        List<Node> params = treegross.tools.XmlTool.getChilds(n, TreatmentElementParameter.class.getSimpleName());
        String aName, value;
        for (Node p : params) {
            aName = p.getAttributes().getNamedItem("name").getNodeValue();
            value = p.getAttributes().getNamedItem("value").getNodeValue();
            switch (aName) {
                case "material":
                    material = value;
                    break;
                case "lochdurchmesser":
                    lochdurchmesser = Double.parseDouble(value);
                    break;
                case "verbandx":
                    verbandx = Double.parseDouble(value);
                    break;
                case "verbandy":
                    verbandy = Double.parseDouble(value);
                    break;
            }
        }
    }

    @Override
    public int getGroup() {
        return TreatmentElement.GROUP_OTHER;
    }

    @Override
    public void setDefaults(SpeciesDefMap sdm, Stand st) {
        init(10.0, "211=0.8=30.0;", 2.0, 3.0);
    }
    
}
