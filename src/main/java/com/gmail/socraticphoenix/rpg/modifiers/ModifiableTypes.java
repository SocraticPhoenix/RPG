package com.gmail.socraticphoenix.rpg.modifiers;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.registry.RPGRegisterEvent;

public interface ModifiableTypes {
    ModifiableType POWER = type("power");
    ModifiableType DAMAGE = type("damage", POWER);
    ModifiableType HEAL = type("heal", POWER);

    ModifiableType COST = type("cost");
    ModifiableType COST_COOLDOWN = type("cost_cooldown", COST);
    ModifiableType COST_MANA = type("cost_mana", COST);
    ModifiableType COST_HEALTH = type("cost_health", COST);

    ModifiableType CHARGES = type("cost_charges");

    ModifiableType SPEED = type("speed");
    ModifiableType JUMP = type("jump");

    ModifiableType ON_DAMAGE = type("on_damage");

    ModifiableType HEALTH_REGEN = type("health_regen");
    ModifiableType MANA_REGEN = type("mana_regen");

    ModifiableType CROWD_CONTROL = type("crowd_control");
    ModifiableType KNOCKBACK = type("knockback", CROWD_CONTROL);

    static void register(RPGRegisterEvent ev) {
        ev.register(ModifiableType.class, POWER, DAMAGE, HEAL, COST, COST_COOLDOWN, COST_HEALTH,
                COST_MANA, SPEED, JUMP, ON_DAMAGE, HEALTH_REGEN, MANA_REGEN, CROWD_CONTROL, KNOCKBACK);
    }

    static ModifiableType type(String key, ModifiableType... parents) {
        return new ModifiableType(Items.buildList(parents), RPGPlugin.ID, "rpg.modifiers." + key);
    }

}
