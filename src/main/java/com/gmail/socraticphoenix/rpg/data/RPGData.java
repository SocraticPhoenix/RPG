package com.gmail.socraticphoenix.rpg.data;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.data.character.CharacterData;
import com.gmail.socraticphoenix.rpg.data.character.CooldownData;
import com.gmail.socraticphoenix.rpg.data.character.InventoryData;
import com.gmail.socraticphoenix.rpg.data.character.OptionData;
import com.gmail.socraticphoenix.rpg.data.character.RuntimeData;
import com.gmail.socraticphoenix.rpg.data.character.SpellBookData;
import com.gmail.socraticphoenix.rpg.data.character.StatData;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.Queries;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Optional;

public abstract class RPGData<T extends RPGData<T>> implements DataSerializable {
    public static final DataQuery ID = DataQuery.of("ID");
    public static final DataQuery OWNER = DataQuery.of("Owner");
    public static final DataQuery TYPE = DataQuery.of("Type");

    public static final DataQuery ITEM_MAP = DataQuery.of("ItemMap");
    public static final DataQuery DIMENSIONS = DataQuery.of("Dimensions");
    public static final DataQuery POSITION = DataQuery.of("Position");
    public static final DataQuery ITEM = DataQuery.of("Item");

    public static final DataQuery OPTION = DataQuery.of("Option");
    public static final DataQuery OPTION_MAP = DataQuery.of("OptionMap");

    public static final DataQuery SPELL = DataQuery.of("Spell");
    public static final DataQuery SEQUENCE = DataQuery.of("Sequence");
    public static final DataQuery LOCKED = DataQuery.of("Locked");
    public static final DataQuery BYPASS = DataQuery.of("Bypass");

    public static final DataQuery CHARACTERS = DataQuery.of("Characters");
    public static final DataQuery PLAYER_DATA = DataQuery.of("PlayerData");

    public static final DataQuery INVENTORY_PAGES = DataQuery.of("InventoryPages");
    public static final DataQuery INVENTORY_CARRIED = DataQuery.of("InventoryCarried");
    public static final DataQuery INVENTORY_HOTBAR = DataQuery.of("InventoryHotbar");
    public static final DataQuery INVENTORY_TRINKETS = DataQuery.of("InventoryTrinkets");
    public static final DataQuery INVENTORY_AMMO = DataQuery.of("InventoryAmmo");
    public static final DataQuery INVENTORY_POTIONS = DataQuery.of("InventoryPotions");
    public static final DataQuery INVENTORY_HELMET = DataQuery.of("InventoryHelmet");
    public static final DataQuery INVENTORY_CHESTPLATE = DataQuery.of("InventoryChestplate");
    public static final DataQuery INVENTORY_LEGGINGS = DataQuery.of("InventoryLeggings");
    public static final DataQuery INVENTORY_BOOTS = DataQuery.of("InventoryBoots");
    public static final DataQuery INVENTORY_WAND = DataQuery.of("InventoryWand");
    public static final DataQuery INVENTORY_CRAFTING = DataQuery.of("InventoryCrafting");
    public static final DataQuery INVENTORY_SELECTED_SLOT = DataQuery.of("InventorySelectedSlot");
    public static final DataQuery INVENTORY_MAX_PAGE = DataQuery.of("InventoryMaxPage");

    public static final DataQuery STATS_XP = DataQuery.of("StatsXp");
    public static final DataQuery STATS_HEALTH = DataQuery.of("StatsHealth");
    public static final DataQuery STATS_MANA = DataQuery.of("StatsMana");
    public static final DataQuery STATS_MAX_HEALTH = DataQuery.of("StatsMaxHealth");
    public static final DataQuery STATS_MAX_MANA = DataQuery.of("StatsMaxMana");

    public static final DataQuery OPTIONS = DataQuery.of("Options");
    public static final DataQuery SPELLS = DataQuery.of("Spells");

    public static final DataQuery FIRST_NAME = DataQuery.of("FName");
    public static final DataQuery LAST_NAME = DataQuery.of("LName");
    public static final DataQuery ASCENDED = DataQuery.of("Ascended");
    public static final DataQuery GODS = DataQuery.of("Gods");
    public static final DataQuery INVENTORY = DataQuery.of("Inventory");
    public static final DataQuery SPELL_BOOK = DataQuery.of("SpellBook");
    public static final DataQuery STATS = DataQuery.of("Stats");
    public static final DataQuery ACTIVE = DataQuery.of("Active");

    public static final DataQuery COOLDOWNS = DataQuery.of("Cooldowns");
    public static final DataQuery COOLDOWN = DataQuery.of("Cooldown");

    public static final DataQuery MODIFIER = DataQuery.of("Modifier");
    public static final DataQuery ARGUMENTS = DataQuery.of("Arguments");

    public static final DataQuery MODIFIERS = DataQuery.of("Modifiers");
    public static final DataQuery AUGMENTS = DataQuery.of("Augments");
    public static final DataQuery AUGMENT_SLOTS = DataQuery.of("AugmentSlots");
    public static final DataQuery AUGMENT_COLOR = DataQuery.of("AugmentColor");
    public static final DataQuery OMNIWAND = DataQuery.of("Omniwand");
    public static final DataQuery SPELL_SLOTS = DataQuery.of("SpellSlots");

    public static final DataQuery QUESTS = DataQuery.of("Quests");
    public static final DataQuery KEY = DataQuery.of("Key");
    public static final DataQuery VALUE = DataQuery.of("Value");

    public static DataQuery COMPLETED = DataQuery.of("Completed");
    public static final DataQuery CONVERSATION = DataQuery.of("Conversation");

    public static Optional<CharacterData> active(Player player) {
        return RPGPlugin.getPlugin().getDataStorage().get(player).flatMap(RPGPlayerData::activeCharacter);
    }

    public static Optional<InventoryData> inventory(Player player) {
        return active(player).map(CharacterData::getInventory);
    }

    public static Optional<OptionData> options(Player player) {
        return active(player).map(CharacterData::getOptions);
    }

    public static Optional<SpellBookData> spellbook(Player player) {
        return active(player).map(CharacterData::getSpellBook);
    }

    public static Optional<CooldownData> cooldowns(Player player) {
        return active(player).map(CharacterData::getCooldowns);
    }

    public static Optional<StatData> stats(Player player) {
        return active(player).map(CharacterData::getStats);
    }

    public static Optional<RuntimeData> runtime(Player player) {
        return active(player).map(CharacterData::getRuntime);
    }

    private int contentVersion;

    public RPGData(int contentVersion) {
        this.contentVersion = contentVersion;
    }

    public abstract T copy();

    @Override
    public int getContentVersion() {
        return this.contentVersion;
    }

    @Override
    public DataContainer toContainer() {
        return fill(DataContainer.createNew().set(Queries.CONTENT_VERSION, this.getContentVersion()));
    }

    public abstract DataContainer fill(DataContainer container);

    public abstract Optional<T> from(DataView container);

}
