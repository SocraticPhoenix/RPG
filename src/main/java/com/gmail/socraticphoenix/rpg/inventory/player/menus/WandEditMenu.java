package com.gmail.socraticphoenix.rpg.inventory.player.menus;

import com.flowpowered.math.vector.Vector2i;
import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.click.ClickHelper;
import com.gmail.socraticphoenix.rpg.click.ClickType;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.data.character.InventoryData;
import com.gmail.socraticphoenix.rpg.data.character.RuntimeData;
import com.gmail.socraticphoenix.rpg.data.item.WandData;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomItemData;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomWandData;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomWandDataImpl;
import com.gmail.socraticphoenix.rpg.inventory.InventoryHelper;
import com.gmail.socraticphoenix.rpg.inventory.button.RuntimeButtonAction;
import com.gmail.socraticphoenix.rpg.inventory.button.data.ButtonData;
import com.gmail.socraticphoenix.rpg.inventory.player.ImplementableSelectableMenu;
import com.gmail.socraticphoenix.rpg.modifiers.SetModifier;
import com.gmail.socraticphoenix.rpg.spell.Spell;
import com.gmail.socraticphoenix.rpg.spell.SpellSlot;
import com.gmail.socraticphoenix.rpg.spell.Spells;
import com.gmail.socraticphoenix.rpg.spell.Types;
import com.gmail.socraticphoenix.rpg.translation.Messages;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WandEditMenu extends ImplementableSelectableMenu {

    public WandEditMenu() {
        super(RPGPlugin.ID, "edit_want");
    }

    @Override
    public ItemStack button(Player player) {
        return ItemStack.builder().itemType(ItemTypes.STICK).add(Keys.DISPLAY_NAME, Text.of(TextColors.GREEN, Messages.translate(player, "rpg.menu.wand"))).build();
    }

    public static final int MAX_DISPLAY = 3 * 9;

    @Override
    public void update(Player player, InventoryData data, GridInventory inventory) {
        RPGData.runtime(player).ifPresent(r -> {
            if (r.getSpellSlotModifying() == null) {
                Vector2i wandSlot = InventoryHelper.PAGE_START.add(1, 1);
                InventoryHelper.drawFilledRect(inventory, InventoryHelper.PAGE_START, InventoryHelper.PAGE_START.add(1, 3), InventoryHelper.createBorderItem().createSnapshot());
                InventoryHelper.drawFilledRect(inventory, InventoryHelper.PAGE_START, InventoryHelper.PAGE_START.add(3, 1), InventoryHelper.createBorderItem().createSnapshot());
                InventoryHelper.drawFilledRect(inventory, InventoryHelper.PAGE_START.add(0, 2), InventoryHelper.PAGE_START.add(3, 3), InventoryHelper.createBorderItem().createSnapshot());
                InventoryHelper.drawFilledRect(inventory, InventoryHelper.PAGE_START.add(2, 0), InventoryHelper.PAGE_START.add(3, 3), InventoryHelper.createBorderItem().createSnapshot());

                inventory.set(wandSlot.getX(), wandSlot.getY(), data.getWand());

                Vector2i rightStart = InventoryHelper.PAGE_START.add(6, 0);
                Vector2i rightColStart = rightStart.add(2, 0);
                Vector2i rightEnd = rightStart.add(3, 3);
                Vector2i slotsStart = InventoryHelper.PAGE_START.add(3, 0);
                Vector2i slotsEnd = slotsStart.add(3, 3);
                InventoryHelper.drawFilledRect(inventory, rightStart, rightEnd, InventoryHelper.createBorderItem().createSnapshot());

                if (r.isOmniwand()) {
                    Vector2i button1 = rightColStart;
                    Vector2i counter = rightColStart.add(0, 1);
                    Vector2i button2 = rightColStart.add(0, 2);

                    inventory.set(counter.getX(), counter.getY(), ItemStack.builder().itemType(ItemTypes.PAPER).add(Keys.DISPLAY_NAME,
                            Text.of(TextColors.GOLD, Messages.translate(player, "rpg.menu.wand.slots"), ": ", r.getSpellSlots().size())).itemData(InventoryHelper.createNoopButton()).build());

                    inventory.set(button2.getX(), button2.getY(), ItemStack.builder()
                            .quantity(1)
                            .itemType(ItemTypes.WOOL)
                            .add(Keys.DYE_COLOR, DyeColors.RED)
                            .add(Keys.DISPLAY_NAME, Text.of(TextColors.RED, Messages.translate(player, "rpg.menu.wand.slots.remove")))
                            .itemData(ButtonData.of(new RuntimeButtonAction((player1, inventoryEvent) -> {
                                setSize(player, data, r, inventory, r.getSpellSlots().size() - 1);
                                InventoryHelper.updateAll(player);
                            })))
                            .build());

                    inventory.set(button1.getX(), button1.getY(), ItemStack.builder()
                            .quantity(1)
                            .itemType(ItemTypes.WOOL)
                            .add(Keys.DYE_COLOR, DyeColors.GREEN)
                            .add(Keys.DISPLAY_NAME, Text.of(TextColors.GREEN, Messages.translate(player, "rpg.menu.wand.slots.add")))
                            .itemData(ButtonData.of(new RuntimeButtonAction((player1, inventoryEvent) -> {
                                setSize(player, data, r, inventory, r.getSpellSlots().size() + 1);
                                InventoryHelper.updateAll(player);
                            })))
                            .build());
                }

                int index = 0;
                for (int y = slotsStart.getY(); y < slotsEnd.getY(); y++) {
                    for (int x = slotsStart.getX(); x < slotsEnd.getX(); x++) {
                        if (index < r.getSpellSlots().size()) {
                            SpellSlot slot = r.getSpellSlots().get(index);
                            Spell spell = slot.getSpell();

                            List<Text> lore = new ArrayList<>();
                            lore.add(Text.of(TextColors.BLUE, Messages.translate(player, spell.rawId())));

                            List<SetModifier> modifiers = slot.getModifiers();
                            for (int i = 0; i < modifiers.size(); i++) {
                                SetModifier modifier = modifiers.get(i);

                                Spells.limitLoreLine((String) modifier.getModifier().getDesc().apply(player, modifier.getArguments()), 5).forEach(s -> {
                                    lore.add(Text.of(TextColors.DARK_PURPLE, s));
                                });
                                lore.add(Text.EMPTY);
                            }

                            if (slot.isBypass()) {
                                lore.add(Text.EMPTY);
                                Spells.limitLoreLine(Messages.translateString(player, "rpg.menu.wand.bypass"), 5).forEach(s -> lore.add(Text.of(TextColors.GREEN, s)));
                            }

                            if (slot.isLocked()) {
                                lore.add(Text.EMPTY);
                                Spells.limitLoreLine(Messages.translateString(player, "rpg.menu.wand.locked"), 5).forEach(s -> lore.add(Text.of(TextColors.RED, s)));
                            }

                            inventory.set(x, y, ItemStack.builder()
                                    .itemType(ItemTypes.STAINED_HARDENED_CLAY)
                                    .add(Keys.DYE_COLOR, slot.isLocked() ? DyeColors.RED : slot.isBypass() ? DyeColors.GREEN : DyeColors.PURPLE)
                                    .add(Keys.DISPLAY_NAME, Text.of(TextColors.GOLD, Spells.clickString(player, slot.getSequence())))
                                    .add(Keys.ITEM_LORE, lore)
                                    .itemData(slot.isLocked() ? InventoryHelper.createNoopButton() : ButtonData.of(new RuntimeButtonAction((player1, inventoryEvent) -> {
                                        r.setSpellSlotModifying(slot);
                                        r.setPage(0);
                                        InventoryHelper.updateAll(player);
                                    })))
                                    .build());
                        } else {
                            inventory.set(x, y, InventoryHelper.createBorderItem(DyeColors.RED));
                        }

                        index++;
                    }
                }
            } else {
                List<Spell> spells = new ArrayList<>(r.getSpellSlotModifying().isBypass() ? RPGPlugin.registryFor(Spell.class).get().elements() : RPGData.spellbook(player).get().getSpells().stream().filter(s -> !s.is(Types.PASSIVE)).collect(Collectors.toList()));
                SpellSlot slot = r.getSpellSlotModifying();

                Spell spell = slot.getSpell();

                List<Text> lore = new ArrayList<>();
                lore.add(Text.of(TextColors.GOLD, Spells.clickString(player, slot.getSequence())));
                lore.add(Text.of(TextColors.BLUE, Messages.translate(player, spell.rawId())));

                if (slot.isBypass()) {
                    lore.add(Text.EMPTY);
                    Spells.limitLoreLine(Messages.translateString(player, "rpg.menu.wand.bypass"), 5).forEach(s -> lore.add(Text.of(TextColors.GREEN, s)));
                }

                if (slot.isLocked()) {
                    lore.add(Text.EMPTY);
                    Spells.limitLoreLine(Messages.translateString(player, "rpg.menu.wand.locked"), 5).forEach(s -> lore.add(Text.of(TextColors.RED, s)));
                }

                inventory.set(InventoryHelper.PAGE_START.getX(), InventoryHelper.PAGE_START.getY(), ItemStack.builder()
                        .itemType(ItemTypes.STAINED_HARDENED_CLAY)
                        .add(Keys.DYE_COLOR, slot.isLocked() ? DyeColors.RED : slot.isBypass() ? DyeColors.GREEN : DyeColors.PURPLE)
                        .add(Keys.DISPLAY_NAME, Text.of(TextColors.RED, Messages.translate(player, "rpg.menu.wand.select")))
                        .add(Keys.ITEM_LORE, lore)
                        .itemData(InventoryHelper.createNoopButton())
                        .build());

                int index = r.getPage() * MAX_DISPLAY;
                for (int y = InventoryHelper.PAGE_START.getY(); y < InventoryHelper.PAGE_LIMIT.getY(); y++) {
                    for (int x = InventoryHelper.PAGE_START.getX(); x < InventoryHelper.PAGE_LIMIT.getX(); x++) {
                        if (index < spells.size()) {
                            Spell spellTgrt = spells.get(index);
                            inventory.set(x, y, ItemStack.builder()
                                    .itemType(spellTgrt.icon())
                                    .add(Keys.DISPLAY_NAME, Text.of(TextColors.GOLD, Messages.translate(player, spellTgrt.rawId())))
                                    .add(Keys.ITEM_LORE, Spells.spellbookLore(player, spellTgrt))
                                    .itemData(ButtonData.of(new RuntimeButtonAction((player1, inventoryEvent) -> {
                                        List<SpellSlot> slots = Items.looseClone(r.getSpellSlots());
                                        SpellSlot modifying = r.getSpellSlotModifying();
                                        int indx = slots.indexOf(modifying);
                                        if (indx != -1) {
                                            slots.remove(indx);
                                            SpellSlot newSlot = new SpellSlot(spellTgrt, modifying.getSequence(), modifying.getModifiers(), modifying.isLocked(), modifying.isBypass());
                                            slots.add(indx, newSlot);

                                            WandData newData = new WandData(slots, r.isOmniwand());
                                            ItemStack wand = data.getWand().copy();
                                            wand.offer(new CustomWandDataImpl(newData));
                                            wand.offer(Keys.ITEM_LORE, Spells.wandLore(player, newData, wand.get(CustomItemData.class).map(c -> c.value().get()).orElse(null)));
                                            data.setWand(wand);
                                        }

                                        r.setSpellSlots(slots);
                                        r.setPage(0);
                                        r.setSpellSlotModifying(null);
                                        InventoryHelper.updateAll(player);
                                    })))
                                    .build());
                        } else {
                            inventory.set(x, y, InventoryHelper.createBorderItem());
                        }
                        index++;
                    }
                }
            }
        });
    }

    private void setSize(Player player, InventoryData data, RuntimeData r, GridInventory inventory, int size) {
        if (size <= 0 || size > 9) {
            return;
        }

        List<List<ClickType>> sequences = ClickHelper.generateKeybinds(size);
        List<SpellSlot> newSlots = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            if (i < r.getSpellSlots().size()) {
                SpellSlot previous = r.getSpellSlots().get(i);
                newSlots.add(new SpellSlot(previous.getSpell(), sequences.get(i), previous.getModifiers(), previous.isLocked(), previous.isBypass()));
            } else {
                newSlots.add(new SpellSlot(Spells.NONE, sequences.get(i), new ArrayList<>(), false, false));
            }
        }

        r.setSpellSlots(Items.looseClone(newSlots));
        WandData newData = new WandData(newSlots, r.isOmniwand());

        ItemStack wand = data.getWand();
        wand.offer(new CustomWandDataImpl(newData));
        wand.offer(Keys.ITEM_LORE, Spells.wandLore(player, newData, wand.get(CustomItemData.class).map(c -> c.value().get()).orElse(null)));
        data.setWand(wand);
    }

    @Override
    public void sync(Player player, InventoryData data, GridInventory inventory) {
        RPGData.runtime(player).ifPresent(r -> {
            if (r.getSpellSlotModifying() == null) {
                Vector2i wandSlot = InventoryHelper.PAGE_START.add(1, 1);
                ItemStack wand = inventory.getSlot(wandSlot.getX(), wandSlot.getY()).get().poll().orElse(ItemStack.empty());
                if (!wand.equals(data.getWand())) {
                    data.setWand(wand);
                    r.setSpellSlotModifying(null);
                    r.setPage(0);
                    Optional<WandData> spells = wand.get(CustomWandData.class).map(c -> c.value().get());
                    if (spells.isPresent()) {
                        r.setSpellSlots(spells.get().getSlots().stream().map(SpellSlot::copy).collect(Collectors.toList()));
                        r.setOmniwand(spells.get().isOmniwand());
                    } else {
                        r.setSpellSlots(new ArrayList<>());
                        r.setOmniwand(false);
                    }
                }
            }
        });
    }

    @Override
    public String name(Player player, InventoryData data) {
        return Messages.translateString(player, "rpg.menu.wand");
    }

    @Override
    public int maxPage(Player player) {
        return 0;
    }

}
