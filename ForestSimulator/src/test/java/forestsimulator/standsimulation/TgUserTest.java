package forestsimulator.standsimulation;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Locale;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class TgUserTest {
    private static final File BASE_DIRECTORY = new File("D:\\some\\base\\directory");
    private static final String LINE_SEP = System.getProperty("line.separator");

    private static final String INI_FILE = "program.directory=user\n"
            + "data.directory=data_standsimulation\n"
            + "working.directory=output_standsimulation\n"
            + "language.code=de\n"
            + "settings.file=ForestSimulatorSettingsBW.xml\n"
            + "climate_data.file=climate_data.mdb\n"
            + "graphics3d=0\n";
    
    @Test
    public void settingsParsedCorrectly() throws IOException {
        TgUser userSettings = new TgUser(BASE_DIRECTORY);
        userSettings.loadSettings(iniContent());
        assertThat(userSettings.getProgramDir()).isEqualTo(canonicalFileOf(BASE_DIRECTORY, "user"));
        assertThat(userSettings.getDataDir()).isEqualTo(canonicalFileOf(BASE_DIRECTORY, "data_standsimulation"));
        assertThat(userSettings.getWorkingDir()).isEqualTo(canonicalFileOf(BASE_DIRECTORY, "output_standsimulation"));
        assertThat(userSettings.getLanguageShort()).isEqualTo(new Locale("de"));
        assertThat(userSettings.getXMLSettings()).isEqualTo("ForestSimulatorSettingsBW.xml");
        assertThat(userSettings.getPlugIn()).isEqualTo("ForestSimulatorSettingsBW.xml");
        assertThat(userSettings.getClimateDatabase()).isEqualTo(canonicalFileOf(BASE_DIRECTORY, "climate_data.mdb"));
        
        assertThat(userSettings.getGrafik3D()).isFalse();
    }
    
    @Test
    public void savingSettingsWorksCorrectly() throws IOException {
        StringWriter buffer = new StringWriter();
        TgUser userSettings = new TgUser(BASE_DIRECTORY);
        userSettings.loadSettings(iniContent());
        userSettings.saveSettingsTo(buffer, "D:\\program_dir", "data_dir", "output_dir", Locale.forLanguageTag("en"), "Settings.xml", "data_dir\\climate.mdb", 1);
        assertThat(buffer.toString()).isEqualTo(
                "program.directory=D:\\program_dir" + LINE_SEP
                + "data.directory=data_dir" + LINE_SEP
                + "working.directory=output_dir" + LINE_SEP
                + "language.code=en" + LINE_SEP
                + "settings.file=Settings.xml" + LINE_SEP
                + "climate_data.file=data_dir\\climate.mdb" + LINE_SEP
                + "graphics3d=1" + LINE_SEP);
    }
    
    @Test
    public void loadingOfAbsoluteWindowsPath() throws IOException {
        TgUser userSettings = new TgUser(BASE_DIRECTORY);
        String absolutePath = makeAbsolute("D:\\some\\\\base");
        userSettings.loadSettings(new StringReader("data.directory=" + absolutePath));
        assertThat(userSettings.getDataDir()).isEqualTo(new File(absolutePath).getCanonicalFile());
    }

    @Test
    public void configuredLanguageCodeUsed() throws IOException {
        TgUser userSettings = new TgUser(BASE_DIRECTORY);
        userSettings.loadSettings(new StringReader("language.code=es"));
        assertThat(userSettings.getLanguageShort()).isEqualTo(new Locale("es"));
    }
    
    @Test
    public void languageIsTakenFromEnvironment() throws IOException {
        System.setProperty("user.language", "fr");
        TgUser userSettings = new TgUser(BASE_DIRECTORY);
        userSettings.loadSettings(new StringReader("language.code="));
        assertThat(userSettings.getLanguageShort()).isEqualTo(new Locale("fr"));
    }
    
    @Test
    public void graphicSettingParsedCorrectly() throws IOException {
        TgUser userSettings = new TgUser(BASE_DIRECTORY);
        userSettings.loadSettings(new StringReader("graphics3d=1"));
        assertThat(userSettings.getGrafik3D()).isTrue();
    }

    private static String makeAbsolute(String absolutePath) {
        if (new File(absolutePath).isAbsolute()) {
            return absolutePath;
        }
        return "/" + absolutePath;
    }
    
    private static File canonicalFileOf(File base, String subdirectory) throws IOException {
        return new File(base, subdirectory).getCanonicalFile();
    }

    private static Reader iniContent() {
        return new StringReader(INI_FILE);
    }
}
