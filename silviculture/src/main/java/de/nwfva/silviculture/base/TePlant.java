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
import treegross.base.FunctionInterpreter;
import treegross.base.SpeciesDefMap;
import treegross.base.SpeciesNotDefinedException;
import treegross.base.Stand;
import treegross.base.Tree;
import treegross.treatment.TreatmentElements2;

/**
 *
 * @author jhansen
 */
public class TePlant implements TreatmentElement {

    private final String name = "Plant";
    private final String label = "te_plant";
    private final String desc = "te_plant_info";

    private double schlussgrad;
    private String material;
    private double verbandx;
    private double verbandy;
    private int onetime = 0;

    public TePlant(double schlussgrad, String material, double verbandx, double verbandy) {
        init(schlussgrad, material, verbandx, verbandy);
    }

    public TePlant() {
        this(0.3, "211=0.8=32.4;", 2.0, 3.0);
    }

    private void init(double schlussgrad, String material, double verbandx, double verbandy) {
        this.schlussgrad = schlussgrad;
        this.material = material;
        this.verbandx = verbandx;
        this.verbandy = verbandy;
    }

    //logic of treatment element:
    public void makePlanting(Stand st) {
        //Do nothing but printing line of text to stdout:
        TreatmentElements2 te2 = new TreatmentElements2();

// Pflanzstring zerlegen
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
// sollcov berechnen Verband und Pflanzplatz 5qm       
        double sollcov = 0.0;
        for (int i = 0; i < nArten; i++) {
            if (verbandx * verbandy > 0.0) {
                sollcov = sollcov + 5.0 * (10000.0 / (verbandx * verbandy)) * nArtPro[i];
            }
        }
// Sollbedeckung der Verjüngung sollte zu 60% aufgelaufen sein        
        sollcov = sollcov/10000.0;
// Prozentanteile der Arten aufsummieren für die Auswahl mit random        
        for (int i = 1; i < nArten; i++) {
            nArtPro[i] = nArtPro[i] + nArtPro[i - 1];
        }
        double cov = te2.getDegreeOfCover(0, st, true);
        double covunder = te2.getDegreeOfCover(0, st, false) - cov;
        if (sollcov*0.75 > 1.0) sollcov = 1.0;

        
        if (cov <= schlussgrad && sollcov*0.75 > covunder) {
            Competition comp = new Competition();


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
                double cover, xp, yp;

// make Grid 1 by 1 m and find position with less crown coverage                  
                for (int i = 0; i < nx; i++) {
                    xp = (i + 1) * verbandx + xmin;
                    for (int j = 0; j < ny; j++) {
                        yp = (j + 1) * verbandy + ymin;
// Prüfen, ob der Pfanzplatz schon besetzt ist im Radius 1 m                        
                        boolean frei = true;
                        double ent =0.0;
                          for (int jj = 0; jj < st.ntrees; jj++) {
                              if (st.tr[jj].out < 0){
                                  ent = Math.pow(st.tr[jj].x-xp, 2.0)+ Math.pow(st.tr[jj].y-yp, 2.0);
                                  if (ent > 0.0) ent = Math.sqrt(ent);
                                  if (ent < 1.0) {
                                      frei = false;
                                      break;
                                  }
                              }
                              
                          }
                        
                        
// Abprüfen, ob der Punkt auf der Fläche ist sonst weiterspringen
                        if (comp.pnpoly(xp, yp, st) != 0 && frei) {
// Berechnen, wieviel Überdeckung und wieviel in der Fläche mittels 1x1 Raster

                            double ran = Math.random();
                            int choArt = 0;
                            double siteindex = 30.0;
                            if (ran < nArtPro[0]) {
                                choArt = nArt[0];
                                siteindex = nArtBon[0];
                            }
                            for (int kk = 1; kk < nArten; kk++) {
                                if (ran >= nArtPro[kk - 1] && ran < nArtPro[kk]) {
                                    choArt = nArt[kk];
                                    siteindex = nArtBon[kk];
                                }
                            }

                            try {
                                if (choArt > 0) {
                                    double h = 0.8 + Math.random()*0.2;
                                    st.addTreeFromPlanting(choArt, "p" + st.ntrees + "_" + st.year, 1, -1, 1.0, h, 0.1, 2.52, siteindex, xp, yp, 0, 0, 0, 0);
                               }
                            } catch (SpeciesNotDefinedException ex) {
                                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "", ex);
                            }

                        } // Punkt im Polygon

                    }
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
        params.add(new TreatmentElementParameter("schlussgrad", RBHolder.getResourceBundle().getString("te_coverage"), RBHolder.getResourceBundle().getString("te_coverage_info"), schlussgrad));
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
                case "schlussgrad":
                    schlussgrad = Double.parseDouble(value);
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
        init(10.0, "211=0.8=32.4;", 2.0, 3.0);
        onetime =0;
    }
    
}
