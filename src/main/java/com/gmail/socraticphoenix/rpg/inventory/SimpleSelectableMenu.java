package com.gmail.socraticphoenix.rpg.inventory;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.data.character.InventoryData;
import com.gmail.socraticphoenix.rpg.inventory.InventoryHelper;
import com.gmail.socraticphoenix.rpg.inventory.SelectableMenus;
import com.gmail.socraticphoenix.rpg.inventory.button.ButtonAction;
import com.gmail.socraticphoenix.rpg.registry.AbstractRegistryItem;
import com.gmail.socraticphoenix.rpg.registry.RPGRegistryItem;
import io.github.socraticphoenix.inversey.Consumer3;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.type.GridInventory;

import java.util.function.BiFunction;
import java.util.function.Function;

public class SimpleSelectableMenu extends SelectableMenu {
    protected Function<Player, ItemStack> button;
    protected Consumer3<Player, InventoryData, GridInventory> sync;
    protected Consumer3<Player, InventoryData, GridInventory> update;
    protected BiFunction<Player, InventoryData, String> name;
    protected Function<Player, Integer> maxPage;

    public SimpleSelectableMenu(Function<Player, Integer> maxPage, Function<Player, ItemStack> button, Consumer3<Player, InventoryData, GridInventory> update, Consumer3<Player, InventoryData, GridInventory> sync, BiFunction<Player, InventoryData, String> name, String pluginId, String id) {
        super(pluginId, id);
        this.maxPage = maxPage;
        this.button = button;
        this.sync = sync;
        this.update = update;
        this.name = name;
    }

    @Override
    public ItemStack button(Player player) {
        return this.button.apply(player);
    }

    @Override
    public void update(Player player, InventoryData data, GridInventory inventory) {
        this.update.accept(player, data, inventory);
    }

    @Override
    public void sync(Player player, InventoryData data, GridInventory inventory) {
        this.sync.accept(player, data, inventory);
    }

    @Override
    public String name(Player player, InventoryData data) {
        return this.name.apply(player, data);
    }

    @Override
    public int maxPage(Player player) {
        return this.maxPage.apply(player);
    }

}
