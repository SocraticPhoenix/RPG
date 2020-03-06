package com.gmail.socraticphoenix.rpg.modifiers;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.registry.RPGRegisterEvent;
import com.sun.org.apache.xpath.internal.operations.Mod;

public interface ModifiableTypes {
    ModifiableType POWER = type("power");
    ModifiableType DAMAGE_INCREASE = type("damage", POWER);

    ModifiableType DAMAGE_REDUCTION = type("damage_reduction");

    ModifiableType COST = type("cost");
    ModifiableType COST_COOLDOWN = type("cost_cooldown", COST);
    ModifiableType COST_MANA = type("cost_mana", COST);

    ModifiableType SPEED = type("speed");
    ModifiableType JUMP = type("jump");

    ModifiableType ON_HIT = type("on_hit");

    ModifiableType HEALTH_REGEN = type("health_regen");
    ModifiableType MANA_REGEN = type("mana_regen");

    static void register(RPGRegisterEvent ev) {
        ev.register(ModifiableType.class, POWER, DAMAGE_INCREASE, DAMAGE_REDUCTION, COST, COST_COOLDOWN,
                COST_MANA, SPEED, JUMP, ON_HIT, HEALTH_REGEN, MANA_REGEN);
    }

    static ModifiableType type(String key, ModifiableType... parents) {
        return new ModifiableType(Items.buildList(parents), RPGPlugin.ID, "rpg.modifiers." + key);
    }

}
