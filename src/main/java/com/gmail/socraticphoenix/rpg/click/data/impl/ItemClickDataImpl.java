package com.gmail.socraticphoenix.rpg.click.data.impl;

import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.click.ItemClickPredicate;
import com.gmail.socraticphoenix.rpg.click.data.ImmutableItemClickData;
import com.gmail.socraticphoenix.rpg.click.data.ItemClickData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

public class ItemClickDataImpl extends AbstractData<ItemClickData, ImmutableItemClickData> implements ItemClickData {
    private ItemClickPredicate predicate;
    private long clickInterval;

    public ItemClickDataImpl(ItemClickPredicate predicate, long clickInterval) {
        this.predicate = predicate;
        this.clickInterval = clickInterval;

        this.registerGettersAndSetters();
    }

    @Override
    public Value<ItemClickPredicate> predicate() {
        return Sponge.getRegistry().getValueFactory()
                .createValue(RPGDataKeys.CLICK_PREDICATE, this.predicate);
    }

    @Override
    public Value<Long> clickInterval() {
        return Sponge.getRegistry().getValueFactory()
                .createValue(RPGDataKeys.CLICK_INTERVAL, this.clickInterval);
    }

    private ItemClickPredicate getPredicate() {
        return predicate;
    }

    private void setPredicate(ItemClickPredicate predicate) {
        this.predicate = predicate;
    }

    private long getClickInterval() {
        return clickInterval;
    }

    private void setClickInterval(long clickInterval) {
        this.clickInterval = clickInterval;
    }

    @Override
    protected void registerGettersAndSetters() {
        registerKeyValue(RPGDataKeys.CLICK_PREDICATE, this::predicate);
        registerKeyValue(RPGDataKeys.CLICK_INTERVAL, this::clickInterval);

        registerFieldGetter(RPGDataKeys.CLICK_PREDICATE, this::getPredicate);
        registerFieldGetter(RPGDataKeys.CLICK_INTERVAL, this::getClickInterval);

        registerFieldSetter(RPGDataKeys.CLICK_PREDICATE, this::setPredicate);
        registerFieldSetter(RPGDataKeys.CLICK_INTERVAL, this::setClickInterval);
    }

    @Override
    public Optional<ItemClickData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<ItemClickData> mergeData = dataHolder.get(ItemClickData.class);

        if (mergeData.isPresent()) {
            ItemClickData merged = overlap.merge(this, mergeData.get());
            this.predicate = merged.predicate().get();
            this.clickInterval = merged.clickInterval().get();
        }

        return Optional.of(this);
    }

    @Override
    public Optional<ItemClickData> from(DataContainer container) {
        if (!container.contains(RPGDataKeys.CLICK_INTERVAL, RPGDataKeys.CLICK_PREDICATE)) {
            return Optional.empty();
        }

        this.predicate = container.getSerializable(RPGDataKeys.CLICK_PREDICATE.getQuery(), ItemClickPredicate.class).get();
        this.clickInterval = container.getLong(RPGDataKeys.CLICK_INTERVAL.getQuery()).get();

        return Optional.of(this);
    }

    @Override
    public DataContainer toContainer() {
        return fillContainer(super.toContainer());
    }

    protected DataContainer fillContainer(DataContainer container) {
        container.set(RPGDataKeys.CLICK_INTERVAL, clickInterval);
        container.set(RPGDataKeys.CLICK_PREDICATE, predicate);

        return container;
    }

    @Override
    public ItemClickData copy() {
        return new ItemClickDataImpl(this.predicate, this.clickInterval);
    }

    @Override
    public ImmutableItemClickData asImmutable() {
        return new ImmutableItemClickDataImpl(this.predicate, this.clickInterval);
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

}
