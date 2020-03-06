package com.gmail.socraticphoenix.rpg.modifiers;

import com.gmail.socraticphoenix.rpg.spell.Spell;
import com.gmail.socraticphoenix.rpg.spell.Type;

public interface ModifierConditions {
    ModifierCondition SPELL_TYPE = (player, type, arguments, context) -> ((Spell) context.get(Modifiers.SPELL)).type().is((Type) arguments.get(Modifiers.SPELL_TYPE));
    ModifierCondition TYPE = (player, type, arguments, context) -> arguments.get(Modifiers.TYPE) == context.get(Modifiers.TYPE);

    static ModifierCondition type(ModifiableType type) {
        return (player, type1, arguments, context) -> type1.is(type);
    }

}
