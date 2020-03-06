package com.gmail.socraticphoenix.rpg.crafting;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.collect.coupling.Pair;
import com.gmail.socraticphoenix.rpg.inventory.InventoryHelper;
import com.gmail.socraticphoenix.rpg.inventory.storage.ItemStorage;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackComparators;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SimpleShapelessRecipe extends AbstractRecipe {
    private Collection<ItemStackSnapshot> required;
    private ItemStackSnapshot result;

    public SimpleShapelessRecipe(Collection<ItemStackSnapshot> required, ItemStackSnapshot result, String pluginId, String id) {
        super(pluginId, id);
        this.required = required;
        this.result = result;
    }

    @Override
    public boolean test(ItemStorage storage, Player player) {
        List<ItemStackSnapshot> required = Items.looseClone(this.required, ArrayList::new);
        ItemStorage.Flat flat = storage.flatView();
        for (int i = 0; i < flat.size(); i++) {
            Optional<ItemStack> stackOptional = flat.get(i);
            if (stackOptional.isPresent()) {
                ItemStack stack = stackOptional.get();

                Optional<ItemStackSnapshot> match = required.stream().filter(snapshot -> ItemStackComparators.IGNORE_SIZE.compare(snapshot.createStack(), stack) == 0).findFirst();
                if (match.isPresent()) {
                    required = required.stream().filter(snapshot -> ItemStackComparators.IGNORE_SIZE.compare(snapshot.createStack(), stack) != 0).collect(Collectors.toList());
                } else {
                    return false;
                }
            }
        }

        return required.isEmpty();
    }

    @Override
    public Optional<Pair<ItemStack, ItemStorage>> results(ItemStorage storage, Player player) {
        return Optional.of(Pair.of(this.result.createStack(), InventoryHelper.decreaseAllByOne(storage.copy())));
    }

}
