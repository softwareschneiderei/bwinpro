package treegross.dynamic.siteindex;

import java.time.Month;


public interface MonthlyValue<T> {
    Month month();
    T value();
}
