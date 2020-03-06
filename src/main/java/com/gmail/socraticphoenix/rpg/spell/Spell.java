package com.gmail.socraticphoenix.rpg.spell;

import com.gmail.socraticphoenix.rpg.modifiers.SetModifier;
import com.gmail.socraticphoenix.rpg.registry.RPGRegistryItem;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.item.ItemType;

import java.util.List;

public interface Spell extends RPGRegistryItem {

    default String translateDescKey() {
        return rawId() + ".desc";
    }

    List<Type> types();

    ItemType icon();

    Type type();

    Cost cost();

    void activate(Living caster, List<SetModifier> modifiers);

    List<SetModifier> passiveModifiers();

    void deactivate(Living caster);

    default boolean is(Type type) {
        return this.type().is(type);
    }

}
