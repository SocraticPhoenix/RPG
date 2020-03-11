package com.gmail.socraticphoenix.rpg.spell;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.augment.AugmentSlot;
import com.gmail.socraticphoenix.rpg.click.ClickType;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.modifiers.SetModifier;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.entity.living.player.Player;

import java.util.*;
import java.util.stream.Collectors;

public class SpellSlot implements DataSerializable {
    private boolean bypass;
    private Type allowed;
    private Spell spell;
    private List<ClickType> sequence;
    private List<SetModifier> modifiers;
    private List<AugmentSlot> augmentSlots;

    public SpellSlot(Spell spell, List<ClickType> sequence, List<SetModifier> modifiers, List<AugmentSlot> augments, Type allowed, boolean bypass) {
        this.spell = spell;
        this.sequence = sequence;
        this.bypass = bypass;
        this.modifiers = modifiers;
        this.augmentSlots = augments;
        this.allowed = allowed;
    }

    public List<AugmentSlot> getAugmentSlots() {
        return augmentSlots;
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

    public boolean isRestricted() {
        return this.getAllowed() != Types.SPELL && this.getAllowed() != Types.TRIGGERED;
    }

    public Type getAllowed() {
        return allowed;
    }

    public Set<Spell> getAllowedSpells(Player player) {
        Set<Spell> spells = new LinkedHashSet<>();

        RPGData.spellbook(player).ifPresent(book -> {
            if (this.isBypass()) {
                RPGPlugin.registryFor(Spell.class).get().elements().forEach(spell -> {
                    if (spell.type().is(this.getAllowed()) &&
                            (!spell.type().is(Types.PASSIVE) || !book.getSpells().contains(spell))) {
                        spells.add(spell);
                    }
                });
            } else {
                book.getSpells().forEach(spell -> {
                    if (spell.type().is(this.getAllowed()) && !spell.type().is(Types.PASSIVE)) {
                        spells.add(spell);
                    }
                });
            }
        });

        return spells;
    }

    public List<SetModifier> getAllMods() {
        List<SetModifier> mods = new ArrayList<>();
        mods.addAll(getModifiers());
        this.augmentSlots.forEach(a -> mods.addAll(a.getAugments()));
        return mods;
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
                .set(RPGData.ALLOWED, this.allowed)
                .set(RPGData.BYPASS, this.bypass)
                .set(RPGData.MODIFIERS, this.modifiers)
                .set(RPGData.AUGMENT_SLOTS, this.augmentSlots)
                .set(RPGData.SEQUENCE, this.sequence.stream().map(ClickType::toString).collect(Collectors.toList()));
    }

    public SpellSlot copy() {
        return new SpellSlot(this.spell, Items.looseClone(this.sequence), this.modifiers.stream().map(SetModifier::copy).collect(Collectors.toList()), this.augmentSlots.stream().map(AugmentSlot::copy).collect(Collectors.toList()), this.allowed, this.bypass);
    }

    public static class Builder implements DataBuilder<SpellSlot> {

        @Override
        public Optional<SpellSlot> build(DataView container) throws InvalidDataException {
            if (!container.contains(RPGData.SPELL, RPGData.AUGMENT_SLOTS, RPGData.SEQUENCE, RPGData.ALLOWED, RPGData.BYPASS, RPGData.MODIFIERS)) {
                return Optional.empty();
            }

            Spell spell = container.getSerializable(RPGData.SPELL, Spell.class).get();
            List<ClickType> types = new ArrayList<>();
            container.getStringList(RPGData.SEQUENCE).get().forEach(s -> types.add(ClickType.valueOf(s)));

            return Optional.of(new SpellSlot(spell, types, container.getSerializableList(RPGData.MODIFIERS, SetModifier.class).get(),
                    container.getSerializableList(RPGData.AUGMENT_SLOTS, AugmentSlot.class).get(), container.getSerializable(RPGData.ALLOWED, Type.class).get(),
                    container.getBoolean(RPGData.BYPASS).get()));
        }

    }

}
