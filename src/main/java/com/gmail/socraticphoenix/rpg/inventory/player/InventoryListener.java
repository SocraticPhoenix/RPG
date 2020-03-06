package com.gmail.socraticphoenix.rpg.inventory.player;

import com.gmail.socraticphoenix.rpg.data.item.ItemData;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomItemData;
import com.gmail.socraticphoenix.rpg.inventory.InventoryHelper;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ChangeInventoryEvent;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.CraftItemEvent;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.entity.MainPlayerInventory;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.UUID;

public class InventoryListener {

    @Listener
    public void onSwapHand(ChangeInventoryEvent.SwapHand event) {
        event.setCancelled(true);
    }

    @Listener
    public void onNumberPress(ClickInventoryEvent.NumberPress event) {
        event.setCancelled(true);
    }

    @Listener
    public void onDrop(DropItemEvent ev) {
        ev.setCancelled(true);
    }

    @Listener
    public void onRecipe(CraftItemEvent ev) {
        ev.setCancelled(true);
    }

    @Listener
    public void onClose(InteractInventoryEvent.Close ev) {
        if (!ev.getCursorTransaction().getFinal().isEmpty() || !ev.getCursorTransaction().getOriginal().isEmpty()) {
            ev.setCancelled(true);
            ev.getCursorTransaction().setValid(false);
        }
    }

    @Listener
    public void onPickup(ChangeInventoryEvent.Pickup.Pre event, @First Player player) {
        event.setCancelled(true);
        event.getFinal().forEach(snapshot -> {
            ItemStack stack = snapshot.createStack();
            if(stack.get(CustomItemData.class).isPresent()) {
                UUID id = stack.get(CustomItemData.class).get().value().get().getOwner();
                if (id != ItemData.NONE && !id.equals(player.getUniqueId())) {
                    return;
                }
            }

            Inventory inventory = player.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(MainPlayerInventory.class));
            if (inventory instanceof MainPlayerInventory) {
                MainPlayerInventory main = (MainPlayerInventory) inventory;
                InventoryTransactionResult result = main.getGrid().offer(stack);
                if (result.getType() == InventoryTransactionResult.Type.SUCCESS) {
                    Location<World> location = event.getTargetEntity().getLocation();
                    event.getTargetEntity().remove();
                    result.getRejectedItems().forEach(item -> {
                        Item newItem = (Item) location.getExtent().createEntity(EntityTypes.ITEM, location.getPosition());
                        newItem.offer(Keys.REPRESENTED_ITEM, item);
                        location.spawnEntity(newItem);
                    });
                    InventoryHelper.syncAll(player);
                }
            }
        });
    }

}
