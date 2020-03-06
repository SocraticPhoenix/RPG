package com.gmail.socraticphoenix.rpg.inventory.player;

import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.data.character.OptionData;
import com.gmail.socraticphoenix.rpg.event.RPGEquipEvent;
import com.gmail.socraticphoenix.rpg.inventory.InventoryHelper;
import com.gmail.socraticphoenix.rpg.inventory.storage.ItemStorage;
import com.gmail.socraticphoenix.rpg.options.Options;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ChangeInventoryEvent;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.entity.Hotbar;
import org.spongepowered.api.item.inventory.entity.MainPlayerInventory;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class HotbarListener {
    public static final int SELECTED_SLOT = 5;

    @Listener
    public void onClick(ClickInventoryEvent event, @First Player player) {
        Hotbar hotbar = player.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(Hotbar.class));

        event.getTransactions().forEach(slotTransaction -> {
            if (hotbar.containsInventory(slotTransaction.getSlot().transform())) {
                event.setCancelled(true);
            }
        });
    }

    @Listener
    public void onSlotChange(ChangeInventoryEvent.Held event, @First Player player) {
        Hotbar hotbar = player.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(Hotbar.class));

        int startIndex = event.getOriginalSlot().transform().getInventoryProperty(SlotIndex.class).get().getValue();
        int index = event.getFinalSlot().transform().getInventoryProperty(SlotIndex.class).get().getValue();


        Optional<OptionData> options = RPGData.options(player);
        if (!options.isPresent()) {
            event.setCancelled(true);
            return;
        }

        boolean userNumberSelection = options.get().getOrCreate(Options.USE_NUMBER_SELECTION).getValue();

        if (index <= 1) {
            return;
        } else if (startIndex <= 1) {
            hotbar.setSelectedSlotIndex(SELECTED_SLOT);
        } else if (index != SELECTED_SLOT || (userNumberSelection && startIndex != index)) {
            event.setCancelled(true);

            RPGData.inventory(player).ifPresent(data -> {
                ItemStorage.Flat hotbarStorage = data.getHotbar().flatView();
                int size = hotbarStorage.size();

                if (userNumberSelection && index != SELECTED_SLOT + 1 && index != SELECTED_SLOT - 1 && index != SELECTED_SLOT) {
                    int newSlot;
                    if (index < SELECTED_SLOT) {
                        newSlot = index - 2;
                    } else {
                        newSlot = index - 5;
                    }

                    if (newSlot >= 0 && newSlot < size) {
                        data.setSelectedSlot(newSlot);
                        InventoryHelper.syncHotbarDataAndHotbar(player);
                    }
                } else {
                    int move = index - startIndex;
                    int newSlot = data.getSelectedSlot() + move;

                    if (newSlot < 0) {
                        newSlot = 0;
                    } else if (newSlot >= size) {
                        newSlot = size - 1;
                    }

                    if (newSlot >= 0 && newSlot < size) {
                        data.setSelectedSlot(newSlot);
                        InventoryHelper.syncHotbarDataAndHotbar(player);
                    }
                }

                Sponge.getEventManager().post(new RPGEquipEvent(event.getOriginalSlot().peek().orElse(ItemStack.empty()), event.getFinalSlot().peek().orElse(ItemStack.empty()), player));
            });
        }
    }

}
