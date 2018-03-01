package forestsimulator.standsimulation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;

public class TgUserTest {
    private static final File BASE_DIRECTORY = new File("D:\\some\\base\\directory");
    private static final String LINE_SEP = System.getProperty("line.separator");

    private static final String INI_FILE = ".\\user\n"
            + ".\\data_standsimulation\n"
            + ".\\output_standsimulation\n"
            + "Deutsch\n"
            + "ForestSimulatorSettingsBW.xml\n"
            + "0\n";
    
    @Test
    public void settingsParsedCorrectly() throws IOException {
        TgUser userSettings = new TgUser(BASE_DIRECTORY);
        userSettings.loadSettings(iniContent());
        assertThat(userSettings.getProgramDir(), is(canonicalFileOf(BASE_DIRECTORY, "user")));
        assertThat(userSettings.getDataDir(), is(canonicalFileOf(BASE_DIRECTORY, "data_standsimulation")));
        assertThat(userSettings.getWorkingDir(), is(canonicalFileOf(BASE_DIRECTORY, "output_standsimulation")));
        assertThat(userSettings.getLanguageShort(), is("de"));
        assertThat(userSettings.getXMLSettings(), is("ForestSimulatorSettingsBW.xml"));
        assertThat(userSettings.getPlugIn(), is("ForestSimulatorSettingsBW.xml"));
        assertThat(userSettings.getGrafik3D(), is(0));
    }
    
    @Test
    public void savingSettingsWorksCorrectly() throws IOException {
        StringWriter buffer = new StringWriter();
        TgUser userSettings = new TgUser(BASE_DIRECTORY);
        userSettings.saveSettingsTo(buffer, "program_dir", "data_dir", "output_dir", "language", "Settings.xml", 1);
        assertThat(buffer.toString(), is(
                "program_dir" + LINE_SEP
                + "data_dir" + LINE_SEP
                + "output_dir" + LINE_SEP
                + "language" + LINE_SEP
                + "Settings.xml" + LINE_SEP
                + "1" + LINE_SEP));
    }
    private static File canonicalFileOf(File base, String subdirectory) throws IOException {
        return new File(base, subdirectory).getCanonicalFile();
    }

    @Test
    public void configuredLanguageCodeUsed() throws IOException {
        TgUser userSettings = new TgUser(BASE_DIRECTORY);
        userSettings.loadSettings(iniContent());
        assertThat(userSettings.getLanguageShort(), is("de"));
    } 

    private static BufferedReader iniContent() {
        return new BufferedReader(new StringReader(INI_FILE));
    }
    
}
