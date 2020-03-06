package com.gmail.socraticphoenix.rpg.event;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.impl.AbstractEvent;
import org.spongepowered.api.item.inventory.ItemStack;

public class RPGEquipEvent extends AbstractEvent {
    private ItemStack previous;
    private ItemStack current;
    private Cause cause;

    public RPGEquipEvent(ItemStack previous, ItemStack current, Player player) {
        this.previous = previous;
        this.current = current;
        this.cause = Cause.builder().append(player).build(EventContext.builder().build());
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }

    public ItemStack getPrevious() {
        return previous;
    }

    public ItemStack getCurrent() {
        return current;
    }

}
