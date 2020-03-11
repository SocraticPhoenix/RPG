package com.gmail.socraticphoenix.rpg.inventory;

import com.flowpowered.math.vector.Vector2i;
import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomItemData;
import com.gmail.socraticphoenix.rpg.inventory.button.ButtonAction;
import com.gmail.socraticphoenix.rpg.inventory.button.RuntimeButtonAction;
import com.gmail.socraticphoenix.rpg.inventory.button.data.ButtonData;
import com.gmail.socraticphoenix.rpg.inventory.storage.ItemStorage;
import com.gmail.socraticphoenix.rpg.registry.registries.ButtonActionRegistry;
import com.gmail.socraticphoenix.rpg.stats.StatHelper;
import com.gmail.socraticphoenix.rpg.translation.Messages;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.entity.Hotbar;
import org.spongepowered.api.item.inventory.entity.MainPlayerInventory;
import org.spongepowered.api.item.inventory.property.InventoryCapacity;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface InventoryHelper {
    Vector2i MAIN_DIMENSIONS = new Vector2i(9, 6);

    Vector2i HOTBAR_START = new Vector2i(0, 0);
    Vector2i HOTBAR_LIMIT = new Vector2i(9, 2);

    Vector2i PAGE_START = new Vector2i(0, 3);
    Vector2i PAGE_LIMIT = new Vector2i(9, 6);

    Vector2i CARRY_START = new Vector2i(0, 0);
    Vector2i CARRY_LIMIT = new Vector2i(9, 3);

    Vector2i BUTTON_ROW_START = new Vector2i(0, 2);

    static Vector2i fromIndex(int index, int width) {
        return new Vector2i(index % width, index / width);
    }

    static int toIndex(Vector2i pos, int width) {
        return pos.getX() + pos.getY() * width;
    }

    static void openInventory(Player player) {
        RPGData.inventory(player).ifPresent(data -> {
            player.closeInventory();

            Sponge.getScheduler().createTaskBuilder().execute(() -> {
                Inventory inventory = Inventory.builder()
                        .of(InventoryArchetypes.DOUBLE_CHEST)
                        .withCarrier(player)
                        .property(InventoryTitle.of(Messages.translate(player, "rpg.menu")))
                        .property(InventoryDimension.of(9, 6))
                        .property(InventoryCapacity.of(9 * 6))
                        .listener(InteractInventoryEvent.class, event -> {
                            if (!event.isCancelled()) {
                                event.getCause().first(Player.class).ifPresent(p -> {
                                    InventoryHelper.syncAll(p);
                                    InventoryHelper.updateAll(p);
                                    InventoryHelper.syncHotbarDataAndHotbar(p);
                                });
                            }
                        })
                        .build(RPGPlugin.getPlugin());

                player.openInventory(inventory);
                InventoryHelper.updateAll(player);
                InventoryHelper.syncHotbarDataAndHotbar(player);
            }).delayTicks(3).submit(RPGPlugin.getPlugin());
        });
    }

    static Optional<GridInventory> searchForMain(Player player) {
        return searchForSizedGrid(player, MAIN_DIMENSIONS);
    }

    static Optional<GridInventory> searchForCarrier(Player player) {
        Inventory inv = player.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(MainPlayerInventory.class));
        if (inv instanceof MainPlayerInventory) {
            return Optional.of(((MainPlayerInventory) inv).getGrid());
        }

        return Optional.empty();
    }

    static Optional<GridInventory> searchForSizedGrid(Player player, Vector2i size) {
        return player.getOpenInventory().map(c -> {
            Inventory inventory = c.query(QueryOperationTypes.INVENTORY_TYPE.of(GridInventory.class));

            for (Inventory inv : inventory) {
                if (inv instanceof GridInventory) {
                    GridInventory gridInventory = (GridInventory) inv;
                    if (gridInventory.getDimensions().equals(size)) {
                        return gridInventory;
                    }
                }
            }
            return null;
        });
    }

    static void updateAll(Player player) {
        searchForMain(player).ifPresent(gridInventory -> {
            InventoryHelper.setupButtons(player, gridInventory);
            InventoryHelper.updateHotbar(player, gridInventory);
            InventoryHelper.updateMenu(player, gridInventory);
        });

        searchForCarrier(player).ifPresent(gridInventory -> {
            InventoryHelper.updateCarried(player, gridInventory);
        });

        InventoryHelper.syncHotbarDataAndHotbar(player);
    }

    static void syncAll(Player player) {
        InventoryHelper.searchForMain(player).ifPresent(gridInventory -> {
            InventoryHelper.syncMenu(player, gridInventory);
            InventoryHelper.syncCarried(player, gridInventory);
            InventoryHelper.syncHotbar(player, gridInventory);
        });

        InventoryHelper.searchForCarrier(player).ifPresent(gridInventory -> {
            InventoryHelper.syncCarried(player, gridInventory);
        });
    }

    static void updateMenu(Player player, GridInventory inventory) {
        RPGData.runtime(player).ifPresent(data -> {
            data.getMenu().update(player, inventory);
        });
    }

    static void updateHotbar(Player player, GridInventory inventory) {
        RPGData.inventory(player).ifPresent(data -> {
            drawItemStorage(inventory, HOTBAR_START, HOTBAR_LIMIT, data.getHotbar());
        });
    }

    static void updateCarried(Player player, GridInventory inventory) {
        RPGData.inventory(player).ifPresent(data -> {
            drawItemStorage(inventory, CARRY_START, CARRY_LIMIT, data.getCarried());
        });
    }

    static void syncMenu(Player player, GridInventory inventory) {
        RPGData.runtime(player).ifPresent(data -> {
            data.getMenu().sync(player, inventory);
        });
    }

    static void syncHotbar(Player player, GridInventory inventory) {
        RPGData.inventory(player).ifPresent(data -> {
            ItemStorage storage = syncItemStorage(inventory, HOTBAR_START, HOTBAR_LIMIT, data.getHotbar());
            data.setHotbar(storage);
        });
    }

    static void syncCarried(Player player, GridInventory inventory) {
        RPGData.inventory(player).ifPresent(data -> {
            ItemStorage storage = syncItemStorage(inventory, CARRY_START, CARRY_LIMIT, data.getCarried());
            data.setCarried(storage);
        });
    }

    static ItemStack uselessCopy(ItemStack stack) {
        ItemStack.Builder builder = ItemStack.builder();
        builder.itemType(stack.getType())
            .quantity(stack.getQuantity());

        if (stack.get(Keys.DISPLAY_NAME).isPresent()) {
            builder.add(Keys.DISPLAY_NAME, Text.of(TextColors.RED, stack.get(Keys.DISPLAY_NAME)));
        }

        if (stack.get(Keys.ITEM_DURABILITY).isPresent()) {
            builder.add(Keys.ITEM_DURABILITY, stack.get(Keys.ITEM_DURABILITY).get());
        }

        return builder.build();
    }

    static void syncHotbarDataAndHotbar(Player player) {
        RPGData.inventory(player).ifPresent(hotbarData -> {
            Hotbar hotbar = player.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(Hotbar.class));
            int newSlot = hotbarData.getSelectedSlot();

            ItemStorage.Flat stacks = hotbarData.getHotbar().flatView();

            for (int i = newSlot - 3, j = 2; i <= newSlot + 3; i++, j++) {
                SlotIndex targetIndex = SlotIndex.of(j);
                if (i >= 0 && i < stacks.size() && stacks.get(i).isPresent()) {
                    ItemStack stack = stacks.get(i).get();
                    hotbar.set(targetIndex, stack);
                } else {
                    hotbar.getSlot(targetIndex).get().clear();
                }
            }
        });
    }

    static ItemStorage syncItemStorage(GridInventory inventory, Vector2i a, Vector2i b, ItemStorage itemStorage) {
        return syncItemStorage(inventory, a, b, itemStorage, stack -> true);
    }

    static ItemStorage syncItemStorage(GridInventory inventory, Vector2i a, Vector2i b, ItemStorage itemStorage, Predicate<ItemStack> sync) {
        for (int x = a.getX(); x < b.getX(); x++) {
            for (int y = a.getY(); y < b.getY(); y++) {
                SlotPos target = SlotPos.of(x, y);
                Optional<Slot> slotOptional = inventory.getSlot(target);
                if (slotOptional.isPresent()) {
                    Optional<ItemStack> stackOptional = slotOptional.get().peek();
                    if (stackOptional.isPresent() && stackOptional.get().getType() != ItemTypes.NONE && sync.test(stackOptional.get())) {
                        itemStorage.set(x - a.getX(), y - a.getY(), stackOptional.get());
                    } else {
                        itemStorage.remove(x - a.getX(), y - a.getY());
                    }
                }
            }
        }

        return itemStorage;
    }

    static void drawItemStorage(GridInventory inventory, Vector2i a, Vector2i b, ItemStorage storage, ItemStorage backup) {
        for (int x = a.getX(), index = 0; x < b.getX(); x++) {
            for (int y = a.getY(); y < b.getY(); y++, index++) {
                SlotPos target = SlotPos.of(x, y);
                Optional<ItemStack> stackOptional = storage.get(x - a.getX(), y - a.getY());

                Optional<Slot> slotOptional = inventory.getSlot(target);

                if (slotOptional.isPresent()) {
                    Slot slot = slotOptional.get();
                    if (stackOptional.isPresent() && stackOptional.get().getType() != ItemTypes.NONE) {
                        slot.set(stackOptional.get());
                    } else {
                        stackOptional = backup.get(x - a.getX(), y - a.getY());
                        if (stackOptional.isPresent() && stackOptional.get().getType() != ItemTypes.NONE) {
                            slot.set(stackOptional.get());
                        } else {
                            slot.clear();
                        }
                    }
                }
            }
        }
    }

    static void drawItemStorage(GridInventory inventory, Vector2i a, Vector2i b, ItemStorage storage) {
        drawItemStorage(inventory, a, b, storage, storage);
    }

    static void cleanPages(Player player, int page) {
        removeUnnecessaryPages(player, page);
        makeNecessaryPages(player, page);
    }

    static void makeNecessaryPages(Player player, int page) {
        RPGData.inventory(player).ifPresent(data -> {
            List<ItemStorage> pages = data.getBankPages();
            for (int i = pages.size(); i <= page; i++) {
                pages.add(ItemStorage.of(9, 3));
            }
        });
    }

    static void removeUnnecessaryPages(Player player, int page) {
        RPGData.inventory(player).ifPresent(data -> {
            List<ItemStorage> pages = data.getBankPages();
            for (int i = pages.size() - 1; i > page; i--) {
                if (pages.get(i).itemCount() == 0) {
                    pages.remove(i);
                } else {
                    break;
                }
            }
        });
    }

    static void setupButtons(Player player, GridInventory inventory) {
        RPGData.inventory(player).ifPresent(data -> RPGData.runtime(player).ifPresent(r -> {
            List<ItemStack> buttons = Items.buildList(
                    ItemStack.builder()
                            .itemType(ItemTypes.PAPER)
                            .add(Keys.DISPLAY_NAME, Text.of(TextColors.GOLD, r.getMenu().name(player)))
                            .itemData(createNoopButton())
                            .build(),
                    ItemStack.builder()
                            .itemType(ItemTypes.WOOL)
                            .add(Keys.DYE_COLOR, DyeColors.RED)
                            .add(Keys.DISPLAY_NAME, Text.of(TextColors.RED, Messages.translate(player, "rpg.bank.previous_page")))
                            .itemData(ButtonData.of(ButtonActions.PREVIOUS_PAGE))
                            .build(),
                    ItemStack.builder()
                            .itemType(ItemTypes.WOOL)
                            .add(Keys.DYE_COLOR, DyeColors.GREEN)
                            .add(Keys.DISPLAY_NAME, Text.of(TextColors.GREEN, Messages.translate(player, "rpg.bank.next_page")))
                            .itemData(ButtonData.of(ButtonActions.NEXT_PAGE))
                            .build()
            );

            Iterator<SelectableMenu> iterator = RPGPlugin.getPlugin().getRegistry().registryFor(SelectableMenu.class).get().elements().iterator();
            int index = 3;

            while (iterator.hasNext() && index < 9) {
                SelectableMenu menu = iterator.next();
                ItemStack button = menu.button(player);
                button.offer(ButtonData.of(menu.action()));
                buttons.add(button);
                index++;
            }

            for (int i = 0; i < 9 && i < buttons.size(); i++) {
                ItemStack stack = buttons.get(i);
                inventory.set(i + BUTTON_ROW_START.getX(), BUTTON_ROW_START.getY(), stack);
            }

        }));
    }

    static void clear(GridInventory inventory, Vector2i a, Vector2i b) {
        for (int x = a.getX(); x < b.getX(); x++) {
            for (int y = a.getY(); y < b.getY(); y++) {
                inventory.getSlot(x, y).ifPresent(Slot::clear);
            }
        }
    }


    static void drawFilledRect(GridInventory inventory, Vector2i a, Vector2i b, ItemStackSnapshot item) {
        for (int x = a.getX(); x < b.getX(); x++) {
            for (int y = a.getY(); y < b.getY(); y++) {
                inventory.set(SlotPos.of(x, y), item.createStack());
            }
        }
    }

    static ItemStack createBorderItem() {
        return createBorderItem(DyeColors.BLACK);
    }

    static ItemStack createBorderItem(DyeColor color) {
        return ItemStack.builder().itemType(ItemTypes.STAINED_GLASS_PANE).add(Keys.DYE_COLOR, color).add(Keys.DISPLAY_NAME, Text.of(""))
                .itemData(createNoopButton()).build();
    }

    static ItemStorage decreaseAllByOne(ItemStorage storage) {
        ItemStorage.Flat flat = storage.flatView();
        for (int i = 0; i < flat.size(); i++) {
            Optional<ItemStack> stackOptional = flat.get(i);
            if (stackOptional.isPresent()) {
                ItemStack stack = stackOptional.get().copy();
                if (stack.getQuantity() == 1) {
                    flat.remove(i);
                } else {
                    stack.setQuantity(stack.getQuantity() - 1);
                    flat.set(i, stack);
                }
            }
        }

        return storage;
    }

    static ButtonData createNoopButton() {
        return ButtonData.of(RPGPlugin.getPlugin().getRegistry().registryFor(ButtonAction.class).get().get(ButtonActionRegistry.NO_OP).get());
    }

    static ButtonAction createCraftingAction(ItemStack stack, ItemStorage storage, Slot slot) {
        return new RuntimeButtonAction(((player, targetInventoryEvent) -> {
            RPGData.inventory(player).ifPresent(data -> {
                searchForCarrier(player).ifPresent(inventory -> {
                    if (inventory.canFit(stack)) {
                        data.setCrafting(storage);
                        InventoryHelper.updateAll(player);
                        slot.clear();
                        inventory.offer(stack);
                        InventoryHelper.syncAll(player);
                        InventoryHelper.updateAll(player);
                    }
                });
            });
        }));
    }


}
