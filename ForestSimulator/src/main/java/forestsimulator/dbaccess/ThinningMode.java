package forestsimulator.dbaccess;

import java.util.NoSuchElementException;
import treegross.base.thinning.AgeBasedThinning;
import treegross.base.thinning.HeightBasedThinning;
import treegross.base.thinning.ModerateThinning;

public enum ThinningMode {
    AGE("age") {
        @Override
        protected ModerateThinning moderateThinning(String definition) {
            return new AgeBasedThinning(definition);
        }
    },
    HEIGHT("height") {
        @Override
        protected ModerateThinning moderateThinning(String definition) {
            return new HeightBasedThinning(definition);
        }
    };
    
    private final String name;

    private ThinningMode(String name) {
        this.name = name;
    }
    
    public static ModerateThinning forName(String name, String definition) {
        for (ThinningMode mode : values()) {
            if (mode.name.equals(name)) {
                return mode.moderateThinning(definition);
            }
        }
        throw new NoSuchElementException("No moderate thinning found for name " + name);
    }

    protected abstract ModerateThinning moderateThinning(String definition);
}
