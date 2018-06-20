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
package treegross.base;

/**
 * TreeGrOSS : class competition defines the competition indicies
 * http://treegross.sourceforge.net
 *
 * @version 2.0 30-NOV-2004
 * @author	Juergen Nagel
 *
 * For more information see: NAGEL, J. (1999): Konzeptionelle Überlegungen zum
 * schrittweisen Aufbau eines waldwachstumskundlichen Simulationssystems für
 * Nordwestdeutschland. Schriften aus der Forstlichen Fakultät der Universität
 * Göttingen und der Nieders. Forstl. Versuchsanstalt, Band 128, J.D.
 * Sauerländer's Verlag, Frankfurt a.M., S.122
 *
 * or
 *
 * BWINPro User's Manual http://www.nfv.gwdg
 */
public class Competition2 extends Competition implements PlugInCompetition {

    @Override
    public double getc66c(Tree t) {
        double h66 = t.h - (2.0 * (t.h - t.cb) / 3.0);
        double c66 = 0.0;
        double cri;
        for (int i = 0; i < t.st.ntrees; i++) {
            if (t.st.tr[i].out >= t.st.year && t.st.tr[i].outtype != OutType.FALLEN) {
                if (t.st.tr[i].cb >= h66) {
                    cri = t.st.tr[i].cw / 2.0;
                } else {
                    cri = t.st.tr[i].calculateCwAtHeight(h66) / 2.0;
                }
                c66 = c66 + t.st.tr[i].fac * Math.PI * cri * cri;
            }
        }
        c66 = c66 / (10000 * t.st.size);
        return c66;
    }

    @Override
    public void replaceC66xyAndC66cxy(Tree t, double influenceZoneRadius) {
        // if there are 15 neighbor trees detected the influenece zone needs to be corrected
        // why at all and why 15 ??????
        /*if (t.nNeighbor >= 15 )
            influenceZoneRadius=Math.sqrt(Math.pow(t.st.tr[t.neighbor[t.nNeighbor-1]].x-t.x,2.0)+
                Math.pow(t.st.tr[t.neighbor[t.nNeighbor-1]].y-t.y,2.0));*/

        // influenece zone should be at least 2m radius
        if (influenceZoneRadius < 2.0) {
            influenceZoneRadius = 2.0;
        }

        double h66 = t.cb + (t.h - t.cb) / 3.0; // height for the cross section of subject tree
        t.c66xy = 0.0; // set values to 0
        t.c66cxy = 0.0;

        // 1. we check how many percent of the influence zone is in the
        // stand pologon. The percentage is stored in perc 
        double perc = getPercCircleInStand(influenceZoneRadius, t.x, t.y, t.st);

        // Now we check for each tree             
        // add trees own area
        // this is done in loop over all trees
        //t.c66xy = t.fac * Math.PI * Math.pow((t.cw/2.0),2.0);
        double e;
        /*for (int k=0; k<t.nNeighbor; k++){*/
        //calculate with all trees in stand:
        int j;
        double overlap;
        double percOverlapInStand;
        double cri;
        for (int k = 0; k < t.st.ntrees; k++) {
            //int j = t.neighbor[k];
            j = k;
            // Experimental: Für Mischbestände die Gewichte festlegen
            // Buche überlappt von Buche gewicht=1.0;
            // Buche überlappt von Fichte gewicht=0.84;
            // Fichte überlappt von Buche gewicht=0.98;
            double gewicht = 1.0;
            if (t.code == 211 && t.st.tr[j].code == 511) {
                gewicht = 1.69;
            }
            if (t.code == 511 && t.st.tr[j].code == 211) {
                gewicht = 0.64;
            }

            e = Math.pow(t.x - t.st.tr[j].x, 2.0) + Math.pow(t.y - t.st.tr[j].y, 2.0);
            if (e != 0.0) {
                if (e > 0) {
                    e = Math.sqrt(e);
                }
                //cri=0.0;
                // neighbours gehen nur max bis influienceZone ein vgl. stand
                // 2. Achtung wenn baum j mit seiner vollen kronenbreite in die influenzsZone ragt,
                //    ist dies nicht für die Kronenbreite in h66 garantiert, dann kann
                //    es sein, dass overlap= 0 ist und getoverlapPerc NaN !!!!
                //    daher: vorher abfragen ob overlap >0
                //    und methode overlap prüfen, op überhaupt punkte im Überlappungsbereich sind
                if (e < influenceZoneRadius + t.st.tr[j].cw / 2.0 && t.st.tr[j].h > h66 && t.st.tr[j].out < 0) {
                    if (t.st.tr[j].cb >= h66) {
                        cri = t.st.tr[j].cw / 2.0;
                    } else {
                        cri = t.st.tr[j].calculateCwAtHeight(h66) / 2.0;
                    }
                    // reduce overlap area -> use only percentage inside the stand
                    overlap = overlap(cri, influenceZoneRadius, e);
                    //nur wenn Überlappung c66xy erhöhen
                    if (overlap > 0) {
                        percOverlapInStand = getPercOverlapInStand(influenceZoneRadius, t.x, t.y, cri, t.st.tr[j].x, t.st.tr[j].y, t.st);
                        t.c66xy += t.st.tr[j].fac * (overlap * percOverlapInStand) * gewicht;
                    }
                } else if (e < influenceZoneRadius + t.st.tr[j].cw / 2.0 && t.st.tr[j].h > h66 && t.st.tr[j].out >= t.st.year && t.st.tr[j].outtype != OutType.FALLEN) {
                    if (t.st.tr[j].cb >= h66) {
                        cri = t.st.tr[j].cw / 2.0;
                    } else {
                        cri = t.st.tr[j].calculateCwAtHeight(h66) / 2.0;
                    }
                    // reduce overlap area -> use only percentage inside the stand
                    overlap = overlap(cri, influenceZoneRadius, e);
                    //nur wenn Überlappung c66xy erhöhen
                    if (overlap > 0) {
                        percOverlapInStand = getPercOverlapInStand(influenceZoneRadius, t.x, t.y, cri, t.st.tr[j].x, t.st.tr[j].y, t.st);
                        t.c66cxy += t.st.tr[j].fac * (overlap * percOverlapInStand) * gewicht;
                    }
                }
            }
        }
        double div = perc * Math.PI * Math.pow(influenceZoneRadius, 2.0);
        t.c66xy = t.c66xy / div;
        t.c66cxy = t.c66cxy / div;
    }
}
