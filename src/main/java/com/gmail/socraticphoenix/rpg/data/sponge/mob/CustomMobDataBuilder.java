package com.gmail.socraticphoenix.rpg.data.sponge.mob;

import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.data.mob.MobData;
import com.gmail.socraticphoenix.rpg.data.sponge.impl.RPGCustomDataBuilder;

public class CustomMobDataBuilder extends RPGCustomDataBuilder<MobData, CustomMobData, ImmutableCustomMobData> {

    public CustomMobDataBuilder() {
        super(RPGDataKeys.MOB_DATA, new MobData(), MobData.class, CustomMobDataImpl::new);
    }

}
