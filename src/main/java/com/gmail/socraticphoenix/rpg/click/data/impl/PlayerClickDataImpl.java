package com.gmail.socraticphoenix.rpg.click.data.impl;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.click.ClickType;
import com.gmail.socraticphoenix.rpg.click.data.ImmutablePlayerClickData;
import com.gmail.socraticphoenix.rpg.click.data.PlayerClickData;
import com.google.common.collect.ImmutableList;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.ListValue;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlayerClickDataImpl extends AbstractData<PlayerClickData, ImmutablePlayerClickData> implements PlayerClickData {
    private List<ClickType> sequence;
    private long lastClick;

    public PlayerClickDataImpl(List<ClickType> sequence, long lastClick) {
        this.sequence = sequence;
        this.lastClick = lastClick;

        this.registerGettersAndSetters();
    }

    public Value<Long> lastClicked() {
        return Sponge.getRegistry().getValueFactory()
                .createValue(RPGDataKeys.LAST_CLICKED, this.lastClick);
    }

    public ListValue<ClickType> sequence() {
        return Sponge.getRegistry().getValueFactory()
                .createListValue(RPGDataKeys.CLICK_SEQUENCE, this.sequence);
    }

    private List<ClickType> getSequence() {
        return sequence;
    }

    private void setSequence(List<ClickType> sequence) {
        this.sequence = sequence;
    }

    private long getLastClick() {
        return lastClick;
    }

    private void setLastClick(long lastClick) {
        this.lastClick = lastClick;
    }

    @Override
    protected void registerGettersAndSetters() {
        registerKeyValue(RPGDataKeys.LAST_CLICKED, this::lastClicked);
        registerKeyValue(RPGDataKeys.CLICK_SEQUENCE, this::sequence);

        registerFieldGetter(RPGDataKeys.LAST_CLICKED, this::getLastClick);
        registerFieldGetter(RPGDataKeys.CLICK_SEQUENCE, this::getSequence);

        registerFieldSetter(RPGDataKeys.LAST_CLICKED, this::setLastClick);
        registerFieldSetter(RPGDataKeys.CLICK_SEQUENCE, this::setSequence);
    }

    @Override
    public Optional<PlayerClickData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<PlayerClickData> mergeData = dataHolder.get(PlayerClickData.class);

        if (mergeData.isPresent()) {
            PlayerClickData merged = overlap.merge(this, mergeData.get());
            this.lastClick = merged.lastClicked().get();
            this.sequence = merged.sequence().get();
        }

        return Optional.of(this);
    }

    @Override
    public Optional<PlayerClickData> from(DataContainer container) {
        if (!container.contains(RPGDataKeys.LAST_CLICKED, RPGDataKeys.CLICK_SEQUENCE)) {
            return Optional.empty();
        }

        this.lastClick = container.getLong(RPGDataKeys.LAST_CLICKED.getQuery()).get();
        this.sequence = container.getStringList(RPGDataKeys.CLICK_SEQUENCE.getQuery()).get().stream().map(ClickType::valueOf).collect(Collectors.toList());

        return Optional.of(this);
    }

    @Override
    public DataContainer toContainer() {
        return fillContainer(super.toContainer());
    }

    protected DataContainer fillContainer(DataContainer container) {
        container.set(RPGDataKeys.LAST_CLICKED, lastClick);
        container.set(RPGDataKeys.CLICK_SEQUENCE.getQuery(), sequence.stream().map(String::valueOf).collect(Collectors.toList()));

        return container;
    }

    @Override
    public PlayerClickDataImpl copy() {
        return new PlayerClickDataImpl(Items.looseClone(this.sequence), this.lastClick);
    }

    @Override
    public ImmutablePlayerClickDataImpl asImmutable() {
        return new ImmutablePlayerClickDataImpl(ImmutableList.copyOf(this.sequence), this.lastClick);
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

}
