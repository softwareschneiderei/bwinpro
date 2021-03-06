package forestsimulator.standsimulation;

import forestsimulator.gui.ComboBoxTableCellRenderer;
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
        protected Object cellValueOf(Species sp) {
            return sp.spDef.shortName;
        }
    },
    Code("TreatmentTableColumn.code") {
        @Override
        protected Object cellValueOf(Species sp) {
            return sp.code;
        }
    },
    ThinningMode("TreatmentTableColumn.thinningMode") {
        @Override
        protected void setCellEditor(TableColumnModel columnModel) {
            columnModel.getColumn(ordinal()).setCellEditor(new DefaultCellEditor(new JComboBox(ThinningModeName.values())));
        }

        @Override
        protected void setCellRenderer(TableColumnModel columnModel) {
            columnModel.getColumn(ordinal()).setCellRenderer(new ComboBoxTableCellRenderer());
        }

        @Override
        protected Object cellValueOf(Species sp) {
            return sp.thinningSettings().getMode();
        }
    },
    ThinningType("TreatmentTableColumn.thinningType") {
        @Override
        protected Object cellValueOf(Species sp) {
            return sp.thinningSettings().typeDefinition();
        }
    },
    ThinningIntensity("TreatmentTableColumn.thinningIntensity") {
        @Override
        protected Object cellValueOf(Species sp) {
            return sp.thinningSettings().intensityDefinition();
        }
    },
    ThinningHeight("TreatmentTableColumn.thinningHeight") {
        @Override
        protected Object cellValueOf(Species sp) {
            return integerFormat().format(sp.trule.minCropTreeHeight);
        }
    },
    TargetD("TreatmentTableColumn.targetD") {
        @Override
        protected Object cellValueOf(Species sp) {
            return integerFormat().format(sp.trule.targetDiameter);
        }
    },
    CropTrees("TreatmentTableColumn.croptrees") {
        @Override
        protected Object cellValueOf(Species sp) {
            long nct = Math.round(sp.trule.numberCropTreesWanted / (sp.trule.targetCrownPercent / 100.0));
            if (nct <= 1) {
                nct = sp.spDef.cropTreeNumber;
            }
            return String.valueOf(nct);
        }
    },
    CompetitorCount("TreatmentTableColumn.competitorsCount") {
        @Override
        protected Object cellValueOf(Species sp) {
            return sp.trule.competitorTakeOutDefinition;
        }
    },
    Mixture("TreatmentTableColumn.mixture") {
        @Override
        protected Object cellValueOf(Species sp) {
            return integerFormat().format(sp.trule.targetCrownPercent);
        }
    };

    private static final ResourceBundle bundle = ResourceBundle.getBundle("forestsimulator/gui");

    private final String resourceKey;
     
    private TreatmentTableColumn(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public static void applyCellRendering(TableColumnModel columnModel) {
        for (TreatmentTableColumn value : values()) {
            value.setCellEditor(columnModel);
            value.setCellRenderer(columnModel);
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

    protected void setCellRenderer(TableColumnModel columnModel) {
        // do nothing by default
    }
}
