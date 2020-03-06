package com.gmail.socraticphoenix.rpg.data.sponge.mob;

import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.data.mob.MobData;
import com.gmail.socraticphoenix.rpg.data.sponge.impl.RPGImmutableCustomDataImpl;

public class ImmutableCustomMobDataImpl extends RPGImmutableCustomDataImpl<MobData, CustomMobData, ImmutableCustomMobData> implements ImmutableCustomMobData {

    public ImmutableCustomMobDataImpl(MobData value) {
        super(value, RPGDataKeys.MOB_DATA);
    }

    @Override
    public CustomMobData asMutable() {
        return new CustomMobDataImpl(this.value.copy());
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

}
