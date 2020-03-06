package com.gmail.socraticphoenix.rpg.data.item;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.spell.SpellSlot;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WandData extends RPGData<WandData> {
    private List<SpellSlot> slots;
    private boolean omniwand;

    public WandData() {
        this(new ArrayList<>(), false);
    }

    public WandData(List<SpellSlot> slots, boolean omniwand) {
        super(0);
        this.slots = slots;
        this.omniwand = omniwand;
    }

    public List<SpellSlot> getSlots() {
        return slots;
    }

    public void setSlots(List<SpellSlot> slots) {
        this.slots = slots;
    }

    public boolean isOmniwand() {
        return omniwand;
    }

    public void setOmniwand(boolean omniwand) {
        this.omniwand = omniwand;
    }

    @Override
    public WandData copy() {
        return new WandData(Items.looseClone(this.slots), this.omniwand);
    }

    @Override
    public DataContainer fill(DataContainer container) {
        return container
                .set(OMNIWAND, this.omniwand)
                .set(SPELL_SLOTS, this.slots);
    }

    @Override
    public Optional<WandData> from(DataView container) {
        if (!container.contains(OMNIWAND, SPELL_SLOTS)) {
            return Optional.empty();
        }

        this.omniwand = container.getBoolean(OMNIWAND).get();
        this.slots = container.getSerializableList(SPELL_SLOTS, SpellSlot.class).get();

        return Optional.of(this);
    }

    public static class Builder implements DataBuilder<WandData> {

        @Override
        public Optional<WandData> build(DataView container) throws InvalidDataException {
            return new WandData().from(container);
        }

    }

}
