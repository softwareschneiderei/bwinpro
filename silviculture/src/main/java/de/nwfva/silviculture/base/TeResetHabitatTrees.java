/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nwfva.silviculture.base;

import java.util.ArrayList;
//import java.util.List;
import java.util.Locale;
import org.w3c.dom.Node;
import treegross.base.SpeciesDefMap;
import treegross.base.Stand;

/**
 *
 * @author jhansen
 */
public class TeResetHabitatTrees implements TreatmentElement {

    private final String name = "reset habitat trees";
    private final String label = "te_reset";
    private final String desc = "te_reset_habitat";

    public TeResetHabitatTrees() {
        init();
    }

    private void init() {

    }

    //logic of treatment element:
    public void makeSelection(Stand st) {
        //Do nothing but printing line of text to stdout:
        if (st != null) {
            for (int i = 0; i < st.ntrees; i++) {
                if (st.tr[i].habitat && st.tr[i].out < 0 && st.tr[i].d >= 7.0) {
                    st.tr[i].habitat = false;
                }
            }
        }
        //System.out.println(name + " executed. Habitatbäume: "  );
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
        params = new ArrayList<>(3);
        return params;
    }

    @Override
    public void parse(Node n) {
        //ToDo Error handling
        /*
         List<Node> params = treegross.tools.XmlTool.getChilds(n, TreatmentElementParameter.class.getSimpleName());
         String aName, value;
         for (Node p : params) {            
         aName = p.getAttributes().getNamedItem("name").getNodeValue();
         value = p.getAttributes().getNamedItem("value").getNodeValue();            
         //System.out.println("parsing: "+aName+" value: " +value);

         }
         */
    }

    @Override
    public int getGroup() {
        return TreatmentElement.GROUP_NAT_CONVERSATION;
    }

    @Override
    public void setDefaults(SpeciesDefMap sdm, Stand st) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
