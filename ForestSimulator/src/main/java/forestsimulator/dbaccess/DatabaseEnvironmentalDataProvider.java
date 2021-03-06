package forestsimulator.dbaccess;

import java.io.File;
import treegross.dynamic.siteindex.MonthlyValues;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import treegross.base.StandLocation;
import treegross.dynamic.siteindex.AnnualNitrogenDeposition;
import treegross.dynamic.siteindex.EnvironmentVariables;
import treegross.dynamic.siteindex.EnvironmentalDataProvider;
import treegross.dynamic.siteindex.SeasonMeanValues;
import treegross.dynamic.siteindex.MonthlyToSeasonMapper;

public class DatabaseEnvironmentalDataProvider implements EnvironmentalDataProvider {
    private static final Month vegetationStart = Month.MARCH;
    private static final Month vegetationEnd = Month.AUGUST;

    private final File databaseFile;
    private final ConnectionFactory databaseConnector;

    public DatabaseEnvironmentalDataProvider(File databaseFile) {
        super();
        this.databaseFile = databaseFile;
        databaseConnector = new ConnectionFactory();
    }

    @Override
    public EnvironmentVariables environmentalDataFor(StandLocation location, String scenario) {
        Map<Year, List<MonthlyValues>> rawData = new HashMap<>();
        try (
                Connection con = databaseConnector.openDBConnection(databaseFile, "", "");
                PreparedStatement ps = con.prepareStatement(
                        "select * from input_data where federal_state_code = ? and growing_subregion = ? and scenario = ?"
                        + " and month >= ? and month <= ?")) {
            ps.setString(1, location.federalState);
            ps.setString(2, location.growingSubRegion);
            ps.setString(3, scenario);
            ps.setInt(4, vegetationStart.getValue());
            ps.setInt(5, vegetationEnd.getValue());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    readRow(rs, rawData);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseEnvironmentalDataProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        EnvironmentVariables result = new EnvironmentVariables();
        result.addGrowingSeasons(rawData.entrySet().stream().map(DatabaseEnvironmentalDataProvider::mapMonthliesToSeason).collect(toList()));
        return result;
    }

    private void readRow(final ResultSet rs, Map<Year, List<MonthlyValues>> rawData) throws SQLException {
        final Year year = Year.of(rs.getInt("Year"));
        rawData.putIfAbsent(year, new ArrayList<>());
        rawData.get(year).add(new MonthlyValues(
                rs.getDouble("Temperature_mean_degree_C"),
                rs.getDouble("Precipitation_sum_mm"),
                new AnnualNitrogenDeposition(rs.getDouble("NDeposition_annual_sum_kghaa"))));
    }

    private static SeasonMeanValues mapMonthliesToSeason(Map.Entry<Year, List<MonthlyValues>> entry) {
        return new MonthlyToSeasonMapper().mapMonthlies(entry.getKey(), entry.getValue());
    }
}
