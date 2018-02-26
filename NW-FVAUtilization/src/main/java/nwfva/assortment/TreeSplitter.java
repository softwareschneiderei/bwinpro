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
package nwfva.assortment;

import java.util.logging.Logger;
import treegross.base.JSortiererNFV;
import treegross.base.Tree;


/**
 * Der TreeSplitter kann dafür benutzt werden, einen Stamm in vorgegebene Sortimente
 * zu zerlegen. Die Sortimente müssen zuerst mit der Methode setAssortments übergeben werden.
 * Danach können die vorhandenen Sortimente mit ihren Maßen berechnet werden und mit der 
 * Methode getTreeLogs() und getNumberOfLogs aus der Klasse abgefragt werden. 
 * 
 * Summary: This class is for the calculation of given assortments in a tree trunc. 
 * 1. You have to set the assortments
 * 2. You can calculate the logs 
 * 3. You can return the log information and the number of logs
 *
 * 
 * @author J. Nagel
 */
public class TreeSplitter {
    private static final Logger log = Logger.getLogger( nwfva.assortment.NWFVA_Nutzung.class.getName() );

     LoggingSortiment ls[] = new LoggingSortiment[500];
     TreeLog tl[] = new TreeLog[100];
     int ntl = 0;
     int nLsChoosen = 0;
     int nausgewaehlt = 0;

/**
 *  Einstellen der Sortimente, welche bei der Aushaltung berücksichtigt werden
 * @param lsx Array mit den Sortimenten
 * @param nls Anzahl der Sortimente
 */
     public void setAssortments(LoggingSortiment lsx[], int nls){
// Logging Sortimente nach ausgewaehlt bzw. nicht sortieren
        ls = lsx; 
        LoggingSortiment temp = new LoggingSortiment();
        for (int i=0;i< nls-1;i++){
           for (int j=i+1;j< nls;j++){
               if (ls[j].ausgewaehlt) {
                   temp=ls[i];
                   ls[i]=ls[j];
                   ls[j]=temp;
               }
           }
        }
// Festellen der Anzahl ausgewaehlter ls
        nausgewaehlt = 0;
        for (int i=0;i< nls;i++) {if (ls[i].ausgewaehlt) {nausgewaehlt = nausgewaehlt +1 ;}}
// ausgewaehlte Logging Sortimente nach Gewichtung sortieren
        for (int i=0;i< nausgewaehlt-1;i++){
           for (int j=i+1;j< nausgewaehlt;j++){
               if (ls[j].gewicht > ls[i].gewicht) {
                   temp=ls[i];
                   ls[i]=ls[j];
                   ls[j]=temp;
               }
           }
        }
     }
/**
 * Methode um die Aushaltung für einen Baum und einen Fällhöhe auszuführen
 * @param tr  Baum aus dem TreeGrOSS package
 * @param fellingHeight Fällschnitt [m]
 */
     public void splitTree(Tree tr, double fellingHeight){
//        log.info("Splitter Baum"+tr.no);        
        ntl = 0;
        JSortiererNFV sortierer = new JSortiererNFV(tr.st.sp[0].spDef.taperFunctionXML);
        double startHeight=fellingHeight;
        int taperFunctionNr=sortierer.getFunNumber(tr.code);
        for (int j=0;j<nausgewaehlt;j++) {
            double ran = Math.random() * 100.0;
            if (tr.code >= ls[j].artvon && tr.code <= ls[j].artbis && ran <= ls[j].wahrscheinlich){
                boolean logFound = true;
                double endHeight = tr.h;
                if (ls[j].bisKronenansatz) { endHeight = tr.cb;}
                boolean logOk = true;
                if (ls[j].nurZBaum && tr.crop == false) {logOk = false;}
                while(logFound && logOk) {
// Berechnung des Volumens mit Zugabe, dafür werden minH und maxH um die Zugabe erhöht
                      double zugmin = 0.0;
                      if (ls[j].zugabeProzent > 0.0) zugmin = ls[j].minH * ls[j].zugabeProzent/100.0;
                      if (ls[j].zugabeCm > 0.0) zugmin = ls[j].zugabeCm/100.0;
                      double zugmax = 0.0;
                      if (ls[j].zugabeProzent > 0.0) zugmax = ls[j].maxH * ls[j].zugabeProzent/100.0;
                      if (ls[j].zugabeCm > 0.0) zugmax = ls[j].zugabeCm/100.0;
                      logFound = sortierer.getAssortment(taperFunctionNr , tr.d, 1.3, 0.0, 0.0, tr.h,
                                    startHeight, endHeight, ls[j].minD, ls[j].maxD, ls[j].minTop, ls[j].maxTop,
                                    ls[j].minH+zugmin, ls[j].maxH+zugmax, 0.0, 0.0, 0.0);
                      if(logFound) {
// Berechnung des Sortiments mit der Zugabe unter der Schaftform                         
                          double dmR_cm= sortierer.getADmB_cm();
                          double doR_cm= sortierer.getADm_cm();
                          double lzug_m = sortierer.getALae_m();
                          double volmR = sortierer.getAVolmR_m3();
                          double voloR = sortierer.getAVoloR_m3();
// Berechnung des Sortiments ohne die Zugabe     calculate volume by Huber               
                          logFound = sortierer.getAssortment(taperFunctionNr , tr.d, 1.3, 0.0, 0.0, tr.h,
                                    startHeight, endHeight, ls[j].minD, ls[j].maxD, ls[j].minTop, ls[j].maxTop,
                                    ls[j].minH, ls[j].maxH, 0.0, 0.0, 0.0);
                          dmR_cm= sortierer.getADmB_cm();
                          doR_cm= sortierer.getADm_cm();
                          double l_m = startHeight;
                          l_m = sortierer.getALae_m();
                          double vo1HmR = 0.0;
                          vo1HmR = sortierer.getVolumeHuber(dmR_cm,l_m);
                          double vo1HoR = sortierer.getVolumeHuber(doR_cm,l_m);
                          double zeitbedarf = 0;//tef[ls[j].zeitFunktion].getTime(doR_cm)*vo1HoR;
                          tl[ntl] = new TreeLog(ls[j].name,ls[j].entnahme,startHeight, l_m , 
                                  vo1HmR, vo1HoR, volmR, voloR, zeitbedarf, dmR_cm);
                          startHeight=startHeight + lzug_m;
                          ntl = ntl +1;          

                       }
                       if (ls[j].mehrfach == false) {logFound = false;} //Abbruch
                  }// while log found
              } // Baumart
         }   // for j
     }

/**
 * Liefert die ausgehaltenen Sortimente zurück
 * @return Liefert einen Array mit den ausgehaltenen Stücken zurück.
 */     
     public TreeLog[] getTreeLogs(){
         return tl;
     }
/**
 * Liefert die Anzahl der ausgehaltenen Stücke Abschnitte zurück
 * @return 
 */
     public int getNumberOfLogs(){
         return ntl;
     }
}
