package forestsimulator.standsimulation;

import static forestsimulator.util.NumberFormatters.integerFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.table.TableColumnModel;
import treegross.base.Species;
import treegross.base.thinning.ThinningModeName;

public enum TreatmentTableColumn {
    Species("TreatmentTableColumn.species") {
        @Override
        public Object cellValueOf(Species sp) {
            return sp.spDef.shortName;
        }
    },
    Code("TreatmentTableColumn.code") {
        @Override
        public Object cellValueOf(Species sp) {
            return sp.code;
        }
    },
    ThinningMode("TreatmentTableColumn.thinningMode") {
        @Override
        protected void setCellEditor(TableColumnModel columnModel) {
            columnModel.getColumn(ordinal()).setCellEditor(new DefaultCellEditor(new JComboBox(ThinningModeName.values())));
        }

        @Override
        public Object cellValueOf(Species sp) {
            return sp.thinningSettings().getMode();
        }
    },
    ThinningHeight("TreatmentTableColumn.thinningHeight") {
        @Override
        public Object cellValueOf(Species sp) {
            return integerFormat().format(sp.trule.minCropTreeHeight);
        }
    },
    ThinningType("TreatmentTableColumn.thinningType") {
        @Override
        protected void setCellEditor(TableColumnModel columnModel) {
            columnModel.getColumn(ordinal()).setCellEditor(new DefaultCellEditor(new JComboBox(treegross.base.thinning.ThinningType.values())));
        }
        
        @Override
        public Object cellValueOf(Species sp) {
            // TODO: change this to dynamic thinning type with a thinning definition
            return sp.thinningSettings().type();
        }
        
    },
    ThinningIntensity("TreatmentTableColumn.thinningIntensity") {
        @Override
        public Object cellValueOf(Species sp) {
            return sp.thinningSettings().intensityDefinition();
        }
    },
    TargetD("TreatmentTableColumn.targetD") {
        @Override
        public Object cellValueOf(Species sp) {
            return integerFormat().format(sp.trule.targetDiameter); //To change body of generated methods, choose Tools | Templates.
        }
    },
    CropTrees("TreatmentTableColumn.croptrees") {
        @Override
        public Object cellValueOf(Species sp) {
            long nct = Math.round(sp.trule.numberCropTreesWanted / (sp.trule.targetCrownPercent / 100.0));
            if (nct <= 1) {
                nct = sp.spDef.cropTreeNumber;
            }
            return String.valueOf(nct);
        }
    },
    Mixture("TreatmentTableColumn.mixture") {
        @Override
        public Object cellValueOf(Species sp) {
            return integerFormat().format(sp.trule.targetCrownPercent);
        }
    };

    private static final ResourceBundle bundle = ResourceBundle.getBundle("forestsimulator/gui");

    private final String resourceKey;
     
    private TreatmentTableColumn(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public static void setCellEditors(TableColumnModel columnModel) {
        for (TreatmentTableColumn value : values()) {
            value.setCellEditor(columnModel);
        }
    }
    
    public static List<Object> rowFor(Species sp) {
        List<Object> row = new ArrayList<>();
        for (TreatmentTableColumn value : values()) {
            row.add(value.cellValueOf(sp));
        }
        return row;
    }

    protected abstract Object cellValueOf(Species sp);
 
    @Override
    public String toString() {
        return bundle.getString(resourceKey);
    }    

    protected void setCellEditor(TableColumnModel columnModel) {
        // do nothing by default
    }
}
