package com.gmail.socraticphoenix.rpg.inventory.player;

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

public class SelectableMenu extends AbstractRegistryItem<RPGRegistryItem> {
    private String pluginId;
    private String id;

    protected Function<Player, ItemStack> button;
    protected Consumer3<Player, InventoryData, GridInventory> sync;
    protected Consumer3<Player, InventoryData, GridInventory> update;
    protected BiFunction<Player, InventoryData, String> name;
    protected Function<Player, Integer> maxPage;

    public SelectableMenu(Function<Player, Integer> maxPage, Function<Player, ItemStack> button, Consumer3<Player, InventoryData, GridInventory> update, Consumer3<Player, InventoryData, GridInventory> sync, BiFunction<Player, InventoryData, String> name, String pluginId, String id) {
        super(pluginId, id);
        this.maxPage = maxPage;
        this.button = button;
        this.sync = sync;
        this.update = update;
        this.name = name;
        this.pluginId = pluginId;
        this.id = id;
    }

    protected SelectableMenu(String pluginId, String id) {
        super(pluginId, id);
        this.pluginId = pluginId;
        this.id = id;
    }

    public Function<Player, ItemStack> button() {
        return this.button;
    }

    public String name(Player player) {
        return RPGData.inventory(player).map(data -> name.apply(player, data)).orElse("");
    }

    public void sync(Player player, GridInventory inventory) {
        RPGData.inventory(player).ifPresent(data -> sync.accept(player, data, inventory));
    }

    public void update(Player player, GridInventory inventory) {
        RPGData.inventory(player).ifPresent(data -> update.accept(player, data, inventory));
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
        int max = this.maxPage.apply(player);
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
