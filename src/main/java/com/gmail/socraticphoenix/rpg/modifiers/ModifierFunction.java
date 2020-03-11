package com.gmail.socraticphoenix.rpg.modifiers;

import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface ModifierFunction<T> {

    T modify(T current, T lastPrio, Map<T, Integer> iterations, Map<String, Object> arguments, Map<String, Object> context);

}
