package com.gmail.socraticphoenix.rpg.inventory.player.menus;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.data.character.InventoryData;
import com.gmail.socraticphoenix.rpg.inventory.InventoryHelper;
import com.gmail.socraticphoenix.rpg.inventory.player.SelectableMenu;
import com.gmail.socraticphoenix.rpg.inventory.storage.ItemStorage;
import com.gmail.socraticphoenix.rpg.translation.Messages;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class BankMenu extends SelectableMenu {

    public BankMenu() {
        super(player -> RPGData.inventory(player).map(InventoryData::getMaxPage).orElse(1),
                player -> ItemStack.builder().itemType(ItemTypes.CHEST).add(Keys.DISPLAY_NAME, Text.of(TextColors.GOLD, Messages.translate(player, "rpg.bank"))).build(),
                (player, data, inventory) -> RPGData.runtime(player).ifPresent(r -> {
                    InventoryHelper.drawItemStorage(inventory, InventoryHelper.PAGE_START, InventoryHelper.PAGE_LIMIT, data.getBankPages().get(r.getPage()));
                }),
                (player, data, inventory) -> RPGData.runtime(player).ifPresent(r -> {
                    ItemStorage storage = InventoryHelper.syncItemStorage(inventory, InventoryHelper.PAGE_START, InventoryHelper.PAGE_LIMIT, data.getBankPages().get(r.getPage()));
                    data.getBankPages().set(r.getPage(), storage);
                }),
                (player, data) -> RPGData.runtime(player).map(r -> {
                    return Messages.translateString(player, "rpg.bank.page") + " " + (r.getPage() + 1);
                }).get(),
                RPGPlugin.ID, "bank_inventory");
    }

    @Override
    public void setPage(Player player, InventoryData data, GridInventory inventory, int page) {
        InventoryHelper.cleanPages(player, page);
        super.setPage(player, data, inventory, page);
    }
}
