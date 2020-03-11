package com.gmail.socraticphoenix.rpg.modifiers;

import com.gmail.socraticphoenix.rpg.spell.Spell;
import com.gmail.socraticphoenix.rpg.spell.Type;

public interface ModifierConditions {
    ModifierCondition SPELL_TYPE = (type, arguments, context) -> ((Spell) context.get(Modifiers.SPELL)).type().is((Type) arguments.get(Modifiers.SPELL_TYPE));
    ModifierCondition TYPE = (type, arguments, context) -> ((ModifiableType) context.get(Modifiers.TYPE)).is((ModifiableType) arguments.get(Modifiers.TYPE));

    static ModifierCondition type(ModifiableType type) {
        return (type1, arguments, context) -> type1.is(type);
    }

}
