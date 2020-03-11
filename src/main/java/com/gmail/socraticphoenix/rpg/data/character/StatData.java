package com.gmail.socraticphoenix.rpg.data.character;

import com.gmail.socraticphoenix.rpg.data.RPGData;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public class StatData extends RPGData<StatData> {
    private int level;
    private int xpSinceLevel;

    private int maxHealth;
    private int health;
    private int maxMana;
    private int mana;

    public StatData(int level, int xpSinceLevel, int maxHealth, int health, int maxMana, int mana) {
        super(0);
        this.level = level;
        this.xpSinceLevel = xpSinceLevel;
        this.maxHealth = maxHealth;
        this.health = health;
        this.maxMana = maxMana;
        this.mana = mana;
    }

    public StatData() {
        this(0, 0, 100, 100, 100, 100);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXpSinceLevel() {
        return xpSinceLevel;
    }

    public void setXpSinceLevel(int xpSinceLevel) {
        this.xpSinceLevel = xpSinceLevel;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    @Override
    public StatData copy() {
        return new StatData(this.level, this.xpSinceLevel, maxHealth, health, maxMana, mana);
    }

    @Override
    public DataContainer fill(DataContainer container) {
        return container.set(STATS_XP, this.xpSinceLevel)
                .set(STATS_LEVEL, this.level)
                .set(STATS_MANA, this.mana)
                .set(STATS_MAX_MANA, this.maxMana)
                .set(STATS_HEALTH, this.health)
                .set(STATS_MAX_HEALTH, this.maxHealth);
    }

    @Override
    public Optional<StatData> from(DataView container) {
        if (!container.contains(STATS_XP, STATS_LEVEL, STATS_MANA, STATS_HEALTH,
                STATS_MAX_HEALTH, STATS_MAX_MANA)) {
            return Optional.empty();
        }

        this.level = container.getInt(STATS_LEVEL).get();
        this.xpSinceLevel = container.getInt(STATS_XP).get();
        this.maxHealth = container.getInt(STATS_MAX_HEALTH).get();
        this.health = container.getInt(STATS_HEALTH).get();
        this.maxMana = container.getInt(STATS_MAX_MANA).get();
        this.mana = container.getInt(STATS_MANA).get();

        return Optional.of(this);
    }

    public static class Builder implements DataBuilder<StatData> {

        @Override
        public Optional<StatData> build(DataView container) throws InvalidDataException {
            return new StatData().from(container);
        }

    }

}
