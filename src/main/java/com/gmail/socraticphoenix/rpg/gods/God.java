package com.gmail.socraticphoenix.rpg.gods;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.registry.AbstractRegistryItem;
import com.gmail.socraticphoenix.rpg.registry.RPGRegistryItem;
import com.gmail.socraticphoenix.rpg.spell.Spell;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class God extends AbstractRegistryItem<RPGRegistryItem> {
    private String key;
    private Predicate<Spell> spells;

    public God(String key, Predicate<Spell> spells, String pluginId, String id) {
        super(pluginId, id);
        this.key = key;
        this.spells = spells;
    }

    public Collection<Spell> spells() {
        return RPGPlugin.registryFor(Spell.class).get().elements().stream().filter(spells).collect(Collectors.toList());
    }

}
