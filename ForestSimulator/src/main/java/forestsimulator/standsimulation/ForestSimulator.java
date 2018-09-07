/** http://www.nw-fva.de
   Version 19-02-2009

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

import forestsimulator.language.UserLanguage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import treegross.base.*;

public class ForestSimulator {
    private static final Logger batchLogger = Logger.getLogger("BatchLogger");
    private static final Logger logger = Logger.getLogger(TgJFrame.class.getName());
    private static final ResourceBundle messages = ResourceBundle.getBundle("forestsimulator/gui");

    public static void main(String args[]) throws IOException {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$s %2$s %5$s%6$s%n");
        System.setProperty("swing.plaf.metal.controlFont", "Tahoma");
        System.setProperty("swing.aatext", "true");
        System.getProperties().list(System.out);
        
        File baseDirectory = new File(".");
        
        Stand st = new Stand();
        final TgUser userSettings = new TgUser(baseDirectory);
        if (!userSettings.fileExistsInWorkingDir("ForestSimulator.ini")) {
            JDialog settings = new TgUserDialog(null, true);
            settings.setVisible(true);
            JTextArea about = new JTextArea(messages.getString("TgJFrame.applySettingsDialog.message"));
            JOptionPane.showMessageDialog(null, about, messages.getString("TgJFrame.applySettingsDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
            return;
        }
        System.out.println("Settings laden ");
        logger.log(Level.INFO, "TgJFrame local path : {0}", baseDirectory.getCanonicalPath());
        userSettings.loadSettings();
        UserLanguage language = UserLanguage.forLocale(userSettings.getLanguageShort());
        Locale.setDefault(language.locale());
        st.modelRegion = userSettings.getPlugIn();
        st.FileXMLSettings = userSettings.XMLSettings;
        logger.log(Level.INFO, "Modell :{0}", userSettings.getPlugIn());
        
        configureBatchlogger(userSettings);
        
        JFrame mainWindow = new TgJFrame(st, userSettings, language);
        mainWindow.setVisible(true);
    }

    private static void configureBatchlogger(final TgUser userSettings) throws SecurityException, IOException {
        final FileHandler fileHandler = new FileHandler(new File(userSettings.getWorkingDir(), "batch.log").getAbsolutePath());
        fileHandler.setFormatter(new SimpleFormatter());
        batchLogger.addHandler(fileHandler);
    }
}
