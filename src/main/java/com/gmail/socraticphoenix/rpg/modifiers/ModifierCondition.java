package com.gmail.socraticphoenix.rpg.modifiers;

import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Map;

public interface ModifierCondition {

    boolean shouldModifiy(ModifiableType type, Map<String, Object> arguments, Map<String, Object> context);

    default ModifierCondition or(ModifierCondition other) {
        return (type, arguments, context) -> this.shouldModifiy(type, arguments, context) || other.shouldModifiy(type, arguments, context);
    }

    default ModifierCondition and(ModifierCondition other) {
        return (type, arguments, context) -> this.shouldModifiy(type, arguments, context) && other.shouldModifiy(type, arguments, context);
    }

}
