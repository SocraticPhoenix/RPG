package com.gmail.socraticphoenix.rpg.translation;

import java.util.Locale;
import java.util.Map;

public class TranslationMap {
    private Locale locale;
    private Map<String, String> translations;

    public TranslationMap(Locale locale, Map<String, String> translations) {
        this.locale = locale;
        this.translations = translations;
    }

    public String get(String key) {
        return this.translations.getOrDefault(key, key);
    }

    public Locale getLocale() {
        return locale;
    }

    public String getName() {
        return translations.getOrDefault("rpg.lang.name", "{language name unset}");
    }

}
