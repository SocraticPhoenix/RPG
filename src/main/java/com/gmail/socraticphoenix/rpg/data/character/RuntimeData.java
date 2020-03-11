package com.gmail.socraticphoenix.rpg.data.character;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.augment.AugmentSlot;
import com.gmail.socraticphoenix.rpg.data.item.ItemData;
import com.gmail.socraticphoenix.rpg.inventory.SelectableMenus;
import com.gmail.socraticphoenix.rpg.inventory.SelectableMenu;
import com.gmail.socraticphoenix.rpg.modifiers.SetModifier;
import com.gmail.socraticphoenix.rpg.modifiers.SortedList;
import com.gmail.socraticphoenix.rpg.options.values.SetOption;
import com.gmail.socraticphoenix.rpg.spell.SpellSlot;
import org.spongepowered.api.entity.living.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RuntimeData {
    private SelectableMenu menu;
    private int page;

    private List<SetModifier> modifiers;
    private List<Player> party;

    private SetOption optionModifying;

    private List<SpellSlot> spellSlots = new ArrayList<>();
    private boolean omniwand;
    private SpellSlot spellSlotModifying;

    private ItemData augmentModifying;
    private List<SpellSlot> augmentSpellSlots = new ArrayList<>();
    private SpellSlot augmentSpellSlotModifying;

    public RuntimeData(SelectableMenu menu, int page) {
        this.menu = menu;
        this.page = page;
        this.modifiers = new SortedList<>();
        this.party = new ArrayList<>();
    }

    public RuntimeData(SelectableMenu menu, int page, SetOption optionModifying, List<SetModifier> modifiers, List<SpellSlot> spellSlots, boolean omniwand, SpellSlot spellSlotModifying, List<SpellSlot> augmentSpellSlots, SpellSlot augmentSpellSlotModifying, ItemData augmentModifying, List<Player> party) {
        this.menu = menu;
        this.page = page;
        this.optionModifying = optionModifying;
        this.modifiers = modifiers;
        this.spellSlots = spellSlots;
        this.omniwand = omniwand;
        this.spellSlotModifying = spellSlotModifying;
        this.augmentSpellSlots = augmentSpellSlots;
        this.augmentSpellSlotModifying = augmentSpellSlotModifying;
        this.party = party;
        this.augmentModifying = augmentModifying;
    }

    public RuntimeData() {
        this(SelectableMenus.BANK_INVENTORY, 0);
    }

    public RuntimeData copy() {
        return new RuntimeData(menu, page, optionModifying == null ? null : optionModifying.copy(), new SortedList<>(this.modifiers.stream().map(SetModifier::copy).collect(Collectors.toList())),
                this.spellSlots.stream().map(SpellSlot::copy).collect(Collectors.toList()), omniwand, spellSlotModifying == null ? null : spellSlotModifying.copy(),
                this.augmentSpellSlots.stream().map(SpellSlot::copy).collect(Collectors.toList()), augmentSpellSlotModifying == null ? null : augmentSpellSlotModifying.copy(), this.augmentModifying.copy(), Items.looseClone(this.party));
    }

    public List<SpellSlot> getAugmentSpellSlots() {
        return augmentSpellSlots;
    }

    public ItemData getAugmentModifying() {
        return augmentModifying;
    }

    public void setAugmentSpellSlots(List<SpellSlot> augmentSpellSlots) {
        this.augmentSpellSlots = augmentSpellSlots;
    }

    public void setAugmentModifying(ItemData augmentModifying) {
        this.augmentModifying = augmentModifying;
    }

    public SpellSlot getAugmentSpellSlotModifying() {
        return augmentSpellSlotModifying;
    }

    public void setAugmentSpellSlotModifying(SpellSlot augmentSpellSlotModifying) {
        this.augmentSpellSlotModifying = augmentSpellSlotModifying;
    }

    public List<Player> getParty() {
        return party;
    }

    public List<SpellSlot> getSpellSlots() {
        return spellSlots;
    }

    public void setSpellSlots(List<SpellSlot> spellSlots) {
        this.spellSlots = spellSlots;
    }

    public boolean isOmniwand() {
        return omniwand;
    }

    public void setOmniwand(boolean omniwand) {
        this.omniwand = omniwand;
    }

    public SpellSlot getSpellSlotModifying() {
        return spellSlotModifying;
    }

    public void setSpellSlotModifying(SpellSlot spellSlotModifying) {
        this.spellSlotModifying = spellSlotModifying;
    }

    public SetOption getOptionModifying() {
        return optionModifying;
    }

    public void setOptionModifying(SetOption optionModifying) {
        this.optionModifying = optionModifying;
    }

    public SelectableMenu getMenu() {
        return menu;
    }

    public List<SetModifier> getModifiers() {
        return modifiers;
    }

    public void setModifiers(List<SetModifier> modifiers) {
        this.modifiers = modifiers;
    }

    public void setMenu(SelectableMenu menu) {
        this.menu = menu;
        this.page = 0;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

}
