/* http://www.nw-fva.de
 Version 07-11-2008

 (c) 2002 Juergen Nagel, Northwest German Forest Research Station, 
 Grätzelstr.2, 37079 Göttingen, Germany
 E-Mail: Juergen.Nagel@nw-fva.de
 
 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation.

 This program is distributed in the hope that it will be useful,
 but WITHOUT  WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 */
/**
 * TreeGrOSS : class species defines the drived species information
 * http://treegross.sourceforge.net
 *
 * @version 30-NOV-2004
 * @author	Juergen Nagel
 */
package treegross.base;

public class Species {

    /**
     * Species Code according to Lower Saxony
     */
    public int code;
    /**
     * age of the dominant trees to calculate the siteindex
     */
    public double h100age;
    /**
     * diameter and height of middle stem and dominant stem; site index age 100
     */
    public double dg, hg, d100, h100, hbon, dgout;
    /**
     * volume, basal area, number of stems per hectare
     */
    public double vol, gha, nha;
    /**
     * volume, basal area, number of stems per hectare of removal & dying that year
     */
    public double vhaout, ghaout, nhaout;
    /**
     * percentage of basal area, of crown surface of living trees
     */
    public double percBA, percCSA;
    /**
     * standsize [ha]
     */
    public double size;
    public SpeciesDef spDef = new SpeciesDef();
    public TreatmentRuleSpecies trule = new TreatmentRuleSpecies();
    public double totalPrice;
    public double[] price;
    public double vjkl1, vjkl2, vjkl3;
    public String heightcurveUsed = "";
    public double heightcurveUsedP0, heightcurveUsedP1, heightcurveUsedP2;
    /**
     * amount of dead wood
     */
    public double volumeOfDeadwood;
    public double dmax;

    public Species() {
        this(Integer.MIN_VALUE);
    }

    Species(int code) {
        super();
        this.code = code;
    }

    public Species(Species sp) {
        code = sp.code;
        d100 = sp.d100;
        dg = sp.dg;
        dgout = sp.dgout;
        gha = sp.gha;
        ghaout = sp.ghaout;
        h100 = sp.h100;
        h100age = sp.h100age;
        hbon = sp.hbon;
        heightcurveUsed = sp.heightcurveUsed;
        heightcurveUsedP0 = sp.heightcurveUsedP0;
        heightcurveUsedP1 = sp.heightcurveUsedP1;
        heightcurveUsedP2 = sp.heightcurveUsedP2;
        hg = sp.hg;
        nha = sp.nha;
        nhaout = sp.nhaout;
        percBA = sp.percBA;
        percCSA = sp.percCSA;
        if (price != null) {
            price = sp.price.clone();
        }
        size = sp.size;
        spDef = sp.spDef.clone();
        totalPrice = sp.totalPrice;
        trule = sp.trule.clone();
        vhaout = sp.vhaout;
        vjkl1 = sp.vjkl1;
        vjkl2 = sp.vjkl2;
        vjkl3 = sp.vjkl3;
        vol = sp.vol;
    }

    /**
     * set all attributes of a species to zero, this should be used if a species
     * is new
     */
    void setZero() {
        h100age = 0;
        dg = 0.0;
        hg = 0.0;
        d100 = 0.0;
        h100 = 0.0;
        //hbon=0.0;
        dgout = 0.0;
        vol = 0.0;
        gha = .0;
        nha = 0.0;
        vhaout = 0.0;
        ghaout = 0.0;
        nhaout = 0.0;
        percBA = 0.0;
        percCSA = 0.0;
        volumeOfDeadwood = 0.0;
        //heightcurveUsedP0=0.0;heightcurveUsedP1=0.0;heightcurveUsedP2=0.0;
    }

    void updateSpecies(Stand st) {
        setZero();
        double ghaStand = 0.0;
        for (int i = 0; i < st.ntrees; i++) {
            if (st.tr[i].code == code) {
                if (st.tr[i].d >= 7) {
                    if (st.tr[i].out < 1) {
                        nha += st.tr[i].fac;
                        gha += st.tr[i].fac * Math.PI * (st.tr[i].d / 200.0) * (st.tr[i].d / 200.0);
                        vol += st.tr[i].fac * st.tr[i].v;
                    } else {
                        if (st.tr[i].out == st.year) {
                            nhaout += st.tr[i].fac;
                            ghaout += st.tr[i].fac * Math.PI * (st.tr[i].d / 200.0) * (st.tr[i].d / 200.0);
                            vhaout += st.tr[i].fac * st.tr[i].v;
                        }
                    }
                }
                if (st.tr[i].isDead()) {
                    volumeOfDeadwood += st.tr[i].fac * st.tr[i].volumeDeadwood;
                }
            }
            if (st.tr[i].d >= 7 && st.tr[i].isLiving()) {
                ghaStand += st.tr[i].fac * Math.PI * (st.tr[i].d / 200.0) * (st.tr[i].d / 200.0);
            }
        }
        volumeOfDeadwood = volumeOfDeadwood / st.size;
        nha = nha / st.size;
        gha = gha / st.size;
        vol = vol / st.size;

        ghaStand = ghaStand / st.size;
        st.bha = ghaStand;

        nhaout = nhaout / st.size;
        ghaout = ghaout / st.size;
        vhaout = vhaout / st.size;

        if (nha > 0.0) {
            dg = 200 * Math.sqrt(gha / (Math.PI * nha));   //Dg
        }
        if (nhaout > 0.0) {
            dgout = 200 * Math.sqrt(ghaout / (Math.PI * nhaout));   //Dgout
        }
        // Hilfsvariable für Siteindex
        double siH100 = 0.0;
        double nsiH100 = 0.0;

        // calculate d100 and the average age of d100 it is needed for Hbon (site index) 
        // The 100 trees are distributed, according to basal area percent
        double n100 = st.size * 100.0 * gha / ghaStand;  // n100 according to stand size
        n100 = Math.round(n100);
        if (n100 <= 0) {
            n100 = 1;
        }
        if (nha > 0 && n100 > 0) {
            double jj = 0;
            int k = 0;

            while (jj < n100 && k < st.ntrees) {
                if (st.tr[k].d >= 7 && st.tr[k].code == code && st.tr[k].isLiving()) {
                    d100 = d100 + st.tr[k].fac * Math.PI * (st.tr[k].d / 200.0) * (st.tr[k].d / 200.0);
                    h100age = h100age + st.tr[k].fac * st.tr[k].age;

                    if (st.tr[k].si.defined()) {
                        siH100 = siH100 + st.tr[k].si.value * st.tr[k].fac;
                        nsiH100 = nsiH100 + st.tr[k].fac;
                    }
                    jj = jj + st.tr[k].fac;
                }
                k = k + 1;
            }

            d100 = 200.0 * Math.sqrt(d100 / (Math.PI * jj));
            h100age = h100age / jj;

            if (nsiH100 > 0.0) {
                siH100 = siH100 / nsiH100;
            } else {
                siH100 = 0.0;
            }
        }
        // Check, if Petterson height curve gives realistic value if not use Uniformheight
        boolean pettersonFalse = false;

        // calculate diameter-height curve
        int ndh = 0; // number of diameter and height values

        for (int j = 0; j < st.ntrees; j++) {
            if (st.tr[j].d >= 7 && st.tr[j].code == code && st.tr[j].h >/*=*/ 1.3 && (st.tr[j].isLiving() || st.tr[j].out == st.year)) {
                ndh++;
            }
        }

        int missingheights = st.getMissingHeight(0);
        // number of diameter-height values > 5 then height curve
        if (ndh > 5) {
            int k = ndh / 1000; //if there are more than 1000 ndh then use only every k th tree
            k = k + 1;
            ndh = 0;
            for (int j = 0; j < st.ntrees; j = j + k) {
                if (st.tr[j].d >= 7 && st.tr[j].code == code && st.tr[j].h >/*=*/ 1.3 && (st.tr[j].out < 1 || st.tr[j].out == st.year)) {
                    ndh = ndh + 1;
                }
            }

            HeightCurve m = new HeightCurve();
            m.heightcurve();

            for (int j = 0; j < st.ntrees; j = j + k) {
                if (st.tr[j].d >= 7 && st.tr[j].code == code && st.tr[j].h >/*=*/ 1.3 && (st.tr[j].out < 1 || st.tr[j].out == st.year)) {
                    m.adddh(spDef.heightCurve, ndh, st.tr[j].d, st.tr[j].h);
                }
            }
            m.start();

            if (dg > 0) {
                hg = m.hwert(spDef.heightCurve, dg);
            }
            if (d100 > 0) {
                h100 = m.hwert(spDef.heightCurve, d100);
            }

            heightcurveUsed = m.getHeightCurveName(spDef.heightCurve);
            heightcurveUsedP0 = m.getb0();
            heightcurveUsedP1 = m.getb1();
            heightcurveUsedP2 = m.getb2();

            if (m.hwert(spDef.heightCurve, 80) > 80.0) {
                pettersonFalse = true;
            }

            if (missingheights > 0) {
                for (int j = 0; j < st.ntrees; j++) {
                    if (st.tr[j].d >= 7 && st.tr[j].code == code && st.tr[j].h < 1.3) {
                        st.tr[j].h = m.hwert(spDef.heightCurve, st.tr[j].d);
                    }
                }
            }

        }

        // Alternative if there are only a few  1 to 4 heights
        // Use uniform height curve 
        double dk = 0.0;
        double hk = 0.0;

        if ((ndh > 0 && ndh <= 5) || pettersonFalse) {
            for (int j = 0; j < st.ntrees; j++) {
                if (st.tr[j].d >= 7 && st.tr[j].code == code && st.tr[j].h > 1.3 && (st.tr[j].out < 1 || st.tr[j].out == st.year)) {
                    if (st.tr[j].d > dk) {
                        dk = st.tr[j].d;
                        hk = st.tr[j].h;
                    }
                }
            }
            double dgmerk = dg;
            double hgmerk;// = hg;
            Tree tree = new Tree();
            tree.d = d100;
            tree.sp = this;
            tree.sp.dg = dk;
            tree.sp.hg = hk;
            FunctionInterpreter fi = new FunctionInterpreter();
            h100 = fi.getValueForTree(tree, tree.sp.spDef.uniformHeightCurveXML);

            // UniformHeight ufh = new UniformHeight();
            // hg=ufh.height(this,dg,dk,hk, st);
            tree.d = dg;
            hgmerk = fi.getValueForTree(tree, tree.sp.spDef.uniformHeightCurveXML);
            // h100=ufh.height(this,d100,dk,hk, st);
            heightcurveUsed = "Einheitshöhenkurve";
            heightcurveUsedP0 = dk;
            heightcurveUsedP1 = hk;

            if (missingheights > 0) {
                for (int j = 0; j < st.ntrees; j++) {
                    if (st.tr[j].d >= 7 && st.tr[j].code == code && st.tr[j].h < 1.3) {
                        tree.d = st.tr[j].d;
                        st.tr[j].h = fi.getValueForTree(tree, tree.sp.spDef.uniformHeightCurveXML);
                          //st.tr[j].h=ufh.height(st.tr[j].sp,st.tr[j].d,dk,hk,st); 
                        //System.out.println("Species:baum :"+st.tr[j].d+"  "+st.tr[j].h);}
                    }
                }
            }
            dg = dgmerk;
            hg = hgmerk;
        }

        //
        // If no height try another species
        //       
        if (ndh <= 0) {
            for (int j = 0; j < st.ntrees; j++) {
                if (st.tr[j].d >= 7 && st.tr[j].h > 1.3 && st.tr[j].out < 1) {
                    if (st.tr[j].d > dk) {
                        dk = st.tr[j].d;
                        hk = st.tr[j].h;
                    }
                }
            }

            // UniformHeight ufh = new UniformHeight();
            // hg=ufh.height(this,dg,dk,hk,st);
            heightcurveUsed = "Einheitshöhenkurve: ";
            heightcurveUsedP0 = dk;
            heightcurveUsedP1 = hk;
            double dgmerk = dg;
            double hgmerk = hg;
            Tree tree = new Tree();
            tree.d = d100;
            tree.sp = this;
            tree.sp.dg = dk;
            tree.sp.hg = hk;
            FunctionInterpreter fi = new FunctionInterpreter();
            if (dk > 7.0) {
                h100 = fi.getValueForTree(tree, tree.sp.spDef.uniformHeightCurveXML);
            }
            // h100=ufh.height(this,d100,dk,hk,st);

            if (missingheights > 0) {
                for (int j = 0; j < st.ntrees; j++) {
                    if (st.tr[j].d >= 7 && st.tr[j].code == code && st.tr[j].h < 1.3) {
                        tree.d = st.tr[j].d;
                        st.tr[j].h = fi.getValueForTree(tree, tree.sp.spDef.uniformHeightCurveXML);
                        //st.tr[j].h=ufh.height(st.tr[j].sp,st.tr[j].d,dk,hk,st); 
                        //System.out.println("Species:baum :"+st.tr[j].d+"  "+st.tr[j].h);}
                    }
                }
            }
            dg = dgmerk;
            hg = hgmerk;
        }

        // set siteindex, if h100 > 0.0
        if (siH100 > 0.0) {
            hbon = siH100;
        } else {
            Tree atree = new Tree();
            atree.sp = this;
            atree.d = 30;
            atree.age = (int) h100age;
            atree.h = h100;
            atree.code = code;
            if (hbon <= 0.0) {
                hbon = atree.calculateSiteIndex().value;
            }
        }
    }

    public void loadTrRuleDefault() {
        TreatmentRuleSpecies tr = new TreatmentRuleSpecies();
        trule = tr.loadTreatmentRule(1000.0, spDef.targetDiameter, spDef.heightOfThinningStart);
    }

    public double getTotalPrice() {
        totalPrice = 0;
        for (int i = 0; i < price.length; i++) {
            if (price[i] != -1) {
                totalPrice += price[i];
            }
        }
        return totalPrice;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("species ").append(code).append(" ageh100:").append(h100age).append(" dg:")
                .append(dg).append(" hg:").append(hg).append(" d100:").append(d100)
                .append(" G:").append(gha);
        return sb.toString();
    }

    public Tree referenceTree() {
        Tree atree = new Tree();
        atree.sp = this;
        atree.d = d100;
        atree.h = h100;
        atree.age = (int) Math.round(h100age);
        atree.cw = atree.calculateCw();
        atree.code = code;
        return atree;
    }
}
