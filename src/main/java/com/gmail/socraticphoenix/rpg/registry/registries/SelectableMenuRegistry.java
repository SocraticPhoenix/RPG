package com.gmail.socraticphoenix.rpg.registry.registries;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.inventory.player.SelectableMenu;
import com.gmail.socraticphoenix.rpg.registry.AbstractRegistry;

public class SelectableMenuRegistry extends AbstractRegistry<SelectableMenu> {

    public SelectableMenuRegistry() {
        super(RPGPlugin.ID, "selectable_menus", SelectableMenu.class);
    }

}
