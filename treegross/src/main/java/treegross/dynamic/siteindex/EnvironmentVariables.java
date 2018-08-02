package treegross.dynamic.siteindex;

import java.time.Month;
import java.time.Year;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import treegross.util.MeanCalculator;
import treegross.util.SlidingMeanCalculator;

public class EnvironmentVariables implements Iterable<GrowingSeasonValues> {
    private final Map<Year, GrowingSeasonValues> growingSeasons = new TreeMap<>();

    public EnvironmentVariables() {
        super();
    }

    protected EnvironmentVariables(EnvironmentVariables environmentVariables) {
        super();
        growingSeasons.putAll(environmentVariables.growingSeasons);
    }
    
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

    // TODO: change double to aridity index type
    public double aridityIndexOf(Year year) {
        return Month.values().length * growingSeasonPrecipitationSumOf(year) / (growingSeasonMeanTemperatureOf(year) + 10);
    }
    
    public void addGrowingSeason(GrowingSeasonValues growingSeason) {
        addGrowingSeasons(Arrays.asList(growingSeason));
    }

    public void addGrowingSeasons(Iterable<GrowingSeasonValues> growingSeasons) {
        growingSeasons.forEach((seasonValues) -> {
            this.growingSeasons.put(seasonValues.year, seasonValues);
        });
    }
    
    public EnvironmentVariables calculate5YearMeans() {
        final SlidingMeanCalculator<GrowingSeasonValues> slidingMeanCalculator = new SlidingMeanCalculator<>(5);
        if (!growingSeasons.isEmpty()) {
            slidingMeanCalculator.fillCalculatorWindow(iterator().next());
        }
        return calculateMeanWith(slidingMeanCalculator);
    }

    public EnvironmentVariables calculateWeighted5YearMeans() {
        return calculateMeanWith(new Weighted5YearMeanCalculator<>());
    }

    private EnvironmentVariables calculateMeanWith(MeanCalculator<GrowingSeasonValues> window) {
        EnvironmentVariables result = new EnvironmentVariables();
        forEach(growingSeason -> {
            window.add(growingSeason);
            result.addGrowingSeason(new GrowingSeasonValues(
                    growingSeason.year,
                    window.meanOf(season -> season.meanTemperature),
                    window.meanOf(season -> season.meanPrecipitationSum),
                    new AnnualNitrogenDeposition(window.meanOf(season -> season.nitrogenDeposition.value))));
        });
        return result;
    }

    private void checkEntryFor(Year year) throws NoSuchElementException {
        if (!growingSeasons.containsKey(year)) {
            throw new NoSuchElementException("No values for year " + year + " found.");
        }
    }

    @Override
    public Iterator<GrowingSeasonValues> iterator() {
        return growingSeasons.values().iterator();
    }

    public boolean hasDataFor(Year year) {
        return growingSeasons.containsKey(year);
    }

    /**
     * 
     * @param startInclusive start year of period inclusive
     * @param endInclusive end year of period inclusive
     * @return false if there is environmental data for all year in the given period
     */
    public boolean dataMissingFor(Year startInclusive, Year endInclusive) {
        try {
            for (Year current = startInclusive; current.isBefore(endInclusive) || current.equals(endInclusive); current = current.plusYears(1)) {
                checkEntryFor(current);
            }
            return false;
        } catch (NoSuchElementException e) {
            return true;
        }
    }
}
