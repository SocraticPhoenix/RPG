package com.gmail.socraticphoenix.rpg.projectile;

import com.gmail.socraticphoenix.rpg.data.sponge.mob.CustomProjectileData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.action.CollideEvent;

public class ProjectileHandler implements Runnable {

    @Listener
    public void onCollide(CollideEvent ev) {
        ev.setCancelled(true);
    }

    @Override
    public void run() {
        Sponge.getServer().getWorlds().forEach(w -> w.getEntities(e -> !e.isRemoved()).forEach(e -> {
            e.get(CustomProjectileData.class).ifPresent(c -> c.value().get().update(e));
        }));
    }

}
