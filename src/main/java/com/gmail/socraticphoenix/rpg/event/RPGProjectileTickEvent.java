package com.gmail.socraticphoenix.rpg.event;

import com.gmail.socraticphoenix.rpg.data.mob.ProjectileData;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.impl.AbstractEvent;

public class RPGProjectileTickEvent extends AbstractEvent {
    private Entity projectile;
    private ProjectileData data;
    private Cause cause;

    public RPGProjectileTickEvent(Entity projectile, ProjectileData data) {
        this.projectile = projectile;
        this.data = data;
        this.cause = Cause.builder().append(projectile).build(EventContext.builder().build());
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }

}
