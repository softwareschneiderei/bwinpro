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
public class TeThinCTByAWert implements TreatmentElement {

    private final String name = "Thin Crop Trees by A-Wert";
    private final String label = "te_awert";
    private final String desc = "te_awert_info";

    private double aWert;

    public TeThinCTByAWert(double aWert) {
        init(aWert);
    }

    public TeThinCTByAWert() {
        this(4.0);
    }

    private void init(double aWert) {
        this.aWert = aWert;
    }

    //logic of treatment element:
    public void doThinning(Stand st) {
        if (st != null) {
            double wert = 0.0;
            double ent;
            for (int i = 0; i < st.ntrees; i++) {
                if (st.tr[i].out < 0 && st.tr[i].crop) {
                    // we found a crop tree and remove now all other trees
                    for (int j = 0; j < st.ntrees; j++) {
                        if (st.tr[j].d > 7 && st.tr[j].out < 0 && st.tr[j].crop == false && st.tr[j].habitat == false) {
                            ent = Math.sqrt(Math.pow(st.tr[i].x - st.tr[j].x, 2.0) + Math.pow(st.tr[i].y - st.tr[j].y, 2.0));
                            if (ent > 0) {
                                wert = (st.tr[i].h / ent) * (st.tr[j].d / st.tr[i].d);
                            }
                            if (wert > aWert) {
                                st.tr[j].out = st.year;
                                st.tr[j].outtype = 2;
                            }
                        }
                    }
                }
            }

        }
        System.out.println(name + " executed.");
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
        doThinning(st);
    }

    @Override
    public ArrayList<TreatmentElementParameter> getRequiredParameters() {
        ArrayList<TreatmentElementParameter> params;
        params = new ArrayList<>(3);
        params.add(new TreatmentElementParameter("aWert", RBHolder.getResourceBundle().getString("te_awert"), RBHolder.getResourceBundle().getString("te_value"), aWert));
        return params;
    }

    @Override
    public void parse(Node n) {
        //ToDo Error handling
        List<Node> params = treegross.tools.XmlTool.getChilds(n, TreatmentElementParameter.class.getSimpleName());
        String aName, value;
        for (Node p : params) {
            aName = p.getAttributes().getNamedItem("name").getNodeValue();
            value = p.getAttributes().getNamedItem("value").getNodeValue();
            switch (aName) {
                case "aWert":
                    aWert = Double.parseDouble(value);
                    break;
            }
        }
    }

    @Override
    public int getGroup() {
        return TreatmentElement.GROUP_THINNING;
    }

    @Override
    public void setDefaults(SpeciesDefMap sdm, Stand st) {
          init(4.0);
    }
    
}
