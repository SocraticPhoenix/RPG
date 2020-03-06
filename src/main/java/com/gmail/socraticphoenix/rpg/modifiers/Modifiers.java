package com.gmail.socraticphoenix.rpg.modifiers;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.collect.coupling.KeyValue;
import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.data.character.RuntimeData;
import com.gmail.socraticphoenix.rpg.data.item.ItemData;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomItemData;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomWandData;
import com.gmail.socraticphoenix.rpg.data.sponge.mob.CustomMobData;
import com.gmail.socraticphoenix.rpg.event.RPGEquipEvent;
import com.gmail.socraticphoenix.rpg.event.RPGGainSpellEvent;
import com.gmail.socraticphoenix.rpg.registry.RPGRegisterEvent;
import com.gmail.socraticphoenix.rpg.spell.Spell;
import com.gmail.socraticphoenix.rpg.spell.Type;
import com.gmail.socraticphoenix.rpg.spell.Types;
import com.gmail.socraticphoenix.rpg.translation.Messages;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

public class Modifiers {
    public static String VALUE = "Value";
    public static String SPELL_TYPE = "SpellType";
    public static String TYPE = "Type";
    public static String SPELL = "Spell";

    private static final BiFunction<String, DataView, Object> reader = (key, view) -> {
        if (key.equals(VALUE)) {
            return view.getDouble(DataQuery.of(key)).get();
        } else if (key.equals(SPELL_TYPE)) {
            return view.getSerializable(DataQuery.of(key), Type.class).get();
        }
        return null;
    };

    public static final Modifier<Double> SPELL_PERCENTAGE_DAMAGE_BOOST = new Modifier<>(
            ModifierFunctions.PERCENTAGE_BOOST, ModifierConditions.type(ModifiableTypes.DAMAGE_INCREASE).and(ModifierConditions.SPELL_TYPE),
            (player, stringMap) -> Messages.translateString(player, "rpg.mods.percent_damage", stringMap.get(VALUE), Messages.translateString(player, ((Type) stringMap.get(SPELL_TYPE)).getKey())),
            reader,
            Items.buildMap(HashMap::new, KeyValue.of(VALUE, 0.0), KeyValue.of(SPELL_TYPE, Types.TRIGGERED)),
            20, RPGPlugin.ID, "spell_type_percentage_damage_boost");

    public static final Modifier<Double> SPELL_FLAT_DAMAGE_BOOST = new Modifier<>(
            ModifierFunctions.FLAT_BOOST, ModifierConditions.type(ModifiableTypes.DAMAGE_INCREASE).and(ModifierConditions.SPELL_TYPE),
            (player, stringMap) -> Messages.translateString(player, "rpg.mods.flat_damage", stringMap.get(VALUE), Messages.translateString(player, ((Type) stringMap.get(SPELL_TYPE)).getKey())),
            reader,
            Items.buildMap(HashMap::new, KeyValue.of(VALUE, 0.0), KeyValue.of(SPELL_TYPE, Types.TRIGGERED)),
            10, RPGPlugin.ID, "spell_type_flat_damage_boost");

    public static final Modifier<Double> FLAT_SPEED_BOOST = new Modifier<>(
            ModifierFunctions.FLAT_BOOST, ModifierConditions.type(ModifiableTypes.SPEED),
            (player, stringMap) -> Messages.translateString(player, "rpg.mods.flat_speed", stringMap.get(VALUE)),
            reader,
            Items.buildMap(HashMap::new, KeyValue.of(VALUE, 0.0)),
            10, RPGPlugin.ID, "flat_speed_boost");

    public static final Modifier<Double> FLAT_JUMP_BOOST = new Modifier<>(
            ModifierFunctions.FLAT_BOOST, ModifierConditions.type(ModifiableTypes.JUMP),
            (player, stringMap) -> Messages.translateString(player, "rpg.mods.flat_jump", stringMap.get(VALUE)),
            reader,
            Items.buildMap(HashMap::new, KeyValue.of(VALUE, 0.0)),
            10, RPGPlugin.ID, "flat_jump_boost");

    @Listener
    public void onRegister(RPGRegisterEvent ev) {
        ev.register(Modifier.class, SPELL_PERCENTAGE_DAMAGE_BOOST, SPELL_FLAT_DAMAGE_BOOST, FLAT_SPEED_BOOST, FLAT_JUMP_BOOST);
    }

    @Listener
    public void onEquip(RPGEquipEvent ev, @First Player player) {
        RPGData.runtime(player).ifPresent(r -> RPGData.spellbook(player).ifPresent(s -> {
            r.getModifiers().removeAll(Modifiers.getRelevant(ev.getPrevious(), s.getSpells()));
            r.getModifiers().addAll(Modifiers.getRelevant(ev.getCurrent(), s.getSpells()));
        }));
    }

    @Listener
    public void onSpell(RPGGainSpellEvent ev, @First Player player) {
        RPGData.runtime(player).ifPresent(r -> {
            r.getModifiers().removeAll(ev.getSpell().passiveModifiers());
            r.getModifiers().addAll(ev.getSpell().passiveModifiers());
        });
    }

    public static List<SetModifier> getRelevant(ItemStack stack, Set<Spell> spellBook) {
        List<SetModifier> modifiers = new ArrayList<>();
        stack.get(CustomItemData.class).ifPresent(c -> {
            ItemData data = c.value().get();
            modifiers.addAll(data.getModifiers());
            data.getAugmentSlots().forEach(a -> modifiers.addAll(a.getAugments()));
        });
        stack.get(CustomWandData.class).ifPresent(c -> c.value().get().getSlots().forEach(s -> {
            Spell spell = s.getSpell();
            if (s.isBypass() && !spellBook.contains(spell)) {
                modifiers.addAll(spell.passiveModifiers());
            }
        }));
        return modifiers;
    }

    public static List<SetModifier> getRelevant(Living living) {
        if (living instanceof Player) {
            return RPGData.runtime((Player) living).map(RuntimeData::getModifiers).orElse(new SortedList<>());
        } else {
            return living.get(CustomMobData.class).map(c -> c.value().get().getModifiers()).orElse(new SortedList<>());
        }
    }

    public static <T> T modify(Living target, T value, ModifiableType type, Map<String, Object> context, List<SetModifier> modifiers) {
        context = new HashMap<>(context);
        context.put(Modifiers.TYPE, type);

        T current = value;
        T lastPrio = value;
        int prio = 0;
        Map<T, Integer> iterations = new HashMap<>();
        iterations.put(current, 0);

        for (int i = 0; i < modifiers.size(); i++) {
            SetModifier<T> modifier = modifiers.get(i);
            int currPrio = modifier.getModifier().getPriority();

            if (prio != currPrio) {
                prio = currPrio;
                lastPrio = current;
            }

            if (modifier.getModifier().getCondition().shouldModifiy(target, type, modifier.getArguments(), context)) {
                current = modifier.getModifier().getFunction().modify(target, current, lastPrio, iterations, modifier.getArguments(), context);
                iterations.put(current, currPrio);
            }
        }

        return current;
    }

}
