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
import treegross.treatment.CropTreeSelection;
import treegross.treatment.CropTreeSpecies;

/**
 *
 * @author jhansen
 */
public class TeSelectCropTrees implements TreatmentElement {

    private final String name = "crop tree selection";
    private final String label = "te_croptree";
    private final String desc = "te_croptree_info";

    private int[] code = new int[3];
    private int[] nha = new int[3];
    private double[] mixpercent = new double[3];;

    private ArrayList<Object> defhardwoodOnly;

    public TeSelectCropTrees(int code1, int nha1, double mixpercent1, int code2, int nha2, double mixpercent2,
            int code3, int nha3, double mixpercent3) {
        init(code1, nha1, mixpercent1, code2, nha2, mixpercent2, code3, nha3, mixpercent3);
    }

    public TeSelectCropTrees() {
        this(0, 0, 100.0, 0, 0, 0.0, 0, 0, 0.0);
    }

    private void init(int code1, int nha1, double mixpercent1, int code2, int nha2, double mixpercent2,
            int code3, int nha3, double mixpercent3) {
        this.code[0] = code1;
        this.nha[0] = nha1;
        this.mixpercent[0] = mixpercent1;
        this.code[1] = code2;
        this.nha[1] = nha2;
        this.mixpercent[1] = mixpercent2;
        this.code[2] = code3;
        this.nha[2] = nha3;
        this.mixpercent[2] = mixpercent3;
    }

    //logic of treatment element:
    public void makeSelection(Stand st) {
        //Do nothing but printing line of text to stdout:
        if (st != null) {
            double hmin = 9000.0;
            for (int i = 0; i < st.nspecies; i++) {
                if (st.sp[i].hg < hmin && st.sp[i].hg > 1.3) {
                    hmin = st.sp[i].hg - 1.0;
                }
            }

// new crop tree settings laden
            for (int i = 0; i < st.nspecies; i++) {
                st.sp[i].trule.numberCropTreesWanted = 0;
                st.sp[i].trule.targetCrownPercent = 0.0;
                st.sp[i].trule.minCropTreeHeight = 10.0;

                if (st.sp[i].code == code[0]) {
                    st.sp[i].trule.numberCropTreesWanted = (int) Math.round(nha[0] * mixpercent[0] / 100.0);
                    st.sp[i].trule.targetCrownPercent = mixpercent[0];
                    st.sp[i].trule.minCropTreeHeight = hmin;
                }
                if (st.sp[i].code == code[1]) {
                    st.sp[i].trule.numberCropTreesWanted = (int) Math.round(nha[1] * mixpercent[1] / 100.0);
                    st.sp[i].trule.targetCrownPercent = mixpercent[1];
                    st.sp[i].trule.minCropTreeHeight = hmin;
                }
                if (st.sp[i].code == code[2]) {
                    st.sp[i].trule.numberCropTreesWanted = (int) Math.round(nha[2] * mixpercent[2] / 100.0);
                    st.sp[i].trule.targetCrownPercent = mixpercent[2];
                    st.sp[i].trule.minCropTreeHeight = hmin;
                }
            }
            CropTreeSpecies ctspecies[] = new CropTreeSpecies[30];
            /**
             * Number of crop trees to select
             */
            int n_ct_ha;
            /**
             * distance of crop trees to select
             */
            double dist_ct;
            //Initialize Croptreespecies dependent on target mixture percentage and height of first thinning
            for (int i = 0; i < st.nspecies; i++) {
                if (st.sp[i].trule.numberCropTreesWanted == 0) {
                    dist_ct = 0.01;
                } //            else dist_ct=0.90* Math.sqrt((st.size*100.0*st.sp[i].trule.targetCrownPercent/(st.sp[i].trule.numberCropTreesWanted*st.size)));
                else {
                    dist_ct = 0.80 * Math.sqrt((100.0 * st.sp[i].trule.targetCrownPercent / (st.sp[i].trule.numberCropTreesWanted)));
                }
                //Initialize CropTreeSpecies
                ctspecies[i] = new CropTreeSpecies();
                ctspecies[i].addCtsp(st.sp[i].code, st.sp[i].trule.numberCropTreesWanted, dist_ct, st.sp[i].trule.minCropTreeHeight);
            }
            //Select Croptrees dependent on CTSpecies
            CropTreeSelection ctselect = new CropTreeSelection();
            ctselect.selectCropTrees(st, ctspecies);
        }
        System.out.println(name + " executed. Habitatbäume: ");
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
        params.add(new TreatmentElementParameter("code1", "1. "+RBHolder.getResourceBundle().getString("te_cropspecies"), RBHolder.getResourceBundle().getString("te_cropspecies_info"), code[0]));
        params.add(new TreatmentElementParameter("nha1", RBHolder.getResourceBundle().getString("te_ncrop"), RBHolder.getResourceBundle().getString("te_ncrop_info"), nha[0]));
        params.add(new TreatmentElementParameter("mixpercent1", RBHolder.getResourceBundle().getString("te_mix"), RBHolder.getResourceBundle().getString("te_mix_info"), mixpercent[0]));
        params.add(new TreatmentElementParameter("code2", "2. "+RBHolder.getResourceBundle().getString("te_cropspecies"), RBHolder.getResourceBundle().getString("te_cropspecies_info"), code[1]));
        params.add(new TreatmentElementParameter("nha2", RBHolder.getResourceBundle().getString("te_ncrop"), RBHolder.getResourceBundle().getString("te_ncrop_info"), nha[1]));
        params.add(new TreatmentElementParameter("mixpercent2",  RBHolder.getResourceBundle().getString("te_mix"), RBHolder.getResourceBundle().getString("te_mix_info"), mixpercent[1]));
        params.add(new TreatmentElementParameter("code3", "3. "+RBHolder.getResourceBundle().getString("te_cropspecies"), RBHolder.getResourceBundle().getString("te_cropspecies_info"), code[2]));
        params.add(new TreatmentElementParameter("nha3", RBHolder.getResourceBundle().getString("te_ncrop"), RBHolder.getResourceBundle().getString("te_ncrop_info"), nha[2]));
        params.add(new TreatmentElementParameter("mixpercent3",  RBHolder.getResourceBundle().getString("te_mix"), RBHolder.getResourceBundle().getString("te_mix_info"), mixpercent[2]));
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
            //System.out.println("parsing: "+aName+" value: " +value);
            switch (aName) {
                case "code1":
                    code[0] = Integer.parseInt(value);
                    break;
                case "nha1":
                    nha[0] = Integer.parseInt(value);
                    break;
                case "mixpercent1":
                    mixpercent[0] = Double.parseDouble(value);
                    break;
                case "code2":
                    code[1] = Integer.parseInt(value);
                    break;
                case "nha2":
                    nha[1] = Integer.parseInt(value);
                    break;
                case "mixpercent2":
                    mixpercent[1] = Double.parseDouble(value);
                    break;
                case "code3":
                    code[2] = Integer.parseInt(value);
                    break;
                case "nha3":
                    nha[2] = Integer.parseInt(value);
                    break;
                case "mixpercent3":
                    mixpercent[2] = Double.parseDouble(value);
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
        int[] index = { -9,-9,-9 };
        for (int i=0; i < 3; i++){
            double gmax = -9.0;
            for (int j=0; j < st.nspecies; j++){
                if (gmax < st.sp[j].gha && index[0] != j && index[1] != j ){
                    gmax = st.sp[j].gha;
                    index[i] = j;
                }
            }
         }
        //
        for (int i=0; i < 3; i++){
            if (index[i] >= 0){
                code[i] = st.sp[index[i]].code;
                nha[i] = st.sp[index[i]].spDef.cropTreeNumber;
                mixpercent[i] = st.sp[index[i]].percCSA;
            }
        }
        // Abgleich des Mischungsprozents
        double sum =0 ;
        for (int i=0; i < 3; i++){
            sum = sum + mixpercent[i];
        }
        for (int i=0; i < 3; i++){
            mixpercent[i] = Math.round(10*mixpercent[i] *100.0 /sum)/10;
        }
        init(code[0], nha[0], mixpercent[0] , code[1], nha[1], mixpercent[1], code[2], nha[2], mixpercent[2]);
    }
    
}
