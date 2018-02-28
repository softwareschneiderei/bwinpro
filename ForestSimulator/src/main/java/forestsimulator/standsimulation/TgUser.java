/** http://www.nw-fva.de
 * Version 07-11-2008
 *
 * (c) 2002 Juergen Nagel, Northwest German Forest Research Station,
 * Grätzelstr.2, 37079 Göttingen, Germany
 * E-Mail: Juergen.Nagel@nw-fva.de
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT  WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package forestsimulator.standsimulation;

import java.io.*;
import java.net.*;
import java.util.logging.Logger;

class TgUser {

    File workingDir;
    File programDir;
    File dataDir;
    File workingDirIni;
    File programDirIni;
    File dataDirIni;
    String plugIn = "XML";
    String XMLSettings = "";
    String language = "en";
    int grafik3D = 0;
    String update = "01012007";
    String nwfva = null;
    private final Logger logger = Logger.getLogger(getClass().getName());

    public TgUser() {
    }

    /**
     * The user settings are load from a file TgUser.txt, which has to be in the
     * same directory
     */
    public void loadSettings(File baseDirectory) {
        try (BufferedReader iniFile = iniFileReaderIn(baseDirectory)) {
            loadSettings(iniFile, baseDirectory);
        } catch (IOException | NumberFormatException e) {
            logger.warning(e.getMessage());
        }
    }

    private static BufferedReader iniFileReaderIn(File baseDirectory) throws FileNotFoundException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(new File(baseDirectory, "ForestSimulator.ini"))));
    }

    void loadSettings(BufferedReader in, File baseDirectory) throws IOException, NumberFormatException {
        programDirIni = new File(baseDirectory, in.readLine());
        programDir = programDirIni.getCanonicalFile();
        dataDirIni = new File(baseDirectory, in.readLine());
        dataDir = dataDirIni.getCanonicalFile();
        workingDirIni = new File(baseDirectory, in.readLine());
        workingDir = workingDirIni.getCanonicalFile();
        language = in.readLine();
        XMLSettings = in.readLine();
        plugIn = XMLSettings;
        int m = XMLSettings.indexOf(" -");
        if (m > 0) {
            nwfva = XMLSettings.substring(m + 2);
            XMLSettings = XMLSettings.substring(0, m);
        }
        grafik3D = Integer.parseInt(in.readLine());
    }

    public boolean fileExists(String fname) {
        File f = new File(fname);
        System.out.println(f + (f.exists() ? " is found " : " is missing "));
        return f.exists();
    }

    public File getWorkingDir() {
        return workingDir;
    }

    public File getProgramDir() {
        return programDir;
    }

    public File getDataDir() {
        return dataDir;
    }

    public File getWorkingDirIni() {
        return workingDirIni;
    }

    public File getProgramDirIni() {
        return programDirIni;
    }

    public File getDataDirIni() {
        return dataDirIni;
    }

    public String getXMLSettings() {
        return plugIn;
    }

    public String getPlugIn() {
        return plugIn;
    }

    public int getGrafik3D() {
        return grafik3D;
    }

    public String getLanguageShort() {
        String languageShort = "";
        if (language.compareTo("Deutsch") == 0) {
            languageShort = "de";
        }
        if (language.compareTo("Espanol") == 0) {
            languageShort = "es";
        }
        if (language.compareTo("Polish") == 0) {
            languageShort = "pl";
        }
        return languageShort;
    }

    public boolean needsUpdate(String lastupdate) {
        update = lastupdate;
        boolean erg = false;
        URL url = null;
        String updateInternet = null;
        String fname = "https://www.nw-fva.de/~nagel/downloads/bwin7version.txt";
//            String fname="file:///W:/public_html/downloads/bwin7version.txt";
        try {
            url = new URL(fname);
            URLConnection urlcon = url.openConnection();

            urlcon.setReadTimeout(1000);
            java.io.BufferedReader br = new java.io.BufferedReader(
                    new java.io.InputStreamReader(
                            urlcon.getInputStream()));
            updateInternet = br.readLine();

        } catch (java.io.IOException e) {
            System.out.println("kein Internet Check möglich !");
        }
        if (updateInternet != null) {
            int yearNet = Integer.parseInt(updateInternet.substring(6, 10));
            int monthNet = Integer.parseInt(updateInternet.substring(3, 5));
            int dayNet = Integer.parseInt(updateInternet.substring(0, 2));
            int year = Integer.parseInt(update.substring(6, 10));
            int month = Integer.parseInt(update.substring(3, 5));
            int day = Integer.parseInt(update.substring(0, 2));
            double sumNet = 365 * yearNet + 31 * monthNet + dayNet;
            double sum = 365 * year + 31 * month + day;
            if (sum < sumNet) {
                erg = true;
            }
        }
        return erg;
    }
}
