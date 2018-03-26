/* http://www.nw-fva.de
   Version 05-04-2013

   (c) 2002-12 Juergen Nagel, Northwest German Forest Research Station, 
       Gr�tzelstr.2, 37079 G�ttingen, Germany
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
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.*;
import org.jdom.DocType;





/**
 * @author Juergen Nagel
 *
 * Main jFrame of the Forest Simulator, this controlls the whole GUI
 */
public class TgJFrame extends JFrame implements ActionListener, ItemListener, StandChangeListener
{	
   private static final Logger log = Logger.getLogger( forestsimulator.standsimulation.TgJFrame.class.getName() );
   FileHandler logHandler = null;

   String bwinproVersion="Version 7.9.02 XML";
   String bwinproLastUpdate="17-03-2017";
   boolean accessInput = true;
   static Stand st = new Stand();
   SpeciesDefMap SDM = new SpeciesDefMap();
   String currentFile="";
   StringBuffer ColorInfo; 
   String seite;
//   TgStandMap zf  = new TgStandMap(st, this); //add standmap class
//   TgPPmap pp = new TgPPmap(st, this); //add prallel projection class
   TgStandMap zf;   //add standmap class
   TgPPmap pp;  //add prallel projection class
   TgHTMLsv sv = new TgHTMLsv(st);
//   TgGrafik gr = new TgGrafik(st);
   TgGrafik gr ;
   TgProgramInfo tgProgramInfo;
   TgDesign sd;
   TgYieldTable yt = new TgYieldTable();
   Treatment2 tl = new Treatment2();    
//   TgTreatmentMan3 treatmentMan3;
   
   TgTreatmentMan4 treatmentMan4;
   
   
   TgStandInfo tsi; 
   String language="en";
   
   MyMenubar menubar;
   TgStandMapMenu tgStandMapMenu;
   TgPPMapMenu tgPPMapMenu;
   TgGrafikMenu tgGrafikMenu;
   TgToolbar toolbar;
   TgScreentoolbar tgScreentoolbar;
   TgInternalFrame[] iframe  = new TgInternalFrame[8];
   JDesktopPane dp = new JDesktopPane();
   TgUser user= new TgUser();
   private JFrame owner; 
   ResourceBundle messages;
   private Manager3D manager3d;

   
   
   boolean grafik3D = false;
   boolean StandardColors = false;
   boolean tfUpdateTrue = true;
   String programDir="";
   String workingDir="";
   String dataDir="";
   String plugIn="XML";
   String kspDataFile="";
   String kspNextPlot="";
   Dimension scr;
   
   int kspTyp=0;
   
	// Konstruktor of TgJFrame()
	public TgJFrame(Stand stneu)
	{
// Fehler logger bereitstellen
        try{
           logHandler = new FileHandler("log.txt");
           logHandler.setFormatter(new SimpleFormatter());
           log.addHandler( logHandler );
           log.info( "ForestSimulator Version: "+ bwinproVersion );
        }
        catch (Exception e){};

           st=stneu;
           st.addStandChangeListener(this);
           scr= Toolkit.getDefaultToolkit().getScreenSize();                
           setSize(scr.width,(scr.height-(scr.height/50)));
           java.io.File f = new java.io.File("");
           String localPath=null;
           try{ 
               localPath= f.getCanonicalPath();
              } catch (Exception e){};
           if (user.fileExists(localPath+System.getProperty("file.separator")+"ForestSimulator.ini")==false) {
                    JDialog settings = new TgUserDialog(this, true);
                    settings.setVisible(true);
                    JTextArea about = new JTextArea("Please restart program with new settings");
                    JOptionPane.showMessageDialog(this, about, "About", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
            }
            else {System.out.println("Settings laden "); 
                      log.info("TgJFrame local path : "+ localPath);
                      user.loadSettings(localPath);
                      language=user.getLanguageShort();
                      plugIn=user.getPlugIn();
                      st.modelRegion=plugIn;
                      st.FileXMLSettings=user.XMLSettings;
                      log.info("Modell :"+plugIn);
            }
            setTitle(getTitle()+"Forest Simulator BWINPro 7 "+bwinproVersion+" - Modell: "+plugIn);
//
        boolean available3d = false;
        PackageInfo info3d=new PackageInfo();
        if(info3d.isJ3DInstalled()){
            new Query3DProperties();
            available3d=true;
        } 
            
            
//
//        accessInput = true;

        if (plugIn.indexOf("nwfva") > 0) {
            accessInput = true;
        }

                zf  = new TgStandMap(st, this); //add standmap class
                pp = new TgPPmap(st, this); //add prallel projection class
                gr = new TgGrafik(st);    
                Locale currentLocale;
                currentLocale = new Locale(language, "");
                if (user.grafik3D==0 && available3d) grafik3D=true;

                programDir=user.getProgramDir();
                SDM.readFromPath(programDir+System.getProperty("file.separator")+"models"+System.getProperty("file.separator")+st.FileXMLSettings);
                st.setSDM(SDM);
                workingDir=user.getWorkingDir();
                currentFile=user.getDataDir();
                st.setProgramDir(programDir);
                
                loadGenralSettings(programDir);
 
                messages = ResourceBundle.getBundle("forestsimulator.standsimulation.TgJFrame",currentLocale);
                 
                JPanel zfneu = new JPanel();
                zfneu.setLayout(new BorderLayout());
                JPanel tgStandMapMenus = new JPanel();
                tgStandMapMenus.setLayout(new BoxLayout(tgStandMapMenus, BoxLayout.X_AXIS));                
// adding the menu
		tgStandMapMenu = new TgStandMapMenu(this, this, language);
                tgStandMapMenu.setAlignmentY(Component.CENTER_ALIGNMENT);
                tgStandMapMenus.add(tgStandMapMenu); 
                zfneu.add(tgStandMapMenus,BorderLayout.NORTH);
                zfneu.add(zf,BorderLayout.CENTER);
//
// Simple parallel project  or 3D View of stand              
                JPanel ppneu = new JPanel();
               if (grafik3D){
                    manager3d =new Manager3D(new JPanel(),programDir, true);
                    if (manager3d.get3DAvailable()){
                      ppneu.setPreferredSize(new Dimension((((scr.width-140)/2)-(scr.width/50)), (scr.height/2)-(scr.height/50)));                           
                      manager3d =new Manager3D(ppneu, programDir,true);
                      grafik3D = true;
                    }
                }
                else{
                    ppneu.setLayout(new BorderLayout());
                    JPanel tgPPMapMenus = new JPanel();
                    tgPPMapMenus.setLayout(new BoxLayout(tgPPMapMenus, BoxLayout.X_AXIS));                
           // adding the menu
		    tgPPMapMenu = new TgPPMapMenu(this, this, language); 
                    tgPPMapMenu.setAlignmentY(Component.CENTER_ALIGNMENT);
                    tgPPMapMenus.add(tgPPMapMenu); 
                    ppneu.add(tgPPMapMenus,BorderLayout.NORTH);
                    ppneu.add(pp,BorderLayout.CENTER);
                    grafik3D = false;
                }

         
//                
                tsi = new TgStandInfo(language);

// Treatment Manager Window                
/*                treatmentMan3 = new TgTreatmentMan3(st, this,language);
                JPanel treatmentPanel = new JPanel();
                treatmentPanel.setLayout(new BorderLayout());
                JPanel tgTreatmentMenus = new JPanel();
                tgTreatmentMenus.setLayout(new BoxLayout(tgTreatmentMenus, BoxLayout.X_AXIS));                
                treatmentPanel.add(tgTreatmentMenus,BorderLayout.NORTH); 
                treatmentPanel.add(treatmentMan3,BorderLayout.CENTER); 
*/
                treatmentMan4 = new TgTreatmentMan4(st, this,language);
                JPanel treatmentPanel = new JPanel();
                treatmentPanel.setLayout(new BorderLayout());
                JPanel tgTreatmentMenus = new JPanel();
                tgTreatmentMenus.setLayout(new BoxLayout(tgTreatmentMenus, BoxLayout.X_AXIS));                
                treatmentPanel.add(tgTreatmentMenus,BorderLayout.NORTH); 
                treatmentPanel.add(treatmentMan4,BorderLayout.CENTER); 
                
                sd = new TgDesign(st, this, language);
// add Grafik Menu 
                JPanel grWithMenu = new JPanel();
                grWithMenu.setLayout(new BorderLayout());
                JPanel tggrMenus = new JPanel();
                tggrMenus.setLayout(new BoxLayout(tggrMenus, BoxLayout.X_AXIS));                
		tgGrafikMenu = new TgGrafikMenu(this, this, language); 
                tgGrafikMenu.setAlignmentY(Component.CENTER_ALIGNMENT);
                tggrMenus.add(tgGrafikMenu); 
                grWithMenu.add(tggrMenus,BorderLayout.NORTH);
                grWithMenu.add(gr,BorderLayout.CENTER);
                
                iframe[0] = new TgInternalFrame(zfneu, messages.getString("Standmap"));
                iframe[1] = new TgInternalFrame(ppneu, messages.getString("Parallel_Projection"));
            	iframe[2] = new TgInternalFrame(grWithMenu, messages.getString("Graphics"));
                iframe[3] = new TgInternalFrame(sd, messages.getString("Add_Trees"));
                iframe[4] = new TgInternalFrame(treatmentPanel, messages.getString("Simulation_Setting"));
                iframe[5] = new TgInternalFrame(tsi,messages.getString("Stand_Info"));
   
//                user.currentDir();
                
              
                tgProgramInfo = new TgProgramInfo(this);
                iframe[6] = new TgInternalFrame(tgProgramInfo,messages.getString("Program_Info"));
              
                
                JPanel menus = new JPanel();
                menus.setLayout(new BoxLayout(menus, BoxLayout.X_AXIS));                
           // adding the menu
	 	menubar = new MyMenubar(this, this, language, st, accessInput); 
                menubar.setAlignmentY(Component.CENTER_ALIGNMENT);
                menus.add(menubar); 
		menus.add(Box.createRigidArea(new Dimension(20, 0)));
           //adding the toolbar
                toolbar = new TgToolbar(this, programDir, language);
                menus.add(toolbar);
           // adding screen toolbar
                tgScreentoolbar = new TgScreentoolbar(this, programDir);
                menus.add(tgScreentoolbar);
                getContentPane().add(menus, BorderLayout.NORTH);
                
                // Adding the InternalFrames
                iframe[0].setLocation(155,0);
                iframe[1].setLocation((140+(scr.width-140)/2),0);
                iframe[2].setLocation(100,0);
                iframe[0].setVisible(false);
                iframe[1].setVisible(false);
                iframe[2].setVisible(false);
                iframe[3].setVisible(false);
                iframe[4].setVisible(false);
                iframe[5].setVisible(false);
                iframe[3].setLocation(((scr.width-100)/2),0);
                iframe[4].setLocation(0,0);              
                iframe[5].setLocation(0,(scr.height/2+(scr.height/20)));
                iframe[6].setLocation(0,0);              
                iframe[6].setVisible(true);
                
                
                
                for(int i = 0; i<7; i++)
                {
                    iframe[i].addInternalFrameListener(new MyInternalFrameListener());
                    dp.add(iframe[i]);
                }
                
                getContentPane().add(dp, BorderLayout.CENTER);
                
		 
		// add a windowListener for closing the window		
		addWindowListener(
			new WindowAdapter ()
		{	public void windowClosing (WindowEvent e)
			{	
                            //writeFile(ColorInfo);
                            dispose(); System.exit(0); 
			}
		});
		
                // make TgJFrame visible
                user.loadSettings(localPath);            
                setVisible(true);
                zf.neuzeichnen();
                pp.neuzeichnen();
                gr.starten();
                tfUpdateTrue=true;
               // sd.showdesigner(st); 
                if(!available3d) JOptionPane.showMessageDialog(null,"Es ist keine Java3D-API installiert.","Java3D",JOptionPane.ERROR_MESSAGE);
                if (user.needsUpdate(bwinproLastUpdate)) JOptionPane.showMessageDialog(null,"Es gibt eine neue Version auf http://www.nw-fva.de. update empfohlen","ForestSimulator BWINPro7 Update Check",JOptionPane.ERROR_MESSAGE);
               
	}
        
//-----------------------------------------------------------------------------	
        /** In case an action is performed */
	public void actionPerformed(ActionEvent e)
	{	
            Object obj= e.getSource(); 
            String cmd= e.getActionCommand(); 
                System.out.println(cmd);
            if (obj instanceof JMenuItem)
            {	
                if (cmd.equals("new"))
                {
                    yt.setYieldTableNew();
                    showIframes();
                    JDialog newstand = new TgNewStand(this, true, st, this, language );   
                    Model mo =new Model();
                    st.setModelRegion(mo.getPlugInName(plugIn));
                    newstand.setVisible(true);
                    if (grafik3D ) manager3d.setStand(st);
                    tfUpdateTrue=true;
                    if (st.size>0){
                        iframe[3].setVisible(true);
                        menubar.cmi[3].setSelected(true);
                    }
                    if (st.modelRegion.indexOf("default")>-1) {
                           st.ingrowthActive=false;
                    }
                }                     
               
                if (cmd.equals("openTreegross"))
                { 
                       
                       yt.setYieldTableNew();
                       DataExchangeFormat dataex = new DataExchangeFormat();
                       JFileChooser fc = new JFileChooser();
                       TxtFileFilter txtFilter = new TxtFileFilter();
                       fc.addChoosableFileFilter(txtFilter);
                       fc.setCurrentDirectory(new File(user.getDataDir()));
                       int auswahl = fc.showOpenDialog(owner);
                       String pa= fc.getSelectedFile().getPath();
                       String dn= fc.getSelectedFile().getName();
                       Model mo =new Model();
                       st.setModelRegion(mo.getPlugInName(plugIn));
                                              
                       dataex.read(st,pa);
                       log.info("File eingelesen:"+ pa);
                       if (st.ntrees==0){
                           dataex.readOldFormat1(st,pa);
                       }
                       if (st.ntrees==0){
                           dataex.readOldFormat2(st,pa);
                       } 
                       if (st.ntrees==0){
                           dataex.readOldFormat3(st,pa);
                       }                        
                       if (st.getSpeciesDefinedTrue()==false) {
                           String text = st.getSpeciesUndefinedCode();
                           JTextArea about = new JTextArea(messages.getString("ERROR_Reading_Tree1")+text+
                                                           messages.getString("ERROR_Reading_Tree2")+
                                                           messages.getString("ERROR_Reading_Tree3"));
                           about.setBackground(Color.LIGHT_GRAY);
                           JOptionPane.showMessageDialog(this, about, "Error", JOptionPane.INFORMATION_MESSAGE);
                           st.ntrees=0;
                           st.nspecies=0;
                       }
                       st.status=0;
                       st.temp_Integer=0;
                       st.sortbyd();
                       st.missingData();
                       st.descspecies();
                       // set L�we default
//                       Treatment te= new Treatment();
                       GenerateXY gxy =new GenerateXY();
                       gxy.zufall(st);
                       if (grafik3D) manager3d.setStand(st);

                       tfUpdateTrue=true;
                       updatetp(false);
                       tfUpdateTrue=true;
                       st.ingrowthActive=false;
                       gr.starten();
                       showIframes();
                 }
                // menu item open: read stand data from unformatted file
                if (cmd.equals("openTreegrossXML"))
                { 
                       yt.setYieldTableNew();
                       JFileChooser fc = new JFileChooser();
                       TxtFileFilter txtFilter = new TxtFileFilter();
                       txtFilter.setExtension("xml");
                       fc.addChoosableFileFilter(txtFilter);
                       fc.setCurrentDirectory(new File(user.getDataDir()));
                       int auswahl = fc.showOpenDialog(owner);
                       String pa= fc.getSelectedFile().getPath();
                       currentFile = pa;
                       String dn= fc.getSelectedFile().getName();
                       Model mo =new Model();
                       st.setModelRegion(mo.getPlugInName(plugIn));
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
                       catch (Exception e2){log.info(e2.toString());  }
                       log.info("File eingelesen:"+ pa);

                       if (st.getSpeciesDefinedTrue()==false) {
                           String text = st.getSpeciesUndefinedCode();
                           JTextArea about = new JTextArea(messages.getString("ERROR_Reading_Tree1")+text+
                                                           messages.getString("ERROR_Reading_Tree2")+
                                                           messages.getString("ERROR_Reading_Tree3"));
                           about.setBackground(Color.LIGHT_GRAY);
                           JOptionPane.showMessageDialog(this, about, "Error", JOptionPane.INFORMATION_MESSAGE);
                           st.ntrees=0;
                           st.nspecies=0;
                       }
                       st.status=0;
                       st.temp_Integer=0;
                       st.sortbyd();
// Test for grouping                       
//                       Groups groups = new Groups(st);
//                       groups.setAutoGrouping();
                       
                       st.missingData();
                       st.descspecies();
                       // set L�we default
                       GenerateXY gxy =new GenerateXY();
                       gxy.zufall(st);
                       if (grafik3D) manager3d.setStand(st);

                       tfUpdateTrue=true;
                       updatetp(false);
                       tfUpdateTrue=true;
                       st.ingrowthActive=false;
                       gr.starten();
                       showIframes();
                 }
                // open NutungsPlaner SQLite Database
                if (cmd.equals("SQlite")){
                   log.info("open NutzungsPlaner SQlite Database: "+user.getDataDir()); 
                   try {
                       String modelPlugIn="forestsimulator.SQLite.SQLiteAccess";  
                       PlugInDBSQLite dialog = (PlugInDBSQLite)Class.forName(modelPlugIn).newInstance();  
                       dialog.startDialog(this,st,user.getDataDir()); 
                       int nGrowingCycles=st.temp_Integer;
                       st.status=0;
                       st.temp_Integer=0;
                       st.sortbyd();
                       st.missingData();
                       st.descspecies();
                       // set L�we default
//                       GenerateXY gxy =new GenerateXY();
//                       gxy.zufall(st);
                       if (grafik3D) manager3d.setStand(st);
                       tfUpdateTrue=true;
                       updatetp(false);
                       tfUpdateTrue=true;
                      
                        
                       
                       st.ingrowthActive=false;
                       gr.starten();
                       showIframes();
                   }
                   catch (Exception ex){log.info(ex.toString());log.info("Fehler SQLite "); }

                   log.info("Open Access File"); 
                }
                // open Access File
                if (cmd.equals("openAccess")){
                   log.info("Open Access File"); 
                   try {
                       String modelPlugIn="forestsimulator.DBAccess.DBAccess";  
                       PlugInDBSQLite dialog = (PlugInDBSQLite)Class.forName(modelPlugIn).newInstance();  
                       dialog.startDialog(this,st,user.getDataDir()); 
                       int nGrowingCycles=st.temp_Integer;
                       st.status=0;
                       st.temp_Integer=0;
                       st.sortbyd();
                       st.missingData();
                       st.descspecies();
                       // set L�we default
//                       GenerateXY gxy =new GenerateXY();
//                       gxy.zufall(st);
                       if (grafik3D) manager3d.setStand(st);
                       tfUpdateTrue=true;
                       updatetp(false);
                       tfUpdateTrue=true;
                      
                        
                       
                       st.ingrowthActive=false;
                       gr.starten();
                       showIframes();
                   }
                   catch (Exception ex){log.info(ex.toString());log.info("kein Access Plugin Vorhanden "); }

                   log.info("Open Access File"); 
                }
                
                
                // Edit Treegross data
                if (cmd.equals("edit")){
                    TgEditTreegross tgEditTreegross = new TgEditTreegross(this, true, st, language);
                    tgEditTreegross.setVisible(true);
                    if (grafik3D ) manager3d.setStand(st);
                    tfUpdateTrue=true;
                    updatetp(false);
                }
                // read stand data from unformatted file
		if (cmd.equals("save"))
		{ 
                    DataExchangeFormat dataex = new DataExchangeFormat();
		    JFileChooser fc = new JFileChooser();
                    TxtFileFilter txtFilter = new TxtFileFilter();
                    fc.addChoosableFileFilter(txtFilter);
                    fc.setCurrentDirectory(new File(user.getDataDir()));
                    int auswahl = fc.showSaveDialog(owner);
                    String pa= fc.getSelectedFile().getPath();
                    String dn= fc.getSelectedFile().getName();    
                    dataex.save(st,pa);
                  }
		if (cmd.equals("saveTreegrossXML"))
		{ 
                    DataExchangeFormat dataex = new DataExchangeFormat();
		    JFileChooser fc = new JFileChooser();
                    TxtFileFilter txtFilter = new TxtFileFilter();
                    txtFilter.setExtension("xml");
                    fc.addChoosableFileFilter(txtFilter);
                    fc.setCurrentDirectory(new File(user.getDataDir()));
                    int auswahl = fc.showSaveDialog(owner);
                    String pa= fc.getSelectedFile().getPath();
                    String dn= fc.getSelectedFile().getName();    
                    TreegrossXML2 treegrossXML2 = new TreegrossXML2();
                    treegrossXML2.saveAsXML(st,pa);
                  }
                
                //Menu save as jpg

                if (cmd.equals("ppJPG")){
                     pp.getJPEG();
                     pp.neuzeichnen();                    
                    
                }            
		  
                if (cmd.equals("Tree values"))
		  { 
                      TgHTMLsv sv= new TgHTMLsv(st);
		      sv.newreport(st,workingDir,"treelist.html",language);
	              seite="file:"+System.getProperty("file.separator")+System.getProperty("file.separator")
                              +System.getProperty("file.separator")+sv.getFilename();
                      StartBrowser startBrowser = new StartBrowser(seite);   
                      startBrowser.start();
		  }  
		  if (cmd.equals("Stand table")){

		        Groups grps = new Groups(st);
                        grps.setAutoGrouping();
                        st.sortbyd();
                        st.descspecies();
                        yt.enterStandDesc(st);
                        yt.writeTable(st,workingDir,"standtable.html", language);
	                    seite="file:"+System.getProperty("file.separator")+System.getProperty("file.separator")
                                +System.getProperty("file.separator")+yt.getFilename();
                        StartBrowser startBrowser = new StartBrowser(seite);   
                        startBrowser.start();
		  }
//
		  if (cmd.equals("Structure table"))  { 
                        st.sortbyd();
                        st.descspecies();
                        TgStructureTable tgStructureTable = new TgStructureTable();
                        tgStructureTable.writeTable(st,workingDir,"standstructure.html", language);
	                seite="file:"+System.getProperty("file.separator")+System.getProperty("file.separator")
                                +System.getProperty("file.separator")+tgStructureTable.getFilename();
                        StartBrowser startBrowser = new StartBrowser(seite);   
                        startBrowser.start();
		  }
//
		  if (cmd.equals("Definition"))  { 
                try {
                    st.getSDM().listCurrentSpeciesDefinition(st, workingDir, "speciesdefinition.html");
                    seite = "file:"+System.getProperty("file.separator")+System.getProperty("file.separator")+workingDir
                                +System.getProperty("file.separator")+"speciesdefinition.html";
                    StartBrowser startBrowser = new StartBrowser(seite);
                    startBrowser.start();
                } catch (IOException ex) {
                    Logger.getLogger(TgJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
		  }
//
		  if (cmd.equals("species_code"))  {
                
                    st.getSDM().listAllSpeciesDefinition(st, workingDir, "speciescode.html");
                    seite = "file:"+System.getProperty("file.separator")+System.getProperty("file.separator")+workingDir
                                +System.getProperty("file.separator")+"speciescode.html";
                    StartBrowser startBrowser = new StartBrowser(seite);
                    startBrowser.start();
                
		  }
//           
		  if (cmd.equals("tree_table"))  { 
                        st.sortbyd();
                        st.descspecies();
                        DataExchangeFormat dataex = new DataExchangeFormat();
                        dataex.writeTreeTable(st);
		  }
                 
//                
                  if (cmd.equals("Info page")){ 
                       seite="file:"+System.getProperty("file.separator")+System.getProperty("file.separator")
                               +System.getProperty("file.separator")+user.programDir+System.getProperty("file.separator")+"index.html";
                       StartBrowser startBrowser = new StartBrowser(seite);   
                       startBrowser.start();
		  } 
//                
                  if (cmd.equals("License")){ 
                       seite="file:"+System.getProperty("file.separator")+System.getProperty("file.separator")
                               +System.getProperty("file.separator")+user.programDir+System.getProperty("file.separator")+"gpl.html";
                       StartBrowser startBrowser = new StartBrowser(seite);   
                       startBrowser.start();
                  }
 //                
                  if (cmd.equals("Introduction")){ 
                      String fileName=user.programDir+System.getProperty("file.separator")+"help"+System.getProperty("file.separator")+"NWFVA11_TreeGrOSS.pdf";
                      String seite="file:"+System.getProperty("file.separator")+System.getProperty("file.separator")+System.getProperty("file.separator")+fileName;
                      StartBrowser startBrowser = new StartBrowser(seite);   
                      startBrowser.start();
		  } 
 //                
                  if (cmd.equals("Changes")){ 
                      String fileName=user.programDir+System.getProperty("file.separator")+"help"+System.getProperty("file.separator")+"FSChanges.pdf";
                      String seite="file:"+System.getProperty("file.separator")+System.getProperty("file.separator")+System.getProperty("file.separator")+fileName;
                      StartBrowser startBrowser = new StartBrowser(seite);   
                      startBrowser.start();
		  } 
//                
                
                // menu item exit
		if (cmd.equals("exit")) 
                {
                    log.info("Programm beendet");
                    System.exit(0); 
                }                
                

                
           
                
            //Menu "Properties"
            
                  if(cmd.equals("Program Settings"))
                  {
                      JDialog settings = new TgUserDialog(this, true);
                      settings.setVisible(true);
                      programDir=user.getWorkingDir();
                      JTextArea about = new JTextArea(messages.getString("Please_restart"));
                  }

                
                  if(cmd.equals("Species Manager"))
                  {
                    TgSpeciesManXML spman = new TgSpeciesManXML(this,true, programDir, st.FileXMLSettings);spman.setVisible(true);
                  }
              //Menu "Help"
                
                if(cmd.equals("About"))
                {
                    JTextArea about = new JTextArea("TreeGrOSS: ForestSimulation "+bwinproVersion+" \n http://www.nw-fva.de \n "+bwinproVersion+" \n (c) 2002-2015 Juergen Nagel, Northwest German Forest Research Station , \n Gr�tzelstr.2, 37079 G�ttingen, Germany \n E-Mail: Juergen.Nagel@nw-fva.de");
                    about.setBackground(Color.LIGHT_GRAY);
                    JOptionPane.showMessageDialog(this, about, "About", JOptionPane.INFORMATION_MESSAGE);
                }
                if(cmd.equals("checksum"))
                {
                    String fname = programDir+System.getProperty("file.separator")+st.FileXMLSettings;
                    JackSumFile jsf = new JackSumFile();
                    String csum=jsf.getSum("ForestSimulatorSettings.xml");
                    JTextArea about = new JTextArea("TreeGrOSS: Model Checksum:  \n "+csum+" \n ");
                    about.setBackground(Color.LIGHT_GRAY);
                    JOptionPane.showMessageDialog(this, about, "Check Sum", JOptionPane.INFORMATION_MESSAGE);
                }

                
            }
				
            if (cmd.equals("Grow"))
            {
                st.sortbyd();
                st.descspecies();
                TreegrossXML2 treegrossXML2 = new TreegrossXML2();
                if (currentFile.indexOf(".")< 12 ) currentFile=currentFile+System.getProperty("file.separator")+"temp.xml";
                Integer jj = st.year; 
                String fname = currentFile.substring(0, currentFile.length()-4)+"_"+jj.toString()+".xml";
                treegrossXML2.saveAsXML(st,fname);
                st.descspecies();
                st.executeMortality();
                st.descspecies();
                yt.enterStandDesc(st);
                st.grow(st.timeStep,st.ingrowthActive);  //grow a 5-year cycle, means  ingrowth
                updatetp(false);
                gr.drawGraph();
                repaint();
            } 
            
            if (cmd.equals("grow_back"))
            {
                st.sortbyd();
                st.descspecies();
                st.growBack(5,true);  //grow a 5-year cycle, means  ingrowth
                updatetp(false);
                gr.drawGraph();
                repaint();
            } 
            
            if (cmd.equals("Treatment"))
            {
                st.descspecies();
                st.sortbyd();
//                treatmentMan3.loadSettingsToStandRule();
                  treatmentMan4.executeTreatment(st);

//                tl.executeManager2(st);
                updatetp(false);
		zf.neuzeichnen(); //Zeichenfl�che neu zeichnen
//                pp.neuzeichnen();
                gr.drawGraph();
                repaint();

            } 
            if (cmd.equals("select_crop_trees"))
            {
                st.descspecies();
                st.sortbyd();
//                treatmentMan3.loadSettingsToStandRule();
//                treatmentMan3.selectCropTrees();
                updatetp(false);
                gr.drawGraph();
                repaint();
            }

            if (cmd.equals("deselect_crop_trees"))
            {
               st.descspecies();
               tl.resetAllCropTrees(st);
               updatetp(false);
               zf.neuzeichnen(); //Zeichenfl�che neu zeichnen
               gr.drawGraph();
               repaint();
            }
            
            
            if (cmd.equals("thin_by_list"))
            {
               st.descspecies();
//               JOptionPane.showMessageDialog(frame,"Geben Sie die Baumnummern ein","Liste",JOptionPane.ERROR_MESSAGE);
               String s = (String)JOptionPane.showInputDialog(
                    this,messages.getString("thin_by_list_mess")+" 1,4,7,18",messages.getString("thin_by_list"),
                    JOptionPane.PLAIN_MESSAGE,null,null,"");
                if (s.length() > 2) {
                    String[] sList = s.split(",");
                    for (int iik = 0; iik < sList.length; iik++) {
                        for (int jk = 0; jk < st.ntrees; jk++) {
                            if (st.tr[jk].no.equals(sList[iik])) {
                                st.tr[jk].out = st.year;
                                st.tr[jk].outtype = 2;
                            }
                        }
                    }
                }
               st.descspecies();
               updatetp(false);
               zf.neuzeichnen(); //Zeichenfl�che neu zeichnen
               gr.drawGraph();
 //              showIframes();
 
               repaint();
            }
            
            if(cmd.equals("Sorting"))
            {
                    SortingDialog sorter = new SortingDialog(this, true, st, programDir, false, workingDir, language,logHandler);
                    sorter.setVisible(true);
                    
            }
            if(cmd.equals("Deadwood"))
            {
//                    DeadwoodDialog sorter = new DeadwoodDialog(this, true, st, programDir, false, workingDir);
//                    sorter.setVisible(true);
            }
            if (cmd.equals("NutrientBalance")) {

                   BiomassDialog nutrientBalance = new BiomassDialog(this, true, st, programDir, false, workingDir,logHandler);
                   nutrientBalance.setVisible(true);

            }

            if (cmd.equals("Roots"))
            {
                 RootBiomassReport rootBiomassReport = new RootBiomassReport();
                 rootBiomassReport.report(st, programDir, workingDir);
                    
            }
            if(cmd.equals("RootingDialog"))
            {
                 RootingDialog rootingDialog = new RootingDialog(this,true,st);
                 rootingDialog.setVisible(true);

            }

            if(cmd.equals("Bwin62")){
                   JFileChooser filechooser = new JFileChooser();
                   filechooser.showOpenDialog(this);
                   String verzeichnisUndDateiName= filechooser.getSelectedFile().getPath();
                   DataExchangeFormat data = new DataExchangeFormat();
                   data.readOldFormat1(st, verzeichnisUndDateiName);
                   filechooser.setApproveButtonText("speichern");
                   filechooser.showOpenDialog(this);
                   verzeichnisUndDateiName= filechooser.getSelectedFile().getPath();
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
             if (cmd.equals("screen1")){
                int xp = (int) (scr.width*0.65); 
                int yp = (int) ((scr.height-105)*0.5);
                iframe[0].setLocation(xp,0);
                iframe[0].setSize(new Dimension(scr.width-xp,scr.height-105-yp));
                iframe[0].setVisible(true);
                iframe[1].setLocation(xp,yp);
                iframe[1].setSize(new Dimension(scr.width-xp,scr.height-105-yp));
                iframe[1].setVisible(true);
                iframe[2].setVisible(false);
                iframe[3].setVisible(false);
                yp = (int) ((scr.height-105)*0.7);
                iframe[4].setLocation(0,0);
                iframe[4].setSize(new Dimension(xp,yp));
                iframe[4].setVisible(true);
                iframe[5].setLocation(0,yp);
                iframe[5].setSize(new Dimension(xp,scr.height-105-yp));
                iframe[5].setVisible(true);
                iframe[6].setVisible(false);
             }
             if (cmd.equals("screen2")){
                iframe[0].setVisible(false);
                iframe[1].setLocation(0,0);
                int yp =(int) ((scr.height-105)*0.65);
                iframe[1].setSize(new Dimension(scr.width,yp));
                iframe[1].setVisible(true);
                iframe[2].setVisible(false);
                iframe[3].setVisible(false);
                iframe[4].setVisible(false);
                iframe[5].setVisible(true);
                iframe[5].setLocation(0,yp);
                iframe[5].setSize(new Dimension(scr.width,scr.height-105-yp));
                iframe[6].setVisible(false);
             }
             if (cmd.equals("screen3")){
                iframe[0].setVisible(false);
                iframe[1].setVisible(false);
                iframe[2].setVisible(false);
                iframe[3].setVisible(false);
                iframe[4].setVisible(false);
                iframe[5].setVisible(false);
                iframe[6].setVisible(false);
                iframe[1].setLocation(0,0);
                iframe[1].setVisible(true);
                iframe[1].setSize(new Dimension(scr.width,scr.height-105));
             }

		 

// Action commands of stand map menu 
            if (cmd.equals("saveStandMapToJPG")){
		        JFileChooser fc = new JFileChooser();
                TxtFileFilter txtFilter = new TxtFileFilter();
                txtFilter.setExtension("jpg");
                fc.addChoosableFileFilter(txtFilter);
                fc.setCurrentDirectory(new File(user.getWorkingDir()));
                int auswahl = fc.showSaveDialog(owner);
                String pa= fc.getSelectedFile().getPath();
                zf.setJPGFilename(pa);
                zf.getJPEG();
                zf.neuzeichnen();
            }
            if (cmd.equals("refreshStandMap")){ zf.neuzeichnen();}
            if (cmd.equals("zoomStandMapIn")){ zf.zoomStatus=1; zf.neuzeichnen();}
            if (cmd.equals("zoomStandMapOut")){ 
                       zf.zoomStatus=0;zf.xlzoom=0;zf.ylzoom=0;zf.xuzoom=0;zf.yuzoom=0;
                       zf.neuzeichnen();
                }
            if (cmd.equals("Factor=1")){ zf.setDbhFactor(1);zf.neuzeichnen();}
            if (cmd.equals("Factor=3")){ zf.setDbhFactor(3);zf.neuzeichnen();}
            if (cmd.equals("Factor=5")){ zf.setDbhFactor(5);zf.neuzeichnen();}
// Action commands of parallel projekction map menu 
            if (cmd.equals("savePPMapToJPG")){
                pp.getJPEG();pp.neuzeichnen();}
            if (cmd.equals("refreshPPMap")){ pp.neuzeichnen();}
            if (cmd.equals("zoomPPMapIn")){ pp.zoomStatus=1; pp.neuzeichnen();}
            if (cmd.equals("zoomPPMapOut")){ 
                       pp.zoomStatus=0;pp.xlzoom=0;pp.ylzoom=0;pp.xuzoom=0;pp.yuzoom=0;
                       pp.neuzeichnen();
                }
            if (cmd.equals("ppSkyColor")){    javax.swing.JColorChooser cc= new javax.swing.JColorChooser();
                          pp.setSkyColor(cc.showDialog(this, "Choose Sky Color",pp.getSkyColor() ));
                          pp.neuzeichnen();}
            if (cmd.equals("ppGroundColor")){    javax.swing.JColorChooser cc= new javax.swing.JColorChooser();
                          pp.setGroundColor(cc.showDialog(this, "Choose Ground Color",pp.getGroundColor() ));
                          pp.neuzeichnen();}
            if (cmd.equals("ppStandGroundColor")){    javax.swing.JColorChooser cc= new javax.swing.JColorChooser();
                          pp.setStandGroundColor(cc.showDialog(this, "Choose Stand Ground Color",pp.getStandGroundColor() ));
                          pp.neuzeichnen();}
// Action commands of Grafik Window 
            if (cmd.equals("SpeciesByCrownSurfaceArea")){gr.setGraphType(0);gr.neuzeichnen();
                              iframe[2].setVisible(false);iframe[2].setVisible(true);}
            if (cmd.equals("DiameterDistribution")){gr.setGraphType(1);gr.neuzeichnen();
                              iframe[2].setVisible(false);iframe[2].setVisible(true);}
            if (cmd.equals("DiameterDistributionCT")){gr.setGraphType(2);gr.neuzeichnen();
                              iframe[2].setVisible(false);iframe[2].setVisible(true);}
            if (cmd.equals("HeightDiameterPlot")){gr.setGraphType(3);gr.neuzeichnen();
                              iframe[2].setVisible(false);iframe[2].setVisible(true);}
            if (cmd.equals("saveChartToJPG")) {
                JFileChooser fc = new JFileChooser();
                TxtFileFilter txtFilter = new TxtFileFilter();
                txtFilter.setExtension("jpg");
                fc.addChoosableFileFilter(txtFilter);
                fc.setCurrentDirectory(new File(user.getWorkingDir()));
                int auswahl = fc.showSaveDialog(owner);
                String pa= fc.getSelectedFile().getPath();
                gr.setJPGFilename(pa);
                gr.saveToJPEG(workingDir); }
	}
        
        public void StandChanged(treegross.base.StandChangeEvent evt){
            System.out.println("stand changed "+evt.getName());
            updatetp(false);
        }
        
//------------------------------------------------------------------------------        
        
        public void itemStateChanged(ItemEvent e)
        {
           Object source = e.getItemSelectable();
           for (int i = 0; i<7; i++)
           {
                if(source==menubar.cmi[i])
                {
                    if(e.getStateChange()==ItemEvent.SELECTED)
                    {
                        iframe[i].setVisible(true);
                    }
                    else
                    {
                        iframe[i].setVisible(false);
                    }
                }
           } 
// item event for stand map           
          if(source==tgStandMapMenu.cmi[0]) {
              if (e.getStateChange()==ItemEvent.SELECTED)zf.setStandMapInfo(true);
              else zf.setStandMapInfo(false);
              zf.neuzeichnen() ;
          }
          if(source==tgStandMapMenu.cmi[1]) {
              if (e.getStateChange()==ItemEvent.SELECTED)zf.setPlotCrownWidth(true);
              else zf.setPlotCrownWidth(false);
              zf.neuzeichnen() ;
          }
          if(source==tgStandMapMenu.cmi[2]) {
              if (e.getStateChange()==ItemEvent.SELECTED)zf.setPlotTreeNumber(true);
              else zf.setPlotTreeNumber(false);
              zf.neuzeichnen() ;
          }
          if(source==tgStandMapMenu.cmi[8]) {
              if (e.getStateChange()==ItemEvent.SELECTED)zf.setMouseThinning(true);
              else zf.setMouseThinning(false);
              if (grafik3D) manager3d.refreshStand(); else pp.neuzeichnen() ;
          }
          if(source==tgStandMapMenu.cmi[9]) {
              if (e.getStateChange()==ItemEvent.SELECTED)zf.setMouseCropTree(true);
              else zf.setMouseCropTree(false);
              pp.neuzeichnen() ;
          }
// item event for parallel map 
          if (grafik3D == false){ 
            if(source==tgPPMapMenu.cmi[0] ) {
              if (e.getStateChange()==ItemEvent.SELECTED)pp.setLivingTrees(true);
              else pp.setLivingTrees(false);
              pp.neuzeichnen() ;
            }
          if(source==tgPPMapMenu.cmi[1] ) {
              if (e.getStateChange()==ItemEvent.SELECTED)pp.setThinnedTrees(true);
              else pp.setThinnedTrees(false);
              pp.neuzeichnen() ;
            }
            if(source==tgPPMapMenu.cmi[2] ) {
              if (e.getStateChange()==ItemEvent.SELECTED)pp.setDeadTrees(true);
              else pp.setDeadTrees(false);
              pp.neuzeichnen() ;
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
        
        
     public void loadGenralSettings(String Dir){
        java.io.File file;
        String fname="";
        try {
            URL url =null;
            int m = Dir.toUpperCase().indexOf("FILE");
            int m2 = Dir.toUpperCase().indexOf("HTTP");
            String trenn =System.getProperty("file.separator");
            fname=Dir+System.getProperty("file.separator")+"models"+System.getProperty("file.separator")+st.FileXMLSettings;
           if ( m < 0 && m2 <0 ) fname="file:"+trenn+trenn+trenn+fname;
            System.out.println("SpeciesDef: URL: "+fname);
            try {
                 url = new URL(fname);}
            catch (Exception e){
                    log.info(e.toString());
                    JTextArea about = new JTextArea("TgDesign Genral Settings: Url file not found: "+fname);
                    JOptionPane.showMessageDialog(null, about, "About", JOptionPane.INFORMATION_MESSAGE);
            }
         SAXBuilder builder = new SAXBuilder();
         URLConnection urlcon = url.openConnection();

         Document doc = builder.build(urlcon.getInputStream());
         
         DocType docType = doc.getDocType();
//        
         Element sortimente =  doc.getRootElement();  
         java.util.List Setting = sortimente.getChildren("GeneralSettings");
         Iterator i = Setting.iterator();
         
         while (i.hasNext()) {
            Element setting = (Element) i.next();
            st.modelRegion=setting.getChild("ModelRegion").getText();
            if (Boolean.parseBoolean(setting.getChild("ErrorComponent").getText())){
                   st.random.setRandomType(treegross.random.RandomNumber.PSEUDO);
               }
            else st.random.setRandomType(treegross.random.RandomNumber.OFF);
            st.ingrowthActive=Boolean.parseBoolean(setting.getChild("IngrowthModul").getText());
            try { st.riskActive=Boolean.parseBoolean(setting.getChild("RiskModul").getText());
                 } catch (Exception e){ st.riskActive=false;}
            st.deadwoodModulName=setting.getChild("DeadwoodModul").getText();
            try { st.deadwoodModulName=setting.getChild("DebriswoodModul").getText();
                 } catch (Exception e){ st.deadwoodModulName="none";}
            try { st.sortingModul=setting.getChild("SortingModul").getText();
                 } catch (Exception e){ st.sortingModul="none";}
            try { st.biomassModul=setting.getChild("BiomassModul").getText();
                 } catch (Exception e){ st.biomassModul="none";}
            try { st.timeStep = Integer.parseInt(setting.getChild("TimeStep").getText());
                 } catch (Exception e){ st.timeStep = 5;}
            break;
            
         }

       } catch (Exception e) {
               log.info(e.toString());
               JTextArea about = new JTextArea("TgDesign file not found : "+fname);
               JOptionPane.showMessageDialog(null, about, "About", JOptionPane.INFORMATION_MESSAGE);
               log.info("SpeciesDef General settings: File nicht gefunden: "+fname);

               }
           
       

        
        
        
    }

       
        
        
        
        public void updatetp(boolean from3D)
        {
            gr.neuzeichnen();
            st.sortbyd();
            st.descspecies();
            zf.neuzeichnen();
            if (grafik3D && !from3D) manager3d.refreshStand();
            else {pp.neuzeichnen();st.sortbyd();st.descspecies();}
            tsi.formUpdate(st);
//            if (tfUpdateTrue==true) {treatmentMan3.formUpdate(st);tfUpdateTrue=false;}
            treatmentMan4.setStand(st);
            if (iframe[2].isVisible()==true){
                iframe[2].setVisible(false);
                iframe[2].setVisible(true);
            }
                       
        }

        void showIframes(){
                int xp = (int) (scr.width*0.65); 
                int yp = (int) ((scr.height-105)*0.5);
                iframe[0].setLocation(xp,0);
                iframe[0].setSize(new Dimension(scr.width-xp,scr.height-105-yp));
                iframe[0].setVisible(true);
                iframe[1].setLocation(xp,yp);
                iframe[1].setSize(new Dimension(scr.width-xp,scr.height-105-yp));
                iframe[1].setVisible(true);
                iframe[2].setVisible(false);
                iframe[3].setVisible(false);
                yp = (int) ((scr.height-105)*0.7);
                iframe[4].setLocation(0,0);
                iframe[4].setSize(new Dimension(xp,yp));
                iframe[4].setVisible(true);
                iframe[5].setLocation(0,yp);
                iframe[5].setSize(new Dimension(xp,scr.height-105-yp));
                iframe[5].setVisible(true);
                iframe[6].setVisible(false);
        }
//
// start a stand from other programs
//     public v        

        public void setStand(Stand stl){
            st=stl;
            Model mo =new Model();
            st.setModelRegion(mo.getPlugInName(plugIn));
            System.out.println("set Stand down");
            updatetp(false);
            System.out.println("set Stand down");
        }
        StringBuffer getFile(String path, String name)
        {
            
            File file = new File(path,name);
            FileReader fr;
            StringBuffer sbuffer = new StringBuffer(500);
            
            try
            {
                fr = new FileReader(file);
                boolean end = false;
                int c;
                while(!end)
                {
                    c = fr.read();
                    if(c==-1)
                    {
                        end = true;
                    }
                    else
                    {
                        sbuffer.append( (char)c);
                    }
                }
                fr.close();
                
            }
        
        catch(java.io.IOException e){log.info(e.toString());}
            
            return sbuffer;
        }
        
        
       
        
        public class MyInternalFrameListener extends InternalFrameAdapter
        {
            public void internalFrameClosing(InternalFrameEvent e) {
                for(int i = 0; i<7; i++)
                {
                    if(e.getInternalFrame()==iframe[i])
                    {
                        iframe[i].setVisible(false);
                        menubar.cmi[i].setState(false);
                    }
                }
            }
        }  
}


 