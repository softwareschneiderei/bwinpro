/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nwfva.silviculture.base;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Node;
import treegross.base.SpeciesDefMap;
import treegross.base.Stand;

/**
 *
 * @author jhansen
 */
public class TeSTrails implements TreatmentElement {

    private final String name = "skidtrails";
    private final String label = "te_skidtrails";
    private final String desc = "te_skidtrails_info";

    private int width;
    private double distance;

    public TeSTrails(int width, double distance) {
        init(width, distance);
    }

    public TeSTrails() {
        this(4, 20);
    }

    private void init(int width, double distance) {
        this.width = width;
        this.distance = distance;

    }

    //logic of treatment element:
    public void makeTrails(Stand st) {
        //Do nothing but printing line of text to stdout:
        if (st != null) {
            double xmin = Double.POSITIVE_INFINITY;
            double xmax = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < st.ncpnt; i++) {
                if (st.cpnt[i].x < xmin) {
                    xmin = st.cpnt[i].x;
                }
                if (st.cpnt[i].x > xmax) {
                    xmax = st.cpnt[i].x;
                }
            }
            xmin = xmin - distance / 2.0;
            do {
                xmin = xmin + distance;
                double x2 = xmin + width;
                for (int i = 0; i < st.ntrees; i++) {
                    if (st.tr[i].out < 0 && st.tr[i].x > xmin && st.tr[i].x < x2) {
                        st.tr[i].out = st.year;
                        st.tr[i].outBySkidtrail = true;
                        if (st.tr[i].d < st.tr[i].sp.spDef.targetDiameter) {
                            st.tr[i].outtype = 2;
                        } else {
                            st.tr[i].outtype = 3;
                        }
                    }
                }
            } while (xmin < xmax);

        }
        System.out.println(name + " executed. width: " + width + ", distance: " + distance);
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
        makeTrails(st);
    }

    @Override
    public ArrayList<TreatmentElementParameter> getRequiredParameters() {
        ArrayList<TreatmentElementParameter> params;
        params = new ArrayList<>(3);
        params.add(new TreatmentElementParameter("width", RBHolder.getResourceBundle().getString("te_width"), RBHolder.getResourceBundle().getString("te_width_info"), width));
        params.add(new TreatmentElementParameter("distance", RBHolder.getResourceBundle().getString("te_distance"), RBHolder.getResourceBundle().getString("te_distance_info"), distance));
        return params;
    }

    @Override
    public void parse(Node n) {
        List<Node> params = treegross.tools.XmlTool.getChilds(n, TreatmentElementParameter.class.getSimpleName());
        String aName, value;
        for (Node p : params) {
            aName = p.getAttributes().getNamedItem("name").getNodeValue();
            value = p.getAttributes().getNamedItem("value").getNodeValue();
            //System.out.println("parsing: "+aName+" value: " +value);
            switch (aName) {
                case "width":
                    width = Integer.parseInt(value);
                    break;
                case "distance":
                    distance = Double.parseDouble(value);
                    break;
            }
        }
    }

    @Override
    public int getGroup() {
        return TreatmentElement.GROUP_PREPARATION;
    }

    @Override
    public void setDefaults(SpeciesDefMap sdm, Stand st) {
        init(4,20);
        
    }
    
}
