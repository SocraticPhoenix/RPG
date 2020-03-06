package com.gmail.socraticphoenix.rpg.spell;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.registry.AbstractRegistryItem;
import com.gmail.socraticphoenix.rpg.registry.RPGRegistryItem;

import java.util.List;

public class Type extends AbstractRegistryItem<RPGRegistryItem> {
    private String key;
    private List<Type> parents;

    public Type(String key, List<Type> parents) {
        super(RPGPlugin.ID, key);
        this.key = key;
        this.parents = parents;
    }

    public String getKey() {
        return key;
    }

    public List<Type> getParents() {
        return parents;
    }

    public boolean is(Type type) {
        return type == this || parents.stream().anyMatch(t -> t.is(type));
    }

}
