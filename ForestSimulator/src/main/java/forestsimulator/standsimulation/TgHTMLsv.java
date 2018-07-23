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
import java.util.logging.Level;
import java.util.logging.Logger;

class TgHTMLsv {

    private static final Logger logger = Logger.getLogger(TgHTMLsv.class.getName());
    private static final ResourceBundle messages = ResourceBundle.getBundle("forestsimulator/gui");
    private static final DecimalFormat f = new DecimalFormat("0.00");

    private File file;

    public TgHTMLsv(Stand st) {
    }

    void newreport(Stand st, String path, String fname, Locale preferredLanguage) {
        this.file = new File(path, fname);

        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            out.println("<HTML>");
            out.println("<H2><P align=center>" + messages.getString("TgHTMLsv.heading.text") + "</P align=center></H2> ");
            out.println("<P><B>" + messages.getString("TgHTMLsv.standname.label") + st.standname);
            out.println("<BR>" + messages.getString("TgHTMLsv.standsize.label") + st.size);
            out.println("<BR>" + messages.getString("TgHTMLsv.year.label") + st.year + "</B></P>");
            out.println("<HR>");
            out.println("<TABLE BORDER>");
            out.println("<TR>"
                    + tableHeaderOf(messages.getString("TgHTMLsv.column.header.number"))
                    + tableHeaderOf(messages.getString("TgHTMLsv.column.header.species"))
                    + tableHeaderOf(messages.getString("TgHTMLsv.column.header.age"))
                    + tableHeaderOf(messages.getString("TgHTMLsv.column.header.dbh"))
                    + tableHeaderOf(messages.getString("TgHTMLsv.column.header.height"))
                    + tableHeaderOf(messages.getString("TgHTMLsv.column.header.cb"))
                    + tableHeaderOf(messages.getString("TgHTMLsv.column.header.cw"))
                    + tableHeaderOf(messages.getString("TgHTMLsv.column.header.cr"))
                    + tableHeaderOf(messages.getString("TgHTMLsv.column.header.hd"))
                    // TODO: Extract Vol. String into resource bundle
                    + tableHeaderOf("Vol.")
                    + tableHeaderOf(messages.getString("TgHTMLsv.column.header.cr") + "aus")
                    + tableHeaderOf(messages.getString("TgHTMLsv.column.header.x"))
                    + tableHeaderOf(messages.getString("TgHTMLsv.column.header.y"))
                    // TODO: Extract z into resource bundle
                    + tableHeaderOf("z")
                    + tableHeaderOf(messages.getString("TgHTMLsv.column.header.c66"))
                    + tableHeaderOf(messages.getString("TgHTMLsv.column.header.c66c"))
                    + tableHeaderOf(messages.getString("TgHTMLsv.column.header.c66xy"))
                    + tableHeaderOf(messages.getString("TgHTMLsv.column.header.c66cxy"))
                    + tableHeaderOf(messages.getString("TgHTMLsv.column.header.croptree"))
                    + tableHeaderOf(messages.getString("TgHTMLsv.column.header.outtype"))
                    + tableHeaderOf(messages.getString("TgHTMLsv.column.header.layer"))
                    + tableHeaderOf(messages.getString("TgHTMLsv.column.header.si"))
                    + "</TR>");

            st.forTreesMatching(tree -> tree.isLiving(), tree -> {
                writeDataRow(out, tree);
            });
            int jz = -5;
            while (jz < 100) {
                jz = jz + 5;
                for (int i = 0; i < st.ntrees; i++) {
                    if (st.tr[i].year == st.tr[i].out - jz) {
                        Tree tree = st.tr[i];
                        writeDataRow(out, tree);
                    }
                }
            }
            out.println("</TABLE>");
            out.println("<br>" + messages.getString("TgHTMLsv.created.label") + st.modelRegion + "</br></HTML>");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not write HTML report.", e);
        }
    }

    private void writeDataRow(PrintWriter out, Tree tree) {
        out.println("<TR>"
                + valueCellOf(tree.no)
                + valueCellOf(tree.code)
                + valueCellOf(tree.age)
                + valueCellOf(f.format(tree.d))
                + valueCellOf(f.format(tree.h))
                + valueCellOf(f.format(tree.cb))
                + valueCellOf(f.format(tree.cw))
                + valueCellOf(f.format((tree.h - tree.cb) / tree.h))
                + valueCellOf(f.format((tree.h / tree.d)))
                + valueCellOf(f.format(tree.v))
                + valueCellOf(f.format(tree.out))
                + valueCellOf(f.format(tree.x))
                + valueCellOf(f.format(tree.y))
                + valueCellOf(f.format(tree.z))
                + valueCellOf(f.format(tree.c66))
                + valueCellOf(f.format(tree.c66c))
                + valueCellOf(f.format(tree.c66xy))
                + valueCellOf(f.format(tree.c66cxy))
                + valueCellOf(tree.crop)
                + valueCellOf(tree.outtype)
                + valueCellOf(tree.layer)
                + valueCellOf(f.format(tree.si))
                + "</TR>");
    }
    
    private String tableHeaderOf(String heading) {
        return "<TH BGCOLOR=\"#C0C0C0\"><FONT SIZE=2>" + heading + "</FONT></TH>";
    }
    
    private String valueCellOf(Object value) {
        return "<TD><FONT SIZE=2>" + value + "</FONT></TD>";
    }

    public String getFilename() {
        return file.getAbsolutePath();
    }
}
