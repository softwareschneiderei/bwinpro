/* http://www.nw-fva.de
   Version 2013-01-11

   (c) 2013 Juergen Nagel, Northwest German Forest Research Station, 
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
package nwfva.biomass;

import java.util.logging.Logger;
import nwfva.assortment.LoggingSortiment;
import nwfva.assortment.TreeLog;
import nwfva.assortment.TreeSplitter;
import treegross.base.FunctionInterpreter;
import treegross.base.JSortiererNFV;
import treegross.base.Tree;

/**
 * Klasse zur Berechnung von Biomassen- und Nährstoffgehalten. Die Klasse benötigt
 * das Package assortments, um die Sortimente aus einem Baum schneiden zu können.
 * Die Sortimente müssen zuerst mit der Methode setAssortments übergeben werden. Darüber
 * hinaus müssen die Biomassefunktionen und Nährstoffkonzentrationen mit der Methode
 * setBiomassFunctions() übergeben werden. Erstdanach kann die Methode getTreesBiomass()
 * aufgerufen werden. Diese liefert die Ergebnisse als BiomassLine zurück.
 * Es ist mit dieser Methode auch möglich die Biomasse und die Nährstoffe für nur ein 
 * Sortiment zu berechnen. In diesem Fall darf nur ein Sortiment übergeben werden und
 * es müssen die Aushaltung von Brenn- und Restholz auf false gesetzt werden.
 * 
 * @author nagel
 */
public class BiomassCalculation {
    private static final Logger log = Logger.getLogger( nwfva.assortment.NWFVA_Nutzung.class.getName() );

    BiomassSetting nbs[] = new BiomassSetting[50];
    int nnbs =0;
    BiomassLine nbl = new BiomassLine();
    LoggingSortiment ls[] = new LoggingSortiment[500];
    int nls =0;

/**
 * Übergabe der Biomassefunktionen und Nährstoffkonzentrationen
 * @param nbsx Array mit Biomasseeinstellungen für die Baumarten
 * @param nnbsx Anzahl der Baumarten und Einstellungen
 */    
    public void setBiomassFunctions(BiomassSetting nbsx[], int nnbsx){
        nbs = nbsx;
        nnbs = nnbsx;
    }
/**
 * Übergabe der Standardsortimente
 * @param lsx Array mit der Definition der Sortimente
 * @param nlsx Anzahl der Definitionen
 */    
    public void setAssortments(LoggingSortiment lsx[], int nlsx){
        ls = lsx; 
        nls = nlsx;
    }

/**
 * Berechnung der Biomassen und Nährstoffe
 * Vorher müssen die beiden Settings durchgeführt werden: 
 * setBiomassFunctions
 * setAssortments
 * @param tr TreeGrOSS Baum
 * @param nblx Biomassenergebniszeile, diese enthält bereits die Einstellungen, 
 * wieviel Prozent der Nadelmasse und des Reisigs entnommen werden sowie, ob
 * Brennholz und Restholz ausgehalten wird.
 * @return nbl Biomassen und Nähstoffe
 */    
    public BiomassLine getTreesBiomass(Tree tr,BiomassLine nblx){
           nbl = nblx;
           int nbsindex = -9;
           for (int ik=0;ik < nnbs;ik++){
               if (tr.code == nbs[ik].code) nbsindex=ik;
           }
           if (nbsindex < 0) {
               for (int ik=0; ik < nnbs;ik++) {
                    String codestr = new Integer(tr.code).toString();
                    if (nbs[ik].specieslist.indexOf(codestr)> -1) {nbsindex=ik;}
               }
           }
           if (nbsindex < 0) { nbsindex = 0;}
           
                    
//                   
// in nbsindex sollte jetzt der rictige Index zu finden sein
//
           JSortiererNFV sortierer = new JSortiererNFV(nbs[0].taperFuctionClass);
           FunctionInterpreter fi = new FunctionInterpreter();
           double vol = fi.getValueForTree(tr,nbs[nbsindex].stemVolumeFunction );
           double volSortimente =0.0;
           double volRinde = 0.0;
           double volFirewood=0.0;
           double volBarkFirewood=0.0;
           double volRestwood=0.0;
           double volBarkRestwood=0.0;
           double bmsort=0.0;
           double bmsortBark=0.0;
           double bmfire=0.0;
           double bmfireBark=0.0;
           double bmrest=0.0;
           double bmrestBark=0.0;
           double bmbranch=0.0;
           double bmreisig=0.0;
           double bmleaf=0.0;
                    
           double stemBM = fi.getValueForTree(tr,nbs[nbsindex].stemBMkg) ;
           double barkBM = fi.getValueForTree(tr,nbs[nbsindex].barkBMkg) ;
           double branchBM = fi.getValueForTree(tr,nbs[nbsindex].branchBMkg);
           double reisigBM = fi.getValueForTree(tr,nbs[nbsindex].reisigBMkg);
           double stemVol = 0.0;
           double barkVol = 0.0;
           boolean found =sortierer.getAssortment(nbs[nbsindex].taperFunctionNumber , tr.d, 1.3, 0.0, 0.0, tr.h,
                          0.3, 900.0, 7.0, 999.9, 7.0, 999.9,0.0, 999.9, 0.0, 0.0, 0.0);
           if (found) { stemVol= sortierer.getAVoloR_m3();
               barkVol = sortierer.getAVolmR_m3()-sortierer.getAVoloR_m3();
               }
// wenn Sortimente aus dem Baum geschnitten werden sollen 
           if(nbl.sortiments){
                 TreeSplitter splitter = new TreeSplitter();
                 splitter.setAssortments(ls, nls);
                 splitter.splitTree(tr, 0.3);
                 TreeLog tl[] = new TreeLog[100];
                 int ntl = 0;
                 tl = splitter.getTreeLogs();
                 ntl = splitter.getNumberOfLogs();
// verarbeiten der entnommen Sortimentsstücke                 
                 for (int jj=0; jj< ntl; jj++){
                     if (tl[jj].removed){
                         volSortimente = volSortimente+tl[jj].vol_oR;
                         volRinde = volRinde +(tl[jj].vol_mR - tl[jj].vol_oR);
                     }
                 }
// Sortimente verrechnen                           
                 bmsort = (volSortimente*stemBM/stemVol)*tr.fac/tr.st.size;
                 bmsortBark = (volRinde*barkBM/barkVol)*tr.fac/tr.st.size;
                 nbl.sortimentsBM =  bmsort;
                 nbl.barksortimentsBM = bmsortBark;
           }
           if (nbl.firewood){
                 volFirewood = (stemVol-volSortimente)*0.01*nbl.firewoodPerc;
                 bmfire = (volFirewood*stemBM/stemVol)*tr.fac/tr.st.size;
                 nbl.firewoodBM = bmfire;
                 volBarkFirewood = (barkVol- volRinde)*0.01*nbl.firewoodPerc; 
                 bmfireBark = (volBarkFirewood*barkBM/barkVol)*tr.fac/tr.st.size;
                 nbl.barkfirewoodBM = bmfireBark ;

           }
           if (nbl.restwood){
                 volRestwood = (stemVol - volSortimente - volFirewood)*0.01*nbl.restwoodPerc;
                 bmrest = (volRestwood*stemBM/stemVol)*tr.fac/tr.st.size;
                 nbl.restwoodBM = bmrest ;
                 volBarkRestwood = (barkVol- volRinde - volBarkFirewood)*0.01*nbl.restwoodPerc;
                 bmrestBark = (volBarkRestwood*barkBM/barkVol)*tr.fac/tr.st.size;
                 nbl.barkrestwoodBM = bmrestBark;
                 bmbranch = 0.01*nbl.restwoodPerc*(branchBM)*tr.fac/tr.st.size;
                 nbl.branchBM =  bmbranch;
                 bmreisig = 0.01*nbl.restwoodPerc*(reisigBM)*tr.fac/tr.st.size;
                 double facx = fi.getValueForTree(tr,nbs[nbsindex].leafBM);
                 nbl.reisigBM = bmreisig*(1.0-facx) ;
                 nbl.leafBM = bmreisig*facx*0.01*nbl.needlePerc;
                 bmleaf = bmreisig*facx*0.01*nbl.needlePerc;
                 bmreisig = bmreisig*(1.0-facx);
            }
// Add Elements                    
            nbl.sumC= fi.getValueForTree(tr, nbs[nbsindex].woodFacC)*(bmsort+bmfire+bmrest)+
                   fi.getValueForTree(tr,nbs[nbsindex].barkFacC)*(bmsortBark+bmfireBark+bmrestBark)+
                   fi.getValueForTree(tr,nbs[nbsindex].branchFacC)*(bmbranch)+
                   fi.getValueForTree(tr,nbs[nbsindex].reisigFacC)*(bmreisig)+
                   fi.getValueForTree(tr,nbs[nbsindex].leafFacC)*(bmleaf);           
            nbl.sumN= fi.getValueForTree(tr, nbs[nbsindex].woodFacN)*(bmsort+bmfire+bmrest)+
                   fi.getValueForTree(tr,nbs[nbsindex].barkFacN)*(bmsortBark+bmfireBark+bmrestBark)+
                   fi.getValueForTree(tr,nbs[nbsindex].branchFacN)*(bmbranch)+
                   fi.getValueForTree(tr,nbs[nbsindex].reisigFacN)*(bmreisig)+
                   fi.getValueForTree(tr,nbs[nbsindex].leafFacN)*(bmleaf);           
            nbl.sumS= fi.getValueForTree(tr, nbs[nbsindex].woodFacS)*(bmsort+bmfire+bmrest)+
                   fi.getValueForTree(tr,nbs[nbsindex].barkFacS)*(bmsortBark+bmfireBark+bmrestBark)+
                   fi.getValueForTree(tr,nbs[nbsindex].branchFacS)*(bmbranch)+
                   fi.getValueForTree(tr,nbs[nbsindex].reisigFacS)*(bmreisig)+
                   fi.getValueForTree(tr,nbs[nbsindex].leafFacS)*(bmleaf);           
            nbl.sumP= fi.getValueForTree(tr, nbs[nbsindex].woodFacP)*(bmsort+bmfire+bmrest)+
                   fi.getValueForTree(tr,nbs[nbsindex].barkFacP)*(bmsortBark+bmfireBark+bmrestBark)+
                   fi.getValueForTree(tr,nbs[nbsindex].branchFacP)*(bmbranch)+
                   fi.getValueForTree(tr,nbs[nbsindex].reisigFacP)*(bmreisig)+
                   fi.getValueForTree(tr,nbs[nbsindex].leafFacP)*(bmleaf);           
            nbl.sumK= fi.getValueForTree(tr, nbs[nbsindex].woodFacK)*(bmsort+bmfire+bmrest)+
                   fi.getValueForTree(tr,nbs[nbsindex].barkFacK)*(bmsortBark+bmfireBark+bmrestBark)+
                   fi.getValueForTree(tr,nbs[nbsindex].branchFacK)*(bmbranch)+
                   fi.getValueForTree(tr,nbs[nbsindex].reisigFacK)*(bmreisig)+
                   fi.getValueForTree(tr,nbs[nbsindex].leafFacK)*(bmleaf);           
            nbl.sumCa= fi.getValueForTree(tr, nbs[nbsindex].woodFacCa)*(bmsort+bmfire+bmrest)+
                   fi.getValueForTree(tr,nbs[nbsindex].barkFacCa)*(bmsortBark+bmfireBark+bmrestBark)+
                   fi.getValueForTree(tr,nbs[nbsindex].branchFacCa)*(bmbranch)+
                   fi.getValueForTree(tr,nbs[nbsindex].reisigFacCa)*(bmreisig)+
                   fi.getValueForTree(tr,nbs[nbsindex].leafFacCa)*(bmleaf);           
            nbl.sumMg= fi.getValueForTree(tr, nbs[nbsindex].woodFacMg)*(bmsort+bmfire+bmrest)+
                   fi.getValueForTree(tr,nbs[nbsindex].barkFacMg)*(bmsortBark+bmfireBark+bmrestBark)+
                   fi.getValueForTree(tr,nbs[nbsindex].branchFacMg)*(bmbranch)+
                   fi.getValueForTree(tr,nbs[nbsindex].reisigFacMg)*(bmreisig)+
                   fi.getValueForTree(tr,nbs[nbsindex].leafFacMg)*(bmleaf);           
            nbl.sumMn= fi.getValueForTree(tr, nbs[nbsindex].woodFacMn)*(bmsort+bmfire+bmrest)+
                   fi.getValueForTree(tr,nbs[nbsindex].barkFacMn)*(bmsortBark+bmfireBark+bmrestBark)+
                   fi.getValueForTree(tr,nbs[nbsindex].branchFacMn)*(bmbranch)+
                   fi.getValueForTree(tr,nbs[nbsindex].reisigFacMn)*(bmreisig)+
                   fi.getValueForTree(tr,nbs[nbsindex].leafFacMn)*(bmleaf);           
            nbl.sumFe= fi.getValueForTree(tr, nbs[nbsindex].woodFacFe)*(bmsort+bmfire+bmrest)+
                   fi.getValueForTree(tr,nbs[nbsindex].barkFacFe)*(bmsortBark+bmfireBark+bmrestBark)+
                   fi.getValueForTree(tr,nbs[nbsindex].branchFacFe)*(bmbranch)+
                   fi.getValueForTree(tr,nbs[nbsindex].reisigFacFe)*(bmreisig)+
                   fi.getValueForTree(tr,nbs[nbsindex].leafFacFe)*(bmleaf);      
            nbl.sumBOup= fi.getValueForTree(tr, nbs[nbsindex].woodFacBOup)*(bmsort+bmfire+bmrest)+
                   fi.getValueForTree(tr,nbs[nbsindex].barkFacBOup)*(bmsortBark+bmfireBark+bmrestBark)+
                   fi.getValueForTree(tr,nbs[nbsindex].branchFacBOup)*(bmbranch)+
                   fi.getValueForTree(tr,nbs[nbsindex].reisigFacBOup)*(bmreisig)+
                   fi.getValueForTree(tr,nbs[nbsindex].leafFacBOup)*(bmleaf);      
            nbl.sumBNH4= fi.getValueForTree(tr, nbs[nbsindex].woodFacBNH4)*(bmsort+bmfire+bmrest)+
                   fi.getValueForTree(tr,nbs[nbsindex].barkFacBNH4)*(bmsortBark+bmfireBark+bmrestBark)+
                   fi.getValueForTree(tr,nbs[nbsindex].branchFacBNH4)*(bmbranch)+
                   fi.getValueForTree(tr,nbs[nbsindex].reisigFacBNH4)*(bmreisig)+
                   fi.getValueForTree(tr,nbs[nbsindex].leafFacBNH4)*(bmleaf);      
            nbl.sumBNO3= fi.getValueForTree(tr, nbs[nbsindex].woodFacBNO3)*(bmsort+bmfire+bmrest)+
                   fi.getValueForTree(tr,nbs[nbsindex].barkFacBNO3)*(bmsortBark+bmfireBark+bmrestBark)+
                   fi.getValueForTree(tr,nbs[nbsindex].branchFacBNO3)*(bmbranch)+
                   fi.getValueForTree(tr,nbs[nbsindex].reisigFacBNO3)*(bmreisig)+
                   fi.getValueForTree(tr,nbs[nbsindex].leafFacBNO3)*(bmleaf);      
                    

    return nbl;
    }
    
    
}
