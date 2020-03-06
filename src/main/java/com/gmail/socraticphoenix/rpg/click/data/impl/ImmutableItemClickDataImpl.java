package com.gmail.socraticphoenix.rpg.click.data.impl;

import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.click.ItemClickPredicate;
import com.gmail.socraticphoenix.rpg.click.data.ImmutableItemClickData;
import com.gmail.socraticphoenix.rpg.click.data.ItemClickData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

public class ImmutableItemClickDataImpl extends AbstractImmutableData<ImmutableItemClickData, ItemClickData> implements ImmutableItemClickData {
    private ItemClickPredicate predicate;
    private long clickInterval;

    public ImmutableItemClickDataImpl(ItemClickPredicate predicate, long clickInterval) {
        this.predicate = predicate;
        this.clickInterval = clickInterval;

        this.registerGetters();
    }

    @Override
    public ImmutableValue<ItemClickPredicate> predicate() {
        return Sponge.getRegistry().getValueFactory()
                .createValue(RPGDataKeys.CLICK_PREDICATE, this.predicate).asImmutable();
    }

    @Override
    public ImmutableValue<Long> clickInterval() {
        return Sponge.getRegistry().getValueFactory()
                .createValue(RPGDataKeys.CLICK_INTERVAL, this.clickInterval).asImmutable();
    }

    private ItemClickPredicate getPredicate() {
        return predicate;
    }

    private long getClickInterval() {
        return clickInterval;
    }

    @Override
    protected void registerGetters() {
        registerKeyValue(RPGDataKeys.CLICK_PREDICATE, this::predicate);
        registerKeyValue(RPGDataKeys.CLICK_INTERVAL, this::clickInterval);

        registerFieldGetter(RPGDataKeys.CLICK_PREDICATE, this::getPredicate);
        registerFieldGetter(RPGDataKeys.CLICK_INTERVAL, this::getClickInterval);
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
    public ItemClickData asMutable() {
        return new ItemClickDataImpl(this.predicate, this.clickInterval);
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

}
