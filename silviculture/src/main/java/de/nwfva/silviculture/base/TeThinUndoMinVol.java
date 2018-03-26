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
public class TeThinUndoMinVol implements TreatmentElement {

    private final String name = "Undo Thinning By Minimum Volume";
    private final String label = "te_minvolume";
    private final String desc = "te_minvolume_info";

    private double aWert;

    public TeThinUndoMinVol(double aWert) {
        init(aWert);
    }

    public TeThinUndoMinVol() {
        this(10.0);
    }

    private void init(double aWert) {
        this.aWert = aWert;
    }

    //logic of treatment element:
    public void doThinning(Stand st) {
        if (st != null) {
            double wert = 0.0;
            for (int i = 0; i < st.ntrees; i++) {
                if (st.tr[i].d > 7 && st.tr[i].out == st.year && st.tr[i].outtype > 1) {
                    wert = wert + st.tr[i].v * st.tr[i].fac;
                }
            }
            wert = wert / st.size;
            if (wert < aWert) {
                for (int i = 0; i < st.ntrees; i++) {
                    if (st.tr[i].d > 7 && st.tr[i].out == st.year && st.tr[i].outtype > 1) {
                        st.tr[i].out = -1;
                        st.tr[i].outtype = 0;
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
        params.add(new TreatmentElementParameter("aWert", RBHolder.getResourceBundle().getString("te_minvolume"), RBHolder.getResourceBundle().getString("te_minvol_info"), aWert));
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
        init(10.0);
    }
       
}
