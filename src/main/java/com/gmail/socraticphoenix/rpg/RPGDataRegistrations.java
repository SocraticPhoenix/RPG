package com.gmail.socraticphoenix.rpg;

import com.gmail.socraticphoenix.rpg.augment.AugmentSlot;
import com.gmail.socraticphoenix.rpg.click.ItemClickPredicate;
import com.gmail.socraticphoenix.rpg.click.data.ImmutableItemClickData;
import com.gmail.socraticphoenix.rpg.click.data.ImmutablePlayerClickData;
import com.gmail.socraticphoenix.rpg.click.data.ItemClickData;
import com.gmail.socraticphoenix.rpg.click.data.PlayerClickData;
import com.gmail.socraticphoenix.rpg.click.data.impl.ImmutableItemClickDataImpl;
import com.gmail.socraticphoenix.rpg.click.data.impl.ImmutablePlayerClickDataImpl;
import com.gmail.socraticphoenix.rpg.click.data.impl.ItemClickDataImpl;
import com.gmail.socraticphoenix.rpg.click.data.impl.ItemClickDataManipulatorBuilder;
import com.gmail.socraticphoenix.rpg.click.data.impl.PlayerClickDataImpl;
import com.gmail.socraticphoenix.rpg.click.data.impl.PlayerClickDataManipulatorBuilder;
import com.gmail.socraticphoenix.rpg.crafting.RPGCraftingRecipe;
import com.gmail.socraticphoenix.rpg.data.PlayerData;
import com.gmail.socraticphoenix.rpg.data.character.CharacterData;
import com.gmail.socraticphoenix.rpg.data.character.CooldownData;
import com.gmail.socraticphoenix.rpg.data.character.InventoryData;
import com.gmail.socraticphoenix.rpg.data.character.OptionData;
import com.gmail.socraticphoenix.rpg.data.character.QuestData;
import com.gmail.socraticphoenix.rpg.data.character.SpellBookData;
import com.gmail.socraticphoenix.rpg.data.character.StatData;
import com.gmail.socraticphoenix.rpg.data.item.ItemData;
import com.gmail.socraticphoenix.rpg.data.mob.MobData;
import com.gmail.socraticphoenix.rpg.data.item.WandData;
import com.gmail.socraticphoenix.rpg.data.mob.NPCData;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomItemData;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomItemDataBuilder;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomItemDataImpl;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomWandData;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomWandDataBuilder;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomWandDataImpl;
import com.gmail.socraticphoenix.rpg.data.sponge.item.ImmutableCustomItemData;
import com.gmail.socraticphoenix.rpg.data.sponge.item.ImmutableCustomItemDataImpl;
import com.gmail.socraticphoenix.rpg.data.sponge.item.ImmutableCustomWandData;
import com.gmail.socraticphoenix.rpg.data.sponge.item.ImmutableCustomWandDataImpl;
import com.gmail.socraticphoenix.rpg.data.sponge.mob.CustomMobData;
import com.gmail.socraticphoenix.rpg.data.sponge.mob.CustomMobDataBuilder;
import com.gmail.socraticphoenix.rpg.data.sponge.mob.CustomMobDataImpl;
import com.gmail.socraticphoenix.rpg.data.sponge.mob.CustomNPCData;
import com.gmail.socraticphoenix.rpg.data.sponge.mob.CustomNPCDataBuilder;
import com.gmail.socraticphoenix.rpg.data.sponge.mob.CustomNPCDataImpl;
import com.gmail.socraticphoenix.rpg.data.sponge.mob.ImmutableCustomMobData;
import com.gmail.socraticphoenix.rpg.data.sponge.mob.ImmutableCustomMobDataImpl;
import com.gmail.socraticphoenix.rpg.data.sponge.mob.ImmutableCustomNPCData;
import com.gmail.socraticphoenix.rpg.data.sponge.mob.ImmutableCustomNPCDataImpl;
import com.gmail.socraticphoenix.rpg.gods.God;
import com.gmail.socraticphoenix.rpg.inventory.button.ButtonAction;
import com.gmail.socraticphoenix.rpg.inventory.button.data.ButtonData;
import com.gmail.socraticphoenix.rpg.inventory.button.data.ImmutableButtonData;
import com.gmail.socraticphoenix.rpg.inventory.button.data.impl.ButtonDataImpl;
import com.gmail.socraticphoenix.rpg.inventory.button.data.impl.ButtonDataManipulatorBuilder;
import com.gmail.socraticphoenix.rpg.inventory.button.data.impl.ImmutableButtonDataImpl;
import com.gmail.socraticphoenix.rpg.inventory.player.SelectableMenu;
import com.gmail.socraticphoenix.rpg.inventory.storage.ItemStorage;
import com.gmail.socraticphoenix.rpg.inventory.storage.ItemStorageBuilder;
import com.gmail.socraticphoenix.rpg.modifiers.Modifier;
import com.gmail.socraticphoenix.rpg.modifiers.SetModifier;
import com.gmail.socraticphoenix.rpg.options.values.Option;
import com.gmail.socraticphoenix.rpg.options.values.SetOption;
import com.gmail.socraticphoenix.rpg.options.values.SetOptionMap;
import com.gmail.socraticphoenix.rpg.quest.Quest;
import com.gmail.socraticphoenix.rpg.quest.QuestObjective;
import com.gmail.socraticphoenix.rpg.registry.RPGRegistry;
import com.gmail.socraticphoenix.rpg.registry.RPGRegistryItemBuilder;
import com.gmail.socraticphoenix.rpg.scoreboard.ScoreboardStat;
import com.gmail.socraticphoenix.rpg.spell.Spell;
import com.gmail.socraticphoenix.rpg.spell.SpellSlot;
import com.gmail.socraticphoenix.rpg.spell.Type;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataManager;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.plugin.PluginContainer;

public class RPGDataRegistrations {
    private RPGPlugin plugin;
    private PluginContainer container;

    public RPGDataRegistrations(RPGPlugin plugin, PluginContainer container) {
        this.container = container;
        this.plugin = plugin;
    }

    @Listener
    public void onDataRegistration(GameRegistryEvent.Register<DataRegistration<?, ?>> event) {
        DataManager manager = Sponge.getDataManager();

        manager.registerBuilder(RPGRegistry.class, new RPGRegistryItemBuilder<>(RPGRegistry.class));
        manager.registerBuilder(ButtonAction.class, new RPGRegistryItemBuilder<>(ButtonAction.class));
        manager.registerBuilder(ItemClickPredicate.class, new RPGRegistryItemBuilder<>(ItemClickPredicate.class));
        manager.registerBuilder(SelectableMenu.class, new RPGRegistryItemBuilder<>(SelectableMenu.class));
        manager.registerBuilder(RPGCraftingRecipe.class, new RPGRegistryItemBuilder<>(RPGCraftingRecipe.class));
        manager.registerBuilder(ScoreboardStat.class, new RPGRegistryItemBuilder<>(ScoreboardStat.class));
        manager.registerBuilder(Option.class, new RPGRegistryItemBuilder<>(Option.class));
        manager.registerBuilder(Spell.class, new RPGRegistryItemBuilder<>(Spell.class));
        manager.registerBuilder(God.class, new RPGRegistryItemBuilder<>(God.class));
        manager.registerBuilder(Modifier.class, new RPGRegistryItemBuilder<>(Modifier.class));
        manager.registerBuilder(Type.class, new RPGRegistryItemBuilder<>(Type.class));
        manager.registerBuilder(Quest.class, new RPGRegistryItemBuilder<>(Quest.class));
        manager.registerBuilder(QuestObjective.class, new RPGRegistryItemBuilder<>(QuestObjective.class));

        manager.registerBuilder(SpellSlot.class, new SpellSlot.Builder());
        manager.registerBuilder(SetOption.class, new SetOption.Builder());
        manager.registerBuilder(SetOptionMap.class, new SetOptionMap.Builder());
        manager.registerBuilder(ItemStorage.class, new ItemStorageBuilder());
        manager.registerBuilder(StatData.class, new StatData.Builder());
        manager.registerBuilder(OptionData.class, new OptionData.Builder());
        manager.registerBuilder(SpellBookData.class, new SpellBookData.Builder());
        manager.registerBuilder(InventoryData.class, new InventoryData.Builder());
        manager.registerBuilder(CooldownData.class, new CooldownData.Builder());
        manager.registerBuilder(PlayerData.class, new PlayerData.Builder());
        manager.registerBuilder(CharacterData.class, new CharacterData.Builder());
        manager.registerBuilder(SetModifier.class, new SetModifier.Builder());
        manager.registerBuilder(ItemData.class, new ItemData.Builder());
        manager.registerBuilder(WandData.class, new WandData.Builder());
        manager.registerBuilder(MobData.class, new MobData.Builder());
        manager.registerBuilder(QuestData.class, new QuestData.Builder());
        manager.registerBuilder(NPCData.class, new NPCData.Builder());
        manager.registerBuilder(AugmentSlot.class, new AugmentSlot.Builder());

        DataRegistration.builder()
                .dataClass(PlayerClickData.class)
                .dataImplementation(PlayerClickDataImpl.class)
                .immutableClass(ImmutablePlayerClickData.class)
                .immutableImplementation(ImmutablePlayerClickDataImpl.class)
                .builder(new PlayerClickDataManipulatorBuilder())
                .dataName("Player Click Data")
                .manipulatorId(RPGPlugin.ID + ":player_click_data")
                .buildAndRegister(this.container);

        DataRegistration.builder()
                .dataClass(ItemClickData.class)
                .dataImplementation(ItemClickDataImpl.class)
                .immutableClass(ImmutableItemClickData.class)
                .immutableImplementation(ImmutableItemClickDataImpl.class)
                .builder(new ItemClickDataManipulatorBuilder(this.plugin))
                .dataName("Item Click Data")
                .manipulatorId(RPGPlugin.ID + ":item_click_data")
                .buildAndRegister(this.container);

        DataRegistration.builder()
                .dataClass(ButtonData.class)
                .dataImplementation(ButtonDataImpl.class)
                .immutableClass(ImmutableButtonData.class)
                .immutableImplementation(ImmutableButtonDataImpl.class)
                .builder(new ButtonDataManipulatorBuilder(this.plugin))
                .dataName("Button Data")
                .manipulatorId(RPGPlugin.ID + ":button_data")
                .buildAndRegister(this.container);

        DataRegistration.builder()
                .dataClass(CustomItemData.class)
                .dataImplementation(CustomItemDataImpl.class)
                .immutableClass(ImmutableCustomItemData.class)
                .immutableImplementation(ImmutableCustomItemDataImpl.class)
                .builder(new CustomItemDataBuilder())
                .dataName("Item Data")
                .manipulatorId(RPGPlugin.ID + ":item_data")
                .buildAndRegister(this.container);

        DataRegistration.builder()
                .dataClass(CustomWandData.class)
                .dataImplementation(CustomWandDataImpl.class)
                .immutableClass(ImmutableCustomWandData.class)
                .immutableImplementation(ImmutableCustomWandDataImpl.class)
                .builder(new CustomWandDataBuilder())
                .dataName("Wand Data")
                .manipulatorId(RPGPlugin.ID + ":wand_data")
                .buildAndRegister(this.container);

        DataRegistration.builder()
                .dataClass(CustomMobData.class)
                .dataImplementation(CustomMobDataImpl.class)
                .immutableClass(ImmutableCustomMobData.class)
                .immutableImplementation(ImmutableCustomMobDataImpl.class)
                .builder(new CustomMobDataBuilder())
                .dataName("Mob Data")
                .manipulatorId(RPGPlugin.ID + ":mob_data")
                .buildAndRegister(this.container);

        DataRegistration.builder()
                .dataClass(CustomNPCData.class)
                .dataImplementation(CustomNPCDataImpl.class)
                .immutableClass(ImmutableCustomNPCData.class)
                .immutableImplementation(ImmutableCustomNPCDataImpl.class)
                .builder(new CustomNPCDataBuilder())
                .dataName("NPC Data")
                .manipulatorId(RPGPlugin.ID + ":npc_data")
                .buildAndRegister(this.container);
    }

}
