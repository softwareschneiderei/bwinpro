package forestsimulator.standsimulation;

import java.awt.HeadlessException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import treegross.base.Stand;

/**
 *
 * @author nagel
 */
public class WET {
    private final ResourceBundle messages = ResourceBundle.getBundle("forestsimulator/gui");

    int[] wets;
    int nWets = 0;
    String wttxt[] = new String[23];

    public int[] loadWETs(String Dir) {
        String fname = "";
        nWets = 0;
        URL url = null;
        try {
            int m = Dir.toUpperCase().indexOf("FILE");
            int m2 = Dir.toUpperCase().indexOf("HTTP");
            String trenn = System.getProperty("file.separator");
            fname = Dir + System.getProperty("file.separator") + "moduls" + System.getProperty("file.separator") + "WET" + System.getProperty("file.separator") + "WET_Definition.xml";
            if (m < 0 && m2 < 0) {
                fname = "file:" + trenn + trenn + trenn + fname;
            }
            System.out.println("SpeciesDef: URL: " + fname);
            try {
                url = new URL(fname);
            } catch (MalformedURLException e) {
                showFileNotFoundMessage(fname);
            }

            SAXBuilder builder = new SAXBuilder();
            URLConnection urlcon = url.openConnection();

            Document doc = builder.build(urlcon.getInputStream());

            Element sortimente = doc.getRootElement();
            List Sortiment = sortimente.getChildren("WET_Definition");
            Iterator i = Sortiment.iterator();

            while (i.hasNext()) {
                Element sortiment = (Element) i.next();
                String land = "";
                int wbr = 0;
                land = sortiment.getChild("Land").getText().trim();
                wbr = Integer.parseInt(sortiment.getChild("WaldbauRegel").getText().trim());
                if (land.equals("Niedersachsen") == true && wbr == 0) {
                    nWets = nWets + 1;
                }
            }
            wets = new int[nWets];
            nWets = 0;

            sortimente = doc.getRootElement();
            Sortiment = sortimente.getChildren("WET_Definition");
            i = Sortiment.iterator();

            while (i.hasNext()) {
                Element sortiment = (Element) i.next();
                String land = "";
                land = sortiment.getChild("Land").getText().trim();
                int wbr = 0;
                wbr = Integer.parseInt(sortiment.getChild("WaldbauRegel").getText().trim());
                land = sortiment.getChild("Land").getText().trim();
                if (land.equals("Niedersachsen") && wbr == 0) {
                    wets[nWets] = Integer.parseInt(sortiment.getChild("WET").getText().trim());
                    nWets = nWets + 1;
                }
            }
        } catch (HeadlessException | IOException | NumberFormatException | JDOMException e) {
            showFileNotFoundMessage(fname);
            System.out.println("SpeciesDef: File nicht gefunden: " + fname);
        }
        return wets;
    }

    private void showFileNotFoundMessage(String fname) throws HeadlessException {
        JTextArea message = new JTextArea(MessageFormat.format(messages.getString("WET.file_not_found.message"), fname));
        JOptionPane.showMessageDialog(null, message, messages.getString("WET.file_not_found.title"), JOptionPane.INFORMATION_MESSAGE);
    }

    public int getWetNr() {
        int wetNr = 21;
        return wetNr;
    }

    public int getWT(Stand st) {
        int wt = 0;
// Hauptart suchen
        int art1 = 0;
        double g1 = 0;
        for (int i = 0; i < st.nspecies; i++) {
            if (st.sp[i].gha > g1) {
                art1 = st.sp[i].code;
                g1 = st.sp[i].gha;
            }
        }
        wt = ((int) Math.floor(art1 / 100)) * 10;
        if (st.nspecies > 1) {
            int art2 = 0;
            double g2 = 0;
            for (int i = 0; i < st.nspecies; i++) {
                if (st.sp[i].gha > g2 && art1 != st.sp[i].code) {
                    art2 = st.sp[i].code;
                    g2 = st.sp[i].gha;
                }
            }
            wt = wt + ((int) Math.floor(art2 / 100));
        }
        return wt;
    }

    public String[] getWTtxt(String Dir, int nr) {
        java.io.File file;
        String fname = "";
        nWets = 0;
        URL url = null;
        try {
            int m = Dir.toUpperCase().indexOf("FILE");
            int m2 = Dir.toUpperCase().indexOf("HTTP");
            String trenn = System.getProperty("file.separator");
            fname = Dir + System.getProperty("file.separator") + "moduls" + System.getProperty("file.separator") + "WET" + System.getProperty("file.separator") + "WET_Definition.xml";
            if (m < 0 && m2 < 0) {
                fname = "file:" + trenn + trenn + trenn + fname;
            }
            System.out.println("SpeciesDef: URL: " + fname);
            try {
                url = new URL(fname);
            } catch (MalformedURLException e) {
                showFileNotFoundMessage(fname);
            }

            SAXBuilder builder = new SAXBuilder();
            URLConnection urlcon = url.openConnection();

            Document doc = builder.build(urlcon.getInputStream());

            DocType docType = doc.getDocType();
//        
            Element sortimente = doc.getRootElement();
            List Sortiment = sortimente.getChildren("WET_Definition");
            Iterator i = Sortiment.iterator();

            while (i.hasNext()) {
                Element sortiment = (Element) i.next();
                String land = "";
                land = sortiment.getChild("Land").getText().trim();
                int wbr = 0;
                int nrx = 0;
                wbr = Integer.parseInt(sortiment.getChild("WaldbauRegel").getText().trim());
                nrx = Integer.parseInt(sortiment.getChild("WET").getText().trim());
                land = sortiment.getChild("Land").getText().trim();
                if (land.equals("Niedersachsen") && wbr == 0 && nrx == nr) {
                    wttxt[0] = sortiment.getChild("Artenmix").getText().trim();
                    wttxt[1] = sortiment.getChild("WaldbauRegel").getText().trim();
                    wttxt[2] = sortiment.getChild("Erschliessungs_Hoehe").getText().trim();
                    wttxt[3] = sortiment.getChild("Dftyp").getText().trim();
                    wttxt[4] = sortiment.getChild("Dfintensitaet").getText().trim();
                    wttxt[5] = sortiment.getChild("DfvolumenMin").getText().trim();
                    wttxt[6] = sortiment.getChild("DfvolumenMax").getText().trim();
                    wttxt[7] = sortiment.getChild("DfnurZB").getText().trim();
                    wttxt[8] = sortiment.getChild("Hiebsform").getText().trim();
                    wttxt[9] = sortiment.getChild("Hiebsvolumenmin").getText().trim();
                    wttxt[10] = sortiment.getChild("Hiebsvolumenmax").getText().trim();
                    try {
                        wttxt[11] = sortiment.getChild("Verjuengungsgang").getText().trim();
                    } catch (Exception e) {
                        wttxt[11] = " ";
                    }
                    try {
                        wttxt[12] = sortiment.getChild("PflanzungsSchlussgrad").getText().trim();
                    } catch (Exception e) {
                        wttxt[12] = "0.0";
                    }
                    wttxt[13] = sortiment.getChild("UnterstandEntfernen").getText().trim();
                    wttxt[14] = sortiment.getChild("Habitatbaeume").getText().trim();
                    wttxt[15] = sortiment.getChild("Habitatbaumarten").getText().trim();
                    wttxt[16] = sortiment.getChild("Minderheit").getText().trim();
                    wttxt[17] = sortiment.getChild("ZielBHDZB").getText().trim();
                    wttxt[18] = sortiment.getChild("ZielZBäume").getText().trim();
                }
            }
        } catch (HeadlessException | IOException | NumberFormatException | JDOMException e) {
            showFileNotFoundMessage(fname);
            System.out.println("SpeciesDef: File nicht gefunden: " + fname);
        }
        return wttxt;
    }
}
