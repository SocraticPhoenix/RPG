package com.gmail.socraticphoenix.rpg.registry.registries;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.registry.AbstractRegistry;
import com.gmail.socraticphoenix.rpg.spell.Type;

public class TypeRegistry extends AbstractRegistry<Type> {

    public TypeRegistry() {
        super(RPGPlugin.ID, "spell_type", Type.class);
    }

}
