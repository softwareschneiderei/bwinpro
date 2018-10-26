package treegross.dynamic.siteindex;

import java.time.Year;
import java.util.Arrays;
import java.util.List;

public class MonthlyToSeasonMapper {
    
    public SeasonMeanValues mapMonthlies(Year year, MonthlyValues... monthlyValues) {
        return mapMonthlies(year, Arrays.asList(monthlyValues));
    }

    public SeasonMeanValues mapMonthlies(Year year, List<MonthlyValues> monthlyValues) {
        double meanTemperature = monthlyValues.stream().mapToDouble((MonthlyValues values) -> values.temperatureMean).sum() / monthlyValues.size();
        double meanPrecipitation = monthlyValues.stream().mapToDouble((MonthlyValues values) -> values.precipitationSum).sum() / monthlyValues.size();
        return new SeasonMeanValues(year, meanTemperature, meanPrecipitation, monthlyValues.get(0).nitrogenDeposition);
    }
    
}
