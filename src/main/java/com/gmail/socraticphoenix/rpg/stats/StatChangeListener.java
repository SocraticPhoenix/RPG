package com.gmail.socraticphoenix.rpg.stats;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.DamageTypes;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.HealEntityEvent;
import org.spongepowered.api.event.filter.cause.First;

public class StatChangeListener {

    @Listener
    public void onDamage(DamageEntityEvent ev, @First DamageSource source) {
        if (source.getType() != DamageTypes.CUSTOM) {
            ev.setCancelled(true);
        } else {
            ev.setBaseDamage(0);
            ev.getModifiers().forEach(m -> ev.setDamage(m.getModifier(), b -> 0));
        }
    }

    @Listener
    public void onHeal(HealEntityEvent ev) {
        ev.setBaseHealAmount(0);
        ev.getModifiers().forEach(m -> ev.setHealAmount(m.getModifier(), b -> 0));
    }

}
