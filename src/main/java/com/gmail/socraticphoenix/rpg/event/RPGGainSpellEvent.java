package com.gmail.socraticphoenix.rpg.event;

import com.gmail.socraticphoenix.rpg.spell.Spell;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.impl.AbstractEvent;

public class RPGGainSpellEvent extends AbstractEvent {
    private Spell spell;
    private Cause cause;

    public RPGGainSpellEvent(Spell spell, Player player) {
        this.spell = spell;
        this.cause = Cause.builder().append(player).build(EventContext.builder().build());
    }

    public Spell getSpell() {
        return spell;
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }

}
