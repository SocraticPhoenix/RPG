package com.gmail.socraticphoenix.rpg.data.character;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.gods.God;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.List;
import java.util.Optional;

public class CharacterData extends RPGData<CharacterData> {
    private boolean active;

    private String firstName;
    private String lastName;
    private boolean ascended;
    private List<God> gods;
    private InventoryData inventory;
    private SpellBookData spellBook;
    private StatData stats;
    private OptionData options;
    private CooldownData cooldowns;

    private RuntimeData runtime;

    public CharacterData(boolean active, String firstName, String lastName, boolean ascended, List<God> gods, InventoryData inventory, SpellBookData spellBook, StatData stats, OptionData options, CooldownData cooldowns, RuntimeData data) {
        super(0);
        this.active = active;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ascended = ascended;
        this.gods = gods;
        this.inventory = inventory;
        this.spellBook = spellBook;
        this.stats = stats;
        this.options = options;
        this.cooldowns = cooldowns;
        this.runtime = data;
    }

    public CharacterData() {
        this(false, "Jon", "Doe", false, Items.buildList(), new InventoryData(), new SpellBookData(), new StatData(), new OptionData(), new CooldownData(), new RuntimeData());
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public void setName(String first, String last) {
        setFirstName(first);
        setLastName(last);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isAscended() {
        return ascended;
    }

    public List<God> getGods() {
        return gods;
    }

    public InventoryData getInventory() {
        return inventory;
    }

    public SpellBookData getSpellBook() {
        return spellBook;
    }

    public StatData getStats() {
        return stats;
    }

    public OptionData getOptions() {
        return options;
    }

    public CooldownData getCooldowns() {
        return cooldowns;
    }

    public void setAscended(boolean ascended) {
        this.ascended = ascended;
    }

    public RuntimeData getRuntime() {
        return runtime;
    }

    @Override
    public CharacterData copy() {
        return new CharacterData(this.active, this.firstName, this.lastName, this.ascended, Items.looseClone(this.gods), this.inventory.copy(), this.spellBook.copy(), this.stats.copy(), this.options.copy(), this.cooldowns.copy(), this.runtime.copy());
    }

    @Override
    public DataContainer fill(DataContainer container) {
        return container.set(ACTIVE, active)
                .set(FIRST_NAME, firstName)
                .set(LAST_NAME, lastName)
                .set(ASCENDED, ascended)
                .set(GODS, gods)
                .set(INVENTORY, inventory)
                .set(SPELL_BOOK, spellBook)
                .set(STATS, stats)
                .set(OPTIONS, options)
                .set(COOLDOWNS, cooldowns);
    }

    @Override
    public Optional<CharacterData> from(DataView container) {
        if (!container.contains(ACTIVE, FIRST_NAME, LAST_NAME, ASCENDED, GODS, INVENTORY, SPELL_BOOK, STATS, OPTIONS, COOLDOWNS)) {
            return Optional.empty();
        }

        this.active = container.getBoolean(ACTIVE).get();
        this.firstName = container.getString(FIRST_NAME).get();
        this.lastName = container.getString(LAST_NAME).get();
        this.ascended = container.getBoolean(ASCENDED).get();
        this.gods = container.getSerializableList(GODS, God.class).get();
        this.inventory = container.getSerializable(INVENTORY, InventoryData.class).get();
        this.spellBook = container.getSerializable(SPELL_BOOK, SpellBookData.class).get();
        this.stats = container.getSerializable(STATS, StatData.class).get();
        this.options = container.getSerializable(OPTIONS, OptionData.class).get();
        this.cooldowns = container.getSerializable(COOLDOWNS, CooldownData.class).get();

        return Optional.of(this);
    }

    public static class Builder implements DataBuilder<CharacterData> {

        @Override
        public Optional<CharacterData> build(DataView container) throws InvalidDataException {
            return new CharacterData().from(container);
        }

    }

}
