package com.gmail.socraticphoenix.rpg.click.data.impl;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.click.ClickType;
import com.gmail.socraticphoenix.rpg.click.data.ImmutablePlayerClickData;
import com.gmail.socraticphoenix.rpg.click.data.PlayerClickData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.value.immutable.ImmutableListValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

import java.util.List;
import java.util.stream.Collectors;

public class ImmutablePlayerClickDataImpl extends AbstractImmutableData<ImmutablePlayerClickData, PlayerClickData> implements ImmutablePlayerClickData {
    private List<ClickType> sequence;
    private long lastClick;

    public ImmutablePlayerClickDataImpl(List<ClickType> sequence, long lastClick) {
        this.sequence = sequence;
        this.lastClick = lastClick;

        this.registerGetters();
    }

    public ImmutableValue<Long> lastClicked() {
        return Sponge.getRegistry().getValueFactory()
                .createValue(RPGDataKeys.LAST_CLICKED, this.lastClick).asImmutable();
    }

    public ImmutableListValue<ClickType> sequence() {
        return Sponge.getRegistry().getValueFactory()
                .createListValue(RPGDataKeys.CLICK_SEQUENCE, this.sequence).asImmutable();
    }

    private List<ClickType> getSequence() {
        return sequence;
    }

    private long getLastClick() {
        return lastClick;
    }

    @Override
    protected void registerGetters() {
        registerKeyValue(RPGDataKeys.LAST_CLICKED, this::lastClicked);
        registerKeyValue(RPGDataKeys.CLICK_SEQUENCE, this::sequence);

        registerFieldGetter(RPGDataKeys.LAST_CLICKED, this::getLastClick);
        registerFieldGetter(RPGDataKeys.CLICK_SEQUENCE, this::getSequence);
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
    public PlayerClickData asMutable() {
        return new PlayerClickDataImpl(Items.looseClone(sequence), lastClick);
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

}
