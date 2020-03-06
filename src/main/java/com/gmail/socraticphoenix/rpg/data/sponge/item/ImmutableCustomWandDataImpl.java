package com.gmail.socraticphoenix.rpg.data.sponge.item;

import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.data.item.WandData;
import com.gmail.socraticphoenix.rpg.data.sponge.impl.RPGImmutableCustomDataImpl;

public class ImmutableCustomWandDataImpl extends RPGImmutableCustomDataImpl<WandData, CustomWandData, ImmutableCustomWandData> implements ImmutableCustomWandData {

    public ImmutableCustomWandDataImpl(WandData value) {
        super(value, RPGDataKeys.WAND_DATA);
    }

    @Override
    public CustomWandData asMutable() {
        return new CustomWandDataImpl(this.value.copy());
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

}
