package forestsimulator.dbaccess;

import treegross.base.thinning.ScenarioThinningSettings;
import java.util.NoSuchElementException;
import treegross.base.thinning.ThinningType;

public enum ScenarioThinningSettingMode {
    AGE(ThinningModeName.AGE.value()) {
        @Override
        protected ScenarioThinningSettings thinningSetting(ThinningType type, String intensityDefinition) {
            return ScenarioThinningSettings.ageBasedScenarioSetting(type, intensityDefinition);
        }
    },
    HEIGHT(ThinningModeName.HEIGHT.value()) {
        @Override
        protected ScenarioThinningSettings thinningSetting(ThinningType type, String intensityDefinition) {
            return ScenarioThinningSettings.heightBasedScenarioSetting(type, intensityDefinition);
        }
    };
    
    private final String name;

    private ScenarioThinningSettingMode(String name) {
        this.name = name;
    }
    
    public static ScenarioThinningSettings forName(String name, ThinningType type, String intensityDefinition) {
        for (ScenarioThinningSettingMode mode : values()) {
            if (mode.name.equals(name)) {
                return mode.thinningSetting(type, intensityDefinition);
            }
        }
        throw new NoSuchElementException("No thinning setting found for name " + name);
    }

    protected abstract ScenarioThinningSettings thinningSetting(ThinningType type, String intensityDefinition);
    
}
