package com.gmail.socraticphoenix.rpg.registry.registries;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.registry.AbstractRegistry;
import com.gmail.socraticphoenix.rpg.spell.Spell;
import org.spongepowered.api.Sponge;

public class SpellRegistry extends AbstractRegistry<Spell> {

    public SpellRegistry() {
        super(RPGPlugin.ID, "spells", Spell.class);
    }

    @Override
    public void register(Spell value) {
        super.register(value);
        Sponge.getEventManager().registerListeners(RPGPlugin.getPlugin(), value);
    }

}
