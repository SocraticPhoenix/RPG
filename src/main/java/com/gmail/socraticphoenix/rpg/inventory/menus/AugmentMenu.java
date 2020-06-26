package com.gmail.socraticphoenix.rpg.inventory.menus;

import com.flowpowered.math.vector.Vector2i;
import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.augment.AugmentSlot;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.data.character.InventoryData;
import com.gmail.socraticphoenix.rpg.data.item.ItemData;
import com.gmail.socraticphoenix.rpg.data.item.WandData;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomItemData;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomItemDataImpl;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomWandData;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomWandDataImpl;
import com.gmail.socraticphoenix.rpg.inventory.InventoryHelper;
import com.gmail.socraticphoenix.rpg.inventory.SelectableMenu;
import com.gmail.socraticphoenix.rpg.inventory.button.RuntimeButtonAction;
import com.gmail.socraticphoenix.rpg.inventory.button.data.ButtonData;
import com.gmail.socraticphoenix.rpg.inventory.storage.ItemStorage;
import com.gmail.socraticphoenix.rpg.spell.SpellSlot;
import com.gmail.socraticphoenix.rpg.spell.Spells;
import com.gmail.socraticphoenix.rpg.translation.Messages;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackComparators;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class AugmentMenu extends SelectableMenu {

    public AugmentMenu() {
        super(RPGPlugin.ID, "augments");
    }

    @Override
    public ItemStack button(Player player) {
        return ItemStack.builder().quantity(1).itemType(ItemTypes.EMERALD).add(Keys.DISPLAY_NAME, Text.of(TextColors.GREEN, Messages.translate(player, "rpg.menu.augments"))).build();
    }

    @Override
    public void open(Player player, InventoryData data, GridInventory inventory) {
        RPGData.runtime(player).ifPresent(r -> {
            ItemStack wand = data.getAugmentEditing();
            if (!wand.equals(data.getAugmentEditing())) {
                data.setAugmentEditing(wand);
                r.setAugmentSpellSlotModifying(null);
                r.setAugmentModifying(null);
                r.setAugmentSpellSlots(new ArrayList<>());
                r.setPage(0);

                Optional<ItemData> item = wand.get(CustomItemData.class).map(c -> c.value().get());
                if (item.isPresent()) {
                    r.setAugmentModifying(item.get().copy());
                }

                Optional<WandData> wandData = wand.get(CustomWandData.class).map(c -> c.value().get());
                if (wandData.isPresent()) {
                    r.setAugmentSpellSlots(wandData.get().copy().getSlots());
                }

            }
        });
    }

    @Override
    public void update(Player player, InventoryData data, GridInventory inventory) {
        RPGData.runtime(player).ifPresent(r -> {
            if (r.getAugmentSpellSlotModifying() == null) {
                Vector2i wand = InventoryHelper.PAGE_START.add(0, 1);

                Vector2i augmentStart = InventoryHelper.PAGE_START.add(2, 0);
                Vector2i augmentLimit = augmentStart.add(3, 3);

                Vector2i borderStart = InventoryHelper.PAGE_START.add(5, 0);
                Vector2i borderLimit = borderStart.add(1, 3);

                Vector2i spellSlotStart = InventoryHelper.PAGE_START.add(6, 0);
                Vector2i spellSlotLimit = spellSlotStart.add(3, 3);

                InventoryHelper.set(InventoryHelper.PAGE_START, inventory, InventoryHelper.createBorderItem());
                InventoryHelper.set(InventoryHelper.PAGE_START.add(0, 2), inventory, InventoryHelper.createBorderItem());
                InventoryHelper.drawFilledRect(inventory, InventoryHelper.PAGE_START.add(1, 0), InventoryHelper.PAGE_START.add(2, 3), InventoryHelper.createBorderItem().createSnapshot());
                InventoryHelper.drawFilledRect(inventory, borderStart, borderLimit, InventoryHelper.createBorderItem().createSnapshot());

                InventoryHelper.set(wand, inventory, data.getAugmentEditing());

                InventoryHelper.drawFilledRect(inventory, spellSlotStart, spellSlotLimit, InventoryHelper.createBorderItem(DyeColors.RED).createSnapshot());

                List<AugmentSlot> slots = r.getAugmentModifying() == null ? Collections.emptyList() : r.getAugmentModifying().getAugmentSlots();

                int index = r.getPage() * MAX_DISPLAY_AUGMENTS;
                for (int y = augmentStart.getY(); y < augmentLimit.getY(); y++) {
                    for (int x = augmentStart.getX(); x < augmentLimit.getX(); x++) {
                        if (index < slots.size()) {
                            AugmentSlot aug = slots.get(index);
                            InventoryHelper.set(x, y, inventory, createAugButton(player, data, aug));
                        } else {
                            InventoryHelper.set(x, y, inventory, InventoryHelper.createBorderItem(DyeColors.RED));
                        }
                        index++;
                    }
                }

                List<SpellSlot> spellSlots = r.getAugmentSpellSlots();
                int sIndex = 0;
                for (int y = spellSlotStart.getY(); y < spellSlotLimit.getY(); y++) {
                    for (int x = spellSlotStart.getX(); x < spellSlotLimit.getX(); x++) {
                        if (sIndex < spellSlots.size()) {
                            SpellSlot slot = spellSlots.get(sIndex);
                            InventoryHelper.set(x, y, inventory, ItemStack.builder()
                                    .itemType(ItemTypes.PAPER)
                                    .add(Keys.DISPLAY_NAME, Text.of(TextColors.GOLD, Spells.clickString(player, slot.getSequence())))
                                    .add(Keys.ITEM_LORE, Spells.spellSlotLore(player, slot))
                                    .itemData(slot.getAugmentSlots().isEmpty() ? InventoryHelper.createNoopButton() : ButtonData.of(new RuntimeButtonAction((player1, inventoryEvent) -> {
                                        r.setAugmentSpellSlotModifying(slot);
                                        r.setPage(0);
                                        InventoryHelper.updateAll(player);
                                    })))
                                    .build());
                        } else {
                            InventoryHelper.set(x, y, inventory, InventoryHelper.createBorderItem(DyeColors.RED));
                        }
                        sIndex++;
                    }
                }
            } else {
                SpellSlot slot = r.getAugmentSpellSlotModifying();

                InventoryHelper.drawFilledRect(inventory, InventoryHelper.PAGE_START, InventoryHelper.PAGE_START.add(1, 3), ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of(TextColors.RED, Messages.translate(player, "rpg.menu.augments.select")))
                        .add(Keys.ITEM_LORE, Spells.spellSlotLore(player, slot))
                        .itemData(ButtonData.of(new RuntimeButtonAction((player1, targetInventoryEvent) -> {
                            r.setPage(0);
                            r.setAugmentSpellSlotModifying(null);
                            r.setAugmentSpellSlots(new ArrayList<>());
                            InventoryHelper.updateAll(player);
                        })))
                        .build().createSnapshot());

                List<AugmentSlot> augments = slot.getAugmentSlots();
                int index = r.getPage() * MAX_DISPLAY_SLOT_AUGMENTS;

                for (int y = InventoryHelper.PAGE_START.getY(); y < InventoryHelper.PAGE_LIMIT.getY(); y++) {
                    for (int x = InventoryHelper.PAGE_START.add(1, 0).getX(); x < InventoryHelper.PAGE_LIMIT.getX(); x++) {
                        if (index < augments.size()) {
                            InventoryHelper.set(x, y, inventory, createAugButton(player, data, augments.get(index)));
                        } else {
                            InventoryHelper.set(x, y, inventory, InventoryHelper.createBorderItem());
                        }
                        index++;
                    }
                }

            }
        });
    }

    private ItemStack createAugButton(Player player, InventoryData data, AugmentSlot aug) {
        if (aug.getAugments().isEmpty()) {
            AtomicReference<ItemStack> ref = new AtomicReference<>();
            ItemStack stack = ItemStack.builder()
                    .itemType(ItemTypes.STAINED_HARDENED_CLAY)
                    .add(Keys.DYE_COLOR, aug.getColor().getDyeColor())
                    .add(Keys.DISPLAY_NAME, Text.of(aug.getColor().getColor(), Messages.translate(player, "rpg.augment.slot.empty",
                            Messages.translateString(player, aug.getColor().getKey()))))
                    .itemData(ButtonData.of(new RuntimeButtonAction((player1, targetInventoryEvent) -> {
                        if (targetInventoryEvent instanceof ClickInventoryEvent) {
                            ClickInventoryEvent ev = (ClickInventoryEvent) targetInventoryEvent;
                            ItemStack clicked = ev.getCursorTransaction().getFinal().createStack();
                            if (clicked.toContainer().equals(ref.get().toContainer())) {
                                ItemStack gem = ev.getCursorTransaction().getOriginal().createStack();
                                if (gem.get(CustomItemData.class).isPresent()) {
                                    ItemData gemData = gem.get(CustomItemData.class).get().value().get();
                                    if (gemData.getAugment().getColor() == aug.getColor()) {
                                        if (!gemData.getAugment().getAugments().isEmpty()) {
                                            if (RPGData.canUse(gem, player)) {
                                                aug.setGem(gem);
                                                aug.setAugments(gemData.getAugment().copy().getAugments());
                                                updateWand(player, data);
                                                ev.getCursorTransaction().setCustom(ItemStackSnapshot.NONE);
                                                InventoryHelper.updateAll(player);
                                                return false;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        return true;
                    }))).build();
            ref.set(stack.copy());
            return stack;
        } else {
            return ItemStack.builder()
                    .from(aug.getGem())
                    .add(Keys.DISPLAY_NAME, Messages.translate(player, "rpg.augment.slot.filled",
                            Messages.translateString(player, aug.getColor().getKey())))
                    .itemData(ButtonData.of(new RuntimeButtonAction((player1, targetInventoryEvent) -> {
                        ItemStack gem = aug.getGem();
                        ItemStorage.Flat inv = data.getCarried().flatView();
                        if (inv.itemCount() < inv.capacity()) {
                            inv.add(gem.copy());
                            aug.setGem(ItemStack.empty());
                            aug.setAugments(new ArrayList<>());
                            updateWand(player, data);
                        }
                    }))).build();
        }
    }

    private void updateWand(Player player, InventoryData data) {
        RPGData.runtime(player).ifPresent(r -> {
            ItemStack wand = data.getAugmentEditing().copy();
            data.setAugmentEditing(wand);

            ItemData itemData = r.getAugmentModifying().copy();
            wand.offer(new CustomItemDataImpl(itemData));

            wand.get(CustomWandData.class).ifPresent(c -> {
                WandData wandData = c.value().get();
                wand.offer(new CustomWandDataImpl(new WandData(r.getAugmentSpellSlots(), wandData.isOmniwand())));
                r.setAugmentSpellSlots(r.getAugmentSpellSlots().stream().map(SpellSlot::copy).collect(Collectors.toList()));
            });

            wand.offer(Keys.ITEM_LORE, Spells.wandLore(player, wand));
            r.setAugmentModifying(itemData.copy());
        });
        InventoryHelper.updateAll(player);
    }

    @Override
    public void sync(Player player, InventoryData data, GridInventory inventory) {
        RPGData.runtime(player).ifPresent(r -> {
            if (r.getAugmentSpellSlotModifying() == null) {
                Vector2i wandSlot = InventoryHelper.PAGE_START.add(0, 1);
                ItemStack wand = inventory.getSlot(wandSlot.getX(), wandSlot.getY()).get().poll().orElse(ItemStack.empty());
                if (!wand.equals(data.getAugmentEditing())) {
                    data.setAugmentEditing(wand);
                    r.setAugmentSpellSlotModifying(null);
                    r.setAugmentModifying(null);
                    r.setAugmentSpellSlots(new ArrayList<>());
                    r.setPage(0);

                    Optional<ItemData> item = wand.get(CustomItemData.class).map(c -> c.value().get());
                    if (item.isPresent()) {
                        r.setAugmentModifying(item.get().copy());
                    }

                    Optional<WandData> wandData = wand.get(CustomWandData.class).map(c -> c.value().get());
                    if (wandData.isPresent()) {
                        r.setAugmentSpellSlots(wandData.get().copy().getSlots());
                    }

                }
            }
        });
    }

    @Override
    public String name(Player player, InventoryData data) {
        return Messages.translateString(player, "rpg.augment");
    }

    public static int MAX_DISPLAY_AUGMENTS = 9;
    public static int MAX_DISPLAY_SLOT_AUGMENTS = 3 * 8;

    @Override
    public int maxPage(Player player) {
        return RPGData.runtime(player).map(r -> {
            if (r.getAugmentSpellSlotModifying() == null) {
                return r.getAugmentModifying().getAugmentSlots().size() / MAX_DISPLAY_AUGMENTS + 1;
            } else {
                return r.getAugmentSpellSlotModifying().getAugmentSlots().size() / MAX_DISPLAY_SLOT_AUGMENTS + 1;
            }
        }).orElse(1);
    }

}
