package com.gmail.socraticphoenix.rpg.data.sponge.item;

import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.data.item.WandData;
import com.gmail.socraticphoenix.rpg.data.sponge.RPGCustomData;
import com.gmail.socraticphoenix.rpg.data.sponge.RPGImmutableCustomData;
import com.gmail.socraticphoenix.rpg.data.sponge.impl.RPGCustomDataImpl;

public class CustomWandDataImpl extends RPGCustomDataImpl<WandData, CustomWandData, ImmutableCustomWandData> implements CustomWandData {

    public CustomWandDataImpl(WandData value) {
        super(value, RPGDataKeys.WAND_DATA, WandData.class, CustomWandDataImpl.class);
    }

    @Override
    public CustomWandData copy() {
        return new CustomWandDataImpl(this.value.copy());
    }

    @Override
    public ImmutableCustomWandData asImmutable() {
        return new ImmutableCustomWandDataImpl(this.value);
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

}
