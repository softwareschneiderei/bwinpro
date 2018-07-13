package treegross.dynamic.siteindex;

import java.time.Month;
import java.time.Year;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class EnvironmentVariables {
    private final Map<Year, GrowingSeasonValues> growingSeasons = new LinkedHashMap<>();
    
    /**
     * Average growing season temperature
     * 
     * @param year desired year 
     * @return mean temperature for given year
     */
    // TODO: change double to MeanTemperature type
    public double growingSeasonMeanTemperatureOf(Year year) {
        checkEntryFor(year);
        return growingSeasons.get(year).meanTemperature;
    }

    /**
     * Average growing season precipitation sum
     * 
     * @param year desired year
     * @return precipitationSum in mm for given year
     */
    // TODO: change double to Precipitation type
    public double growingSeasonPrecipitationSumOf(Year year) {
        checkEntryFor(year);
        return growingSeasons.get(year).meanPrecipitationSum;
    }
    
    /**
     * Total annual nitrogen deposition
     * 
     * @param year desired year
     * @return annual nitrogen deposition for given year
     */
    public AnnualNitrogenDeposition nitrogenDepositionOf(Year year) {
        checkEntryFor(year);
        return growingSeasons.get(year).nitrogenDeposition;
    }

    public double aridityIndexOf(Year year) {
        return Month.values().length * growingSeasonPrecipitationSumOf(year) / (growingSeasonMeanTemperatureOf(year) + 10);
    }

    public void addGrowingSeasons(Iterable<GrowingSeasonValues> growingSeasons) {
        growingSeasons.forEach((seasonValues) -> {
            this.growingSeasons.put(seasonValues.year, seasonValues);
        });
    }

    private void checkEntryFor(Year year) throws NoSuchElementException {
        if (!growingSeasons.containsKey(year)) {
            throw new NoSuchElementException("No values for year " + year + " found.");
        }
    }
}
