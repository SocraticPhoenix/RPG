package com.gmail.socraticphoenix.rpg.registry.registries;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.gods.God;
import com.gmail.socraticphoenix.rpg.registry.AbstractRegistry;
import com.gmail.socraticphoenix.rpg.registry.AbstractRegistryItem;

public class GodRegistry extends AbstractRegistry<God> {

    public GodRegistry() {
        super(RPGPlugin.ID, "gods", God.class);
    }

}
