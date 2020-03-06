package com.gmail.socraticphoenix.rpg.inventory.player;

import com.gmail.socraticphoenix.rpg.data.character.InventoryData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.type.GridInventory;

public abstract class ImplementableSelectableMenu extends SelectableMenu {

    public ImplementableSelectableMenu(String pluginId, String id) {
        super(null, null, null, null, null, pluginId, id);
        this.button = this::button;
        this.update = this::update;
        this.sync = this::sync;
        this.maxPage = this::maxPage;
        this.name = this::name;
    }

    public abstract ItemStack button(Player player);

    public abstract void update(Player player, InventoryData data, GridInventory inventory);

    public abstract void sync(Player player, InventoryData data, GridInventory inventory);

    public abstract String name(Player player, InventoryData data);

    public abstract int maxPage(Player player);

}
