package com.gmail.socraticphoenix.rpg.registry;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.click.ItemClickPredicate;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.impl.AbstractEvent;

public class RPGRegisterEvent extends AbstractEvent {
    private Cause cause;
    private RPGRegistries registries;
    
    public RPGRegisterEvent(RPGPlugin plugin, RPGRegistries registries) {
        this.cause = Cause.builder().append(plugin).build(EventContext.builder().build());
        this.registries = registries;
    }
    
    @Override
    public Cause getCause() {
        return cause;
    }

    public <T extends RPGRegistryItem> RPGRegisterEvent register(Class<T> type, T... vals) {
        this.registries.registryFor(type).ifPresent(r -> r.register(vals));
        return this;
    }
    
}
