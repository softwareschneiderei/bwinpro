package forestsimulator.Stand3D;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.*;

/**
 *
 * @author  jhansen
 */
public class ToolBar3D extends javax.swing.JToolBar {
    private File path;
    public boolean isLeft=false;

    public ToolBar3D(ActionListener al, boolean textures, File iconpath) {
        path=iconpath;
        initComponents(al);
        jToggleButton3.setEnabled(textures);        
    }    

    private void initComponents(ActionListener al) {
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        jToggleButton4 = new javax.swing.JToggleButton();
        jToggleButton5 = new javax.swing.JToggleButton();
        jToggleButton6 = new javax.swing.JToggleButton();
        jToggleButton7 = new javax.swing.JToggleButton();
        
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton(); 
        jButton4 = new javax.swing.JButton();
        
        jToggleButton1.addActionListener(al);
        jToggleButton2.addActionListener(al);
        jToggleButton3.addActionListener(al);
        jToggleButton4.addActionListener(al);
        jToggleButton5.addActionListener(al);
        jToggleButton6.addActionListener(al);
        jToggleButton7.addActionListener(al);
        jButton1.addActionListener(al);
        jButton2.addActionListener(al);
        jButton3.addActionListener(al);
        jButton4.addActionListener(al);
        
        jButton1.setOpaque(false);
        jButton2.setOpaque(false);
        jButton3.setOpaque(false);
        setOpaque(false);
        setMinimumSize(new java.awt.Dimension(0, 0));
        setPreferredSize(new java.awt.Dimension(200, 25));
        setRollover(true);
//
        URL url =null;
        javax.swing.ImageIcon icon;
        jToggleButton1.setSelected(true);
        jToggleButton1.setText("Status");
        jToggleButton1.setToolTipText("Status anzeigen");
        jToggleButton1.setActionCommand("setstatus");
//        System.out.println("ToolBar3D "+path);
        if(path!=null){
            try {
                 url = new File(path, "status.jpg").toURI().toURL();
            }
            catch (MalformedURLException e){ }
//            icon=new javax.swing.ImageIcon(path+"\\status.jpg");
            icon=new javax.swing.ImageIcon(url);
            if(icon.getImageLoadStatus()==java.awt.MediaTracker.COMPLETE){
                jToggleButton1.setText(null);
                jToggleButton1.setIcon(icon);
            } 
        }
        add(jToggleButton1);

        jToggleButton2.setText("tote");
        jToggleButton2.setToolTipText("tote Bäume anzeigen");
        jToggleButton2.setActionCommand("setdead");
        if(path!=null){
            try {
                 url = new File(path, "showdead.jpg").toURI().toURL();
            }
            catch (MalformedURLException e){ }
            icon=new javax.swing.ImageIcon(url);
//            icon=new javax.swing.ImageIcon(path+"\\showdead.jpg");
            if(icon.getImageLoadStatus()==java.awt.MediaTracker.COMPLETE){
                jToggleButton2.setText(null);
                jToggleButton2.setIcon(icon);
            }
        } 
        add(jToggleButton2);

        jToggleButton3.setText("Textur");
        jToggleButton3.setToolTipText("Objekte texturieren");
        jToggleButton3.setActionCommand("settexture");
        if(path!=null){
            try {
                 url = new File(path, "texture.jpg").toURI().toURL();
            }
            catch (Exception e){ }
            icon=new javax.swing.ImageIcon(url);
//            icon=new javax.swing.ImageIcon(path+"\\texture.jpg");
            if(icon.getImageLoadStatus()==java.awt.MediaTracker.COMPLETE){
                jToggleButton3.setText(null);
                jToggleButton3.setIcon(icon);
            }
        } 
        add(jToggleButton3);

        jToggleButton4.setText("Arten");
        jToggleButton4.setToolTipText("Bäume nach Art einfärben");
        jToggleButton4.setActionCommand("setspecies");
        if(path!=null){
            try {
                 url = new File(path, "speciescolor.jpg").toURI().toURL();
            }
            catch (Exception e){ }
            icon=new javax.swing.ImageIcon(url);
//            icon=new javax.swing.ImageIcon(path+"\\speciescolor.jpg");
            if(icon.getImageLoadStatus()==java.awt.MediaTracker.COMPLETE){
                jToggleButton4.setText(null);
                jToggleButton4.setIcon(icon);
            }
        } 
        add(jToggleButton4);
        
        jToggleButton5.setText("Bauminfo");
        jToggleButton5.setSelected(true);
        jToggleButton5.setToolTipText("Bauminfo einblenden");
        jToggleButton5.setActionCommand("showinfo");
        if(path!=null){
            try {
                 url = new File(path, "info.jpg").toURI().toURL();
            } catch (Exception e){ }
            icon=new javax.swing.ImageIcon(url);
//            icon=new javax.swing.ImageIcon(path+"\\info.jpg");
            if(icon.getImageLoadStatus()==java.awt.MediaTracker.COMPLETE){
                jToggleButton5.setText(null);
                jToggleButton5.setIcon(icon);
            }
        } 
        add(jToggleButton5);
        
        jToggleButton6.setSelected(true);
        jToggleButton6.setText("Nebel");
        jToggleButton6.setSelected(true);
        jToggleButton6.setToolTipText("Nebel einblenden");
        jToggleButton6.setActionCommand("showfog");
        if(path!=null){
            try {
                 url = new File(path, "showfog.jpg").toURI().toURL();
            } catch (Exception e){ }
            icon=new javax.swing.ImageIcon(url);
//            icon=new javax.swing.ImageIcon(path+"\\showfog.jpg");
            if(icon.getImageLoadStatus()==java.awt.MediaTracker.COMPLETE){
                jToggleButton6.setText(null);
                jToggleButton6.setIcon(icon);
            }
        } 
        add(jToggleButton6);
        
        jToggleButton7.setSelected(true);
        jToggleButton7.setText("Gitter");
        jToggleButton7.setSelected(true);
        jToggleButton7.setToolTipText("Gitter einblenden");
        jToggleButton7.setActionCommand("showmesh");
        if(path!=null){
            try {
                 url = new File(path, "showmesh.jpg").toURI().toURL();
            } catch (Exception e){ }
            icon=new javax.swing.ImageIcon(url);
//            icon=new javax.swing.ImageIcon(path+"\\showmesh.jpg");
            if(icon.getImageLoadStatus()==java.awt.MediaTracker.COMPLETE){
                jToggleButton7.setText(null);
                jToggleButton7.setIcon(icon);
            }
        } 
        add(jToggleButton7);

        jButton1.setText("Sshot");
        jButton1.setToolTipText("Szene als JPG speichern");
        jButton1.setActionCommand("screenshot");
        if(path!=null){
            try {
                 url = new File(path, "sshot.jpg").toURI().toURL();
            } catch (Exception e){ }
            icon=new javax.swing.ImageIcon(url);
//            icon=new javax.swing.ImageIcon(path+"\\sshot.jpg");
            if(icon.getImageLoadStatus()==java.awt.MediaTracker.COMPLETE){
                jButton1.setText(null);
                jButton1.setIcon(icon);
            }
        } 
        add(jButton1);

        jButton2.setText("f\u00e4llen");
        jButton2.setToolTipText("markierte Bäume fällen");
        jButton2.setActionCommand("harvest");
        if(path!=null){
            try {
                 url = new File(path, "harvest.jpg").toURI().toURL();
            } catch (Exception e){ }
            icon=new javax.swing.ImageIcon(url);
//            icon=new javax.swing.ImageIcon(path+"\\harvest.jpg");
            if(icon.getImageLoadStatus()==java.awt.MediaTracker.COMPLETE){
                jButton2.setText(null);
                jButton2.setIcon(icon);
            }
        } 
        add(jButton2);

        jButton3.setText("Start");
        jButton3.setToolTipText("Ausgangsansicht wiederherstellen");
        jButton3.setActionCommand("goback");
        if(path!=null){
            try {
                 url = new File(path, "start.jpg").toURI().toURL();
            } catch (Exception e){ }
            icon=new javax.swing.ImageIcon(url);
//            icon=new javax.swing.ImageIcon(path+"\\start.jpg");
            if(icon.getImageLoadStatus()==java.awt.MediaTracker.COMPLETE){
                jButton3.setText(null);
                jButton3.setIcon(icon);
            }
        }  
        add(jButton3);
        
        jButton4.setText("Toolbar");
        jButton4.setToolTipText("Toolbar anordnen (oben links)");
        jButton4.setActionCommand("toolpos");
        if(path!=null){
            try {
                 url = new File(path, "toolpos.jpg").toURI().toURL();
            } catch (Exception e){ }
            icon=new javax.swing.ImageIcon(url);
//            icon=new javax.swing.ImageIcon(path+"\\toolpos.jpg");
            if(icon.getImageLoadStatus()==java.awt.MediaTracker.COMPLETE){
                jButton4.setText(null);
                jButton4.setIcon(icon);
            }
        } 
        add(jButton4);
    }    
    
    
    // Variables declaration - do not modify
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    public  javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToggleButton jToggleButton4;
    public javax.swing.JToggleButton jToggleButton5;
    public javax.swing.JToggleButton jToggleButton6;
    public javax.swing.JToggleButton jToggleButton7;
    // End of variables declaration
    
}
