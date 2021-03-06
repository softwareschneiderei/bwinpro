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
package forestsimulator.Stand3D;

import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.JFileChooser;
import treegross.base.Stand;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.media.j3d.Texture2D;
import javax.media.j3d.Canvas3D;
import java.net.*;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author jhansen
 */
public class Manager3D implements ActionListener {

    private final ResourceBundle messages = ResourceBundle.getBundle("forestsimulator/gui");
    private Stand3DScene scene = null;
    private final Texture2D[] textures = new Texture2D[7];
    private JPanel home = null;
    private Stand st = null;
    private boolean speccol = false;
    private boolean treestatus = true;
    private boolean showharvested = false;
    private boolean available3d = false;
    private boolean textured = false;
    private boolean showinfo = true;
    private boolean showfog = true;
    private boolean showmesh = true;
    private File texpath = null;
    //new swing components:
    private JPanel scenePanel;
    private ToolBar3D toolbar3d;

    /**
     * Creates a new instance of Manager3D homepanel (JPanel) the place where
     * teh 3dcanvas should be drawn texturepath (String) the path to the
     * textures for crowns etc if null the option texture will not be available
     * @param homepanel
     * @param texturepath
     * @param showtoolbar
     */
    public Manager3D(JPanel homepanel, File texturepath, boolean showtoolbar) {
        home = homepanel;
        texpath = new File(texturepath, "3dtextures");
        initSwing(new File(texturepath, "icons"));
        setToolbarVisible(showtoolbar);
        PackageInfo info3d = new PackageInfo();
        if (info3d.isJ3DInstalled()) {
            new Query3DProperties().print();
            available3d = true;
        }
        if (!showtoolbar) {
            showinfo = false;
            showfog = false;
            showmesh = false;
        }
        if (!available3d) {
            JOptionPane.showMessageDialog(
                    null,
                    messages.getString("Manager3D.no3d.message"),
                    messages.getString("Manager3D.no3d.title"), JOptionPane.ERROR_MESSAGE);
        }
        if (showtoolbar) {
            loadTextures();
        }
    }

    private void initSwing(File iconpath) {
        home.setLayout(new BorderLayout());
        home.setOpaque(false);
        scenePanel = new JPanel();
        scenePanel.setLayout(new BorderLayout());
        home.add(scenePanel, BorderLayout.CENTER);
        // ToolBar:
        if (iconpath != null) {
            toolbar3d = new ToolBar3D(this, true, iconpath);
        } else {
            toolbar3d = new ToolBar3D(this, false, iconpath);
        }
        home.add(toolbar3d, BorderLayout.NORTH);
        add3dScene();
    }

    public boolean get3DAvailable() {
        return available3d;
    }

    private void loadTextures() {
        if (available3d && texpath != null) {
            URL url0 = null;
            URL url1 = null;
            URL url2 = null;
            URL url3 = null;
            URL url4 = null;
            URL url5 = null;
            URL url6 = null;
//            System.out.println("Manager3D: URL: "+texpath);
            File fname0 = new File(texpath, "blcrown.jpg");
            File fname1 = new File(texpath, "blcrownblend.jpg");
            File fname2 = new File(texpath, "conicrown.jpg");
            File fname3 = new File(texpath, "conicrownbottom.jpg");
            File fname4 = new File(texpath, "standbase.jpg");
            File fname5 = new File(texpath, "bltrunk.jpg");
            File fname6 = new File(texpath, "crownshad1.JPG");
            try {
                url0 = fname0.toURI().toURL();
                url1 = fname1.toURI().toURL();
                url2 = fname2.toURI().toURL();
                url3 = fname3.toURI().toURL();
                url4 = fname4.toURI().toURL();
                url5 = fname5.toURI().toURL();
                url6 = fname6.toURI().toURL();
            } catch (MalformedURLException e) {
                Logger.getLogger(Manager3D.class.getName()).log(Level.WARNING, "Illegal texture path", e);
            }
            try {
                Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
                textures[0] = (Texture2D) (new TextureLoader(url0, canvas)).getTexture();
                textures[1] = (Texture2D) (new TextureLoader(url1, canvas)).getTexture();
                textures[2] = (Texture2D) (new TextureLoader(url2, canvas)).getTexture();
                textures[3] = (Texture2D) (new TextureLoader(url3, canvas)).getTexture();
                textures[4] = (Texture2D) (new TextureLoader(url4, canvas)).getTexture();
                textures[5] = (Texture2D) (new TextureLoader(url5, canvas)).getTexture();
                textures[6] = (Texture2D) (new TextureLoader(url6, canvas)).getTexture();
//            System.out.println("Manager3D: textures build."+texpath+tz);
//            System.out.println("Manager3D: textures build.");
            } catch (Exception e) {
            }

        }
    }

    public void setStand(Stand stand) {
        if (stand == null) {
            return;
        }
        setStand(stand, Arrays.stream(stand.sp, 0, stand.nspecies).mapToInt(species -> species.code).toArray());
    }

    private void setStand(Stand stand, int[] species) {
        if (!available3d) {
            return;
        }
        st = stand;
        scene.loadStand(st, species);
    }

    private void add3dScene() {
        System.out.println("Adding new 3d scene");
        scene = new Stand3DScene(speccol, showharvested, textured, treestatus, showinfo, showfog, showmesh, textures);
        scenePanel.add(scene);
        scene.setPickFocus();
    }

    public void refreshStand() {
        if (scene == null) {
            return;
        }
        scene.setShowingSpecies(null);
        scene.setNewTreeDataAndRefresh(null);
    }

    public void showAllSpecies() {
        if (scene != null) {
            scene.setShowingSpecies(null);
        }
    }

    public void setSpecies(int[] species) {
        if (scene != null) {
            scene.setShowingSpecies(species);
        }
    }

    public void grabFocus() {
        if (scene != null) {
            scene.setPickFocus();
        }
    }

    public String getSelectedTree() {
        String result = "";
        if (scene != null) {

        }
        return result;
    }

    public void letTreefall(String treeno) {
        if (scene != null) {
            scene.harvestTreeInStand(treeno);
        }
    }

    public void setToolbarVisible(boolean visible) {
        toolbar3d.setVisible(visible);
    }

    public boolean isToolbarVisible() {
        return toolbar3d.isVisible();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (scene == null) {
            return;
        }
        // toolbar3d
        if (e.getActionCommand().equals("setstatus")) {
            treestatus = !treestatus;
            scene.setStatus(treestatus);
            scene.setPickFocus();
        }

        if (e.getActionCommand().equals("settexture")) {
            textured = !textured;
            scene.setTextured(textured);
            scene.setPickFocus();
        }

        if (e.getActionCommand().equals("setdead")) {
            showharvested = !showharvested;
            scene.setShowDeadTrees(showharvested);
            scene.setPickFocus();
        }

        if (e.getActionCommand().equals("setspecies")) {
            speccol = !speccol;
            if (textured) {
                textured = false;
                toolbar3d.jToggleButton3.setSelected(textured);
            }
            scene.setSpeciesColor(speccol);
            scene.setPickFocus();
        }

        if (e.getActionCommand().equals("harvest")) {
            scene.harvestAllMarkedTrees();
            scene.setPickFocus();
        }

        if (e.getActionCommand().equals("showinfo")) {
            showinfo = !showinfo;
            scene.showtreeinfo = showinfo;
            scene.setPickFocus();
        }

        if (e.getActionCommand().equals("goback")) {
            scene.setViewAndPositionStart();
            scene.setPickFocus();
        }
        if (e.getActionCommand().equals("screenshot")) {
            scene.setPickFocus();
            saveScreenShot();
            scene.setPickFocus();
        }
        if (e.getActionCommand().equals("showfog")) {
            showfog = toolbar3d.jToggleButton6.isSelected();
            scene.setFogEnable(toolbar3d.jToggleButton6.isSelected());
        }
        if (e.getActionCommand().equals("showmesh")) {
            showmesh = toolbar3d.jToggleButton7.isSelected();
            scene.setMeshVisible(toolbar3d.jToggleButton7.isSelected());
        }
        if (e.getActionCommand().equals("toolpos")) {
            if (!toolbar3d.isLeft) {
                home.remove(toolbar3d);
                toolbar3d.setOrientation(ToolBar3D.VERTICAL);
                toolbar3d.isLeft = true;
                home.add("West", toolbar3d);
                home.revalidate();
            } else {
                home.remove(toolbar3d);
                toolbar3d.setOrientation(ToolBar3D.HORIZONTAL);
                home.add("North", toolbar3d);
                home.revalidate();
                toolbar3d.isLeft = false;
            }
        }
    }

    private void saveImageAsJPEG(BufferedImage img, String filename) {
        File file = new File(filename);
        int ok = JOptionPane.YES_OPTION;
        if (file.exists()) {
            System.out.println("file alrerady exists");
            ok = JOptionPane.showConfirmDialog(
                    home,
                    messages.getString("Manager3D.save_image.overwrite.message"),
                    messages.getString("Manager3D.save_image.overwrite.title"),
                    JOptionPane.YES_NO_OPTION);
        }
        if (ok == JOptionPane.YES_OPTION) {
            try {
                ImageIO.write(img, "JPG", new FileOutputStream(filename));
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    public void copyToClipboard() {
        if (scene != null) {
            //ClipboardUtils.setContents(scene.getScreenShot(), null);
        }
    }

    public void harvestTrees() {
        scene.harvestAllMarkedTrees();
        scene.setPickFocus();
    }

    public void setInitialView() {
        scene.setViewAndPositionStart();
        scene.setPickFocus();
    }

    public void setFogDisabled() {
        scene.setFogEnable(false);
    }

    public void saveScreenShot() {
        if (scene != null) {
            BufferedImage img = scene.getScreenShot();
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.isDirectory()
                            || f.getName().toLowerCase().endsWith(".jpg");
                }

                @Override
                public String getDescription() {
                    return "jpg";
                }
            });
            int returnVal = fc.showSaveDialog(home);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                String path = file.getAbsolutePath();
                if (path.toLowerCase().endsWith(".jpg") == false) {
                    path = path + ".jpg";
                }
                saveImageAsJPEG(img, path);
            } else {
                System.out.println("Speichern abgebrochen");
            }
        } else {
            JOptionPane.showMessageDialog(
                    home,
                    messages.getString("Manager3D.save_screenshot.no_scene.message"),
                    messages.getString("Manager3D.save_screenshot.no_scene.title"),
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
