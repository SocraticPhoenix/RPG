package com.gmail.socraticphoenix.rpg.gods;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.registry.RPGRegisterEvent;
import com.gmail.socraticphoenix.rpg.spell.Types;
import org.spongepowered.api.event.Listener;

public class Gods {
    public static final God KARZAMOC = new God("rpg.gods.karzamoc", s -> !s.is(Types.EXCLUSIVE), RPGPlugin.ID, "karzamoc");

    @Listener
    public void onRegister(RPGRegisterEvent ev) {
        ev.register(God.class, KARZAMOC);
    }

}
