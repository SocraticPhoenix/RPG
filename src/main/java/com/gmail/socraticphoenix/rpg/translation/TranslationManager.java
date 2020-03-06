package com.gmail.socraticphoenix.rpg.translation;

import com.gmail.socraticphoenix.parse.Strings;
import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.options.Options;
import org.spongepowered.api.entity.living.player.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class TranslationManager {
    private Map<Locale, TranslationMap> languages = new LinkedHashMap<>();
    private RPGPlugin plugin;

    public TranslationManager(RPGPlugin plugin) {
        this.plugin = plugin;
    }

    public Map<Locale, TranslationMap> getLanguages() {
        return languages;
    }

    public TranslationMap getTranslations(Locale locale) {
        return this.languages.getOrDefault(locale, this.languages.get(Locale.US));
    }

    public String getTranslation(Locale locale, String key) {
        return getTranslations(locale).get(key);
    }

    public String translate(Player player, String key) {
        return getTranslation(Locale.forLanguageTag(Options.LANGUAGE.getFor(player)), key);
    }

    public void load(Path directory) throws IOException {
        try {
            Files.list(directory).forEach(p -> {
                Map<String, String> translations = new HashMap<>();
                try {
                    Files.walk(p).forEach(file -> {
                        try {
                            if (!Files.isDirectory(file)) {
                                Files.readAllLines(file).forEach(line -> {
                                    if (line.contains("=") && !line.startsWith("#")) {
                                        String[] pieces = line.split("=", 2);
                                        translations.put(pieces[0], Strings.deEscape(pieces[1]));
                                    }
                                });
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Locale locale = new Locale.Builder().setLanguageTag(translations.get("rpg.lang.tag")).build();
                languages.put(locale, new TranslationMap(locale, translations));
            });
        } catch (RuntimeException e) {
            if (e.getCause() instanceof IOException) {
                throw (IOException) e.getCause();
            } else {
                throw e;
            }
        }
    }

}
