package com.gmail.socraticphoenix.rpg.data.sponge.mob;

import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.data.mob.MobData;
import com.gmail.socraticphoenix.rpg.data.sponge.impl.RPGCustomDataImpl;

public class CustomMobDataImpl extends RPGCustomDataImpl<MobData, CustomMobData, ImmutableCustomMobData> implements CustomMobData {

    public CustomMobDataImpl(MobData value) {
        super(value, RPGDataKeys.MOB_DATA, MobData.class, CustomMobDataImpl.class);
    }

    @Override
    public CustomMobData copy() {
        return new CustomMobDataImpl(this.value.copy());
    }

    @Override
    public ImmutableCustomMobData asImmutable() {
        return new ImmutableCustomMobDataImpl(this.value.copy());
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

}
