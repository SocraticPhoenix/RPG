package com.gmail.socraticphoenix.rpg.spell;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.registry.RPGRegisterEvent;

public interface Types {
    Type SPELL = type("spell");
    Type EXCLUSIVE = type("exclusive", SPELL);
    Type INHERITABLE = type("inheritable", SPELL);

    Type PHYSICAL = type("physical", SPELL);
        Type PHYSICAL_MELEE = type("physical_melee", PHYSICAL);
        Type PHYSICAL_RANGED = type("physical_ranged", PHYSICAL);

    Type PASSIVE = type("passive", SPELL);
    Type TRIGGERED = type("triggered", SPELL);
    Type CROWD_CONTROl = type("crowd_control", SPELL);

        Type POSITIVE = type("positive", SPELL);
            Type TEMPORAL = type("temporal", POSITIVE);
                Type HEAL = type("heal", POSITIVE);

            Type ELEMENTAL = type("elemental", POSITIVE);
                Type FIRE = type("fire", ELEMENTAL);
                Type WATER = type("water", ELEMENTAL);
                Type EARTH = type("earth", ELEMENTAL);
                Type AIR = type("air", ELEMENTAL);

                Type ICE = type("ice", WATER, AIR);
                Type LAVA = type("lava", FIRE, EARTH);
                Type STEAM = type("steam", FIRE, WATER);
                Type MUD = type("mud", EARTH, WATER);
                Type SAND = type("sand", EARTH, AIR);
                Type LIGHTNING = type("dry", FIRE, AIR);

        Type NEGATIVE = type("negative", SPELL);
            Type SUMMONING = type("summon", NEGATIVE);
            Type MIND = type("mind", NEGATIVE);
            Type DREAM = type("dream", NEGATIVE);

        Type BEE = type("bee", SPELL);


    static Type type(String name, Type... parents) {
        return new Type("rpg.spells.type." + name, Items.buildList(parents));
    }

    static void register(RPGRegisterEvent ev) {
        ev.register(Type.class, SPELL, EXCLUSIVE, INHERITABLE, PASSIVE, TRIGGERED, CROWD_CONTROl, POSITIVE,
                TEMPORAL, HEAL, ELEMENTAL, FIRE, WATER, EARTH, AIR, ICE, LAVA, STEAM, MUD, SAND, LIGHTNING,
                NEGATIVE, SUMMONING, MIND, DREAM, BEE, PHYSICAL, PHYSICAL_MELEE, PHYSICAL_RANGED);
    }

}
