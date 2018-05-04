package forestsimulator.standsimulation;

import java.util.ResourceBundle;

public enum StandInfoTableColumn {
    Species("StandInfoTableColumn.sp"),
    Ly("StandInfoTableColumn.ly"),
    Age("StandInfoTableColumn.Age"),
    Dg("StandInfoTableColumn.Dg"),
    Hg("StandInfoTableColumn.Hg"),
    D100("StandInfoTableColumn.D100"),
    H100("StandInfoTableColumn.H100"),
    NHA("StandInfoTableColumn.nha"),
    GHA("StandInfoTableColumn.gha"),
    VHA("StandInfoTableColumn.vha"),
    Noutha("StandInfoTableColumn.noutha"),
    Goutha("StandInfoTableColumn.goutha"),
    Voutha("StandInfoTableColumn.voutha"),
    Mix("StandInfoTableColumn.mix");
    
    private static final ResourceBundle bundle = ResourceBundle.getBundle("forestsimulator/gui");
    private final String resourceKey;
     
    private StandInfoTableColumn(String resourceKey) {
        this.resourceKey = resourceKey;
    }
 
    @Override
    public String toString() {
        return bundle.getString(resourceKey);
    }    
}
