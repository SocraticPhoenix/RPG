package com.gmail.socraticphoenix.rpg;

import com.gmail.socraticphoenix.rpg.data.sponge.mob.CustomProjectileData;
import com.gmail.socraticphoenix.rpg.stats.StatHelper;
import org.spongepowered.api.Sponge;

public class RPGTickTask implements Runnable {

    @Override
    public void run() {
        Sponge.getServer().getOnlinePlayers().forEach(player -> {
            StatHelper.updateBars(player);
        });
    }

}
