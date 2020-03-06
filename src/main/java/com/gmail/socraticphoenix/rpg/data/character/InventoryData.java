package com.gmail.socraticphoenix.rpg.data.character;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.inventory.SelectableMenus;
import com.gmail.socraticphoenix.rpg.inventory.player.SelectableMenu;
import com.gmail.socraticphoenix.rpg.inventory.storage.ItemStorage;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InventoryData extends RPGData<InventoryData> {
    private List<ItemStorage> bankPages;
    private ItemStorage carried;
    private ItemStorage hotbar;
    private ItemStorage trinkets;
    private ItemStorage ammo;
    private ItemStorage potions;
    private ItemStorage crafting;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private ItemStack wand;
    private int selectedSlot;
    private int maxPage;

    public InventoryData(List<ItemStorage> bankPages, ItemStorage carried, ItemStorage hotbar, ItemStorage trinkets, ItemStorage ammo, ItemStorage potions, ItemStorage crafting, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, ItemStack wand, int selectedSlot, int maxPage) {
        super(0);
        this.bankPages = bankPages;
        this.carried = carried;
        this.hotbar = hotbar;
        this.trinkets = trinkets;
        this.ammo = ammo;
        this.potions = potions;
        this.crafting = crafting;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.wand = wand;
        this.selectedSlot = selectedSlot;
        this.maxPage = maxPage;
    }

    public List<ItemStorage> getBankPages() {
        return bankPages;
    }

    public ItemStorage getCarried() {
        return carried;
    }

    public ItemStorage getHotbar() {
        return hotbar;
    }

    public ItemStorage getTrinkets() {
        return trinkets;
    }

    public ItemStorage getAmmo() {
        return ammo;
    }

    public ItemStorage getPotions() {
        return potions;
    }

    public ItemStorage getCrafting() {
        return crafting;
    }

    public ItemStack getHelmet() {
        return helmet;
    }

    public ItemStack getChestplate() {
        return chestplate;
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public ItemStack getBoots() {
        return boots;
    }

    public int getSelectedSlot() {
        return selectedSlot;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setHelmet(ItemStack helmet) {
        this.helmet = helmet;
    }

    public void setChestplate(ItemStack chestplate) {
        this.chestplate = chestplate;
    }

    public void setLeggings(ItemStack leggings) {
        this.leggings = leggings;
    }

    public void setBoots(ItemStack boots) {
        this.boots = boots;
    }

    public void setSelectedSlot(int selectedSlot) {
        this.selectedSlot = selectedSlot;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public void setCarried(ItemStorage carried) {
        this.carried = carried;
    }

    public void setHotbar(ItemStorage hotbar) {
        this.hotbar = hotbar;
    }

    public void setTrinkets(ItemStorage trinkets) {
        this.trinkets = trinkets;
    }

    public void setAmmo(ItemStorage ammo) {
        this.ammo = ammo;
    }

    public void setPotions(ItemStorage potions) {
        this.potions = potions;
    }

    public void setCrafting(ItemStorage crafting) {
        this.crafting = crafting;
    }

    public ItemStack getWand() {
        return wand;
    }

    public void setWand(ItemStack wand) {
        this.wand = wand;
    }

    public InventoryData() {
        this(Items.buildList(ItemStorage.of(9, 3)),
                ItemStorage.of(9, 3),
                ItemStorage.of(9, 2),
                ItemStorage.of(5, 1),
                ItemStorage.of(9, 1),
                ItemStorage.of(9, 1),
                ItemStorage.of(3, 3),
                ItemStack.empty(),
                ItemStack.empty(),
                ItemStack.empty(),
                ItemStack.empty(),
                ItemStack.empty(),
                0,
                10);
    }

    @Override
    public InventoryData copy() {
        return new InventoryData(this.bankPages.stream().map(ItemStorage::copy).collect(Collectors.toList()),
                this.carried.copy(), this.hotbar.copy(), this.trinkets.copy(), this.ammo.copy(), this.potions.copy(), this.crafting.copy(),
                this.helmet.copy(), this.chestplate.copy(), this.leggings.copy(), this.boots.copy(), this.wand.copy(), this.selectedSlot, this.maxPage);
    }

    @Override
    public DataContainer fill(DataContainer container) {
        return container.set(INVENTORY_PAGES, this.bankPages)
                .set(INVENTORY_CARRIED, this.carried)
                .set(INVENTORY_HOTBAR, this.hotbar)
                .set(INVENTORY_TRINKETS, this.trinkets)
                .set(INVENTORY_AMMO, this.ammo)
                .set(INVENTORY_POTIONS, this.potions)
                .set(INVENTORY_CRAFTING, this.crafting)
                .set(INVENTORY_HELMET, this.helmet)
                .set(INVENTORY_CHESTPLATE, this.chestplate)
                .set(INVENTORY_LEGGINGS, this.leggings)
                .set(INVENTORY_BOOTS, this.boots)
                .set(INVENTORY_WAND, this.wand)
                .set(INVENTORY_SELECTED_SLOT, this.selectedSlot)
                .set(INVENTORY_MAX_PAGE, this.maxPage);
    }

    @Override
    public Optional<InventoryData> from(DataView container) {
        if (!container.contains(INVENTORY_PAGES, INVENTORY_CARRIED, INVENTORY_HOTBAR, INVENTORY_TRINKETS,
                INVENTORY_AMMO, INVENTORY_POTIONS, INVENTORY_CRAFTING, INVENTORY_HELMET, INVENTORY_CHESTPLATE, INVENTORY_WAND,
                INVENTORY_LEGGINGS, INVENTORY_BOOTS, INVENTORY_SELECTED_SLOT, INVENTORY_MAX_PAGE)) {
            return Optional.empty();
        }

        this.bankPages = container.getSerializableList(INVENTORY_PAGES, ItemStorage.class).get();
        this.carried = container.getSerializable(INVENTORY_CARRIED, ItemStorage.class).get();
        this.hotbar = container.getSerializable(INVENTORY_HOTBAR, ItemStorage.class).get();
        this.trinkets = container.getSerializable(INVENTORY_TRINKETS, ItemStorage.class).get();
        this.ammo = container.getSerializable(INVENTORY_AMMO, ItemStorage.class).get();
        this.potions = container.getSerializable(INVENTORY_POTIONS, ItemStorage.class).get();
        this.crafting = container.getSerializable(INVENTORY_CRAFTING, ItemStorage.class).get();
        this.helmet = container.getSerializable(INVENTORY_HELMET, ItemStack.class).get();
        this.chestplate = container.getSerializable(INVENTORY_CHESTPLATE, ItemStack.class).get();
        this.leggings = container.getSerializable(INVENTORY_LEGGINGS, ItemStack.class).get();
        this.boots = container.getSerializable(INVENTORY_BOOTS, ItemStack.class).get();
        this.wand = container.getSerializable(INVENTORY_WAND, ItemStack.class).get();
        this.selectedSlot = container.getInt(INVENTORY_SELECTED_SLOT).get();
        this.maxPage = container.getInt(INVENTORY_MAX_PAGE).get();

        return Optional.of(this);
    }

    public static class Builder implements DataBuilder<InventoryData> {

        @Override
        public Optional<InventoryData> build(DataView container) throws InvalidDataException {
            return new InventoryData().from(container);
        }

    }

}
