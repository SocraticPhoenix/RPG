package com.gmail.socraticphoenix.rpg.inventory;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.inventory.button.ButtonAction;
import com.gmail.socraticphoenix.rpg.registry.RPGRegisterEvent;
import org.spongepowered.api.event.Listener;

public class ButtonActions {
    public static final ButtonAction NEXT_PAGE = new ButtonAction((player, targetInventoryEvent) -> {
        RPGData.runtime(player).ifPresent(data -> RPGData.inventory(player).ifPresent(inv -> {
            InventoryHelper.searchForMain(player).ifPresent(inventory -> data.getMenu().setPage(player,  inv, inventory, data.getPage() + 1));
        }));
    }, RPGPlugin.ID, "inventory_next_page");

    public static final ButtonAction PREVIOUS_PAGE = new ButtonAction((player, targetInventoryEvent) -> {
        RPGData.runtime(player).ifPresent(data -> RPGData.inventory(player).ifPresent(inv -> {
            InventoryHelper.searchForMain(player).ifPresent(inventory -> data.getMenu().setPage(player,  inv, inventory, data.getPage() - 1));
        }));
    }, RPGPlugin.ID, "inventory_previous_page");

    public static final ButtonAction PAGE_ONE = new ButtonAction((player, targetInventoryEvent) -> {
        RPGData.runtime(player).ifPresent(data -> RPGData.inventory(player).ifPresent(inv -> {
            InventoryHelper.searchForMain(player).ifPresent(inventory -> data.getMenu().setPage(player,  inv, inventory, 0));
        }));
    }, RPGPlugin.ID, "inventory_page_one");

    public static final ButtonAction OPEN_INVENTORY = new ButtonAction(((player, targetInventoryEvent) -> {
        RPGData.runtime(player).ifPresent(data -> data.setMenu(SelectableMenus.BANK_INVENTORY));
        InventoryHelper.openInventory(player);
    }), RPGPlugin.ID, "open_inventory_button");

    @Listener
    public void onRegister(RPGRegisterEvent event) {
        event.register(ButtonAction.class, NEXT_PAGE, PREVIOUS_PAGE, OPEN_INVENTORY);
    }

}
