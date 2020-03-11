package com.gmail.socraticphoenix.rpg.translation;

import com.gmail.socraticphoenix.parse.Strings;
import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.options.Options;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
            Path mappings = directory.resolve("langs");
            Path zip = directory.resolve("translations.zip");
            Files.createDirectories(mappings);

            RPGPlugin.getPlugin().getContainer().getAsset("translations.zip").get().copyToFile(zip, true);
            unzip(zip, mappings);

            Files.list(mappings).forEach(p -> {
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

    private static void unzip(Path zip, Path dest) throws IOException {
        byte[] buffer = new byte[1024];
        ZipInputStream stream = new ZipInputStream(new FileInputStream(zip.toFile()));

        ZipEntry ze = stream.getNextEntry();
        while (ze != null) {
            Path entry = dest.resolve(ze.getName());

            if (ze.isDirectory()) {
                if (!Files.exists(entry)) {
                    Files.createDirectories(entry);
                }
            } else {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int len;
                while ((len = stream.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                out.close();

                Files.write(entry, out.toByteArray(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            }

            stream.closeEntry();
            ze = stream.getNextEntry();
        }
        stream.closeEntry();
        stream.close();
    }

}
