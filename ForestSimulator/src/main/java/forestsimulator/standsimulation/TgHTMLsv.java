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

class TgHTMLsv {

    Stand st = new Stand();
    String filename = "";
    ResourceBundle messages;

    public TgHTMLsv(Stand st) {
    }

    void newreport(Stand st, String path, String fname, Locale preferredLanguage) {
        messages = ResourceBundle.getBundle("forestsimulator/gui");

        try {
            int i, j, merk;
//	    System.out.println("Neuen Bericht erzeugen nach try");
            /**
             * all data is writen in File info/treelist.html
             */
            File file = new File(path, fname);
            filename = file.getCanonicalPath();
            OutputStream os = new FileOutputStream(filename);
            PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(os));
            out.println("<HTML>");
            out.println("<H2><P align=center>" + messages.getString("single_tree_values") + "</P align=center></H2> ");
            out.println("<P><B>" + messages.getString("stand") + st.standname);
            out.println("<BR>" + messages.getString("stand_size") + st.size);
            out.println("<BR>" + messages.getString("year") + st.year + "</B></P>");
//      ss defined to write " 
            String ss;
            char c = 34;
            ss = String.valueOf(c);
            out.println("<HR>");
            out.println("<TABLE BORDER>");
            out.println("<TR><TH BGCOLOR=" + ss + "#C0C0C0" + ss + "><FONT SIZE=2>" + messages.getString("no") + "<TH BGCOLOR=" + ss + "#C0C0C0" + ss
                    + "><FONT SIZE=2>" + messages.getString("species") + "<TH BGCOLOR=" + ss + "#C0C0C0" + ss
                    + "><FONT SIZE=2>" + messages.getString("age") + "<TH BGCOLOR=" + ss + "#C0C0C0" + ss + "><FONT SIZE=2>" + messages.getString("DBH")
                    + "<TH BGCOLOR=" + ss + "#C0C0C0" + ss + "><FONT SIZE=2>" + messages.getString("height") + "<TH BGCOLOR=" + ss + "#C0C0C0" + ss
                    + "><FONT SIZE=2>" + messages.getString("cb") + " <TH BGCOLOR=" + ss + "#C0C0C0" + ss
                    + "><FONT SIZE=2>" + messages.getString("cw")
                    + "<TH BGCOLOR=" + ss + "#C0C0C0" + ss + "><FONT SIZE=2>" + messages.getString("cr") + "<TH BGCOLOR=" + ss + "#C0C0C0" + ss
                    + "><FONT SIZE=2>h/d  <TH BGCOLOR=" + ss + "#C0C0C0" + ss + "><FONT SIZE=2>Vol."
                    + "<TH BGCOLOR=" + ss + "#C0C0C0" + ss + "><FONT SIZE=2>aus    <TH BGCOLOR=" + ss + "#C0C0C0" + ss
                    + "><FONT SIZE=2>x <TH BGCOLOR=" + ss + "#C0C0C0" + ss
                    + "><FONT SIZE=2>y <TH BGCOLOR=" + ss + "#C0C0C0" + ss + "><FONT SIZE=2>z <TH BGCOLOR=" + ss + "#C0C0C0" + ss
                    + "><FONT SIZE=2>c66 <TH BGCOLOR=" + ss + "#C0C0C0" + ss
                    + "><FONT SIZE=2>c66c <TH BGCOLOR=" + ss + "#C0C0C0" + ss
                    + "><FONT SIZE=2>c66xy <TH BGCOLOR=" + ss + "#C0C0C0" + ss
                    + "><FONT SIZE=2>c66cxy <TH BGCOLOR=" + ss + "#C0C0C0" + ss
                    + "><FONT SIZE=2>Z-Baum <TH BGCOLOR=" + ss + "#C0C0C0" + ss
                    + "><FONT SIZE=2>Outtype <TH BGCOLOR=" + ss + "#C0C0C0" + ss
                    + "><FONT SIZE=2>Layer <TH BGCOLOR=" + ss + "#C0C0C0" + ss
                    + "><FONT SIZE=2>si <TH BGCOLOR=" + ss + "#C0C0C0" + ss + "></TR>");
            DecimalFormat f = new DecimalFormat("0.00");

            for (i = 0; i < st.ntrees; i++) {
                if (st.tr[i].out < 0) {
                    out.println("<TR><TD><FONT SIZE=2>" + st.tr[i].no + "<TD><FONT SIZE=2>" + st.tr[i].code + "<TD><FONT SIZE=2>" + st.tr[i].age
                            + "<TD><FONT SIZE=2>" + f.format(st.tr[i].d) + "<TD><FONT SIZE=2>" + f.format(st.tr[i].h)
                            + "<TD><FONT SIZE=2>" + f.format(st.tr[i].cb) + "<TD><FONT SIZE=2>" + f.format(st.tr[i].cw)
                            + "<TD><FONT SIZE=2>" + f.format((st.tr[i].h - st.tr[i].cb) / st.tr[i].h)
                            + "<TD><FONT SIZE=2>" + f.format((st.tr[i].h / st.tr[i].d))
                            + "<TD><FONT SIZE=2>" + f.format(st.tr[i].v) + "<TD><FONT SIZE=2>" + f.format(st.tr[i].out)
                            + "<TD><FONT SIZE=2>" + f.format(st.tr[i].x) + "<TD><FONT SIZE=2>" + f.format(st.tr[i].y)
                            + "<TD><FONT SIZE=2>" + f.format(st.tr[i].z)
                            + "<TD><FONT SIZE=2>" + f.format(st.tr[i].c66) + "<TD><FONT SIZE=2>" + f.format(st.tr[i].c66c)
                            + "<TD><FONT SIZE=2>" + f.format(st.tr[i].c66xy) + "<TD><FONT SIZE=2>" + f.format(st.tr[i].c66cxy)
                            + "<TD><FONT SIZE=2>" + st.tr[i].crop
                            + "<TD><FONT SIZE=2>" + st.tr[i].outtype
                            + "<TD><FONT SIZE=2>" + st.tr[i].layer
                            + "<TD><FONT SIZE=2>" + f.format(st.tr[i].si));
                }
            }
            int jz = -5;
            while (jz < 100) {
                jz = jz + 5;
                for (i = 0; i < st.ntrees; i++) {
                    if (st.tr[i].year == st.tr[i].out - jz) {
                        out.println("<TR><TD><FONT SIZE=2>" + st.tr[i].no + "<TD><FONT SIZE=2>" + st.tr[i].code + "<TD><FONT SIZE=2>" + st.tr[i].age
                                + "<TD><FONT SIZE=2>" + f.format(st.tr[i].d) + "<TD><FONT SIZE=2>" + f.format(st.tr[i].h)
                                + "<TD><FONT SIZE=2>" + f.format(st.tr[i].cb) + "<TD><FONT SIZE=2>" + f.format(st.tr[i].cw)
                                + "<TD><FONT SIZE=2>" + f.format((st.tr[i].h - st.tr[i].cb) / st.tr[i].h)
                                + "<TD><FONT SIZE=2>" + f.format((st.tr[i].h / st.tr[i].d))
                                + "<TD><FONT SIZE=2>" + f.format(st.tr[i].v) + "<TD><FONT SIZE=2>" + f.format(st.tr[i].out)
                                + "<TD><FONT SIZE=2>" + f.format(st.tr[i].x) + "<TD><FONT SIZE=2>" + f.format(st.tr[i].y)
                                + "<TD><FONT SIZE=2>" + f.format(st.tr[i].z)
                                + "<TD><FONT SIZE=2>" + f.format(st.tr[i].c66) + "<TD><FONT SIZE=2>" + f.format(st.tr[i].c66c)
                                + "<TD><FONT SIZE=2>" + f.format(st.tr[i].c66xy) + "<TD><FONT SIZE=2>" + f.format(st.tr[i].c66cxy)
                                + "<TD><FONT SIZE=2>" + st.tr[i].crop
                                + "<TD><FONT SIZE=2>" + st.tr[i].outtype
                                + "<TD><FONT SIZE=2>" + st.tr[i].layer
                                + "<TD><FONT SIZE=2>" + f.format(st.tr[i].si));
                    }
                }
            }

//Ende der Änderung Brandenburg                     
            out.println("</TABLE>");
            out.println("<br>" + messages.getString("created") + st.modelRegion + "</br></HTML>");
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getFilename() {
        return filename;
    }

}
