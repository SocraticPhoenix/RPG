package com.gmail.socraticphoenix.rpg.crafting;

import com.gmail.socraticphoenix.collect.coupling.Pair;
import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.augment.AugmentColor;
import com.gmail.socraticphoenix.rpg.augment.AugmentSlot;
import com.gmail.socraticphoenix.rpg.data.item.ItemData;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomItemData;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomItemDataImpl;
import com.gmail.socraticphoenix.rpg.inventory.InventoryHelper;
import com.gmail.socraticphoenix.rpg.inventory.storage.ItemStorage;
import com.gmail.socraticphoenix.rpg.registry.AbstractRegistryItem;
import com.gmail.socraticphoenix.rpg.spell.Spells;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AugmentCraftingRecipe extends AbstractRegistryItem<AugmentCraftingRecipe> implements RPGCraftingRecipe {

    public AugmentCraftingRecipe() {
        super(RPGPlugin.ID, "augment_crafting");
    }

    @Override
    public boolean test(ItemStorage storage, Player player) {
        List<AugmentSlot> toFill = null;
        AugmentSlot augment = null;
        ItemStorage.Flat flat = storage.flatView();

        for (int i = 0; i < flat.size(); i++) {
            Optional<ItemStack> stackOpt = flat.get(i);
            if (stackOpt.isPresent()) {
                ItemStack stack = stackOpt.get();
                if (stack.get(CustomItemData.class).isPresent()) {
                    ItemData data = stack.get(CustomItemData.class).get().value().get();

                    if (data.getAugment().getColor() != AugmentColor.NONE) {
                        if (augment != null) {
                            return false;
                        } else {
                            augment = data.getAugment();
                        }
                    } else {
                        if (toFill != null) {
                            return false;
                        } else {
                            toFill = data.getAugmentSlots();
                        }
                    }
                } else {
                    return false;
                }
            }
        }

        if (toFill != null && augment != null) {
            for (AugmentSlot slot : toFill) {
                if (slot.getColor() == augment.getColor() && slot.getAugments().isEmpty()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public Optional<Pair<ItemStack, ItemStorage>> results(ItemStorage storage, Player player) {
        ItemStack target = null;
        ItemData targetData = null;
        List<AugmentSlot> toFill = null;
        AugmentSlot augment = null;
        ItemStorage.Flat flat = storage.flatView();

        for (int i = 0; i < flat.size(); i++) {
            Optional<ItemStack> stackOpt = flat.get(i);
            if (stackOpt.isPresent()) {
                ItemStack stack = stackOpt.get();
                ItemData data = stack.get(CustomItemData.class).get().value().get();
                if (data.getAugment().getColor() != AugmentColor.NONE) {
                    augment = data.getAugment();
                } else {
                    target = stack;
                    targetData = data.copy();
                    toFill = data.getAugmentSlots();
                }
            }
        }

        ItemStack result = target.copy();
        List<AugmentSlot> slots = targetData.getAugmentSlots();
        slots.clear();

        boolean filled = false;
        for (AugmentSlot slot : toFill) {
            slot = slot.copy();
            if (!filled && slot.getColor() == augment.getColor() && slot.getAugments().isEmpty()) {
                filled = true;
                slot.getAugments().addAll(augment.getAugments());
            }
            slots.add(slot);
        }

        result.offer(new CustomItemDataImpl(targetData));
        result.offer(Keys.ITEM_LORE, Spells.wandLore(player, result));

        return Optional.of(Pair.of(result, InventoryHelper.decreaseAllByOne(storage.copy())));
    }

}
