package com.gmail.socraticphoenix.rpg;

import com.gmail.socraticphoenix.rpg.modifiers.ModifiableTypes;
import com.gmail.socraticphoenix.rpg.modifiers.Modifiers;
import com.gmail.socraticphoenix.rpg.modifiers.SetModifier;
import com.gmail.socraticphoenix.rpg.stats.StatHelper;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;

import java.util.HashMap;
import java.util.List;

public class RPGTickTask implements Runnable {

    @Override
    public void run() {
        Sponge.getServer().getOnlinePlayers().forEach(player -> {
            StatHelper.updateBars(player);

            double speed = 0;
            double jump = 0;
            List<SetModifier> modifiers = Modifiers.getRelevant(player);

            speed = Modifiers.modify(player, speed, ModifiableTypes.SPEED, new HashMap<>(), modifiers);
            jump = Modifiers.modify(player, jump, ModifiableTypes.JUMP, new HashMap<>(), modifiers);

            PotionEffectData potions = player.getOrCreate(PotionEffectData.class).get();
            potions.addElement(PotionEffect.builder().potionType(PotionEffectTypes.SPEED).particles(false).amplifier((int) speed).duration(20 * 10).build());
            potions.addElement(PotionEffect.builder().potionType(PotionEffectTypes.JUMP_BOOST).particles(false).amplifier((int) jump).duration(20 * 10).build());
            player.offer(potions);
        });
    }

}
