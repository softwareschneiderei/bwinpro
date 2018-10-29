package treegross.dynamic.siteindex;

import java.util.stream.StreamSupport;
import treegross.util.SlidingMeanCalculator;

public class LongtermEnvironmentVariables {

    private final double meanTemperature;
    private final double precipitationSum;
    private final double aridityIndex;
    private final double nitrogenDepositionValue;
    
    public LongtermEnvironmentVariables(EnvironmentVariables rawValues) {
        super();
        final SlidingMeanCalculator<SeasonMeanValues> slidingMeanCalculator = new SlidingMeanCalculator<>(5);
        if (rawValues.iterator().hasNext()) {
            slidingMeanCalculator.fillCalculatorWindow(rawValues.iterator().next());
        }
        StreamSupport.stream(rawValues.spliterator(), false).limit(slidingMeanCalculator.windowSize()).forEachOrdered(mean -> {
            slidingMeanCalculator.add(mean);
        });
        meanTemperature = slidingMeanCalculator.meanOf(season -> season.meanTemperature);
        precipitationSum = slidingMeanCalculator.meanOf(season -> season.meanPrecipitationSum);
        aridityIndex = MonthlyToSeasonMapper.calculateAridityIndexOf(meanTemperature, precipitationSum);
        nitrogenDepositionValue = slidingMeanCalculator.meanOf(season -> season.nitrogenDeposition.value);
    }

    public double growingSeasonMeanTemperature() {
        return 0.9859 * meanTemperature - 0.8488;
    }

    public double growingSeasonPrecipitationSum() {
        return 1.0799 * precipitationSum - 10.691;
    }

    public AnnualNitrogenDeposition nitrogenDeposition() {
        return new AnnualNitrogenDeposition(0.8947 * nitrogenDepositionValue - 2);
    }

    public double aridityIndex() {
        return 1.1187 * aridityIndex - 5.7999;
    }
}
