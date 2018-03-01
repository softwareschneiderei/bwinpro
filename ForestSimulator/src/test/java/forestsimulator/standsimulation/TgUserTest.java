package forestsimulator.standsimulation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;

public class TgUserTest {
    private static final File WORKING_DIRECTORY = new File("D:\\some\\base\\directory");
    private static final String INI_FILE = ".\\user\n"
            + ".\\data_standsimulation\n"
            + ".\\output_standsimulation\n"
            + "Deutsch\n"
            + "ForestSimulatorSettingsBW.xml\n"
            + "0\n";
    
    @Test
    public void settingsParsedCorrectly() throws IOException {
        TgUser userSettings = new TgUser();
        userSettings.loadSettings(iniContent(), WORKING_DIRECTORY);
        assertThat(userSettings.getProgramDir(), is(canonicalFileOf(WORKING_DIRECTORY, "user")));
        assertThat(userSettings.getDataDir(), is(canonicalFileOf(WORKING_DIRECTORY, "data_standsimulation")));
        assertThat(userSettings.getWorkingDir(), is(canonicalFileOf(WORKING_DIRECTORY, "output_standsimulation")));
        assertThat(userSettings.getLanguageShort(), is("de"));
        assertThat(userSettings.getXMLSettings(), is("ForestSimulatorSettingsBW.xml"));
        assertThat(userSettings.getPlugIn(), is("ForestSimulatorSettingsBW.xml"));
        assertThat(userSettings.getGrafik3D(), is(0));
    }

    private static File canonicalFileOf(File base, String subdirectory) throws IOException {
        return new File(base, subdirectory).getCanonicalFile();
    }

    @Test
    public void configuredLanguageCodeUsed() throws IOException {
        TgUser userSettings = new TgUser();
        userSettings.loadSettings(iniContent(), WORKING_DIRECTORY);
        assertThat(userSettings.getLanguageShort(), is("de"));
    } 

    private static BufferedReader iniContent() {
        return new BufferedReader(new StringReader(INI_FILE));
    }
    
}
