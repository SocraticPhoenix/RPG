package com.gmail.socraticphoenix.rpg.event;

import com.gmail.socraticphoenix.rpg.data.mob.ProjectileData;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.impl.AbstractEvent;

import java.util.Optional;

public class RPGProjectileHitEvent extends AbstractEvent {
    private Entity projectile;
    private Cause cause;
    private ProjectileData data;

    public RPGProjectileHitEvent(Entity projectile, ProjectileData data) {
        this.projectile = projectile;
        this.cause = Cause.builder().append(projectile).build(EventContext.builder().build());
        this.data = data;
    }

    public ProjectileData getData() {
        return data;
    }

    public Entity getProjectile() {
        return projectile;
    }

    public Optional<Living> getOwner() {
        return (Optional) this.projectile.getWorld().getEntity(this.data.getOwner());
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }

    public static class Block extends RPGProjectileHitEvent {

        public Block(Entity projectile, ProjectileData data) {
            super(projectile, data);
        }

    }

    public static class Expire extends Block {

        public Expire(Entity projectile, ProjectileData data) {
            super(projectile, data);
        }

    }

    public static class Target extends RPGProjectileHitEvent {
        private Living hit;

        public Target(Entity projectile, Living hit, ProjectileData data) {
            super(projectile, data);
            this.hit = hit;
        }

        public Living getHit() {
            return hit;
        }
    }

}
