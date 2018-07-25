/* http://www.nw-fva.de
   Version 05-04-2013

   (c) 2002-12 Juergen Nagel, Northwest German Forest Research Station, 
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

import forestsimulator.roots.RootingDialog;
import nwfva.assortment.SortingDialog;
import nwfva.biomass.BiomassDialog;
import forestsimulator.Stand3D.PackageInfo;
import forestsimulator.Stand3D.Manager3D;
import forestsimulator.Stand3D.Query3DProperties;
import forestsimulator.language.UserLanguage;
import forestsimulator.util.StopWatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import treegross.base.*;
import treegross.treatment.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.text.MessageFormat;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.*;
import org.jdom.JDOMException;

/**
 * @author Juergen Nagel
 *
 * Main jFrame of the Forest Simulator, this controlls the whole GUI
 */
public class TgJFrame extends JFrame implements ActionListener, ItemListener, StandChangeListener {

    private static final Logger LOGGER = Logger.getLogger(TgJFrame.class.getName());
    private final ResourceBundle messages = ResourceBundle.getBundle("forestsimulator/gui");
    private final TgInternalFrame[] iframe = new TgInternalFrame[8];
    private final SpeciesDefMap speciesDefinitions = new SpeciesDefMap();
    private final FileHandler logHandler;

    String bwinproVersion = "Version 7.8-0.5dev";
    String bwinproLastUpdate = "11.07.2018";
    private boolean accessInput = true;
    private Stand st = new Stand();
    File currentFile;
    StringBuffer ColorInfo;
    String seite;
//   TgStandMap zf  = new TgStandMap(st, this); //add standmap class
//   TgPPmap pp = new TgPPmap(st, this); //add prallel projection class
    TgStandMap zf;   //add standmap class
    TgPPmap pp;  //add prallel projection class
    TgHTMLsv sv = new TgHTMLsv(st);
//   TgGrafik gr = new TgGrafik(st);
    TgGrafik gr;
    TgProgramInfo tgProgramInfo;
    TgDesign sd;
    TgYieldTable yt = new TgYieldTable();
    Treatment2 tl = new Treatment2();
    private final TgTreatmentMan3 treatmentMan3;
    TgStandInfo tsi;
    UserLanguage language;

    MainMenubar menubar;
    TgStandMapMenu tgStandMapMenu;
    TgPPMapMenu tgPPMapMenu;
//   TgTreatmentMan2Menu tgTreatmentMan2Menu;
    TgGrafikMenu tgGrafikMenu;
    TgToolbar toolbar;
    TgScreentoolbar tgScreentoolbar;
    JDesktopPane dp = new JDesktopPane();
    TgUser user;
    private JFrame owner;

    private Manager3D manager3d;

    private boolean grafik3D = false;
    boolean StandardColors = false;
    boolean tfUpdateTrue = true;
    File programDir;
    File workingDir;
    File dataDir;
    String plugIn = "XML";
    String kspDataFile = "";
    String kspNextPlot = "";
    Dimension scr;

    int kspTyp = 0;

    public TgJFrame(Stand stneu) throws IOException {
        logHandler = new FileHandler("log.txt");
        logHandler.setFormatter(new SimpleFormatter());
        LOGGER.addHandler(logHandler);
        LOGGER.log(Level.INFO, "ForestSimulator Version: {0}", bwinproVersion);

        st = stneu;
        st.addStandChangeListener(this);
        scr = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(scr.width, (scr.height - (scr.height / 50)));
        File workingDirectory = new java.io.File(".");
        // TODO: move loading of settings and setting of the locale into ForestSimulator.main()
        user = new TgUser(workingDirectory);
        if (!user.fileExists(workingDirectory.getCanonicalPath() + System.getProperty("file.separator") + "ForestSimulator.ini")) {
            JDialog settings = new TgUserDialog(this, true);
            settings.setVisible(true);
            JTextArea about = new JTextArea(messages.getString("TgJFrame.applySettingsDialog.message"));
            JOptionPane.showMessageDialog(this, about, messages.getString("TgJFrame.applySettingsDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        } else {
            System.out.println("Settings laden ");
            LOGGER.log(Level.INFO, "TgJFrame local path : {0}", workingDirectory.getCanonicalPath());
            user.loadSettings();
            language = UserLanguage.forLocale(user.getLanguageShort());
            Locale.setDefault(language.locale());
            plugIn = user.getPlugIn();
            st.modelRegion = plugIn;
            st.FileXMLSettings = user.XMLSettings;
            LOGGER.log(Level.INFO, "Modell :{0}", plugIn);
        }
        setTitle(getTitle() + "Forest Simulator BWINPro 7 " + bwinproVersion + " - Modell: " + plugIn);
        boolean available3d = is3dAvailable();
        if (plugIn.indexOf("nwfva") > 0) {
            accessInput = true;
        }
        zf = new TgStandMap(st, this); //add standmap class
        pp = new TgPPmap(st, this); //add prallel projection class
        gr = new TgGrafik(st);
        grafik3D = user.getGrafik3D() && is3dAvailable();

        programDir = user.getProgramDir();
        speciesDefinitions.readFromPath(new File(new File(programDir, "models"), st.FileXMLSettings));
        st.setSDM(speciesDefinitions);
        workingDir = user.getWorkingDir();
        currentFile = user.getDataDir();
        st.setProgramDir(programDir);
        loadGeneralSettings(programDir);


        JPanel zfneu = new JPanel();
        zfneu.setLayout(new BorderLayout());
        JPanel tgStandMapMenus = new JPanel();
        tgStandMapMenus.setLayout(new BoxLayout(tgStandMapMenus, BoxLayout.X_AXIS));
// adding the menu
        tgStandMapMenu = new TgStandMapMenu(this, this, language.locale());
        tgStandMapMenu.setAlignmentY(Component.CENTER_ALIGNMENT);
        tgStandMapMenus.add(tgStandMapMenu);
        zfneu.add(tgStandMapMenus, BorderLayout.NORTH);
        zfneu.add(zf, BorderLayout.CENTER);
//
// Simple parallel project  or 3D View of stand              
        JPanel ppneu = new JPanel();
        if (grafik3D) {
            manager3d = new Manager3D(new JPanel(), programDir, true);
            if (manager3d.get3DAvailable()) {
                ppneu.setPreferredSize(new Dimension((((scr.width - 140) / 2) - (scr.width / 50)), (scr.height / 2) - (scr.height / 50)));
                manager3d = new Manager3D(ppneu, programDir, true);
                grafik3D = true;
            }
        } else {
            ppneu.setLayout(new BorderLayout());
            JPanel tgPPMapMenus = new JPanel();
            tgPPMapMenus.setLayout(new BoxLayout(tgPPMapMenus, BoxLayout.X_AXIS));
            // adding the menu
            tgPPMapMenu = new TgPPMapMenu(this, this);
            tgPPMapMenu.setAlignmentY(Component.CENTER_ALIGNMENT);
            tgPPMapMenus.add(tgPPMapMenu);
            ppneu.add(tgPPMapMenus, BorderLayout.NORTH);
            ppneu.add(pp, BorderLayout.CENTER);
        }
        tsi = new TgStandInfo(language.locale());

        // Treatment Manager Window                
        treatmentMan3 = new TgTreatmentMan3(st, this, user);
        JPanel treatmentPanel = new JPanel();
        treatmentPanel.setLayout(new BorderLayout());
        treatmentPanel.add(treatmentMan3, BorderLayout.CENTER);

        sd = new TgDesign(st, this, language.locale());
// add Grafik Menu 
        JPanel grWithMenu = new JPanel();
        grWithMenu.setLayout(new BorderLayout());
        JPanel tggrMenus = new JPanel();
        tggrMenus.setLayout(new BoxLayout(tggrMenus, BoxLayout.X_AXIS));
        tgGrafikMenu = new TgGrafikMenu(this);
        tgGrafikMenu.setAlignmentY(Component.CENTER_ALIGNMENT);
        tggrMenus.add(tgGrafikMenu);
        grWithMenu.add(tggrMenus, BorderLayout.NORTH);
        grWithMenu.add(gr, BorderLayout.CENTER);

        iframe[0] = new TgInternalFrame(zfneu, messages.getString("TgInternalFrame.standmap.title"));
        iframe[1] = new TgInternalFrame(ppneu, messages.getString("TgInternalFrame.parallelProjection.title"));
        iframe[2] = new TgInternalFrame(grWithMenu, messages.getString("TgInternalFrame.graphics.title"));
        iframe[3] = new TgInternalFrame(sd, messages.getString("TgInternalFrame.addTrees.title"));
        iframe[4] = new TgInternalFrame(treatmentPanel, messages.getString("TgInternalFrame.SimulationSetting.title"));
        iframe[5] = new TgInternalFrame(tsi, messages.getString("TgInternalFrame.standInfo.title"));
        tgProgramInfo = new TgProgramInfo(this);
        iframe[6] = new TgInternalFrame(tgProgramInfo, messages.getString("TgInternalFrame.programInfo.title"));

        JPanel menus = new JPanel();
        menus.setLayout(new BoxLayout(menus, BoxLayout.X_AXIS));
        // adding the menu
        menubar = new MainMenubar(this, this, language.locale(), st, accessInput);
        menubar.setAlignmentY(Component.CENTER_ALIGNMENT);
        menus.add(menubar);
        menus.add(Box.createRigidArea(new Dimension(20, 0)));
        //adding the toolbar
        toolbar = new TgToolbar(this, programDir.getCanonicalPath(), language.locale());
        menus.add(toolbar);
        // adding screen toolbar
        tgScreentoolbar = new TgScreentoolbar(this, programDir.getCanonicalPath());
        menus.add(tgScreentoolbar);
        getContentPane().add(menus, BorderLayout.NORTH);

        // Adding the InternalFrames
        iframe[0].setLocation(155, 0);
        iframe[1].setLocation((140 + (scr.width - 140) / 2), 0);
        iframe[2].setLocation(100, 0);
        iframe[0].setVisible(false);
        iframe[1].setVisible(false);
        iframe[2].setVisible(false);
        iframe[3].setVisible(false);
        iframe[4].setVisible(false);
        iframe[5].setVisible(false);
        iframe[3].setLocation(((scr.width - 100) / 2), 0);
        iframe[4].setLocation(0, 0);
        iframe[5].setLocation(0, (scr.height / 2 + (scr.height / 20)));
        iframe[6].setLocation(0, 0);
        iframe[6].setVisible(true);

        for (int i = 0; i < 7; i++) {
            iframe[i].addInternalFrameListener(new MyInternalFrameListener());
            dp.add(iframe[i]);
        }

        getContentPane().add(dp, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //writeFile(ColorInfo);
                dispose();
                System.exit(0);
            }
        });
        user.loadSettings();
        zf.neuzeichnen();
        pp.neuzeichnen();
        gr.starten();
        tfUpdateTrue = true;
        // sd.showdesigner(st); 
        if (!available3d) {
            JOptionPane.showMessageDialog(
                    null,
                    messages.getString("TgJFrame.no3d.message"),
                    messages.getString("TgJFrame.no3d.title"),
                    JOptionPane.ERROR_MESSAGE);
        }
        if (user.needsUpdate(bwinproLastUpdate)) {
            JOptionPane.showMessageDialog(
                    null,
                    messages.getString("TgJFrame.updateCheck.message"),
                    messages.getString("TgJFrame.updateCheck.title"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean is3dAvailable() {
        PackageInfo info3d = new PackageInfo();
        if (info3d.isJ3DInstalled()) {
            new Query3DProperties().print();
            return true;
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        String cmd = e.getActionCommand();
        System.out.println(cmd);
        if (obj instanceof JMenuItem) {
            if (cmd.equals("new")) {
                yt.setYieldTableNew();
                showIframes();
                JDialog newstand = new TgNewStand(this, true, st, this, language.locale());
                Model mo = new Model();
                st.setModelRegion(mo.getPlugInName(plugIn));
                newstand.setVisible(true);
                if (grafik3D) {
                    manager3d.setStand(st);
                }
                tfUpdateTrue = true;
                if (st.size > 0) {
                    iframe[3].setVisible(true);
                    menubar.cmi[3].setSelected(true);
                }
                if (st.modelRegion.contains("default")) {
                    st.ingrowthActive = false;
                }
            }
            if (cmd.equals("openTreegross")) {
                yt.setYieldTableNew();
                DataExchangeFormat dataex = new DataExchangeFormat();
                JFileChooser fc = new JFileChooser();
                TxtFileFilter txtFilter = new TxtFileFilter();
                fc.addChoosableFileFilter(txtFilter);
                fc.setCurrentDirectory(user.getDataDir());
                fc.showOpenDialog(owner);
                String pa = fc.getSelectedFile().getPath();
                fc.getSelectedFile().getName();
                Model mo = new Model();
                st.setModelRegion(mo.getPlugInName(plugIn));

                dataex.read(st, pa);
                LOGGER.log(Level.INFO, "File eingelesen:{0}", pa);
                if (st.ntrees == 0) {
                    dataex.readOldFormat1(st, pa);
                }
                if (st.ntrees == 0) {
                    dataex.readOldFormat2(st, pa);
                }
                if (st.ntrees == 0) {
                    dataex.readOldFormat3(st, pa);
                }
                checkForUndefinedSpecies();
                st.sortbyd();
                st.missingData();
                st.descspecies();
                // set Löwe default
//                       Treatment te= new Treatment();
                GenerateXY gxy = new GenerateXY();
                gxy.zufall(st);
                if (grafik3D) {
                    manager3d.setStand(st);
                }

                tfUpdateTrue = true;
                updatetp(false);
                tfUpdateTrue = true;
                st.ingrowthActive = false;
                gr.starten();
                showIframes();
            }
            // menu item open: read stand data from unformatted file
            if (cmd.equals("openTreegrossXML")) {
                yt.setYieldTableNew();
                JFileChooser fc = new JFileChooser();
                TxtFileFilter txtFilter = new TxtFileFilter();
                txtFilter.setExtension("xml");
                fc.addChoosableFileFilter(txtFilter);
                fc.setCurrentDirectory(user.getDataDir());
                int auswahl = fc.showOpenDialog(owner);
                File pa = fc.getSelectedFile();
                currentFile = pa;
                String dn = fc.getSelectedFile().getName();
                Model mo = new Model();
                st.setModelRegion(mo.getPlugInName(plugIn));
                readStandFromXML(pa);

                checkForUndefinedSpecies();
                st.sortbyd();
// Test for grouping                       
//                       Groups groups = new Groups(st);
//                       groups.setAutoGrouping();

                st.missingData();
                st.descspecies();
                // set Löwe default
                GenerateXY gxy = new GenerateXY();
                gxy.zufall(st);
                if (grafik3D) {
                    manager3d.setStand(st);
                }

                tfUpdateTrue = true;
                updatetp(false);
                tfUpdateTrue = true;
                st.ingrowthActive = false;
                gr.starten();
                showIframes();
            }
            // open NutungsPlaner SQLite Database
            if (cmd.equals("SQlite")) {
                LOGGER.info("open NutzungsPlaner SQlite Database");
                batchProcessingUsingDatabase("forestsimulator.SQLite.SQLiteAccess");
            }
            // open Access File
            if (cmd.equals("openAccess")) {
                LOGGER.info("open NutzungsPlaner Access File");
                batchProcessingUsingDatabase("forestsimulator.dbaccess.DBAccess");
            }

            // Edit Treegross data
            if (cmd.equals("edit")) {
                TgEditTreegross tgEditTreegross = new TgEditTreegross(this, true, st);
                tgEditTreegross.setVisible(true);
                if (grafik3D) {
                    manager3d.setStand(st);
                }
                tfUpdateTrue = true;
                updatetp(false);
            }
            // read stand data from unformatted file
            if (cmd.equals("save")) {
                DataExchangeFormat dataex = new DataExchangeFormat();
                JFileChooser fc = new JFileChooser();
                TxtFileFilter txtFilter = new TxtFileFilter();
                fc.addChoosableFileFilter(txtFilter);
                fc.setCurrentDirectory(user.getDataDir());
                int auswahl = fc.showSaveDialog(owner);
                String pa = fc.getSelectedFile().getPath();
                String dn = fc.getSelectedFile().getName();
                dataex.save(st, pa);
            }
            if (cmd.equals("saveTreegrossXML")) {
                DataExchangeFormat dataex = new DataExchangeFormat();
                JFileChooser fc = new JFileChooser();
                TxtFileFilter txtFilter = new TxtFileFilter();
                txtFilter.setExtension("xml");
                fc.addChoosableFileFilter(txtFilter);
                fc.setCurrentDirectory(user.getDataDir());
                int auswahl = fc.showSaveDialog(owner);
                String pa = fc.getSelectedFile().getPath();
                String dn = fc.getSelectedFile().getName();
                TreegrossXML2 treegrossXML2 = new TreegrossXML2();
                treegrossXML2.saveAsXML(st, pa);
            }
            //Menu save as jpg
            if (cmd.equals("ppJPG")) {
                pp.getJPEG();
                pp.neuzeichnen();
            }
            if (cmd.equals("Tree values")) {
                TgHTMLsv sv = new TgHTMLsv(st);
                sv.newreport(st, workingDir.getAbsolutePath(), "treelist.html", language.locale());
                StartBrowser startBrowser = new StartBrowser("file:///" + sv.getFilename());
                startBrowser.start();
            }
            if (cmd.equals("Stand table")) {
                Groups grps = new Groups(st);
                grps.setAutoGrouping();
                st.sortbyd();
                st.descspecies();
                yt.enterStandDesc(st);
                yt.writeTable(st, workingDir.getAbsolutePath(), "standtable.html", language.locale());
                seite = "file:" + System.getProperty("file.separator") + System.getProperty("file.separator")
                        + System.getProperty("file.separator") + yt.getFilename();
                StartBrowser startBrowser = new StartBrowser(seite);
                startBrowser.start();
            }
            if (cmd.equals("Structure table")) {
                st.sortbyd();
                st.descspecies();
                TgStructureTable tgStructureTable = new TgStructureTable();
                tgStructureTable.writeTable(st, workingDir.getAbsolutePath(), "standstructure.html", language.locale());
                seite = "file:" + System.getProperty("file.separator") + System.getProperty("file.separator")
                        + System.getProperty("file.separator") + tgStructureTable.getFilename();
                StartBrowser startBrowser = new StartBrowser(seite);
                startBrowser.start();
            }
            if (cmd.equals("Definition")) {
                try {
                    st.getSDM().listCurrentSpeciesDefinition(st, workingDir.getAbsolutePath(), "speciesdefinition.html");
                    seite = "file:" + System.getProperty("file.separator") + System.getProperty("file.separator") + workingDir
                            + System.getProperty("file.separator") + "speciesdefinition.html";
                    StartBrowser startBrowser = new StartBrowser(seite);
                    startBrowser.start();
                } catch (IOException ex) {
                    Logger.getLogger(TgJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (cmd.equals("species_code")) {
                st.getSDM().listAllSpeciesDefinition(st, workingDir.getAbsolutePath(), "speciescode.html");
                seite = "file:" + System.getProperty("file.separator") + System.getProperty("file.separator") + workingDir
                        + System.getProperty("file.separator") + "speciescode.html";
                StartBrowser startBrowser = new StartBrowser(seite);
                startBrowser.start();
            }
            if (cmd.equals("tree_table")) {
                st.sortbyd();
                st.descspecies();
                DataExchangeFormat dataex = new DataExchangeFormat();
                dataex.writeTreeTable(st);
            }
            if (cmd.equals("Info page")) {
                seite = "file:" + System.getProperty("file.separator") + System.getProperty("file.separator")
                        + System.getProperty("file.separator") + user.getProgramDir() + System.getProperty("file.separator") + "index.html";
                StartBrowser startBrowser = new StartBrowser(seite);
                startBrowser.start();
            }
            if (cmd.equals("License")) {
                seite = "file:" + System.getProperty("file.separator") + System.getProperty("file.separator")
                        + System.getProperty("file.separator") + user.getProgramDir() + System.getProperty("file.separator") + "gpl.html";
                StartBrowser startBrowser = new StartBrowser(seite);
                startBrowser.start();
            }
            if (cmd.equals("Introduction")) {
                String fileName = user.getProgramDir() + System.getProperty("file.separator") + "help" + System.getProperty("file.separator") + "NWFVA11_TreeGrOSS.pdf";
                String seite = "file:" + System.getProperty("file.separator") + System.getProperty("file.separator") + System.getProperty("file.separator") + fileName;
                StartBrowser startBrowser = new StartBrowser(seite);
                startBrowser.start();
            }
            if (cmd.equals("Changes")) {
                String fileName = user.getProgramDir() + System.getProperty("file.separator") + "help" + System.getProperty("file.separator") + "FSChanges.pdf";
                String seite = "file:" + System.getProperty("file.separator") + System.getProperty("file.separator") + System.getProperty("file.separator") + fileName;
                StartBrowser startBrowser = new StartBrowser(seite);
                startBrowser.start();
            }
            // menu item exit
            if (cmd.equals("exit")) {
                LOGGER.info("Programm beendet");
                System.exit(0);
            }
            //Menu "Properties"
            if (cmd.equals("Program Settings")) {
                JDialog settings = new TgUserDialog(this, true);
                settings.setVisible(true);
                programDir = user.getWorkingDir();
            }
            if (cmd.equals("Species Manager")) {
                TgSpeciesManXML spman = new TgSpeciesManXML(this, true, programDir, st.FileXMLSettings);
                spman.setVisible(true);
            }
            if (cmd.equals("About")) {
                JTextArea about = new JTextArea(MessageFormat.format(messages.getString("TgJFrame.AboutDialog.message"), bwinproVersion, bwinproVersion));
                about.setBackground(Color.LIGHT_GRAY);
                JOptionPane.showMessageDialog(this, about, messages.getString("TgJFrame.AboutDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            }
            if (cmd.equals("checksum")) {
                String fname = programDir + System.getProperty("file.separator") + st.FileXMLSettings;
                JackSumFile jsf = new JackSumFile();
                String csum = jsf.getSum(fname);
                JTextArea about = new JTextArea(MessageFormat.format(messages.getString("TgJFrame.ChecksumDialog.message"), csum));
                about.setBackground(Color.LIGHT_GRAY);
                JOptionPane.showMessageDialog(this, about, messages.getString("TgJFrame.ChecksumDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            }
        }

        if (cmd.equals("Grow")) {
            st.sortbyd();
            st.descspecies();
            TreegrossXML2 treegrossXML2 = new TreegrossXML2();
            String currentFileName = currentFile.getName();
            if (currentFileName.indexOf(".") < 12) {
                currentFileName = currentFileName + System.getProperty("file.separator") + "temp.xml";
            }
            Integer jj = st.year;
            String fname = currentFileName.substring(0, currentFileName.length() - 4) + "_" + jj.toString() + ".xml";
            treegrossXML2.saveAsXML(st, fname);
            st.descspecies();
            st.executeMortality();
            st.descspecies();
            yt.enterStandDesc(st);
            st.grow(st.timeStep, st.ingrowthActive);  //grow a 5-year cycle, means  ingrowth
            updatetp(false);
            gr.drawGraph();
            repaint();
        }
        if (cmd.equals("Treatment")) {
            st.descspecies();
            st.sortbyd();
            treatmentMan3.loadSettingsToStandRule();
            tl.executeManager2(st);
            updatetp(false);
            zf.neuzeichnen(); //Zeichenfläche neu zeichnen
//                pp.neuzeichnen();
            gr.drawGraph();
            repaint();
        }
        if (cmd.equals("grow_back")) {
            st.sortbyd();
            st.descspecies();
            st.growBack(st.timeStep, true);  //grow a 5-year cycle, means  ingrowth
            updatetp(false);
            gr.drawGraph();
            repaint();
        }
        if (cmd.equals("Sorting")) {
            SortingDialog sorter = new SortingDialog(this, true, st, programDir.getAbsolutePath(), false, workingDir.getAbsolutePath(), language.locale(), logHandler);
            sorter.setVisible(true);
        }
        if (cmd.equals("Deadwood")) {
//                    DeadwoodDialog sorter = new DeadwoodDialog(this, true, st, programDir, false, workingDir);
//                    sorter.setVisible(true);
        }
        if (cmd.equals("NutrientBalance")) {
            BiomassDialog nutrientBalance = new BiomassDialog(this, true, st, programDir.getAbsolutePath(), false, workingDir.getAbsolutePath(), logHandler);
            nutrientBalance.setVisible(true);
        }
        if (cmd.equals("Roots")) {
            RootBiomassReport rootBiomassReport = new RootBiomassReport();
            rootBiomassReport.report(st, programDir.getAbsolutePath(), workingDir.getAbsolutePath());
        }
        if (cmd.equals("RootingDialog")) {
            RootingDialog rootingDialog = new RootingDialog(this, true, st);
            rootingDialog.setVisible(true);
        }
        if (cmd.equals("Bwin62")) {
            JFileChooser filechooser = new JFileChooser();
            filechooser.showOpenDialog(this);
            String verzeichnisUndDateiName = filechooser.getSelectedFile().getPath();
            DataExchangeFormat data = new DataExchangeFormat();
            data.readOldFormat1(st, verzeichnisUndDateiName);
            filechooser.setApproveButtonText("speichern");
            filechooser.showOpenDialog(this);
            verzeichnisUndDateiName = filechooser.getSelectedFile().getPath();
            TreegrossXML2 txml = new TreegrossXML2();
            txml.saveAsXML(st, verzeichnisUndDateiName);
        }
        /*           if(cmd.equals("treegross2gml")){
                      JFileChooser fc = new JFileChooser();
                       TxtFileFilter txtFilter = new TxtFileFilter();
                       txtFilter.setExtension("xml");
                       fc.addChoosableFileFilter(txtFilter);
                       fc.setCurrentDirectory(new File(user.getDataDir()));
                       int auswahl = fc.showOpenDialog(owner);
                       String pa= fc.getSelectedFile().getPath();
                       currentFile = pa;
                       String dn= fc.getSelectedFile().getName();
                       TreegrossXML2 treegrossXML2 = new TreegrossXML2();
                       URL url = null;
                       String fname=pa;
                       int m = pa.toUpperCase().indexOf("FILE");
                       int m2 = pa.toUpperCase().indexOf("HTTP");
                       if ( m < 0 && m2 <0 ) fname="file:"+System.getProperty("file.separator")
                               +System.getProperty("file.separator")+System.getProperty("file.separator")+pa;
                       try {
                          url = new URL(fname);
                          st=treegrossXML2.readTreegrossStand(st,url);
                       }
                       catch (Exception e2){  }

                       JFileChooser filechooser = new JFileChooser();
                       filechooser.setDialogTitle("Als GML File speichern");
                       filechooser.setApproveButtonText("speichern");
                       filechooser.showOpenDialog(this);
                       String verzeichnisUndDateiName= filechooser.getSelectedFile().getPath();
                       treegrossXML2.saveAsGML(st, verzeichnisUndDateiName);
            }
         */
// Screen Toolbar                
        if (cmd.equals("screen1")) {
            int xp = (int) (scr.width * 0.65);
            int yp = (int) ((scr.height - 105) * 0.5);
            iframe[0].setLocation(xp, 0);
            iframe[0].setSize(new Dimension(scr.width - xp, scr.height - 105 - yp));
            iframe[0].setVisible(true);
            iframe[1].setLocation(xp, yp);
            iframe[1].setSize(new Dimension(scr.width - xp, scr.height - 105 - yp));
            iframe[1].setVisible(true);
            iframe[2].setVisible(false);
            iframe[3].setVisible(false);
            yp = (int) ((scr.height - 105) * 0.7);
            iframe[4].setLocation(0, 0);
            iframe[4].setSize(new Dimension(xp, yp));
            iframe[4].setVisible(true);
            iframe[5].setLocation(0, yp);
            iframe[5].setSize(new Dimension(xp, scr.height - 105 - yp));
            iframe[5].setVisible(true);
            iframe[6].setVisible(false);
        }
        if (cmd.equals("screen2")) {
            iframe[0].setVisible(false);
            iframe[1].setLocation(0, 0);
            int yp = (int) ((scr.height - 105) * 0.65);
            iframe[1].setSize(new Dimension(scr.width, yp));
            iframe[1].setVisible(true);
            iframe[2].setVisible(false);
            iframe[3].setVisible(false);
            iframe[4].setVisible(false);
            iframe[5].setVisible(true);
            iframe[5].setLocation(0, yp);
            iframe[5].setSize(new Dimension(scr.width, scr.height - 105 - yp));
            iframe[6].setVisible(false);
        }
        if (cmd.equals("screen3")) {
            iframe[0].setVisible(false);
            iframe[1].setVisible(false);
            iframe[2].setVisible(false);
            iframe[3].setVisible(false);
            iframe[4].setVisible(false);
            iframe[5].setVisible(false);
            iframe[6].setVisible(false);
            iframe[1].setLocation(0, 0);
            iframe[1].setVisible(true);
            iframe[1].setSize(new Dimension(scr.width, scr.height - 105));
        }

// Action commands of stand map menu 
        if (cmd.equals("saveStandMapToJPG")) {
            JFileChooser fc = new JFileChooser();
            TxtFileFilter txtFilter = new TxtFileFilter();
            txtFilter.setExtension("jpg");
            fc.addChoosableFileFilter(txtFilter);
            fc.setCurrentDirectory(user.getWorkingDir());
            int auswahl = fc.showSaveDialog(owner);
            String pa = fc.getSelectedFile().getPath();
            zf.setJPGFilename(pa);
            zf.getJPEG();
            zf.neuzeichnen();
        }
        if (cmd.equals("refreshStandMap")) {
            zf.neuzeichnen();
        }
        if (cmd.equals("zoomStandMapIn")) {
            zf.zoomStatus = 1;
            zf.neuzeichnen();
        }
        if (cmd.equals("zoomStandMapOut")) {
            zf.zoomStatus = 0;
            zf.xlzoom = 0;
            zf.ylzoom = 0;
            zf.xuzoom = 0;
            zf.yuzoom = 0;
            zf.neuzeichnen();
        }
        if (cmd.equals("Factor=1")) {
            zf.setDbhFactor(1);
            zf.neuzeichnen();
        }
        if (cmd.equals("Factor=3")) {
            zf.setDbhFactor(3);
            zf.neuzeichnen();
        }
        if (cmd.equals("Factor=5")) {
            zf.setDbhFactor(5);
            zf.neuzeichnen();
        }
// Action commands of parallel projekction map menu 
        if (cmd.equals("savePPMapToJPG")) {
            pp.getJPEG();
            pp.neuzeichnen();
        }
        if (cmd.equals("refreshPPMap")) {
            pp.neuzeichnen();
        }
        if (cmd.equals("zoomPPMapIn")) {
            pp.zoomStatus = 1;
            pp.neuzeichnen();
        }
        if (cmd.equals("zoomPPMapOut")) {
            pp.zoomStatus = 0;
            pp.xlzoom = 0;
            pp.ylzoom = 0;
            pp.xuzoom = 0;
            pp.yuzoom = 0;
            pp.neuzeichnen();
        }
        if (cmd.equals("ppSkyColor")) {
            pp.setSkyColor(JColorChooser.showDialog(this, "Choose Sky Color", pp.getSkyColor()));
            pp.neuzeichnen();
        }
        if (cmd.equals("ppGroundColor")) {
            pp.setGroundColor(JColorChooser.showDialog(this, "Choose Ground Color", pp.getGroundColor()));
            pp.neuzeichnen();
        }
        if (cmd.equals("ppStandGroundColor")) {
            pp.setStandGroundColor(JColorChooser.showDialog(this, "Choose Stand Ground Color", pp.getStandGroundColor()));
            pp.neuzeichnen();
        }
// Action commands of Grafik Window 
        if (cmd.equals("SpeciesByCrownSurfaceArea")) {
            gr.setGraphType(0);
            gr.neuzeichnen();
            iframe[2].setVisible(false);
            iframe[2].setVisible(true);
        }
        if (cmd.equals("DiameterDistribution")) {
            gr.setGraphType(1);
            gr.neuzeichnen();
            iframe[2].setVisible(false);
            iframe[2].setVisible(true);
        }
        if (cmd.equals("DiameterDistributionCT")) {
            gr.setGraphType(2);
            gr.neuzeichnen();
            iframe[2].setVisible(false);
            iframe[2].setVisible(true);
        }
        if (cmd.equals("HeightDiameterPlot")) {
            gr.setGraphType(3);
            gr.neuzeichnen();
            iframe[2].setVisible(false);
            iframe[2].setVisible(true);
        }
        if (cmd.equals("saveChartToJPG")) {
            JFileChooser fc = new JFileChooser();
            TxtFileFilter txtFilter = new TxtFileFilter();
            txtFilter.setExtension("jpg");
            fc.addChoosableFileFilter(txtFilter);
            fc.setCurrentDirectory(user.getWorkingDir());
            int auswahl = fc.showSaveDialog(owner);
            String pa = fc.getSelectedFile().getPath();
            gr.setJPGFilename(pa);
            gr.saveToJPEG(workingDir.getAbsolutePath());
        }
    }

    private void readStandFromXML(File pa) {
        TreegrossXML2 treegrossXML2 = new TreegrossXML2();
        try {
            URL url = pa.toURI().toURL();
            st = treegrossXML2.readTreegrossStand(st, url);
        } catch (MalformedURLException e2) {
            LOGGER.info(e2.toString());
        }
        LOGGER.log(Level.INFO, "File eingelesen:{0}", pa);
    }

    private void checkForUndefinedSpecies() throws HeadlessException {
        if (!st.getSpeciesDefinedTrue()) {
            String text = st.getSpeciesUndefinedCode();
            showUndefinedSpeciesMessage(text);
            st.ntrees = 0;
            st.nspecies = 0;
        }
    }

    private void batchProcessingUsingDatabase(String modelPlugIn) {
        try {
            PlugInDBSQLite dialog = (PlugInDBSQLite) Class.forName(modelPlugIn).newInstance();
            dialog.startDialog(this, st, user);
            st.sortbyd();
            st.missingData();
            st.descspecies();
            // set Löwe default
//                       GenerateXY gxy =new GenerateXY();
//                       gxy.zufall(st);
            if (grafik3D) {
                manager3d.setStand(st);
            }
            tfUpdateTrue = true;
            updatetp(false);
            tfUpdateTrue = true;

            st.ingrowthActive = false;
            gr.starten();
            showIframes();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
            LOGGER.info(ex.toString());
        }
        LOGGER.info("Open database for batch processing successful.");
    }

    private void showUndefinedSpeciesMessage(String text) throws HeadlessException {
        JTextArea about = new JTextArea(MessageFormat.format(messages.getString("TgJFrame.undefined_species.message"), text));
        about.setBackground(Color.LIGHT_GRAY);
        JOptionPane.showMessageDialog(this, about, messages.getString("TgJFrame.undefined_species.title"), JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void StandChanged(StandChangeEvent evt) {
        LOGGER.log(Level.FINE, "stand changed {0}", evt.getName());
        StopWatch update = new StopWatch("Updating tp").start();
        updatetp(false);
        update.printElapsedTime();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getItemSelectable();
        for (int i = 0; i < 7; i++) {
            if (source == menubar.cmi[i]) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    iframe[i].setVisible(true);
                } else {
                    iframe[i].setVisible(false);
                }
            }
        }
// item event for stand map           
        if (source == tgStandMapMenu.cmi[0]) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                zf.setStandMapInfo(true);
            } else {
                zf.setStandMapInfo(false);
            }
            zf.neuzeichnen();
        }
        if (source == tgStandMapMenu.cmi[1]) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                zf.setPlotCrownWidth(true);
            } else {
                zf.setPlotCrownWidth(false);
            }
            zf.neuzeichnen();
        }
        if (source == tgStandMapMenu.cmi[2]) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                zf.setPlotTreeNumber(true);
            } else {
                zf.setPlotTreeNumber(false);
            }
            zf.neuzeichnen();
        }
        if (source == tgStandMapMenu.cmi[8]) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                zf.setMouseThinning(true);
            } else {
                zf.setMouseThinning(false);
            }
            if (grafik3D) {
                manager3d.refreshStand();
            } else {
                pp.neuzeichnen();
            }
        }
        if (source == tgStandMapMenu.cmi[9]) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                zf.setMouseCropTree(true);
            } else {
                zf.setMouseCropTree(false);
            }
            pp.neuzeichnen();
        }
// item event for parallel map 
        if (grafik3D == false) {
            if (source == tgPPMapMenu.cmi[0]) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    pp.setLivingTrees(true);
                } else {
                    pp.setLivingTrees(false);
                }
                pp.neuzeichnen();
            }
            if (source == tgPPMapMenu.cmi[1]) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    pp.setThinnedTrees(true);
                } else {
                    pp.setThinnedTrees(false);
                }
                pp.neuzeichnen();
            }
            if (source == tgPPMapMenu.cmi[2]) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    pp.setDeadTrees(true);
                } else {
                    pp.setDeadTrees(false);
                }
                pp.neuzeichnen();
            }
        }
// item event for treatment manager2  
/*          if(source==tgTreatmentMan2Menu.cbi[0]) {
              if (e.getStateChange()==ItemEvent.SELECTED)st.distanceDependent=true;
              else st.distanceDependent=false;
          }
          if(source==tgTreatmentMan2Menu.cbi[1]) {
              if (e.getStateChange()==ItemEvent.SELECTED)st.randomGrowthEffects=true;
              else st.randomGrowthEffects=false;
          }
          if(source==tgTreatmentMan2Menu.cbi[2]) {
              if (e.getStateChange()==ItemEvent.SELECTED)st.ingrowthActive=true;
              else st.ingrowthActive=false;
          }
         */
    }

    private void loadGeneralSettings(File Dir) {
        File fname = null;
        try {
            URL url = null;
            fname = new File(new File(Dir, "models"), st.FileXMLSettings);
            System.out.println("SpeciesDef: URL: " + fname.toURI().toURL());
            try {
                url = fname.toURI().toURL();
            } catch (MalformedURLException e) {
                LOGGER.info(e.getMessage());
                showSettingsNotFoundMessage(fname);
            }
            SAXBuilder builder = new SAXBuilder();
            URLConnection urlcon = url.openConnection();

            Document doc = builder.build(urlcon.getInputStream());

            Element sortimente = doc.getRootElement();
            java.util.List Setting = sortimente.getChildren("GeneralSettings");
            Iterator i = Setting.iterator();

            while (i.hasNext()) {
                Element setting = (Element) i.next();
                st.modelRegion = setting.getChild("ModelRegion").getText();
                if (Boolean.parseBoolean(setting.getChild("ErrorComponent").getText())) {
                    st.random.setRandomType(treegross.random.RandomNumber.PSEUDO);
                } else {
                    st.random.setRandomType(treegross.random.RandomNumber.OFF);
                }
                st.ingrowthActive = Boolean.parseBoolean(setting.getChild("IngrowthModul").getText());
                st.deadwoodModulName = setting.getChild("DeadwoodModul").getText();
                try {
                    st.deadwoodModulName = setting.getChild("DebriswoodModul").getText();
                } catch (Exception e) {
                    st.deadwoodModulName = "none";
                }
                try {
                    st.sortingModul = setting.getChild("SortingModul").getText();
                } catch (Exception e) {
                    st.sortingModul = "none";
                }
                try {
                    st.biomassModul = setting.getChild("BiomassModul").getText();
                } catch (Exception e) {
                    st.biomassModul = "none";
                }
                try {
                    st.timeStep = Integer.parseInt(setting.getChild("TimeStep").getText());
                } catch (NumberFormatException | NullPointerException e) {
                    LOGGER.log(Level.FINE, "Could not get TimeStep form configuration.", e);
                    st.timeStep = 5;
                }
                break;
            }
        } catch (HeadlessException | IOException | JDOMException e) {
            LOGGER.info(e.toString());
            showSettingsNotFoundMessage(fname);
            LOGGER.log(Level.INFO, "SpeciesDef General settings: File nicht gefunden: {0}", fname);
        }
    }

    private void showSettingsNotFoundMessage(File fname) throws HeadlessException {
        JTextArea about = new JTextArea(MessageFormat.format(messages.getString("TgJFrame.load_settings.error.message"), fname));
        JOptionPane.showMessageDialog(null, about, messages.getString("TgJFrame.load_settings.error.title"), JOptionPane.INFORMATION_MESSAGE);
    }

    public void updatetp(boolean from3D) {
        gr.neuzeichnen();
        st.sortbyd();
        st.descspecies();
        zf.neuzeichnen();
        StopWatch updateStandView = new StopWatch(("Update view")).start();
        if (grafik3D && !from3D) {
            manager3d.refreshStand();
        } else {
            pp.neuzeichnen();
            st.sortbyd();
            st.descspecies();
        }
        updateStandView.printElapsedTime();
        tsi.formUpdate(st);
        if (tfUpdateTrue) {
            treatmentMan3.formUpdate(st);
            tfUpdateTrue = false;
        }
        if (iframe[2].isVisible()) {
            iframe[2].setVisible(false);
            iframe[2].setVisible(true);
        }
    }

    void showIframes() {
        int xp = (int) (scr.width * 0.65);
        int yp = (int) ((scr.height - 105) * 0.5);
        iframe[0].setLocation(xp, 0);
        iframe[0].setSize(new Dimension(scr.width - xp, scr.height - 105 - yp));
        iframe[0].setVisible(true);
        iframe[1].setLocation(xp, yp);
        iframe[1].setSize(new Dimension(scr.width - xp, scr.height - 105 - yp));
        iframe[1].setVisible(true);
        iframe[2].setVisible(false);
        iframe[3].setVisible(false);
        yp = (int) ((scr.height - 105) * 0.7);
        iframe[4].setLocation(0, 0);
        iframe[4].setSize(new Dimension(xp, yp));
        iframe[4].setVisible(true);
        iframe[5].setLocation(0, yp);
        iframe[5].setSize(new Dimension(xp, scr.height - 105 - yp));
        iframe[5].setVisible(true);
        iframe[6].setVisible(false);
    }

    public void setStand(Stand stl) {
        st = stl;
        Model mo = new Model();
        st.setModelRegion(mo.getPlugInName(plugIn));
        System.out.println("set Stand down");
        updatetp(false);
        System.out.println("set Stand down");
    }

    StringBuilder getFile(String path, String name) {
        StringBuilder sbuffer = new StringBuilder(500);
        try (FileReader fr = new FileReader(new File(path, name))) {
            boolean end = false;
            int c;
            while (!end) {
                c = fr.read();
                if (c == -1) {
                    end = true;
                } else {
                    sbuffer.append((char) c);
                }
            }
        } catch (IOException e) {
            LOGGER.info(e.toString());
        }
        return sbuffer;
    }
    
    public void showAddTreesWindow() {
        this.iframe[3].setVisible(true);
    }

    public void hideAddTreesWindow() {
        this.iframe[3].setVisible(false);
    }

    public class MyInternalFrameListener extends InternalFrameAdapter {

        @Override
        public void internalFrameClosing(InternalFrameEvent e) {
            for (int i = 0; i < 7; i++) {
                if (e.getInternalFrame() == iframe[i]) {
                    iframe[i].setVisible(false);
                    menubar.cmi[i].setState(false);
                }
            }
        }
    }
}
