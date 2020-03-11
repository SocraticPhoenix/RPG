package com.gmail.socraticphoenix.rpg.inventory.button;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.inventory.button.data.ButtonData;
import com.gmail.socraticphoenix.rpg.inventory.button.data.ImmutableButtonData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.type.Exclude;
import org.spongepowered.api.event.item.inventory.ChangeInventoryEvent;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

public class ButtonListener {

    @Listener(order = Order.FIRST)
    public void onSwapHand(ChangeInventoryEvent.SwapHand ev, @First Player player) {
        player.getItemInHand(HandTypes.OFF_HAND).ifPresent(stack -> {
            stack.get(ButtonData.class).ifPresent(data -> {
                ev.setCancelled(data.action().get().getHandler().test(player, ev));
            });
        });

        player.getItemInHand(HandTypes.MAIN_HAND).ifPresent(stack -> {
            stack.get(ButtonData.class).ifPresent(data -> {
                ev.setCancelled(true);
            });
        });
    }

    @Listener(order = Order.FIRST)
    public void onChangeHeld(ChangeInventoryEvent.Held ev, @First Player player) {
        ev.getFinalSlot().peek().ifPresent(stack -> {
            stack.get(ButtonData.class).ifPresent(data -> {
                ev.setCancelled(data.action().get().getHandler().test(player, ev));
            });
        });
    }

    @Listener(order = Order.FIRST)
    @Exclude(ClickInventoryEvent.NumberPress.class) //Cancelled in HotbarListener
    public void onClick(ClickInventoryEvent ev, @First Player player) {
        ItemStackSnapshot stack = ev.getCursorTransaction().getFinal();
        stack.get(ImmutableButtonData.class).ifPresent(data -> {
            boolean result = data.action().get().getHandler().test(player, ev);
            ev.setCancelled(result);
            ev.getCursorTransaction().setValid(!result);
        });

        ev.getTransactions().forEach(slotTransaction -> {
            ItemStackSnapshot slotStack = slotTransaction.getFinal();
            slotStack.get(ImmutableButtonData.class).ifPresent(data -> {
                boolean result = data.action().get().getHandler().test(player, ev);
                ev.setCancelled(result);
                ev.getCursorTransaction().setValid(!result);
            });
        });
    }

}
