package treegross.dynamic.siteindex;

import java.time.Month;
import java.time.Year;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
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
        EnvironmentVariables result = new EnvironmentVariables();
        SlidingMeanCalculator<GrowingSeasonValues> window = new SlidingMeanCalculator<>(5);
        fillMeanCalculatorWindow(window);
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

    // TODO: refactor DataAccumulation to something more flexible like the SlidingMeanCalculator<E>
    public EnvironmentVariables calculateWeighted5YearMeans() {
        EnvironmentVariables result = new EnvironmentVariables();
        DataAccumulation temperatureMean = new DataAccumulation();
        DataAccumulation precipitationMean = new DataAccumulation();
        DataAccumulation nitrogenDepositionMean = new DataAccumulation();
        forEach(growingSeason -> {
            double weightedMeanTemperature = temperatureMean.add(growingSeason.meanTemperature).weighted5YearMean();
            double weightedMeanPrecipitation = precipitationMean.add(growingSeason.meanPrecipitationSum).weighted5YearMean();
            AnnualNitrogenDeposition weightedMeanNitrogen = new AnnualNitrogenDeposition(nitrogenDepositionMean.add(growingSeason.nitrogenDeposition.value).weighted5YearMean());
            result.addGrowingSeason(new GrowingSeasonValues(
                    growingSeason.year,
                    weightedMeanTemperature,
                    weightedMeanPrecipitation,
                    weightedMeanNitrogen));
        });
        return result;
    }

    private void fillMeanCalculatorWindow(SlidingMeanCalculator<GrowingSeasonValues> window) {
        for (int i = 0; i < window.windowSize(); i++) {
            window.add(this.iterator().next());
        }
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
}
