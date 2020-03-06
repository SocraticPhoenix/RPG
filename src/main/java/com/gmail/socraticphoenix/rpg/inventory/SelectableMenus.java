package com.gmail.socraticphoenix.rpg.inventory;

import com.flowpowered.math.vector.Vector2i;
import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.collect.coupling.Pair;
import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.crafting.RPGCraftingRecipe;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.inventory.button.ButtonAction;
import com.gmail.socraticphoenix.rpg.inventory.button.data.ButtonData;
import com.gmail.socraticphoenix.rpg.inventory.player.SelectableMenu;
import com.gmail.socraticphoenix.rpg.inventory.player.menus.BankMenu;
import com.gmail.socraticphoenix.rpg.inventory.player.menus.OptionMenu;
import com.gmail.socraticphoenix.rpg.inventory.player.menus.SpellBookMenu;
import com.gmail.socraticphoenix.rpg.inventory.player.menus.WandEditMenu;
import com.gmail.socraticphoenix.rpg.inventory.storage.ItemStorage;
import com.gmail.socraticphoenix.rpg.registry.RPGRegisterEvent;
import com.gmail.socraticphoenix.rpg.translation.Messages;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.ContextValue;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;

public class SelectableMenus {

    public static final SelectableMenu BANK_INVENTORY = new BankMenu();

    public static final SelectableMenu CRAFTING_MENU = new SelectableMenu(
            player -> 1,
            player -> ItemStack.builder().itemType(ItemTypes.CRAFTING_TABLE).add(Keys.DISPLAY_NAME, Text.of(TextColors.GOLD, Messages.translate(player, "rpg.menu.crafting"))).build(),
            (player, data, inventory) -> {
                InventoryHelper.drawFilledRect(inventory, new Vector2i(0, 3), new Vector2i(1, 6), InventoryHelper.createBorderItem().createSnapshot());
                InventoryHelper.drawFilledRect(inventory, new Vector2i(4, 3), new Vector2i(6, 6), InventoryHelper.createBorderItem().createSnapshot());
                InventoryHelper.drawFilledRect(inventory, new Vector2i(7, 3), new Vector2i(9, 6), InventoryHelper.createBorderItem().createSnapshot());
                inventory.set(6, 3, InventoryHelper.createBorderItem());
                inventory.set(6, 5, InventoryHelper.createBorderItem());
                InventoryHelper.drawItemStorage(inventory, new Vector2i(1, 3), new Vector2i(4, 6), data.getCrafting());

                inventory.getSlot(6, 4).ifPresent(slot -> {
                    Optional<RPGCraftingRecipe> recipe = RPGPlugin.getPlugin().getRegistry().registryFor(RPGCraftingRecipe.class).get().elements().stream().filter(craftingRecipe -> craftingRecipe.test(data.getCrafting(), player)).findFirst();
                    if (recipe.isPresent()) {
                        Optional<Pair<ItemStack, ItemStorage>> results = recipe.get().results(data.getCrafting(), player);
                        if (results.isPresent()) {
                            ItemStack stack = results.get().getA();
                            ItemStack button = stack.copy();
                            button.offer(ButtonData.of(InventoryHelper.createCraftingAction(stack, results.get().getB(), slot)));
                            slot.set(button);
                        } else {
                            slot.set(ItemStack.builder()
                                    .itemType(ItemTypes.STAINED_GLASS_PANE)
                                    .add(Keys.DYE_COLOR, DyeColors.RED)
                                    .add(Keys.DISPLAY_NAME, Messages.translate(player, "rpg.menu.crafting.invalid"))
                                    .itemData(InventoryHelper.createNoopButton())
                                    .build());
                        }
                    } else {
                        slot.set(ItemStack.builder()
                                .itemType(ItemTypes.STAINED_GLASS_PANE)
                                .add(Keys.DYE_COLOR, DyeColors.RED)
                                .add(Keys.DISPLAY_NAME, Messages.translate(player, "rpg.menu.crafting.invalid"))
                                .itemData(InventoryHelper.createNoopButton())
                                .build());
                    }
                });
            },
            (player, data, inventory) -> {
                data.setCrafting(InventoryHelper.syncItemStorage(inventory, new Vector2i(1, 3), new Vector2i(4, 6), data.getCrafting()));
            },
            (player, data) -> {
                return Messages.translateString(player, "rpg.menu.crafting");
            },
            RPGPlugin.ID, "crafting_menu"
    );

    public static final SelectableMenu EQUIPMENT_MENU = new SelectableMenu(
            player -> 1,
            player -> ItemStack.builder().itemType(ItemTypes.CHAINMAIL_HELMET).add(Keys.DISPLAY_NAME, Text.of(TextColors.GRAY, Messages.translate(player, "rpg.menu.equipment"))).build(),
            (player, data, inventory) -> {
                ButtonData noop = InventoryHelper.createNoopButton();

                ItemStorage equipmentBackup = ItemStorage.of(Items.buildList(
                        ItemStack.builder()
                                .itemType(ItemTypes.LEATHER_HELMET)
                                .add(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, Messages.translate(player, "rpg.menu.equipment.helmet")))
                                .itemData(noop)
                                .build(),
                        ItemStack.builder()
                                .itemType(ItemTypes.LEATHER_CHESTPLATE)
                                .add(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, Messages.translate(player, "rpg.menu.equipment.chestplate")))
                                .itemData(noop)
                                .build(),
                        ItemStack.builder()
                                .itemType(ItemTypes.LEATHER_LEGGINGS)
                                .add(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, Messages.translate(player, "rpg.menu.equipment.leggings")))
                                .itemData(noop)
                                .build(),
                        ItemStack.builder()
                                .itemType(ItemTypes.LEATHER_BOOTS)
                                .add(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, Messages.translate(player, "rpg.menu.equipment.boots")))
                                .itemData(noop)
                                .build()
                ));
                ItemStorage equipment = ItemStorage.of(Items.buildList(data.getHelmet(), data.getChestplate(), data.getLeggings(), data.getBoots()));

                ItemStack trinket = ItemStack.builder()
                        .itemType(ItemTypes.TOTEM_OF_UNDYING)
                        .add(Keys.DISPLAY_NAME, Text.of(TextColors.GRAY, Messages.translate(player, "rpg.menu.equipment.trinket")))
                        .itemData(noop)
                        .build();
                ItemStorage trinketsBackup = ItemStorage.of(Items.buildList(trinket, trinket, trinket, trinket, trinket));

                ItemStack ammo = ItemStack.builder()
                        .itemType(ItemTypes.SPECTRAL_ARROW)
                        .add(Keys.DISPLAY_NAME, Text.of(TextColors.BLUE, Messages.translate(player, "rpg.menu.equipment.ammo")))
                        .itemData(noop)
                        .build();
                ItemStorage ammoBackup = ItemStorage.of(Items.buildList(ammo, ammo, ammo, ammo, ammo, ammo, ammo, ammo, ammo));

                ItemStack potion = ItemStack.builder()
                        .itemType(ItemTypes.GLASS_BOTTLE)
                        .add(Keys.DISPLAY_NAME, Text.of(TextColors.GREEN, Messages.translate(player, "rpg.menu.equipment.consumable")))
                        .itemData(noop)
                        .build();
                ItemStorage potionBackup = ItemStorage.of(Items.buildList(potion, potion, potion, potion, potion, potion, potion, potion, potion));

                InventoryHelper.drawItemStorage(inventory, InventoryHelper.PAGE_START, new Vector2i(4, 4), equipment, equipmentBackup);
                InventoryHelper.drawItemStorage(inventory, new Vector2i(4, 3), new Vector2i(9, 4), data.getTrinkets(), trinketsBackup);
                InventoryHelper.drawItemStorage(inventory, new Vector2i(0, 4), new Vector2i(9, 5), data.getAmmo(), ammoBackup);
                InventoryHelper.drawItemStorage(inventory, new Vector2i(0, 5), new Vector2i(9, 6), data.getPotions(), potionBackup);
            },
            (player, data, inventory) -> {
                Predicate<ItemStack> test = stack -> !stack.get(ButtonData.class).isPresent();

                data.setTrinkets(InventoryHelper.syncItemStorage(inventory, new Vector2i(4, 3), new Vector2i(9, 4), data.getTrinkets(), test));
                data.setAmmo(InventoryHelper.syncItemStorage(inventory, new Vector2i(0, 4), new Vector2i(9, 5), data.getAmmo(), test));
                data.setPotions(InventoryHelper.syncItemStorage(inventory, new Vector2i(0, 5), new Vector2i(9, 6), data.getPotions(), test));

                ItemStorage equipment = InventoryHelper.syncItemStorage(inventory, new Vector2i(0, 3), new Vector2i(4, 4), ItemStorage.of(4, 1));
                equipment.get(0, 0).ifPresent(stack -> {
                    if(test.test(stack)) {
                        data.setHelmet(stack);
                    }
                });
                equipment.get(1, 0).ifPresent(stack -> {
                    if(test.test(stack)) {
                        data.setChestplate(stack);
                    }
                });
                equipment.get(2, 0).ifPresent(stack -> {
                    if(test.test(stack)) {
                        data.setLeggings(stack);
                    }
                });
                equipment.get(3, 0).ifPresent(stack -> {
                    if(test.test(stack)) {
                        data.setBoots(stack);
                    }
                });
            },
            (player, data) -> {
                return Messages.translateString(player, "rpg.menu.equipment");
            },
            RPGPlugin.ID, "equipment_menu"
    );

    public static final SelectableMenu OTHER_MENUS = new SelectableMenu(
            player -> (RPGPlugin.getPlugin().getRegistry().registryFor(SelectableMenu.class).get().elements().size() - 6) / 27,
            player -> ItemStack.builder().itemType(ItemTypes.WRITABLE_BOOK).add(Keys.DISPLAY_NAME, Text.of(TextColors.YELLOW, Messages.translate(player, "rpg.menu.other_menus"))).build(),
            (player, data, inventory) -> {
                Iterator<SelectableMenu> iterator = RPGPlugin.getPlugin().getRegistry().registryFor(SelectableMenu.class).get().elements().iterator();
                int index = 1;
                while (index < 7 && iterator.hasNext()) {
                    iterator.next();
                    index++;
                }

                index = 27;
                while (iterator.hasNext()) {
                    SelectableMenu menu = iterator.next();
                    ItemStack button = menu.button().apply(player);
                    button.offer(ButtonData.of(menu.action()));
                    inventory.set(SlotIndex.of(index), button);
                    index++;
                }

                for (int i = index; i < inventory.capacity(); i++) {
                    inventory.set(SlotIndex.of(i), InventoryHelper.createBorderItem());
                }
            },
            (player, data, inventory) -> {

            },
            (player, data) -> {
                return Messages.translateString(player, "rpg.menu.other_menus");
            },
            RPGPlugin.ID, "more_options"
    );

    public static final SelectableMenu EDIT_WAND = new WandEditMenu();

    public static final SelectableMenu SPELLBOOK = new SpellBookMenu();

    public static final OptionMenu OPTIONS = new OptionMenu();

    @Listener
    public void onRegister(RPGRegisterEvent event) {
        event.register(SelectableMenu.class, BANK_INVENTORY, SPELLBOOK, EDIT_WAND, CRAFTING_MENU, EQUIPMENT_MENU, OTHER_MENUS, OPTIONS);

        RPGPlugin.registryFor(SelectableMenu.class).get().elements().forEach(s -> event.register(ButtonAction.class, s.action()));
    }

    @Listener
    public void onLeave(ClientConnectionEvent.Disconnect ev) {
        OPTIONS.finishModifying(ev.getTargetEntity());
    }

    @Listener
    public void onClose(InteractInventoryEvent.Close ev, @First Player player) {
        OPTIONS.finishModifying(player);
    }

}
