package com.gmail.socraticphoenix.rpg.registry;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.Optional;

public class RPGRegistries extends AbstractRegistry<RPGRegistry> implements RPGRegistry<RPGRegistry> {
    private RPGPlugin plugin;
    private PluginContainer container;

    public RPGRegistries(RPGPlugin plugin, PluginContainer container) {
        super(RPGPlugin.ID, "registries", RPGRegistry.class);
        this.plugin = plugin;
        this.container = container;

        register(this);
    }

    public <T extends RPGRegistryItem> Optional<RPGRegistry<T>> registryFor(Class<T> type) {
        Optional registry = elements().stream().filter(r -> r.type().equals(type)).findFirst();
        return (Optional<RPGRegistry<T>>) registry;
    }


}
