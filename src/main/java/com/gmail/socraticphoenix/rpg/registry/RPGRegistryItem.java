package com.gmail.socraticphoenix.rpg.registry;

import org.spongepowered.api.data.DataSerializable;

public interface RPGRegistryItem extends DataSerializable {

    String id();

    String rawId();

    String pluginId();

}
