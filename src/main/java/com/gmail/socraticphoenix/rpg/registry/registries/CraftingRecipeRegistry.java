package com.gmail.socraticphoenix.rpg.registry.registries;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.crafting.RPGCraftingRecipe;
import com.gmail.socraticphoenix.rpg.registry.AbstractRegistry;

public class CraftingRecipeRegistry extends AbstractRegistry<RPGCraftingRecipe> {

    public CraftingRecipeRegistry() {
        super(RPGPlugin.ID, "crafting_recipes", RPGCraftingRecipe.class);
    }

}
