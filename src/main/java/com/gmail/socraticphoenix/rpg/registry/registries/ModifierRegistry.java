package com.gmail.socraticphoenix.rpg.registry.registries;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.modifiers.Modifier;
import com.gmail.socraticphoenix.rpg.modifiers.ModifierFunction;
import com.gmail.socraticphoenix.rpg.registry.AbstractRegistry;

public class ModifierRegistry extends AbstractRegistry<Modifier> {

    public ModifierRegistry() {
        super(RPGPlugin.ID, "modifiers", Modifier.class);
    }

}
