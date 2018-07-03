package forestsimulator.dbaccess;

import java.util.NoSuchElementException;
import treegross.base.thinning.AgeBasedThinning;
import treegross.base.thinning.HeightBasedThinning;
import treegross.base.thinning.ThinningType;
import treegross.base.thinning.DynamicThinning;
import treegross.base.thinning.ThinningDefinitions;

public enum ThinningMode {
    AGE("age") {
        @Override
        protected DynamicThinning dynamicThinning(ThinningDefinitions definition) {
            return new AgeBasedThinning(definition);
        }
    },
    HEIGHT("height") {
        @Override
        protected DynamicThinning dynamicThinning(ThinningDefinitions definition) {
            return new HeightBasedThinning(definition);
        }
    };
    
    private final String name;

    private ThinningMode(String name) {
        this.name = name;
    }
    
    public static DynamicThinning forName(String name, ThinningDefinitions definitions) {
        for (ThinningMode mode : values()) {
            if (mode.name.equals(name)) {
                return mode.dynamicThinning(definitions);
            }
        }
        throw new NoSuchElementException("No moderate thinning found for name " + name);
    }

    protected abstract DynamicThinning dynamicThinning(ThinningDefinitions definitions);
    
}
