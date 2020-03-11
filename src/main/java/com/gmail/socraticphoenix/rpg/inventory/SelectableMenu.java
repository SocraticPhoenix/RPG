package com.gmail.socraticphoenix.rpg.inventory;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.data.character.InventoryData;
import com.gmail.socraticphoenix.rpg.inventory.SimpleSelectableMenu;
import com.gmail.socraticphoenix.rpg.inventory.button.ButtonAction;
import com.gmail.socraticphoenix.rpg.registry.AbstractRegistryItem;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.type.GridInventory;

public abstract class SelectableMenu extends AbstractRegistryItem<SelectableMenu> {
    private String pluginId;
    private String id;

    public SelectableMenu(String pluginId, String id) {
        super(pluginId, id);
        this.pluginId = pluginId;
        this.id = id;
    }

    public abstract ItemStack button(Player player);

    public abstract void update(Player player, InventoryData data, GridInventory inventory);

    public abstract void sync(Player player, InventoryData data, GridInventory inventory);

    public abstract String name(Player player, InventoryData data);

    public abstract int maxPage(Player player);

    public String name(Player player) {
        return RPGData.inventory(player).map(data -> this.name(player, data)).orElse("");
    }

    public void sync(Player player, GridInventory inventory) {
        RPGData.inventory(player).ifPresent(data -> this.sync(player, data, inventory));
    }

    public void update(Player player, GridInventory inventory) {
        RPGData.inventory(player).ifPresent(data -> this.update(player, data, inventory));
    }

    public ButtonAction action() {
        String id = pluginId + ":" + this.id + "_selection_button";
        return RPGPlugin.getPlugin().getRegistry().registryFor(ButtonAction.class).get().get(id).orElse(
                new ButtonAction((player, targetInventoryEvent) -> {
                    RPGData.runtime(player).ifPresent(data -> {
                        if (data.getMenu() != this) {
                            InventoryHelper.searchForMain(player).ifPresent(inventory -> {
                                SelectableMenus.OPTIONS.finishModifying(player);

                                data.setMenu(this);
                                InventoryHelper.clear(inventory, InventoryHelper.PAGE_START, InventoryHelper.PAGE_LIMIT);
                                InventoryHelper.updateAll(player);
                            });
                        }
                    });
                }, this.pluginId, this.id + "_selection_button")
        );
    }

    public void setPage(Player player, InventoryData data, GridInventory inventory, int page) {
        int max = this.maxPage(player);
        RPGData.runtime(player).ifPresent(runtime -> {
            int target = page;
            if (target >= max) {
                target = max - 1;
            } else if (target < 0) {
                target = 0;
            }

            runtime.setPage(target);
        });
        InventoryHelper.updateAll(player);
    }


}
