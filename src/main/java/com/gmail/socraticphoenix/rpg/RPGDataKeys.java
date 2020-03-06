package com.gmail.socraticphoenix.rpg;

import com.gmail.socraticphoenix.rpg.click.ClickType;
import com.gmail.socraticphoenix.rpg.click.ItemClickPredicate;
import com.gmail.socraticphoenix.rpg.data.item.ItemData;
import com.gmail.socraticphoenix.rpg.data.mob.MobData;
import com.gmail.socraticphoenix.rpg.data.item.WandData;
import com.gmail.socraticphoenix.rpg.data.mob.NPCData;
import com.gmail.socraticphoenix.rpg.inventory.button.ButtonAction;
import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.ListValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.util.generator.dummy.DummyObjectProvider;

public class RPGDataKeys {
    public static Key<Value<Long>> LAST_CLICKED = DummyObjectProvider.createExtendedFor(Key.class, "LAST_CLICKED");
    public static Key<ListValue<ClickType>> CLICK_SEQUENCE = DummyObjectProvider.createExtendedFor(Key.class, "CLICK_SEQUENCE");

    public static Key<Value<ItemClickPredicate>> CLICK_PREDICATE = DummyObjectProvider.createExtendedFor(Key.class, "CLICK_PREDICATE");
    public static Key<Value<Long>> CLICK_INTERVAL = DummyObjectProvider.createExtendedFor(Key.class, "CLICK_INTERVAL");

    public static Key<Value<ButtonAction>> BUTTON_ACTION = DummyObjectProvider.createExtendedFor(Key.class, "BUTTON_ACTION");

    public static Key<Value<WandData>> WAND_DATA = DummyObjectProvider.createExtendedFor(Key.class, "WAND_DATA");
    public static Key<Value<ItemData>> ITEM_DATA = DummyObjectProvider.createExtendedFor(Key.class, "ITEM_DATA");
    public static Key<Value<MobData>> MOB_DATA = DummyObjectProvider.createExtendedFor(Key.class, "MOB_DATA");
    public static Key<Value<NPCData>> NPC_DATA = DummyObjectProvider.createExtendedFor(Key.class, "NPC_DATA");

    @Listener
    public void onRegistration(GameRegistryEvent.Register<Key<?>> event) {
        LAST_CLICKED = Key.builder()
                .type(new TypeToken<Value<Long>>() {})
                .id("last_clicked")
                .name("Last Clicked")
                .query(DataQuery.of("LastClicked"))
                .build();
        event.register(LAST_CLICKED);
        CLICK_SEQUENCE = Key.builder()
                .type(new TypeToken<ListValue<ClickType>>() {})
                .id("click_sequence")
                .name("Click Sequence")
                .query(DataQuery.of("ClickSequence"))
                .build();
        event.register(CLICK_SEQUENCE);
        CLICK_PREDICATE = Key.builder()
                .type(new TypeToken<Value<ItemClickPredicate>>() {})
                .id("click_predicate")
                .name("Click Predicate")
                .query(DataQuery.of("ClickPredicate"))
                .build();
        event.register(CLICK_PREDICATE);
        CLICK_INTERVAL = Key.builder()
                .type(new TypeToken<Value<Long>>() {})
                .id("click_interval")
                .name("Click Interval")
                .query(DataQuery.of("ClickInterval"))
                .build();
        event.register(CLICK_INTERVAL);
        BUTTON_ACTION = Key.builder()
                .type(new TypeToken<Value<ButtonAction>>() {})
                .id("button_action")
                .name("Button Action")
                .query(DataQuery.of("ButtonAction"))
                .build();
        event.register(BUTTON_ACTION);
        WAND_DATA = Key.builder()
                .type(new TypeToken<Value<WandData>>() {})
                .id("wand_data")
                .name("Wand Data")
                .query(DataQuery.of("WandData"))
                .build();
        event.register(WAND_DATA);
        ITEM_DATA = Key.builder()
                .type(new TypeToken<Value<ItemData>>() {})
                .id("item_data")
                .name("Item Data")
                .query(DataQuery.of("ItemData"))
                .build();
        event.register(ITEM_DATA);
        MOB_DATA = Key.builder()
                .type(new TypeToken<Value<MobData>>() {})
                .id("mob_data")
                .name("Mob Data")
                .query(DataQuery.of("MobData"))
                .build();
        event.register(MOB_DATA);
        NPC_DATA = Key.builder()
                .type(new TypeToken<Value<NPCData>>() {})
                .id("npc_data")
                .name("NPC Data")
                .query(DataQuery.of("NPCData"))
                .build();
        event.register(NPC_DATA);
    }

}
