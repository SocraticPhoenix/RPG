package com.gmail.socraticphoenix.rpg.data.sponge.mob;

import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.data.mob.ProjectileData;
import com.gmail.socraticphoenix.rpg.data.sponge.impl.RPGCustomDataBuilder;

public class CustomProjectileDataBuilder extends RPGCustomDataBuilder<ProjectileData, CustomProjectileData, ImmutableCustomProjectileData> {

    public CustomProjectileDataBuilder() {
        super(RPGDataKeys.PROJECTILE_DATA, new ProjectileData(), ProjectileData.class, CustomProjectileDataImpl::new);
    }

}
