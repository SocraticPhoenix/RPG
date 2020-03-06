package com.gmail.socraticphoenix.rpg.click;

import com.gmail.socraticphoenix.rpg.registry.AbstractRegistryItem;
import com.gmail.socraticphoenix.rpg.registry.RPGRegistryItem;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.List;
import java.util.function.BiPredicate;

public class ItemClickPredicate extends AbstractRegistryItem<RPGRegistryItem> implements DataSerializable, RPGRegistryItem {
    private BiPredicate<ItemStack, List<ClickType>> predicate;

    public ItemClickPredicate(BiPredicate<ItemStack, List<ClickType>> predicate, String pluginId, String id) {
        super(pluginId, id);
        this.predicate = predicate;
    }

    public BiPredicate<ItemStack, List<ClickType>> getPredicate() {
        return predicate;
    }

}
