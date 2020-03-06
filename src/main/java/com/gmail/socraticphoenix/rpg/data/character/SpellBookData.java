package com.gmail.socraticphoenix.rpg.data.character;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.spell.Spell;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class SpellBookData extends RPGData<SpellBookData> {
    Set<Spell> spells;

    public SpellBookData(List<Spell> spells) {
        super(0);
        this.spells = new LinkedHashSet<>();
        this.spells.addAll(spells);
    }

    public SpellBookData() {
        this(new ArrayList<>());
    }

    public SpellBookData(int contentVersion, Set<Spell> spells) {
        super(contentVersion);
        this.spells = spells;
    }

    public Set<Spell> getSpells() {
        return spells;
    }

    public boolean knows(Spell spell) {
        return this.spells.contains(spell);
    }

    @Override
    public SpellBookData copy() {
        return new SpellBookData(Items.looseClone(spells, ArrayList::new));
    }

    @Override
    public DataContainer fill(DataContainer container) {
        return container.set(SPELLS, this.spells);
    }

    @Override
    public Optional<SpellBookData> from(DataView container) {
        if (!container.contains(SPELLS)) {
            return Optional.empty();
        }

        this.spells = new LinkedHashSet<>();
        this.spells.addAll(container.getSerializableList(SPELLS, Spell.class).get());

        return Optional.of(this);
    }

    public static class Builder implements DataBuilder<SpellBookData> {

        @Override
        public Optional<SpellBookData> build(DataView container) throws InvalidDataException {
            return new SpellBookData().from(container);
        }

    }

}
