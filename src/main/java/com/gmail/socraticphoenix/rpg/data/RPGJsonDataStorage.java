package com.gmail.socraticphoenix.rpg.data;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.data.character.CharacterData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.persistence.DataFormats;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class RPGJsonDataStorage implements RPGDataStorage {
    private Path dir;
    private Map<UUID, RPGPlayerData> data = new HashMap<>();

    @Override
    public void init() throws IOException {
        this.dir = RPGPlugin.getPlugin().getConfigDir().resolve("playerdata");
        Files.createDirectories(dir);
    }

    @Override
    public Optional<RPGPlayerData> get(UUID player) {
        return Optional.ofNullable(this.data.get(player));
    }

    @Override
    public RPGPlayerData getOrCreate(UUID player, Consumer<RPGPlayerData> consumer) {
        RPGPlayerData data = this.data.get(player);
        if (data == null) {
            data = new RPGPlayerData(player, new PlayerData(), new ArrayList<>());
            data.addInitializationListener(consumer);
            this.data.put(player, data);

            RPGPlayerData finalData = data;
            Sponge.getScheduler().createTaskBuilder().async().execute(() -> {
                try {
                    Path path = this.dir.resolve(player.toString() + ".json");
                    if (!Files.exists(path)) {
                        save(player, finalData);
                    }

                    DataContainer container = DataFormats.JSON.read(new String(Files.readAllBytes(path), StandardCharsets.UTF_8));

                    finalData.setCharacters(container.getSerializableList(RPGData.CHARACTERS, CharacterData.class).get());
                    finalData.setPlayerData(container.getSerializable(RPGData.PLAYER_DATA, PlayerData.class).get());

                    finalData.initialize();
                } catch (IOException e) {
                    RPGPlugin.getPlugin().getLogger().error("Failed to load playerdata for: " + player, e);
                }
            }).submit(RPGPlugin.getPlugin());
        } else {
            consumer.accept(data);
        }

        return data;
    }

    @Override
    public void save(UUID player) {
        RPGPlayerData data = this.data.get(player);

        if (data != null) {
            data = data.copy();

            RPGPlayerData finalData = data;
            Sponge.getScheduler().createTaskBuilder().async().execute(() -> {
                save(player, finalData);
            }).submit(RPGPlugin.getPlugin());
        }
    }

    @Override
    public void saveAndRemove(UUID player) {
        RPGPlayerData data = this.data.get(player);

        if (data != null) {
            data = data.copy();

            RPGPlayerData finalData = data;
            Sponge.getScheduler().createTaskBuilder().async().execute(() -> {
                save(player, finalData);
                remove(player);
            }).submit(RPGPlugin.getPlugin());
        }
    }

    private void save(UUID player, RPGPlayerData data) {
        try {
            DataContainer value = DataContainer.createNew();
            value.set(RPGData.CHARACTERS, data.getCharacters());
            value.set(RPGData.PLAYER_DATA, data.getPlayerData());

            Path path = this.dir.resolve(player.toString() + ".json");
            String content = DataFormats.JSON.write(value);

            Files.write(path, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            RPGPlugin.getPlugin().getLogger().error("Failed to save playerdata for: " + player, e);
        }
    }

    @Override
    public void remove(UUID player) {
        this.data.remove(player);
    }

    @Override
    public void saveAll() {
        this.data.forEach(this::save);
    }

}
