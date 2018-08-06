package forestsimulator.dbaccess;

import treegross.base.thinning.ThinningModeName;
import java.util.NoSuchElementException;
import treegross.base.thinning.AgeBasedThinning;
import treegross.base.thinning.HeightBasedThinning;
import treegross.base.thinning.ModerateThinning;

public enum ModerateThinningMode {
    AGE(ThinningModeName.AGE.value()) {
        @Override
        protected ModerateThinning moderateThinning(String definition) {
            return new AgeBasedThinning(definition);
        }
    },
    HEIGHT(ThinningModeName.HEIGHT.value()) {
        @Override
        protected ModerateThinning moderateThinning(String definition) {
            return new HeightBasedThinning(definition);
        }
    };
    
    private final String name;

    private ModerateThinningMode(String name) {
        this.name = name;
    }
    
    public static ModerateThinning forName(String name, String definition) {
        for (ModerateThinningMode mode : values()) {
            if (mode.name.equals(name)) {
                return mode.moderateThinning(definition);
            }
        }
        throw new NoSuchElementException("No moderate thinning found for name " + name);
    }

    protected abstract ModerateThinning moderateThinning(String definition);
}
