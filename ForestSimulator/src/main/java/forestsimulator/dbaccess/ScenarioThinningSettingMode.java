package forestsimulator.dbaccess;

import treegross.base.thinning.ThinningModeName;
import treegross.base.thinning.SpeciesThinningSettings;
import java.util.NoSuchElementException;

public enum ScenarioThinningSettingMode {
    AGE(ThinningModeName.AGE) {
        @Override
        protected SpeciesThinningSettings thinningSetting(String typeDefinition, String intensityDefinition) {
            return SpeciesThinningSettings.ageBasedScenarioSetting(typeDefinition, intensityDefinition);
        }
    },
    HEIGHT(ThinningModeName.HEIGHT) {
        @Override
        protected SpeciesThinningSettings thinningSetting(String typeDefinition, String intensityDefinition) {
            return SpeciesThinningSettings.heightBasedScenarioSetting(typeDefinition, intensityDefinition);
        }
    };
    
    private final ThinningModeName name;

    private ScenarioThinningSettingMode(ThinningModeName name) { //NOSONAR, false positive, enum uses this constructor!
        this.name = name;
    }
    
    public static SpeciesThinningSettings forName(ThinningModeName name, String type, String intensityDefinition) {
        for (ScenarioThinningSettingMode mode : values()) {
            if (mode.name == name) {
                return mode.thinningSetting(type, intensityDefinition);
            }
        }
        throw new NoSuchElementException("No thinning setting found for name " + name);
    }

    protected abstract SpeciesThinningSettings thinningSetting(String typeDefinition, String intensityDefinition);
    
}
