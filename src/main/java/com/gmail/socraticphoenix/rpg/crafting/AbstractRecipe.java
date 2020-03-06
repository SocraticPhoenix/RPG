package com.gmail.socraticphoenix.rpg.crafting;

import com.gmail.socraticphoenix.rpg.registry.AbstractRegistryItem;
import com.gmail.socraticphoenix.rpg.registry.RPGRegistryItem;

public abstract class AbstractRecipe extends AbstractRegistryItem<RPGRegistryItem> implements RPGCraftingRecipe {

    public AbstractRecipe(String pluginId, String id) {
        super(pluginId, id);
    }

}
