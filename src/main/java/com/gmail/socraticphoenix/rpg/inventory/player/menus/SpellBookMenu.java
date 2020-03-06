package com.gmail.socraticphoenix.rpg.inventory.player.menus;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.data.character.InventoryData;
import com.gmail.socraticphoenix.rpg.inventory.InventoryHelper;
import com.gmail.socraticphoenix.rpg.inventory.player.ImplementableSelectableMenu;
import com.gmail.socraticphoenix.rpg.spell.Spell;
import com.gmail.socraticphoenix.rpg.spell.Spells;
import com.gmail.socraticphoenix.rpg.translation.Messages;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;

public class SpellBookMenu extends ImplementableSelectableMenu {
    public static final int MAX_SPACE = 27;

    public SpellBookMenu() {
        super(RPGPlugin.ID, "spellbook");
    }

    @Override
    public ItemStack button(Player player) {
        return ItemStack.builder().itemType(ItemTypes.ENCHANTED_BOOK).add(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, Messages.translate(player, "rpg.menu.spellbook"))).build();
    }

    @Override
    public void update(Player player, InventoryData data, GridInventory inventory) {
        int page = RPGData.runtime(player).get().getPage();

        int startIndex = MAX_SPACE * page;
        List<Spell> spells = RPGData.spellbook(player).map(d -> Items.looseClone(d.getSpells(), ArrayList::new)).orElse(new ArrayList<>());

        for (int y = InventoryHelper.PAGE_START.getY(); y < InventoryHelper.PAGE_LIMIT.getY(); y++) {
            for (int x = InventoryHelper.PAGE_START.getX(); x < InventoryHelper.PAGE_LIMIT.getX(); x++) {
                if (startIndex < spells.size()) {
                    Spell spell = spells.get(startIndex);
                    inventory.set(x, y, ItemStack.builder()
                            .itemType(spell.icon())
                            .add(Keys.DISPLAY_NAME, Text.of(TextColors.GOLD, Messages.translate(player, spell.rawId())))
                            .add(Keys.ITEM_LORE, Spells.spellbookLore(player, spell))
                            .itemData(InventoryHelper.createNoopButton())
                            .build());
                } else {
                    inventory.set(x, y, InventoryHelper.createBorderItem());
                }
                startIndex++;
            }
        }

    }

    @Override
    public void sync(Player player, InventoryData data, GridInventory inventory) {

    }

    @Override
    public String name(Player player, InventoryData data) {
       return Messages.translateString(player, "rpg.menu.spellbook");
    }

    @Override
    public int maxPage(Player player) {
        return RPGData.spellbook(player).get().getSpells().size() / MAX_SPACE + 1;
    }

    @Override
    public void setPage(Player player, InventoryData data, GridInventory inventory, int page) {
        int max = 1;
        if (RPGData.spellbook(player).isPresent()) {
            max = RPGData.spellbook(player).get().getSpells().size() / MAX_SPACE;
        }

        if (page < 0) {
            page = 0;
        } else if (page >= max) {
            page = max - 1;
        }

        super.setPage(player, data, inventory, page);
    }
}
