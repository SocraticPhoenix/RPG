package com.gmail.socraticphoenix.rpg.registry;

import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public class RPGRegistryItemBuilder<T extends RPGRegistryItem> extends AbstractDataBuilder<T> {
    private Class<T> theClass;

    public RPGRegistryItemBuilder(Class<T> theClass) {
        super(theClass, 0);
        this.theClass = theClass;
    }

    @Override
    protected Optional<T> buildContent(DataView container) throws InvalidDataException {
        if (!container.contains(RPGData.ID, RPGData.OWNER, RPGData.TYPE)) {
            return Optional.empty();
        }

        Optional<RPGRegistry<T>> registry = RPGPlugin.getPlugin().getRegistry().registryFor(theClass);
        if (registry.isPresent()) {
            String id = container.getString(RPGData.ID).get();
            String pluginId = container.getString(RPGData.OWNER).get();
            Optional<T> item = registry.get().get(pluginId + ":" + id);
            if (item.isPresent()) {
                return item;
            } else {
                throw new InvalidDataException("No " + theClass.getSimpleName() + " registered under id: " + id);
            }
        } else {
            throw new InvalidDataException("Could not find RPGRegistry for class: " + theClass.getName());
        }
    }

}
