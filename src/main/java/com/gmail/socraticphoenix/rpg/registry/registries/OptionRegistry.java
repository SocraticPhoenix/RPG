package com.gmail.socraticphoenix.rpg.registry.registries;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.options.values.Option;
import com.gmail.socraticphoenix.rpg.registry.AbstractRegistry;

public class OptionRegistry extends AbstractRegistry<Option> {

    public OptionRegistry() {
        super(RPGPlugin.ID, "options", Option.class);
    }

}
