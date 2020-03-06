package com.gmail.socraticphoenix.rpg.data.mob;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.data.character.StatData;
import com.gmail.socraticphoenix.rpg.modifiers.SetModifier;
import com.gmail.socraticphoenix.rpg.modifiers.SortedList;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class MobData extends RPGData<MobData> {
    public static final UUID NONE = new UUID(0, 0);

    private List<SetModifier> modifiers;
    private StatData stats;
    private UUID owner;

    public MobData(List<SetModifier> modifiers, StatData stats, UUID owner) {
        super(0);
        this.modifiers = modifiers;
        this.stats = stats;
        this.owner = owner;
    }

    public MobData() {
        this(new SortedList<>(), new StatData(), NONE);
    }

    public List<SetModifier> getModifiers() {
        return modifiers;
    }

    public StatData getStats() {
        return stats;
    }

    public UUID getOwner() {
        return owner;
    }

    @Override
    public MobData copy() {
        return new MobData(this.modifiers.stream().map(SetModifier::copy).collect(Collectors.toList()), this.stats.copy(), this.owner);
    }

    @Override
    public DataContainer fill(DataContainer container) {
        return container.set(MODIFIERS, this.modifiers)
                .set(OWNER, this.owner)
                .set(STATS, this.stats);
    }

    @Override
    public Optional<MobData> from(DataView container) {
        if (!container.contains(MODIFIERS, STATS, OWNER)) {
            return Optional.empty();
        }

        this.modifiers = new SortedList<>(container.getSerializableList(MODIFIERS, SetModifier.class).get());
        this.stats = container.getSerializable(STATS, StatData.class).get();
        this.owner = container.getObject(OWNER, UUID.class).get();

        if(this.owner.equals(NONE)) {
            this.owner = NONE;
        }

        return Optional.of(this);
    }

    public static class Builder implements DataBuilder<MobData> {

        @Override
        public Optional<MobData> build(DataView container) throws InvalidDataException {
            return new MobData().from(container);
        }

    }
}
