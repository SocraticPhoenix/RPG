package com.gmail.socraticphoenix.rpg.event;

import com.gmail.socraticphoenix.rpg.click.ClickType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.impl.AbstractEvent;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.List;

public class RPGClickEvent extends AbstractEvent implements Cancellable {
    private ItemStack stack;
    private List<ClickType> sequence;
    private Cause cause;

    private boolean cancelled = false;

    public RPGClickEvent(ItemStack stack, List<ClickType> sequence, Player player) {
        this.stack = stack;
        this.sequence = sequence;
        this.cause = Cause.builder().append(player).build(EventContext.builder().build());
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }

    public ItemStack getStack() {
        return stack;
    }

    public List<ClickType> getSequence() {
        return sequence;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

}
