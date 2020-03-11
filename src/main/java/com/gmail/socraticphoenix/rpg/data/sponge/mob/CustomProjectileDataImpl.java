package com.gmail.socraticphoenix.rpg.data.sponge.mob;

import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.data.mob.ProjectileData;
import com.gmail.socraticphoenix.rpg.data.sponge.impl.RPGCustomDataImpl;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;

public class CustomProjectileDataImpl extends RPGCustomDataImpl<ProjectileData, CustomProjectileData, ImmutableCustomProjectileData> implements CustomProjectileData {

    public CustomProjectileDataImpl(ProjectileData value) {
        super(value, RPGDataKeys.PROJECTILE_DATA, ProjectileData.class, CustomProjectileDataImpl.class);
    }

    @Override
    public CustomProjectileData copy() {
        return new CustomProjectileDataImpl(this.value.copy());
    }

    @Override
    public ImmutableCustomProjectileData asImmutable() {
        return new ImmutableCustomProjectileDataImpl(this.value.copy());
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

}
