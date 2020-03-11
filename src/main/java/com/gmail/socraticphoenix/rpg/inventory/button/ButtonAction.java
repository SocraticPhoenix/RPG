package com.gmail.socraticphoenix.rpg.inventory.button;

import com.gmail.socraticphoenix.rpg.registry.AbstractRegistryItem;
import com.gmail.socraticphoenix.rpg.registry.RPGRegistryItem;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.TargetInventoryEvent;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class ButtonAction extends AbstractRegistryItem<RPGRegistryItem> implements DataSerializable, RPGRegistryItem {
    private BiPredicate<Player, TargetInventoryEvent> handler;

    public ButtonAction(BiPredicate<Player, TargetInventoryEvent> handler, String pluginId, String id) {
        super(pluginId, id);
        this.handler = handler;
    }

    public ButtonAction(BiConsumer<Player, TargetInventoryEvent> handler, String pluginId, String id) {
        super(pluginId, id);
        this.handler = (player, targetInventoryEvent) -> {
            handler.accept(player, targetInventoryEvent);
            return true;
        };
    }

    public BiPredicate<Player, TargetInventoryEvent> getHandler() {
        return handler;
    }

}
