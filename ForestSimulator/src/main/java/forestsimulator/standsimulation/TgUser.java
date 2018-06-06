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

import forestsimulator.util.Settings;
import java.io.*;
import java.net.*;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Logger;

class TgUser {

    private File workingDir;
    private File programDir;
    private File dataDir;
    private String languageCode = "en";
    String plugIn = "XML";
    String XMLSettings = "";
    int grafik3D = 0;
    String update = "01012007";
    String nwfva = null;
    private static final Logger logger = Logger.getLogger(TgUser.class.getName());
    private final File baseDirectory;
    private final Properties settings;

    public TgUser(File baseDirectory) {
        super();
        this.baseDirectory = baseDirectory;
        this.settings = new Settings();
    }

    /**
     * The user settings are load from a file TgUser.txt, which has to be in the
     * same directory
     */
    public void loadSettings() {
        try (Reader iniFile = settingsFileReader()) {
            loadSettings(iniFile);
        } catch (IOException | NumberFormatException e) {
            logger.warning(e.getMessage());
        }
    }

    private Reader settingsFileReader() throws FileNotFoundException {
        return new InputStreamReader(new FileInputStream(settingsFile()));
    }

    private File settingsFile() {
        return new File(baseDirectory, "ForestSimulator.ini");
    }

    void loadSettings(Reader in) throws IOException, NumberFormatException {
        settings.load(in);
        programDir = parseToFile("program.directory").getCanonicalFile();
        dataDir = parseToFile("data.directory").getCanonicalFile();
        workingDir = parseToFile("working.directory").getCanonicalFile();
        languageCode = settings.getProperty("language.code", "");
        XMLSettings = settings.getProperty("settings.file", "");
        plugIn = XMLSettings;
        int m = XMLSettings.indexOf(" -");
        if (m > 0) {
            nwfva = XMLSettings.substring(m + 2);
            XMLSettings = XMLSettings.substring(0, m);
        }
        grafik3D = Integer.parseInt(settings.getProperty("graphics3d", "0"));
    }

    private File parseToFile(String property) {
        final String normalizedPath = normalizePath(settings.getProperty(property));
        File f = new File(normalizedPath);
        if (f.isAbsolute()) {
            return f;
        }
        return new File(baseDirectory, normalizedPath);
    }

    private static String normalizePath(String path) {
        if (path == null) {
            return ".";
        }
        return path;
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

    public String getXMLSettings() {
        return plugIn;
    }

    public String getPlugIn() {
        return plugIn;
    }

    public boolean getGrafik3D() {
        return grafik3D == 1;
    }

    public Locale getLanguageShort() {
        if (languageCode.isEmpty()) {
            return Locale.forLanguageTag(System.getProperty("user.language"));
        }
        return Locale.forLanguageTag(languageCode);
    }

    public boolean needsUpdate(String lastupdate) {
        update = lastupdate;
        boolean erg = false;
        String updateInternet = null;
        String fname = "https://www.nw-fva.de/~nagel/downloads/bwin7version.txt";
        try {
            URL url = new URL(fname);
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

    public void saveSettings(String programDir, String dataDir, String workingDir, Locale Language, String settingsFileName, int g3D) throws IOException {
        try (Writer ausgabe = new FileWriter(settingsFile())) {
            saveSettingsTo(ausgabe, programDir, dataDir, workingDir, Language, settingsFileName, g3D);
        }        
    }

    void saveSettingsTo(final Writer ausgabe, String programDir, String dataDir, String workingDir, Locale Language, String settingsFileName, int g3D) throws IOException {
        settings.setProperty("program.directory", programDir);
        settings.setProperty("data.directory", dataDir);
        settings.setProperty("working.directory", workingDir);
        settings.setProperty("language.code", Language.toString());
        settings.setProperty("settings.file", settingsFileName);
        settings.setProperty("graphics3d", String.valueOf(g3D));
        settings.store(ausgabe, null);
    }
}
