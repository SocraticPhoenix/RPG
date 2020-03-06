package com.gmail.socraticphoenix.rpg.inventory.player.menus;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.data.character.InventoryData;
import com.gmail.socraticphoenix.rpg.inventory.InventoryHelper;
import com.gmail.socraticphoenix.rpg.inventory.button.data.ButtonData;
import com.gmail.socraticphoenix.rpg.inventory.player.ImplementableSelectableMenu;
import com.gmail.socraticphoenix.rpg.options.values.SetOption;
import com.gmail.socraticphoenix.rpg.translation.Messages;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.UUID;

public class OptionMenu extends ImplementableSelectableMenu {

    public OptionMenu() {
        super(RPGPlugin.ID, "options");
    }

    public void setModifying(Player player, SetOption option) {
        RPGData.runtime(player).ifPresent(data -> {
            data.setOptionModifying(option);
            InventoryHelper.updateAll(player);
        });
    }

    public void finishModifying(Player player) {
        RPGData.runtime(player).ifPresent(data -> {
            if (data.getOptionModifying() != null) {
                SetOption option = data.getOptionModifying();
                data.setOptionModifying(null);
                RPGData.options(player).ifPresent(opt -> {
                    opt.put(option);
                });
                InventoryHelper.updateAll(player);
            }
        });
    }

    @Override
    public ItemStack button(Player player) {
        return ItemStack.builder().itemType(ItemTypes.STONE_PICKAXE).add(Keys.DISPLAY_NAME, Text.of(TextColors.GRAY, "Options")).build();
    }

    @Override
    public String name(Player player, InventoryData data) {
        SetOption modifying = RPGData.runtime(player).get().getOptionModifying();
        if (modifying != null && modifying.type().hasSelectionGui()) {
            return modifying.type().invName(player);
        } else {
            return Messages.translateString(player, "rpg.options");
        }
    }

    @Override
    public int maxPage(Player player) {
        SetOption modifying = RPGData.runtime(player).get().getOptionModifying();
        if (modifying != null && modifying.type().hasSelectionGui()) {
            return modifying.type().maxPage(player);
        } else {
            return RPGData.options(player).map(data -> data.getOptions().size()).orElse(1);
        }
    }

    @Override
    public void update(Player player, InventoryData data, GridInventory inventory) {
        RPGData.runtime(player).ifPresent(r -> {
            UUID id = player.getUniqueId();
            if (r.getOptionModifying() == null || !r.getOptionModifying().type().hasSelectionGui()) {
                InventoryHelper.drawFilledRect(inventory, InventoryHelper.PAGE_START, InventoryHelper.PAGE_LIMIT, InventoryHelper.createBorderItem().createSnapshot());
                Queue<ItemStack> buttons = new ArrayDeque<>();

                RPGData.options(player).ifPresent(optionData -> optionData.getOptions().values().forEach(option -> {
                    buttons.add(ItemStack.builder()
                            .quantity(1)
                            .itemType(option.type().icon())
                            .add(Keys.DISPLAY_NAME, Text.of(TextStyles.BOLD, TextColors.LIGHT_PURPLE, option.type().name(player), ": ", TextStyles.RESET, option.type().nameFor(player, option.getValue())))
                            .itemData(ButtonData.of(option.type().action()))
                            .build());
                }));

                for (int y = InventoryHelper.PAGE_START.getY(); y < InventoryHelper.PAGE_LIMIT.getY() && !buttons.isEmpty(); y++) {
                    for (int x = InventoryHelper.PAGE_START.getX(); x < InventoryHelper.PAGE_LIMIT.getX() && !buttons.isEmpty(); x++) {
                        inventory.set(x, y, buttons.poll());
                    }
                }
            } else {
                SetOption current = r.getOptionModifying();
                current.type().update(this, player, data, inventory, current);
            }
        });
    }

    @Override
    public void sync(Player player, InventoryData data, GridInventory inventory) {

    }

}
