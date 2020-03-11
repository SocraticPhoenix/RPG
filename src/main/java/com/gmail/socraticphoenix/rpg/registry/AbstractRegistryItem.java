package com.gmail.socraticphoenix.rpg.registry;

import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataSerializable;

public class AbstractRegistryItem<Q extends RPGRegistryItem> implements RPGRegistryItem, DataSerializable {
    private String id;
    private String pluginId;

    private String registryId;

    public AbstractRegistryItem(String pluginId, String id) {
        this.id = id;
        this.pluginId = pluginId;
        this.registryId = pluginId + ":" + id;
    }

    @Override
    public String id() {
        return this.registryId;
    }

    @Override
    public String rawId() {
        return this.id;
    }

    @Override
    public String pluginId() {
        return this.pluginId;
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew()
                .set(RPGData.ID, id)
                .set(RPGData.OWNER, this.pluginId);
    }

}
