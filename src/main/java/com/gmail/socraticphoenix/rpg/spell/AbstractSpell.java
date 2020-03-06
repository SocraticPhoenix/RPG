package com.gmail.socraticphoenix.rpg.spell;

import com.gmail.socraticphoenix.rpg.registry.AbstractRegistryItem;
import com.gmail.socraticphoenix.rpg.registry.RPGRegistryItem;
import org.spongepowered.api.item.ItemType;

import java.util.List;

public abstract class AbstractSpell extends AbstractRegistryItem<RPGRegistryItem> implements Spell {
    private List<Type> types;
    private Cost cost;
    private Type type;
    private ItemType icon;

    public AbstractSpell(List<Type> types, Cost cost, String key, ItemType icon, String pluginId) {
        super(pluginId, key);
        this.types = types;
        this.cost = cost;
        this.icon = icon;
        this.type = new Type(key, types);
    }

    @Override
    public ItemType icon() {
        return this.icon;
    }

    @Override
    public List<Type> types() {
        return this.types;
    }

    @Override
    public Type type() {
        return this.type;
    }

    @Override
    public Cost cost() {
        return this.cost;
    }

}
