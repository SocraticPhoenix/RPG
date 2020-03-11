package com.gmail.socraticphoenix.rpg.inventory.button;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.registry.registries.ButtonActionRegistry;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.TargetInventoryEvent;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class RuntimeButtonAction extends ButtonAction {

    public RuntimeButtonAction(BiConsumer<Player, TargetInventoryEvent> handler) {
        super(handler, RPGPlugin.ID, ButtonActionRegistry.NO_OP);
    }

    public RuntimeButtonAction(BiPredicate<Player, TargetInventoryEvent> handler) {
        super(handler, RPGPlugin.ID, ButtonActionRegistry.NO_OP);
    }

}
