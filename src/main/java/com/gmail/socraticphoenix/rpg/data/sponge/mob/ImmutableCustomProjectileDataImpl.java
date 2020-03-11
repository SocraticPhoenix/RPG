package com.gmail.socraticphoenix.rpg.data.sponge.mob;

import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.data.mob.ProjectileData;
import com.gmail.socraticphoenix.rpg.data.sponge.impl.RPGImmutableCustomDataImpl;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;

public class ImmutableCustomProjectileDataImpl extends RPGImmutableCustomDataImpl<ProjectileData, CustomProjectileData, ImmutableCustomProjectileData> implements ImmutableCustomProjectileData {

    public ImmutableCustomProjectileDataImpl(ProjectileData value) {
        super(value, RPGDataKeys.PROJECTILE_DATA);
    }

    @Override
    public CustomProjectileData asMutable() {
        return new CustomProjectileDataImpl(this.value.copy());
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

}
