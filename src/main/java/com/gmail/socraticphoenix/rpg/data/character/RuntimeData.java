package com.gmail.socraticphoenix.rpg.data.character;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.inventory.SelectableMenus;
import com.gmail.socraticphoenix.rpg.inventory.player.SelectableMenu;
import com.gmail.socraticphoenix.rpg.modifiers.SetModifier;
import com.gmail.socraticphoenix.rpg.modifiers.SortedList;
import com.gmail.socraticphoenix.rpg.options.values.SetOption;
import com.gmail.socraticphoenix.rpg.spell.SpellSlot;
import org.spongepowered.api.entity.living.player.Player;

import java.util.ArrayList;
import java.util.List;

public class RuntimeData {
    private SelectableMenu menu;
    private int page;

    private SetOption optionModifying;

    private List<SetModifier> modifiers;

    private List<SpellSlot> spellSlots = new ArrayList<>();
    private boolean omniwand;
    private SpellSlot spellSlotModifying;

    private List<Player> party;

    public RuntimeData(SelectableMenu menu, int page) {
        this.menu = menu;
        this.page = page;
        this.modifiers = new SortedList<>();
        this.party = new ArrayList<>();
    }

    public RuntimeData(SelectableMenu menu, int page, SetOption optionModifying, List<SetModifier> modifiers, List<SpellSlot> spellSlots, boolean omniwand, SpellSlot spellSlotModifying, List<Player> party) {
        this.menu = menu;
        this.page = page;
        this.optionModifying = optionModifying;
        this.modifiers = modifiers;
        this.spellSlots = spellSlots;
        this.omniwand = omniwand;
        this.spellSlotModifying = spellSlotModifying;
        this.party = party;
    }

    public RuntimeData() {
        this(SelectableMenus.BANK_INVENTORY, 0);
    }

    public RuntimeData copy() {
        return new RuntimeData(menu, page, optionModifying == null ? null : optionModifying.copy(), Items.looseClone(modifiers, SortedList::new), Items.looseClone(spellSlots), omniwand, spellSlotModifying == null ? null : spellSlotModifying.copy(), Items.looseClone(this.party));
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
