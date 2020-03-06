package com.gmail.socraticphoenix.rpg.modifiers;

import com.gmail.socraticphoenix.rpg.registry.AbstractRegistryItem;
import com.gmail.socraticphoenix.rpg.registry.RPGRegistryItem;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.entity.living.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class Modifier<T> extends AbstractRegistryItem<RPGRegistryItem> implements Comparable<Modifier<T>> {
    private BiFunction<Player, Map<String, Object>, String> desc;
    private BiFunction<String, DataView, Object> argumentLoader;
    private Map<String, Object> defaultArguments;
    private ModifierFunction<T> function;
    private ModifierCondition condition;
    private int priority;

    public Modifier(ModifierFunction function, ModifierCondition condition, BiFunction<Player, Map<String, Object>, String> desc, BiFunction<String, DataView, Object> argumentLoader, Map<String, Object> defaultArguments, int priority, String pluginId, String id) {
        super(pluginId, id);
        this.function = function;
        this.condition = condition;
        this.defaultArguments = defaultArguments;
        this.desc = desc;
        this.argumentLoader = argumentLoader;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public ModifierFunction<T> getFunction() {
        return function;
    }

    public ModifierCondition getCondition() {
        return condition;
    }

    public BiFunction<String, DataView, Object> getArgumentLoader() {
        return argumentLoader;
    }

    public BiFunction<Player, Map<String, Object>, String> getDesc() {
        return desc;
    }

    public Map<String, Object> getDefaultArguments() {
        return new HashMap<>(defaultArguments);
    }

    @Override
    public int compareTo(Modifier<T> o) {
        return Integer.compare(this.priority, o.getPriority());
    }

}
