package forestsimulator.language;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public enum UserLanguage {
    GERMAN(Locale.GERMAN),
    ENGLISH(Locale.ENGLISH),
    SPANISH(Locale.forLanguageTag("es")),
    POLISH(Locale.forLanguageTag("pl"));
    
    private final Locale locale;
    
    private UserLanguage(Locale l) {
        locale = l;
    }
    
    public static UserLanguage forLocale(Locale toFind) {
        for (UserLanguage language : values()) {
            if (language.locale.getLanguage().equals(toFind.getLanguage())) {
                return language;
            }
        }
        return ENGLISH;
    }
    
    public static LanguageEntry[] entriesFor(Locale locale) {
        List<LanguageEntry> result = new ArrayList<>();
        for (UserLanguage value : values()) {
            result.add(new LanguageEntry(value, locale));
        }
        return result.toArray(new LanguageEntry[0]);
    }

    public Locale locale() {
        return locale;
    }

    public static class LanguageEntry {

        public final UserLanguage language;
        private final Locale outputLocale;

        public LanguageEntry(UserLanguage language, Locale outputLocale) {
            this.language = language;
            this.outputLocale = outputLocale;
        }

        @Override
        public String toString() {
            return language.locale().getDisplayLanguage(outputLocale);
        }

        @Override
        public int hashCode() {
            return Objects.hash(language, outputLocale);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (!(obj instanceof LanguageEntry)) return false;
            LanguageEntry other = (LanguageEntry) obj;
            return Objects.equals(language, other.language) && Objects.equals(outputLocale, other.outputLocale);
        }
    }
}
