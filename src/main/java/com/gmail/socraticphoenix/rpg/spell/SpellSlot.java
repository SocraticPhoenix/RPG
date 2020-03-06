package com.gmail.socraticphoenix.rpg.spell;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.click.ClickType;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.modifiers.SetModifier;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SpellSlot implements DataSerializable {
    private boolean locked;
    private boolean bypass;
    private Spell spell;
    private List<ClickType> sequence;
    private List<SetModifier> modifiers;

    public SpellSlot(Spell spell, List<ClickType> sequence, List<SetModifier> modifiers, boolean locked, boolean bypass) {
        this.spell = spell;
        this.sequence = sequence;
        this.locked = locked;
        this.bypass = bypass;
        this.modifiers = modifiers;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isBypass() {
        return bypass;
    }

    public Spell getSpell() {
        return spell;
    }

    public List<ClickType> getSequence() {
        return sequence;
    }

    public List<SetModifier> getModifiers() {
        return modifiers;
    }

    public boolean hasModifiers() {
        return !getModifiers().isEmpty();
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew().set(RPGData.SPELL, spell)
                .set(RPGData.LOCKED, this.locked)
                .set(RPGData.BYPASS, this.bypass)
                .set(RPGData.MODIFIERS, this.modifiers)
                .set(RPGData.SEQUENCE, this.sequence.stream().map(ClickType::toString).collect(Collectors.toList()));
    }

    public SpellSlot copy() {
        return new SpellSlot(this.spell, Items.looseClone(this.sequence), Items.looseClone(this.modifiers), this.locked, this.bypass);
    }

    public static class Builder implements DataBuilder<SpellSlot> {

        @Override
        public Optional<SpellSlot> build(DataView container) throws InvalidDataException {
            if (!container.contains(RPGData.SPELL, RPGData.SEQUENCE, RPGData.LOCKED, RPGData.BYPASS, RPGData.MODIFIERS)) {
                return Optional.empty();
            }

            Spell spell = container.getSerializable(RPGData.SPELL, Spell.class).get();
            List<ClickType> types = new ArrayList<>();
            container.getStringList(RPGData.SEQUENCE).get().forEach(s -> types.add(ClickType.valueOf(s)));

            return Optional.of(new SpellSlot(spell, types, container.getSerializableList(RPGData.MODIFIERS, SetModifier.class).get(), container.getBoolean(RPGData.LOCKED).get(), container.getBoolean(RPGData.BYPASS).get()));
        }

    }

}
