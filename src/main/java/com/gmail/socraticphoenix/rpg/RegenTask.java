package com.gmail.socraticphoenix.rpg;

import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.modifiers.ModifiableType;
import com.gmail.socraticphoenix.rpg.modifiers.ModifiableTypes;
import com.gmail.socraticphoenix.rpg.modifiers.Modifiers;
import com.gmail.socraticphoenix.rpg.modifiers.SetModifier;
import com.gmail.socraticphoenix.rpg.stats.StatHelper;
import org.spongepowered.api.Sponge;

import java.util.HashMap;
import java.util.List;

public class RegenTask implements Runnable {

    @Override
    public void run() {
        Sponge.getServer().getOnlinePlayers().forEach(player -> {
            double manaRegen = 5;
            double hpRegen = 1;
            List<SetModifier> modifiers = Modifiers.getRelevant(player);

            RPGData.stats(player).ifPresent(s -> {
                StatHelper.setMana(player, s.getMana() + (int) (double) Modifiers.modify(player, manaRegen, ModifiableTypes.MANA_REGEN, new HashMap<>(), modifiers));
                StatHelper.setHealth(player, s.getHealth() + (int) (double) Modifiers.modify(player, hpRegen, ModifiableTypes.HEALTH_REGEN, new HashMap<>(), modifiers));
            });
        });
    }

}
