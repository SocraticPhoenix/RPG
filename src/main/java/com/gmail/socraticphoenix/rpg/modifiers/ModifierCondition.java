package com.gmail.socraticphoenix.rpg.modifiers;

import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Map;

public interface ModifierCondition {

    boolean shouldModifiy(Living target, ModifiableType type, Map<String, Object> arguments, Map<String, Object> context);

    default ModifierCondition or(ModifierCondition other) {
        return (player, type, arguments, context) -> this.shouldModifiy(player, type, arguments, context) || other.shouldModifiy(player, type, arguments, context);
    }

    default ModifierCondition and(ModifierCondition other) {
        return (player, type, arguments, context) -> this.shouldModifiy(player, type, arguments, context) && other.shouldModifiy(player, type, arguments, context);
    }

}
