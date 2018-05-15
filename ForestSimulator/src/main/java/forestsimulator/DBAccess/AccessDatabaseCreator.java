package forestsimulator.DBAccess;

import javax.swing.filechooser.FileFilter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.*;
import java.util.ResourceBundle;

/**
 *
 * @author jhansen this class extends the basis class DatabaseCreator and
 * overwrites its methods. this technique makes it possible to implementate
 * interfaces for other databases easily.
 */
public class AccessDatabaseCreator extends DatabaseCreator {

    private final ResourceBundle messages = ResourceBundle.getBundle("forestsimulator/gui");
    private String mdpath = "";  // a destination where an empty access database exists 
    // private String newdbpath=""; // the path and name of the new database;  inherited from DatabaseCreator

    public AccessDatabaseCreator(String metadatapath) {
        mdpath = metadatapath;
        type = 1; //-> 1=ACCESS must be the same as in DBConn inherited static field from basis class DatabaseCreator
    }

    public AccessDatabaseCreator(String metadatapath, String newdatabasepath, boolean create) {
        type = 1; // inherited static field from basis class DatabaseCreator
        mdpath = metadatapath;
        newdbpath = newdatabasepath;
        // direct creation of the mdb within the construcor:
        if (create) {
            createNewDB(newdbpath);
        }
    }

    public void setMetadatapath(String path) {
        mdpath = path;
    }

    @Override
    public void setNewpath(String path) {
        newdbpath = path;
    }

    public String getMetadatapath() {
        return mdpath;
    }

    @Override
    public String getNewpath() {
        return newdbpath;
    }

    @Override
    public boolean showFileSaveDialog() {
        // 1. eine leere Datenbank im gewünschten Verzeichnis erzeugen:
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory()
                        || f.getName().toLowerCase().endsWith(".mdb");
            }

            @Override
            public String getDescription() {
                return messages.getString("AccessDatabaseCreator.save_dialog.filter.description");
            }
        });
        int returnVal = fc.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if (file.exists()) {
                System.out.println("file already exists");
                newdbpath = file.getAbsolutePath();
                return true;
            }
            return createNewDB(file.getAbsolutePath());
        }
        return false;
    }

    @Override
    public final boolean createNewDB(String newpath) {
        if (mdpath.compareTo("") != 0 && newpath.compareTo("") != 0) {
            String newdb = newpath;
            if (newdb.endsWith(".mdb") == false) {
                newdb = newpath + ".mdb";
            }
            copyDB(newdb);
            newdbpath = newdb;
            File file = new File(newdb);
            return file.exists();
        } else {
            return false;
        }
    }

    private void copyFile(String source, String destination) throws Exception {
        byte[] b = new byte[8192];
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(source));
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destination))) {
            while (bis.read(b) != -1) {
                bos.write(b);
            }
        } catch (IOException eio) {
            System.out.println(eio);
        }
    }

    private void copyDB(String newdb) {
        String emptydb = mdpath + "empty.mdb";
        try {
            copyFile(emptydb, newdb);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    messages.getString("AccessDatabaseCreator.copy_database.error.message"),
                    messages.getString("AccessDatabaseCreator.copy_database.error.title"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
