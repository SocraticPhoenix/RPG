package com.gmail.socraticphoenix.rpg.data.item;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.augment.AugmentColor;
import com.gmail.socraticphoenix.rpg.augment.AugmentSlot;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.modifiers.SetModifier;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ItemData extends RPGData<ItemData> {
    public static final UUID NONE = new UUID(0, 0);

    private List<SetModifier> modifiers;
    private AugmentSlot augment;
    private List<AugmentSlot> augmentSlots;

    private UUID owner;

    public ItemData(List<SetModifier> modifiers, AugmentSlot augment, List<AugmentSlot> augmentSlots, UUID owner) {
        super(0);
        this.modifiers = modifiers;
        this.owner = owner;
        this.augmentSlots = augmentSlots;;
        this.augment = augment;
    }

    public ItemData() {
        this(new ArrayList<>(), new AugmentSlot(AugmentColor.NONE, new ArrayList<>()), new ArrayList<>(), NONE);
    }

    public List<AugmentSlot> getAugmentSlots() {
        return augmentSlots;
    }

    public AugmentSlot getAugment() {
        return augment;
    }

    public UUID getOwner() {
        return owner;
    }

    public List<SetModifier> getModifiers() {
        return modifiers;
    }

    @Override
    public ItemData copy() {
        return new ItemData(this.modifiers.stream().map(SetModifier::copy).collect(Collectors.toList()), this.augment.copy(), this.augmentSlots.stream().map(AugmentSlot::copy).collect(Collectors.toList()), this.owner);
    }

    @Override
    public DataContainer fill(DataContainer container) {
        return container.set(MODIFIERS, this.modifiers)
                .set(AUGMENT_SLOTS, this.augmentSlots)
                .set(AUGMENTS, this.augment)
                .set(OWNER, this.owner);
    }

    @Override
    public Optional<ItemData> from(DataView container) {
        if (!container.contains(MODIFIERS, OWNER, AUGMENT_SLOTS, AUGMENTS)) {
            return Optional.empty();
        }

        this.modifiers = container.getSerializableList(MODIFIERS, SetModifier.class).get();
        this.owner = container.getObject(OWNER, UUID.class).get();
        this.augment = container.getSerializable(AUGMENTS, AugmentSlot.class).get();
        this.augmentSlots = container.getSerializableList(AUGMENT_SLOTS, AugmentSlot.class).get();

        if(this.owner.equals(NONE)) {
            this.owner = NONE;
        }

        return Optional.of(this);
    }

    public static class Builder implements DataBuilder<ItemData> {

        @Override
        public Optional<ItemData> build(DataView container) throws InvalidDataException {
            return new ItemData().from(container);
        }

    }

}
