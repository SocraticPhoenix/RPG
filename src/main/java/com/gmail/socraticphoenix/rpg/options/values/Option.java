package com.gmail.socraticphoenix.rpg.options.values;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.data.character.InventoryData;
import com.gmail.socraticphoenix.rpg.inventory.InventoryHelper;
import com.gmail.socraticphoenix.rpg.inventory.button.ButtonAction;
import com.gmail.socraticphoenix.rpg.inventory.player.SelectableMenu;
import com.gmail.socraticphoenix.rpg.inventory.player.menus.OptionMenu;
import com.gmail.socraticphoenix.rpg.registry.RPGRegistryItem;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.text.Text;

import java.util.HashMap;
import java.util.Map;

public interface Option<T> extends RPGRegistryItem {

    SetOption<T> defaultValue();

    SetOption<T> build(DataContainer container);

    ItemType icon();

    Text nameFor(Player player, T value);

    Text name(Player player);

    boolean hasSelectionGui();

    void update(OptionMenu menu, Player player, InventoryData data, GridInventory inventory, SetOption<T> current);

    String invName(Player player);

    int maxPage(Player player);

    default ButtonAction action() {
        if (hasSelectionGui()) {
            return new ButtonAction((player, inventoryEvent) -> {
                RPGData.runtime(player).ifPresent(data -> {
                    SelectableMenu menu = data.getMenu();
                    if (menu instanceof OptionMenu) {
                        ((OptionMenu) menu).setModifying(player, this.getOptionFor(player).copy());
                    }
                });
            }, RPGPlugin.ID, "option_" + this.rawId());
        } else {
            return new ButtonAction((player, inventoryEvent) -> {
                RPGData.runtime(player).ifPresent(data -> RPGData.inventory(player).ifPresent(inv -> {
                    SelectableMenu menu = data.getMenu();
                    if (menu instanceof OptionMenu) {
                        SetOption option = this.getOptionFor(player).copy();
                        ((OptionMenu) menu).setModifying(player, option);
                        InventoryHelper.searchForMain(player).ifPresent(inventory -> update((OptionMenu) menu, player, inv, inventory, option));
                        ((OptionMenu) menu).finishModifying(player);
                    }
                }));
            }, RPGPlugin.ID, "option_" + this.rawId());
        }
    }

    default SetOption<T> getOptionFor(Player player) {
        return RPGData.options(player).get().getOrCreate(this.defaultValue());
    }

    default T getFor(Player player) {
        return getOptionFor(player).getValue();
    }

}
