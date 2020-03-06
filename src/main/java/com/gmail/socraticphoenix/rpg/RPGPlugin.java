package com.gmail.socraticphoenix.rpg;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.collect.coupling.KeyValue;
import com.gmail.socraticphoenix.rpg.augment.AugmentColor;
import com.gmail.socraticphoenix.rpg.augment.AugmentSlot;
import com.gmail.socraticphoenix.rpg.click.ClickListener;
import com.gmail.socraticphoenix.rpg.click.ClickType;
import com.gmail.socraticphoenix.rpg.click.data.ItemClickData;
import com.gmail.socraticphoenix.rpg.conversation.Conversations;
import com.gmail.socraticphoenix.rpg.crafting.AugmentCraftingRecipe;
import com.gmail.socraticphoenix.rpg.crafting.RPGCraftingRecipe;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.data.RPGDataStorage;
import com.gmail.socraticphoenix.rpg.data.RPGJsonDataStorage;
import com.gmail.socraticphoenix.rpg.data.character.CharacterData;
import com.gmail.socraticphoenix.rpg.data.item.ItemData;
import com.gmail.socraticphoenix.rpg.data.item.WandData;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomItemDataImpl;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomWandDataImpl;
import com.gmail.socraticphoenix.rpg.data.sponge.mob.CustomMobData;
import com.gmail.socraticphoenix.rpg.event.RPGEquipEvent;
import com.gmail.socraticphoenix.rpg.event.RPGGainSpellEvent;
import com.gmail.socraticphoenix.rpg.gods.Gods;
import com.gmail.socraticphoenix.rpg.inventory.ButtonActions;
import com.gmail.socraticphoenix.rpg.inventory.InventoryHelper;
import com.gmail.socraticphoenix.rpg.inventory.SelectableMenus;
import com.gmail.socraticphoenix.rpg.inventory.button.ButtonListener;
import com.gmail.socraticphoenix.rpg.inventory.button.data.ButtonData;
import com.gmail.socraticphoenix.rpg.inventory.player.HotbarListener;
import com.gmail.socraticphoenix.rpg.inventory.player.InventoryListener;
import com.gmail.socraticphoenix.rpg.inventory.storage.ItemStorage;
import com.gmail.socraticphoenix.rpg.modifiers.ModifiableTypes;
import com.gmail.socraticphoenix.rpg.modifiers.Modifiers;
import com.gmail.socraticphoenix.rpg.modifiers.SetModifier;
import com.gmail.socraticphoenix.rpg.options.Options;
import com.gmail.socraticphoenix.rpg.registry.RPGRegisterEvent;
import com.gmail.socraticphoenix.rpg.registry.RPGRegistries;
import com.gmail.socraticphoenix.rpg.registry.RPGRegistry;
import com.gmail.socraticphoenix.rpg.registry.RPGRegistryItem;
import com.gmail.socraticphoenix.rpg.registry.registries.ButtonActionRegistry;
import com.gmail.socraticphoenix.rpg.registry.registries.CraftingRecipeRegistry;
import com.gmail.socraticphoenix.rpg.registry.registries.GodRegistry;
import com.gmail.socraticphoenix.rpg.registry.registries.ItemClickPredicateRegistry;
import com.gmail.socraticphoenix.rpg.registry.registries.ModifierRegistry;
import com.gmail.socraticphoenix.rpg.registry.registries.OptionRegistry;
import com.gmail.socraticphoenix.rpg.registry.registries.QuestObjectiveRegistry;
import com.gmail.socraticphoenix.rpg.registry.registries.QuestRegistry;
import com.gmail.socraticphoenix.rpg.registry.registries.ScoreboardStatRegistry;
import com.gmail.socraticphoenix.rpg.registry.registries.SelectableMenuRegistry;
import com.gmail.socraticphoenix.rpg.registry.registries.SpellRegistry;
import com.gmail.socraticphoenix.rpg.registry.registries.TypeRegistry;
import com.gmail.socraticphoenix.rpg.scoreboard.ScoreboardStats;
import com.gmail.socraticphoenix.rpg.spell.SpellSlot;
import com.gmail.socraticphoenix.rpg.spell.Spells;
import com.gmail.socraticphoenix.rpg.spell.Types;
import com.gmail.socraticphoenix.rpg.stats.StatChangeListener;
import com.gmail.socraticphoenix.rpg.stats.StatHelper;
import com.gmail.socraticphoenix.rpg.translation.TranslationManager;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.entity.Hotbar;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Plugin(id = RPGPlugin.ID, name = RPGPlugin.NAME, version = RPGPlugin.VERSION)
public class RPGPlugin {
    public static final String ID = "socraticrpg";
    public static final String NAME = "RPGPlugin";
    public static final String VERSION = "1.0";

    private static RPGPlugin staticPlugin;

    @Inject
    private RPGPlugin plugin;
    @Inject
    private Logger logger;
    @Inject
    private PluginContainer container;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;
    private Path configPath;
    private Path songsPath;
    private Path translationsPath;

    private RPGRegistries registries;
    private TranslationManager translationManager;
    private RPGDataStorage dataStorage;


    @Listener(order = Order.PRE)
    public void onPreInit(GamePreInitializationEvent ev) throws IOException {
        configPath = configDir.resolve("config.conf");
        songsPath = configDir.resolve("songs");
        translationsPath = configDir.resolve("translations");
        try {
            Files.createDirectories(songsPath);
        } catch (IOException e) {
            logger.error("Failed to create songs directory");
            e.printStackTrace();
        }
        staticPlugin = plugin;
        registries = new RPGRegistries(this, this.container);

        registries.register(new ItemClickPredicateRegistry(),
                new ButtonActionRegistry(), new SelectableMenuRegistry(),
                new CraftingRecipeRegistry(), new ScoreboardStatRegistry(), new OptionRegistry(),
                new SpellRegistry(), new GodRegistry(), new ModifierRegistry(),
                new TypeRegistry(), new QuestRegistry(), new QuestObjectiveRegistry());

        translationManager = new TranslationManager(this);
        translationManager.load(this.translationsPath);

        dataStorage = new RPGJsonDataStorage();
        dataStorage.init();

        ScoreboardStats scoreboardStats = new ScoreboardStats();

        EventManager manager = Sponge.getEventManager();
        Scheduler scheduler = Sponge.getScheduler();

        manager.registerListeners(this, new RPGDataKeys());
        manager.registerListeners(this, new ClickListener());
        manager.registerListeners(this, new HotbarListener());
        manager.registerListeners(this, new InventoryListener());
        manager.registerListeners(this, new ButtonListener());
        manager.registerListeners(this, new ButtonActions());
        manager.registerListeners(this, new SelectableMenus());
        manager.registerListeners(this, new StatChangeListener());
        manager.registerListeners(this, new Options());
        manager.registerListeners(this, new Spells());
        manager.registerListeners(this, new Gods());
        manager.registerListeners(this, new Modifiers());
        manager.registerListeners(this, new Conversations());
        manager.registerListeners(this, new RPGDataRegistrations(this, this.container));
        manager.registerListeners(this, scoreboardStats);

        scheduler.createTaskBuilder().execute(scoreboardStats).intervalTicks(1).submit(this);
        scheduler.createTaskBuilder().execute(new RPGTickTask()).intervalTicks(1).submit(this);
        scheduler.createTaskBuilder().execute(new RegenTask()).intervalTicks(20 * 5).submit(this);
    }

    @Listener
    public void onInit(GameInitializationEvent ev) {
        Sponge.getEventManager().post(new RPGRegisterEvent(this, registries));
    }

    public static RPGPlugin getPlugin() {
        return staticPlugin;
    }

    public static <T extends RPGRegistryItem> Optional<RPGRegistry<T>> registryFor(Class<T> type) {
        return getPlugin().getRegistry().registryFor(type);
    }

    public RPGDataStorage getDataStorage() {
        return dataStorage;
    }

    public RPGRegistries getRegistry() {
        return registries;
    }

    public TranslationManager getTranslationManager() {
        return translationManager;
    }

    public PluginContainer getContainer() {
        return container;
    }

    public Logger getLogger() {
        return logger;
    }

    public Path getConfigDir() {
        return configDir;
    }

    public Path getConfigPath() {
        return configPath;
    }

    public Path getSongsPath() {
        return songsPath;
    }


    @Listener
    public void onLogin(ClientConnectionEvent.Join ev) {
        Player player = ev.getTargetEntity();
        this.dataStorage.getOrCreate(player, d -> {
            if (d.getCharacters().isEmpty()) {
                CharacterData dta = new CharacterData();
                dta.setName("Bob", "McPants");
                dta.setActive(true);

                d.getCharacters().add(dta);

                SetModifier dmgBoost = new SetModifier(Modifiers.SPELL_PERCENTAGE_DAMAGE_BOOST, Items.buildMap(HashMap::new, KeyValue.of(Modifiers.VALUE, 100.0), KeyValue.of(Modifiers.SPELL_TYPE, Types.TRIGGERED)));

                WandData spellSlotData = new WandData(Items.buildList(new SpellSlot(Spells.BLAST, Items.buildList(ClickType.PRIMARY),
                        Items.buildList(dmgBoost), true, true)), false);

                WandData spellSlotData2 = new WandData(Items.buildList(
                        new SpellSlot(Spells.BLAST, Items.buildList(ClickType.PRIMARY), Items.buildList(), false, false)), true);

                WandData spellSlotData3 = new WandData(Items.buildList(
                        new SpellSlot(Spells.SLASH, Items.buildList(ClickType.PRIMARY), Items.buildList(), true, false)), false);

                ItemData data1 = new ItemData(Items.buildList(), new AugmentSlot(AugmentColor.NONE, Items.buildList()), Items.buildList(
                        new AugmentSlot(AugmentColor.BLUE, Items.buildList()),
                        new AugmentSlot(AugmentColor.RED, Items.buildList(dmgBoost))
                ), ItemData.NONE);
                ItemData data2 = new ItemData(Items.buildList(), new AugmentSlot(AugmentColor.NONE, Items.buildList()), Items.buildList(
                        new AugmentSlot(AugmentColor.BLUE, Items.buildList(dmgBoost, dmgBoost)),
                        new AugmentSlot(AugmentColor.RED, Items.buildList())
                ), ItemData.NONE);
                ItemData data3 = new ItemData(Items.buildList(), new AugmentSlot(AugmentColor.BLUE, Items.buildList(dmgBoost)), Items.buildList(), ItemData.NONE);

                List<ItemStack> manyWands = Items.buildList(
                        ItemStack.builder()
                                .quantity(1)
                                .itemType(ItemTypes.STICK)
                                .add(Keys.DISPLAY_NAME, Text.of("Wand of Blasting"))
                                .add(Keys.ITEM_LORE, Spells.wandLore(player, spellSlotData, null))
                                .itemData(new CustomWandDataImpl(spellSlotData))
                                .itemData(ItemClickData.of(Spells.SPELL_PREDICATE, 1500))
                                .build(),
                        ItemStack.builder()
                                .quantity(1)
                                .itemType(ItemTypes.STICK)
                                .add(Keys.DISPLAY_NAME, Text.of("Omni-Wand"))
                                .add(Keys.ITEM_LORE, Spells.wandLore(player, spellSlotData2, data2))
                                .itemData(new CustomItemDataImpl(data2))
                                .itemData(new CustomWandDataImpl(spellSlotData2))
                                .itemData(ItemClickData.of(Spells.SPELL_PREDICATE, 1500))
                                .build(),
                        ItemStack.builder()
                                .quantity(1)
                                .itemType(ItemTypes.STONE_SWORD)
                                .add(Keys.DISPLAY_NAME, Text.of("\"Wand\""))
                                .add(Keys.ITEM_LORE, Spells.wandLore(player, spellSlotData3, data1))
                                .itemData(new CustomItemDataImpl(data1))
                                .itemData(new CustomWandDataImpl(spellSlotData3))
                                .itemData(ItemClickData.of(Spells.SPELL_PREDICATE, 1500))
                                .build(),
                        ItemStack.builder()
                                .quantity(1)
                                .itemType(ItemTypes.EMERALD)
                                .add(Keys.DISPLAY_NAME, Text.of("Emerald Augment of Damage"))
                                .add(Keys.ITEM_LORE, Spells.wandLore(player, null, data3))
                                .itemData(new CustomItemDataImpl(data3))
                                .build()
                );
                ItemStorage.Flat storage = dta.getInventory().getHotbar().flatView();
                storage.addAll(manyWands);

                dta.getGods().add(Gods.KARZAMOC);
                dta.getSpellBook().getSpells().addAll(Gods.KARZAMOC.spells());

                Hotbar hotbar = player.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(Hotbar.class));
                hotbar.set(SlotIndex.of(0), ItemStack.builder().itemType(ItemTypes.WRITABLE_BOOK).itemData(InventoryHelper.createNoopButton()).add(Keys.DISPLAY_NAME, Text.of("Quest Book")).build());
                hotbar.set(SlotIndex.of(1), ItemStack.builder().itemType(ItemTypes.CHEST).itemData(ButtonData.of(ButtonActions.OPEN_INVENTORY)).add(Keys.DISPLAY_NAME, Text.of("Open Inventory")).build());
                hotbar.setSelectedSlotIndex(HotbarListener.SELECTED_SLOT);
            }

            InventoryHelper.updateAll(player);
            ItemStack held = player.getItemInHand(HandTypes.MAIN_HAND).orElse(ItemStack.empty());
            Sponge.getEventManager().post(new RPGEquipEvent(held, held, player));
            RPGData.spellbook(player).ifPresent(s -> s.getSpells().forEach(sp -> Sponge.getEventManager().post(new RPGGainSpellEvent(sp, player))));
        });
    }

    @Listener
    public void  onSpawn(SpawnEntityEvent ev) {
        ev.getEntities().forEach(e -> {
            if (e instanceof Living) {
                e.offer(e.getOrCreate(CustomMobData.class).get());
                StatHelper.updateHealth((Living) e, (Living) e);
            }
        });
    }

    @Listener
    public void onLogoff(ClientConnectionEvent.Disconnect ev) {
        this.dataStorage.saveAndRemove(ev.getTargetEntity());
    }

    @Listener
    public void onShutdown(GameStoppedServerEvent ev) {
        this.dataStorage.saveAll();
    }

    @Listener
    public void onRegister(RPGRegisterEvent ev) {
        Types.register(ev);
        ModifiableTypes.register(ev);
        ev.register(RPGCraftingRecipe.class, new AugmentCraftingRecipe());
    }

}
