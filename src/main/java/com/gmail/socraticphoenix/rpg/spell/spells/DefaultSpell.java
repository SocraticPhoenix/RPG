package com.gmail.socraticphoenix.rpg.spell.spells;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.collect.coupling.KeyValue;
import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.event.RPGUpdateModifiersEvent;
import com.gmail.socraticphoenix.rpg.modifiers.ModifiableTypes;
import com.gmail.socraticphoenix.rpg.modifiers.Modifiers;
import com.gmail.socraticphoenix.rpg.modifiers.SetModifier;
import com.gmail.socraticphoenix.rpg.spell.AbstractSpell;
import com.gmail.socraticphoenix.rpg.spell.Cost;
import com.gmail.socraticphoenix.rpg.spell.Types;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.item.ItemTypes;

import java.util.HashMap;
import java.util.List;

public class DefaultSpell extends AbstractSpell {
    private List<SetModifier> modifiers;

    public DefaultSpell() {
        super(Items.buildList(Types.INHERITABLE, Types.PASSIVE), Cost.free(), "rpg.spells.default", ItemTypes.COMPASS, RPGPlugin.ID);
        this.modifiers = Items.buildList(
                new SetModifier(Modifiers.FLAT_SPEED_BOOST, Items.buildMap(HashMap::new, KeyValue.of(Modifiers.VALUE, 3.0))),
                new SetModifier(Modifiers.FLAT_JUMP_BOOST, Items.buildMap(HashMap::new, KeyValue.of(Modifiers.VALUE, 0.0))));
    }

    @Override
    public List<SetModifier> passiveModifiers() {
        return this.modifiers;
    }

    @Listener
    public void onUpdate(RPGUpdateModifiersEvent ev, @First Living living) {
        double speed = 0;
        double jump = 0;
        List<SetModifier> modifiers = Modifiers.getRelevant(living);

        speed = Modifiers.modify(living, speed, ModifiableTypes.SPEED, new HashMap<>(), modifiers);
        jump = Modifiers.modify(living, jump, ModifiableTypes.JUMP, new HashMap<>(), modifiers);

        PotionEffectData potions = living.getOrCreate(PotionEffectData.class).get();
        potions.removeAll(e -> e.getType() == PotionEffectTypes.SPEED || e.getType() == PotionEffectTypes.SLOWNESS || e.getType() == PotionEffectTypes.JUMP_BOOST);

        if (speed >= 0) {
            potions.addElement(PotionEffect.builder().potionType(PotionEffectTypes.SPEED).particles(false).amplifier((int) speed).duration(Integer.MAX_VALUE).build());
        } else {
            potions.addElement(PotionEffect.builder().potionType(PotionEffectTypes.SLOWNESS).particles(false).amplifier((int) -speed).duration(Integer.MAX_VALUE).build());
        }
        potions.addElement(PotionEffect.builder().potionType(PotionEffectTypes.JUMP_BOOST).particles(false).amplifier((int) jump).duration(Integer.MAX_VALUE).build());
        living.offer(potions);
    }

}
