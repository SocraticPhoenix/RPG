package com.gmail.socraticphoenix.rpg.crafting;

import com.gmail.socraticphoenix.collect.coupling.Pair;
import com.gmail.socraticphoenix.rpg.inventory.storage.ItemStorage;
import com.gmail.socraticphoenix.rpg.registry.RPGRegistryItem;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

public interface RPGCraftingRecipe extends RPGRegistryItem {

    boolean test(ItemStorage storage, Player player);

    Optional<Pair<ItemStack, ItemStorage>> results(ItemStorage storage, Player player);

}
