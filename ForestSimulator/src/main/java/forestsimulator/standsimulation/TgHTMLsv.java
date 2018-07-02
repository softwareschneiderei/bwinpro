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

    private File file;

    public TgHTMLsv(Stand st) {
    }

    void newreport(Stand st, String path, String fname, Locale preferredLanguage) {
        ResourceBundle messages = ResourceBundle.getBundle("forestsimulator/gui");
        this.file = new File(path, fname);

        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            out.println("<HTML>");
            out.println("<H2><P align=center>" + messages.getString("TgHTMLsv.heading.text") + "</P align=center></H2> ");
            out.println("<P><B>" + messages.getString("TgHTMLsv.standname.label") + st.standname);
            out.println("<BR>" + messages.getString("TgHTMLsv.standsize.label") + st.size);
            out.println("<BR>" + messages.getString("TgHTMLsv.year.label") + st.year + "</B></P>");
            out.println("<HR>");
            out.println("<TABLE BORDER>");
            out.println("<TR><TH BGCOLOR=\"#C0C0C0\"><FONT SIZE=2>" + messages.getString("TgHTMLsv.column.header.number") + "<TH BGCOLOR=\"#C0C0C0\""
                    + "><FONT SIZE=2>" + messages.getString("TgHTMLsv.column.header.species") + "<TH BGCOLOR=\"#C0C0C0\""
                    + "><FONT SIZE=2>" + messages.getString("TgHTMLsv.column.header.age")
                    + "<TH BGCOLOR=\"#C0C0C0\"><FONT SIZE=2>" + messages.getString("TgHTMLsv.column.header.dbh")
                    + "<TH BGCOLOR=\"#C0C0C0\"><FONT SIZE=2>" + messages.getString("TgHTMLsv.column.header.height") + "<TH BGCOLOR=\"#C0C0C\""
                    + "><FONT SIZE=2>" + messages.getString("TgHTMLsv.column.header.cb") + " <TH BGCOLOR=\"#C0C0C0\""
                    + "><FONT SIZE=2>" + messages.getString("TgHTMLsv.column.header.cw")
                    + "<TH BGCOLOR=\"#C0C0C0\"><FONT SIZE=2>" + messages.getString("TgHTMLsv.column.header.cr") + "<TH BGCOLOR=\"#C0C0C0\""
                    + "><FONT SIZE=2>" + messages.getString("TgHTMLsv.column.header.hd") + "<TH BGCOLOR=\"#C0C0C0\"><FONT SIZE=2>Vol."
                    + "<TH BGCOLOR=\"#C0C0C0\"><FONT SIZE=2>" + messages.getString("TgHTMLsv.column.header.cr") + "aus    <TH BGCOLOR=\"#C0C0C0\""
                    + "><FONT SIZE=2>" + messages.getString("TgHTMLsv.column.header.x") + "<TH BGCOLOR=\"#C0C0C0\""
                    + "><FONT SIZE=2>" + messages.getString("TgHTMLsv.column.header.y") + "<TH BGCOLOR=\"#C0C0C0\"><FONT SIZE=2>z <TH BGCOLOR=\"#C0C0C0\""
                    + "><FONT SIZE=2>" + messages.getString("TgHTMLsv.column.header.c66") + "<TH BGCOLOR=\"#C0C0C0\""
                    + "><FONT SIZE=2>" + messages.getString("TgHTMLsv.column.header.c66c") + "<TH BGCOLOR=\"#C0C0C0\""
                    + "><FONT SIZE=2>" + messages.getString("TgHTMLsv.column.header.c66xy") + "<TH BGCOLOR=\"#C0C0C0\""
                    + "><FONT SIZE=2>" + messages.getString("TgHTMLsv.column.header.c66cxy") + "<TH BGCOLOR=\"#C0C0C0\""
                    + "><FONT SIZE=2>" + messages.getString("TgHTMLsv.column.header.croptree") + "<TH BGCOLOR=\"#C0C0C0\""
                    + "><FONT SIZE=2>" + messages.getString("TgHTMLsv.column.header.outtype") + "<TH BGCOLOR=\"#C0C0C0\""
                    + "><FONT SIZE=2>" + messages.getString("TgHTMLsv.column.header.layer") + "<TH BGCOLOR=\"#C0C0C0\""
                    + "><FONT SIZE=2>" + messages.getString("TgHTMLsv.column.header.si") + "<TH BGCOLOR=\"#C0C0C0\"></TR>");
            DecimalFormat f = new DecimalFormat("0.00");

            st.forTreesMatching(tree -> tree.isLiving(), tree -> {
                    out.println("<TR><TD><FONT SIZE=2>" + tree.no + "<TD><FONT SIZE=2>" + tree.code + "<TD><FONT SIZE=2>" + tree.age
                            + "<TD><FONT SIZE=2>" + f.format(tree.d) + "<TD><FONT SIZE=2>" + f.format(tree.h)
                            + "<TD><FONT SIZE=2>" + f.format(tree.cb) + "<TD><FONT SIZE=2>" + f.format(tree.cw)
                            + "<TD><FONT SIZE=2>" + f.format((tree.h - tree.cb) / tree.h)
                            + "<TD><FONT SIZE=2>" + f.format((tree.h / tree.d))
                            + "<TD><FONT SIZE=2>" + f.format(tree.v) + "<TD><FONT SIZE=2>" + f.format(tree.out)
                            + "<TD><FONT SIZE=2>" + f.format(tree.x) + "<TD><FONT SIZE=2>" + f.format(tree.y)
                            + "<TD><FONT SIZE=2>" + f.format(tree.z)
                            + "<TD><FONT SIZE=2>" + f.format(tree.c66) + "<TD><FONT SIZE=2>" + f.format(tree.c66c)
                            + "<TD><FONT SIZE=2>" + f.format(tree.c66xy) + "<TD><FONT SIZE=2>" + f.format(tree.c66cxy)
                            + "<TD><FONT SIZE=2>" + tree.crop
                            + "<TD><FONT SIZE=2>" + tree.outtype
                            + "<TD><FONT SIZE=2>" + tree.layer
                            + "<TD><FONT SIZE=2>" + f.format(tree.si));
            });
            int jz = -5;
            while (jz < 100) {
                jz = jz + 5;
                for (int i = 0; i < st.ntrees; i++) {
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
            out.println("<br>" + messages.getString("TgHTMLsv.created.label") + st.modelRegion + "</br></HTML>");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getFilename() {
        return file.getAbsolutePath();
    }
}
