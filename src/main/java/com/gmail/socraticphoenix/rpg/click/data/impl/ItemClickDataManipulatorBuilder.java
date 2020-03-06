package com.gmail.socraticphoenix.rpg.click.data.impl;

import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.click.ItemClickPredicate;
import com.gmail.socraticphoenix.rpg.click.data.ImmutableItemClickData;
import com.gmail.socraticphoenix.rpg.click.data.ItemClickData;
import com.gmail.socraticphoenix.rpg.registry.registries.ItemClickPredicateRegistry;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public class ItemClickDataManipulatorBuilder implements DataManipulatorBuilder<ItemClickData, ImmutableItemClickData> {
    private RPGPlugin plugin;

    public ItemClickDataManipulatorBuilder(RPGPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public ItemClickData create() {
        return new ItemClickDataImpl(this.plugin.getRegistry().registryFor(ItemClickPredicate.class).get().get(ItemClickPredicateRegistry.ALWAYS_TRUE).get(), 2000);
    }

    @Override
    public Optional<ItemClickData> createFrom(DataHolder dataHolder) {
        return create().fill(dataHolder);
    }

    @Override
    public Optional<ItemClickData> build(DataView container) throws InvalidDataException {
        if (!container.contains(RPGDataKeys.CLICK_INTERVAL, RPGDataKeys.CLICK_PREDICATE)) {
            return Optional.empty();
        }

        return Optional.of(new ItemClickDataImpl(container.getSerializable(RPGDataKeys.CLICK_PREDICATE.getQuery(), ItemClickPredicate.class).get(),
                container.getLong(RPGDataKeys.CLICK_INTERVAL.getQuery()).get()));

    }

}
