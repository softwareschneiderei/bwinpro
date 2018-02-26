/** http://www.nw-fva.de
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
import java.io.*; 
import java.net.*;


class TgUser 
{ 
    String workingDir="";
    String programDir="";
    String dataDir="";
    String workingDirIni="";
    String programDirIni="";
    String dataDirIni="";
    String plugIn="XML";
    String XMLSettings="";
    String language="en";
    int grafik3D = 0;
    String update = "01012007";
    String nwfva = null;
     
    public TgUser() {}
/** The user settings are load from a file TgUser.txt, which has to be in 
 *  the same directory
 */   
    public void loadSettings(String lpath) 
    {
	try 
        {  
            String s;
	    BufferedReader in=
	    new BufferedReader(new InputStreamReader(new FileInputStream(lpath+System.getProperty("file.separator")+"ForestSimulator.ini")));
            String textf = ".";
            programDir=in.readLine();
            if (System.getProperty("file.separator").equals("/")) 
                programDir= programDir.replaceAll("\\\\", "/");
            programDirIni=programDir;
            int m1 = programDir.indexOf(textf);
            if (m1 == 0 ) programDir=lpath+System.getProperty("file.separator")+programDir.substring(2,programDir.length());
            dataDir=in.readLine();
            if (System.getProperty("file.separator").equals("/")) 
                dataDir= dataDir.replaceAll("\\\\", "/");
            dataDirIni=dataDir;
            m1 = dataDir.indexOf(textf);
            if (m1 == 0 ) dataDir=lpath+System.getProperty("file.separator")+dataDir.substring(2,dataDir.length());
            workingDir=in.readLine();
            if (System.getProperty("file.separator").equals("/")) 
                workingDir= workingDir.replaceAll("\\\\", "/");
            workingDirIni= workingDir;
            m1 = workingDir.indexOf(textf);
            if (m1 == 0 ) workingDir=lpath+System.getProperty("file.separator")+workingDir.substring(2,workingDir.length());
            language = in.readLine();
            XMLSettings = in.readLine();
            plugIn = XMLSettings;
            int m = XMLSettings.indexOf(" -");
            if (m > 0){
                nwfva = XMLSettings.substring(m+2);
                XMLSettings = XMLSettings.substring(0,m);
            }
            grafik3D = Integer.parseInt(in.readLine());
            in.close();
        }
        catch (Exception e) 
        {	
            System.out.println(e); 
        }		 

   }
   public void currentDir() {
        java.io.File f = new java.io.File("");
        String localPath="";
        try{
//           localPath=  f.getAbsolutePath();
           localPath=  f.getCanonicalPath();
        }
        catch (Exception e){};
        
       System.out.println ("Current dir : " + localPath);
    
 }
   public boolean fileExists(String fname) {
    File f = new File(fname);
    System.out.println(f + (f.exists()? " is found " : " is missing "));
    return f.exists();
  }
  public String getWorkingDir(){
      return workingDir;
  }
  public String getProgramDir(){
      return programDir;
  }
  public String getDataDir(){
      return dataDir;
  }
 public String getWorkingDirIni(){
      return workingDirIni;
  }
  public String getProgramDirIni(){
      return programDirIni;
  }
  public String getDataDirIni(){
      return dataDirIni;
  }
  public String getXMLSettings(){
      return plugIn;
  }
   public String getPlugIn(){
      return plugIn;
  }
   public int getGrafik3D(){
      return grafik3D;
  }
 
  public String getLanguageShort(){
      String languageShort="";
      if (language.compareTo("Deutsch")==0) languageShort="de";
      if (language.compareTo("Espanol")==0) languageShort="es";
      if (language.compareTo("Polish")==0) languageShort="pl";
      return languageShort;
  }
  
  public boolean needsUpdate(String lastupdate){
            update=lastupdate;
            boolean erg = false;
            URL url =null;
            String updateInternet=null;
            String fname="https://www.nw-fva.de/~nagel/downloads/bwin7version.txt";
//            String fname="file:///W:/public_html/downloads/bwin7version.txt";
            try {
                 url = new URL(fname);
                 URLConnection urlcon = url.openConnection();
                 
                 urlcon.setReadTimeout(1000);
                 java.io.BufferedReader br = new java.io.BufferedReader(
                                new java.io.InputStreamReader(
                                urlcon.getInputStream()));
                 updateInternet=br.readLine();

                 
            }
  
            catch (java.io.IOException e){System.out.println("kein Internet Check möglich !"); }
            if (updateInternet != null){
                int yearNet = Integer.parseInt(updateInternet.substring(6,10));
                int monthNet = Integer.parseInt(updateInternet.substring(3,5));
                int dayNet = Integer.parseInt(updateInternet.substring(0,2));
                int year = Integer.parseInt(update.substring(6,10));
                int month = Integer.parseInt(update.substring(3,5));
                int day = Integer.parseInt(update.substring(0,2));
                double sumNet=365*yearNet+31*monthNet+dayNet;
                double sum=365*year+31*month+day;
                if (sum < sumNet) erg=true;
            }
            return erg;
  
}

  
}
       
