/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nwfva.silviculture.base;

import java.util.ArrayList;
//import java.util.List;
import org.w3c.dom.Node;
import treegross.base.SpeciesDefMap;
import treegross.base.Stand;

/**
 *
 * @author jhansen
 */
public class TeResetCropTrees implements TreatmentElement {

    private final String name = "reset crop tree selection";
    private final String label = "te_reset_croptree";
    private final String desc = "te_reset_croptree_info";

    public TeResetCropTrees() {
        init();
    }

    private void init() {

    }

    //logic of treatment element:
    public void makeSelection(Stand st) {
        //Do nothing but printing line of text to stdout:
        if (st != null) {
            for (int i = 0; i < st.ntrees; i++) {
                if (st.tr[i].crop && st.tr[i].out < 0 && st.tr[i].d >= 7.0) {
                    st.tr[i].crop = false;
                }
            }
        }
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
        makeSelection(st);
    }

    @Override
    public ArrayList<TreatmentElementParameter> getRequiredParameters() {
        ArrayList<TreatmentElementParameter> params;
        params = new ArrayList<>(0);
        return params;
    }

    @Override
    public void parse(Node n) {
    }

    @Override
    public int getGroup() {
        return TreatmentElement.GROUP_PREPARATION;
    }

    @Override
    public void setDefaults(SpeciesDefMap sdm, Stand st) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
