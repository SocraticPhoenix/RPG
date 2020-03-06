package com.gmail.socraticphoenix.rpg.registry;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class AbstractRegistry<T extends RPGRegistryItem> extends AbstractRegistryItem<RPGRegistryItem> implements RPGRegistry<T> {
    private Map<String, T> items = new LinkedHashMap<>();
    private Class<T> type;

    public AbstractRegistry(String pluginId, String id, Class<T> type) {
        super(pluginId, id);
        this.type = type;
    }

    @Override
    public void register(T value) {
        if (items.containsKey(value.id())) {
            throw new IllegalArgumentException("Duplicate ID: " + value.id());
        }

        items.put(value.id(), value);
    }

    @Override
    public Optional<T> get(String id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Collection<T> elements() {
        return items.values();
    }

    @Override
    public Class<T> type() {
        return this.type;
    }


}
