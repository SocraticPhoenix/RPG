package com.gmail.socraticphoenix.rpg.registry.registries;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.inventory.button.ButtonAction;
import com.gmail.socraticphoenix.rpg.registry.AbstractRegistry;

public class ButtonActionRegistry extends AbstractRegistry<ButtonAction> {
    public static final String NO_OP = RPGPlugin.ID + ":no_op";

    public ButtonActionRegistry() {
        super(RPGPlugin.ID, "button_actions", ButtonAction.class);
        register(new ButtonAction((p, e) -> {}, RPGPlugin.ID, "no_op"));
    }

}
