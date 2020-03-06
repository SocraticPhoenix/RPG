package com.gmail.socraticphoenix.rpg.modifiers;

import com.gmail.socraticphoenix.rpg.registry.AbstractRegistryItem;

import java.util.List;

public class ModifiableType extends AbstractRegistryItem<ModifiableType> {
    private List<ModifiableType> parents;

    public ModifiableType(List<ModifiableType> parents, String pluginId, String id) {
        super(pluginId, id);
        this.parents = parents;
    }

    public boolean is(ModifiableType type) {
        return type == this || this.parents.stream().anyMatch(p -> p.is(type));
    }

}
