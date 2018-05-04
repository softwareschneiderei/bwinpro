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
package forestsimulator.standsimulation;

import treegross.base.*;
import java.io.*;
import java.text.*;
import java.util.*;

class TgStructureTable {

    File file = new File("structuretable.html");
    ResourceBundle messages;

    void TgStructureTable() {
    }

    void writeTable(Stand st, String path, String fname, Locale preferredLanguage) {

        StandStructure struc = new StandStructure();
        messages = ResourceBundle.getBundle("forestsimulator/gui");
        this.file = new File(path, fname);
        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            out.println("<HTML>");
            out.println("<H2><P align=center>" + messages.getString("stand_structure_table") + "</P align=center></H2> ");
            out.println("<P><B>" + messages.getString("TgStructureTable.standname.label") + st.standname);
            out.println("<BR>" + messages.getString("stand_size") + st.size);
            out.println("<BR>" + messages.getString("year") + st.year + "</B></P>");
            NumberFormat f = NumberFormat.getInstance(new Locale("en", "US"));
            f.setMaximumFractionDigits(1);
            f.setMinimumFractionDigits(1);
            out.println("<HR>");
            out.println("<TABLE BORDER>");
// Species Richness         
            out.println("<TR><TD><FONT SIZE=2>" + messages.getString("numberOfSpecies")
                    + "<TD><FONT SIZE=2>" + st.nspecies + "</TR>");
// Shannon Index Stammzahl        
            out.println("<TR><TD><FONT SIZE=2>" + messages.getString("shannon") + "_N "
                    + "<TD><FONT SIZE=2>" + f.format(struc.shannon_N(st)) + "</TR>");
// Shannon Index Grundfläche        
            out.println("<TR><TD><FONT SIZE=2>" + messages.getString("shannon") + "_G "
                    + "<TD><FONT SIZE=2>" + f.format(struc.shannon_G(st)) + "</TR>");
// Evenness Index Stammzahl 
            double e_N = 0.0;
            if (st.nspecies > 1) {
                e_N = struc.shannon_N(st) / Math.log(st.nspecies);
            }
            out.println("<TR><TD><FONT SIZE=2>" + "Eveness_N "
                    + "<TD><FONT SIZE=2>" + f.format(e_N) + "</TR>");
// Evenness Index Grundfläche   
            double e_G = 0.0;
            if (st.nspecies > 1) {
                e_G = struc.shannon_G(st) / Math.log(st.nspecies);
            }
            out.println("<TR><TD><FONT SIZE=2>" + "Eveness_G "
                    + "<TD><FONT SIZE=2>" + f.format(e_G) + "</TR>");
// Species Profil         
            out.println("<TR><TD><FONT SIZE=2>" + messages.getString("profil")
                    + "<TD><FONT SIZE=2>" + f.format(struc.a_index(st)) + "</TR>");
// Shannon Index         
            out.println("<TR><TD><FONT SIZE=2>" + messages.getString("th")
                    + "<TD><FONT SIZE=2>" + f.format(struc.th(st)) + "</TR>");
// Shannon Index         
            out.println("<TR><TD><FONT SIZE=2>" + messages.getString("td")
                    + "<TD><FONT SIZE=2>" + f.format(struc.td(st)) + "</TR>");
// Shannon Index         
            out.println("<TR><TD><FONT SIZE=2>" + messages.getString("tart")
                    + "<TD><FONT SIZE=2>" + f.format(struc.tart(st)) + "</TR>");
//        Double lightKMF = 0.0;
//        Double sumKMF = 0.0;
//        for (int i = 0; i < st.ntrees; i++) {
//            if (st.tr[i].out < 0) {
//                lightKMF = lightKMF + st.tr[i].cwLightCrown;
//                double lx = 2.0 * (st.tr[i].h - st.tr[i].cb) / 3.0;
//                double crx = st.tr[i].cw / 2.0;
//                sumKMF = sumKMF + (Math.PI * crx / (6 * lx * lx)) * (Math.exp(Math.log(4 * lx * lx + crx * crx) * 1.5) - crx * crx * crx);;
//
//            }
//        }
//        lightKMF = lightKMF / st.size;
//        sumKMF = sumKMF / st.size;
//         out.println("<TR><TD><FONT SIZE=2>"+"Lichtkrone m²/ha"+
//                     "<TD><FONT SIZE=2>"+f.format(lightKMF)+ "</TR>");
//         out.println("<TR><TD><FONT SIZE=2>"+"Kronenmantel m²/ha"+
//                     "<TD><FONT SIZE=2>"+f.format(sumKMF)+ "</TR>");
            out.println("</HR></TABLE>");

            /*      out.println("<TABLE BORDER>");
      
      for (int i=0;i<st.ntrees;i++){
           double area =0.0;
           double lx = 2.0*(st.tr[i].h-st.tr[i].cb)/3.0;
           double crx = st.tr[i].cw/2.0; 
           double KMF = (Math.PI*crx/(6*lx*lx))*(Math.exp(Math.log(4*lx*lx+crx*crx)*1.5)-crx*crx*crx);;
           double hrel = st.tr[i].h/st.h100;
           for (int j=0;j<st.ntrees;j++){
               if (i != j){
                 double ent = 0.0;
                 ent = Math.sqrt(Math.pow(st.tr[i].x-st.tr[j].x,2.0)+Math.pow(st.tr[i].y-st.tr[j].y,2.0));
                 area = area + overlap(st.tr[i].cw/2.0,st.tr[j].cw/2.0,ent);
                 
                 }}
                 double ant = 0.0;
                 ant = area / (Math.PI*Math.pow(st.tr[i].cw,2.0));
                 out.println("<TR><TD><FONT SIZE=2>"+st.tr[i].no+ "</TD>"+
                     "<TD><FONT SIZE=2>"+st.tr[i].code+ "</TD>"+ 
                     "<TD><FONT SIZE=2>"+st.tr[i].d+ "</TD>"+ 
                     "<TD><FONT SIZE=2>"+st.tr[i].h+ "</TD>"+ 
                     "<TD><FONT SIZE=2>"+st.tr[i].cw+ "</TD>"+ 
                     "<TD><FONT SIZE=2>"+st.tr[i].cb+ "</TD>"+ 
                     "<TD><FONT SIZE=2>"+KMF+ "</TD>"+ 
                     "<TD><FONT SIZE=2>"+area+ "</TD>"+ 
                     "<TD><FONT SIZE=2>"+ant+ "</TD>"+ 
                     "<TD><FONT SIZE=2>"+hrel+ "</TD>"+ 
         
                     "<TD><FONT SIZE=2>"+st.tr[i].cwLightCrown+ "</TR>");

             }
      out.println("</TABLE>");
             */
            out.println("<BR>" + messages.getString("created") + st.modelRegion + "</BR></HTML>");
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getFilename() {
        return file.getAbsolutePath();
    }

    /**
     * calculate overlap area of two circle only if they overlap
     */
    private double overlap(double r1, double r2, double e) {
        double x, y, r1s, r2s;
        //r1 should always be the smaller radius
        if (r1 > r2) {
            x = r1;
            r1 = r2;
            r2 = x;
        }
        if (e >= (r1 + r2)) {
            return 0.0;  //no overlap area =0
        }
        r1s = r1 * r1;
        // partly or full overlap
        if ((e + r1) <= r2) {
            return Math.PI * r1s;
        }
        // part. overlap
        x = e * e;
        r2s = r2 * r2;
        y = Math.sqrt((-e + r1 + r2) * (e + r1 - r2) * (e - r1 + r2) * (e + r1 + r2));
        double f = (r1s * Math.acos((x + r1s - r2s) / (2 * e * r1))) + (r2s * Math.acos((x + r2s - r1s) / (2 * e * r2))) - (0.5 * y);
        if (f < 0) {
            return 0;
        }
        if (Double.isNaN(f)) {
            return 0;
        }
        return f;
    }
}
