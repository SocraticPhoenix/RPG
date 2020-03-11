package com.gmail.socraticphoenix.rpg.modifiers;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.collect.coupling.KeyValue;
import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.data.character.RuntimeData;
import com.gmail.socraticphoenix.rpg.data.item.ItemData;
import com.gmail.socraticphoenix.rpg.data.mob.MobData;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomItemData;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomWandData;
import com.gmail.socraticphoenix.rpg.data.sponge.mob.CustomMobData;
import com.gmail.socraticphoenix.rpg.event.RPGEquipEvent;
import com.gmail.socraticphoenix.rpg.event.RPGGainSpellEvent;
import com.gmail.socraticphoenix.rpg.event.RPGUpdateModifiersEvent;
import com.gmail.socraticphoenix.rpg.registry.RPGRegisterEvent;
import com.gmail.socraticphoenix.rpg.spell.Spell;
import com.gmail.socraticphoenix.rpg.spell.Type;
import com.gmail.socraticphoenix.rpg.spell.Types;
import com.gmail.socraticphoenix.rpg.translation.Messages;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.item.inventory.ItemStack;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Modifiers {
    public static final String VALUE = "Value";
    public static final String SPELL_TYPE = "SpellType";
    public static final String TYPE = "Type";
    public static final String SPELL = "Spell";
    public static final String ELEMENT_TYPE = "ElementTYpe";
    public static final String OWNER = "Owner";
    public static final String CAUSER = "Causer";
    public static final String RECEIVER = "Receiver";

    private static final BiFunction<String, DataView, Object> reader = (key, view) -> {
        if (key.equals(VALUE)) {
            return view.getDouble(DataQuery.of(key)).get();
        } else if (key.equals(SPELL_TYPE)) {
            return view.getSerializable(DataQuery.of(key), Type.class).get();
        }
        return null;
    };

    public static final Modifier<Double> SPELL_PERCENTAGE_DAMAGE_BOOST = new Modifier<>(
            ModifierFunctions.PERCENTAGE_BOOST, ModifierConditions.type(ModifiableTypes.DAMAGE).and(ModifierConditions.SPELL_TYPE),
            (player, stringMap) -> Messages.translateString(player, "rpg.mods.percent_damage", stringMap.get(VALUE), Messages.translateString(player, ((Type) stringMap.get(SPELL_TYPE)).getKey())),
            reader,
            Items.buildMap(HashMap::new, KeyValue.of(VALUE, 0.0), KeyValue.of(SPELL_TYPE, Types.TRIGGERED)),
            Double.class, 20, RPGPlugin.ID, "spell_type_percentage_damage_boost");

    public static final Modifier<Double> SPELL_FLAT_DAMAGE_BOOST = new Modifier<>(
            ModifierFunctions.FLAT_BOOST, ModifierConditions.type(ModifiableTypes.DAMAGE).and(ModifierConditions.SPELL_TYPE),
            (player, stringMap) -> Messages.translateString(player, "rpg.mods.flat_damage", stringMap.get(VALUE), Messages.translateString(player, ((Type) stringMap.get(SPELL_TYPE)).getKey())),
            reader,
            Items.buildMap(HashMap::new, KeyValue.of(VALUE, 0.0), KeyValue.of(SPELL_TYPE, Types.TRIGGERED)),
            Double.class, 10, RPGPlugin.ID, "spell_type_flat_damage_boost");

    public static final Modifier<Double> FLAT_SPEED_BOOST = new Modifier<>(
            ModifierFunctions.FLAT_BOOST, ModifierConditions.type(ModifiableTypes.SPEED),
            (player, stringMap) -> Messages.translateString(player, "rpg.mods.flat_speed", stringMap.get(VALUE)),
            reader,
            Items.buildMap(HashMap::new, KeyValue.of(VALUE, 0.0)),
            Double.class, 10, RPGPlugin.ID, "flat_speed_boost");

    public static final Modifier<Double> FLAT_JUMP_BOOST = new Modifier<>(
            ModifierFunctions.FLAT_BOOST, ModifierConditions.type(ModifiableTypes.JUMP),
            (player, stringMap) -> Messages.translateString(player, "rpg.mods.flat_jump", stringMap.get(VALUE)),
            reader,
            Items.buildMap(HashMap::new, KeyValue.of(VALUE, 0.0)),
            Double.class, 10, RPGPlugin.ID, "flat_jump_boost");

    public static <T> T modify(Living living, T val, ModifiableType type, Map<String, Object> context, List<SetModifier> mods) {
        return modify(val, type, context, KeyValue.of(living, mods));
    }

    @Listener
    public void onRegister(RPGRegisterEvent ev) {
        ev.register(Modifier.class, SPELL_PERCENTAGE_DAMAGE_BOOST, SPELL_FLAT_DAMAGE_BOOST, FLAT_SPEED_BOOST, FLAT_JUMP_BOOST);
    }

    @Listener
    public void onEquip(RPGEquipEvent ev, @First Player player) {
        RPGData.runtime(player).ifPresent(r -> RPGData.spellbook(player).ifPresent(s -> {
            Modifiers.getRelevant(ev.getPrevious(), s.getSpells()).forEach(m -> Modifiers.remove(m, player));
            if (RPGData.canUse(ev.getCurrent(), player)) {
                Modifiers.getRelevant(ev.getCurrent(), s.getSpells()).forEach(m -> Modifiers.add(m, player));
            }
        }));
    }

    @Listener
    public void onSpell(RPGGainSpellEvent ev, @First Player player) {
        RPGData.runtime(player).ifPresent(r -> {
            ev.getSpell().passiveModifiers().forEach(m -> Modifiers.add(m, player));
        });
    }

    public static void add(SetModifier modifier, Player player) {
        RPGData.runtime(player).ifPresent(r -> {
            r.getModifiers().remove(modifier);
            r.getModifiers().add(modifier);
            Sponge.getEventManager().post(new RPGUpdateModifiersEvent.Add(modifier, player));
        });
    }

    public static void add(SetModifier modifier, Living living) {
        if (living instanceof Player) {
            add(modifier, (Player) living);
        } else {
            living.get(CustomMobData.class).ifPresent(c -> {
                MobData data = c.value().get();
                data.getModifiers().remove(modifier);
                data.getModifiers().add(modifier);
                Sponge.getEventManager().post(new RPGUpdateModifiersEvent.Add(modifier, living));
            });
        }
    }

    public static void remove(SetModifier modifier, Player player) {
        RPGData.runtime(player).ifPresent(r -> {
            r.getModifiers().remove(modifier);
            Sponge.getEventManager().post(new RPGUpdateModifiersEvent.Remove(modifier, player));
        });
    }

    public static void removeAll(Player player) {
        RPGData.runtime(player).ifPresent(r -> {
            List<SetModifier> modifiers = new ArrayList<>(r.getModifiers());
            modifiers.forEach(m -> {
                Modifiers.remove(m, player);
            });
        });
    }

    public static void remove(SetModifier modifier, Living living) {
        if (living instanceof Player) {
            remove(modifier, (Player) living);
        } else {
            living.get(CustomMobData.class).ifPresent(c -> {
                MobData data = c.value().get();
                data.getModifiers().remove(modifier);
                Sponge.getEventManager().post(new RPGUpdateModifiersEvent.Remove(modifier, living));
            });
        }
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

    public static List<SetModifier> snapshotRelevant(Living living) {
        return new SortedList<>(getRelevant(living));
    }

    public static <T> T modify(T value, ModifiableType type, Map<String, Object> context, KeyValue<Living, List<SetModifier>>... modifiers) {
        context.put(Modifiers.TYPE, type);

        Map<SetModifier, Map<String, Object>> contexts = new HashMap<>();
        List<SetModifier> mods = new SortedList<>();
        for (KeyValue<Living, List<SetModifier>> mod : modifiers) {
            mods.addAll(mod.getValue());

            Map<String, Object> currContext = new HashMap<>(context);
            currContext.put(Modifiers.OWNER, mod.getKey());

            mod.getValue().forEach(l -> contexts.put(l, currContext));
        }

        return modify(value, type, contexts, mods);
    }

    public static <T> T modify(T value, ModifiableType type, Map<SetModifier, Map<String, Object>> context, List<SetModifier> modifiers) {
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

            if (modifier.getModifier().getType().isInstance(current) &&
                    modifier.getModifier().getCondition().shouldModifiy(type, modifier.getArguments(), context.get(modifier))) {
                current = modifier.getModifier().getFunction().modify(current, lastPrio, iterations, modifier.getArguments(), context.get(modifier));
                iterations.put(current, currPrio);
            }
        }

        return current;
    }

}
