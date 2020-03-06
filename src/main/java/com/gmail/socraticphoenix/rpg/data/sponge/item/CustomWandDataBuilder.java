package com.gmail.socraticphoenix.rpg.data.sponge.item;

import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.data.item.WandData;
import com.gmail.socraticphoenix.rpg.data.sponge.impl.RPGCustomDataBuilder;

public class CustomWandDataBuilder extends RPGCustomDataBuilder<WandData, CustomWandData, ImmutableCustomWandData> {

    public CustomWandDataBuilder() {
        super(RPGDataKeys.WAND_DATA, new WandData(), WandData.class, CustomWandDataImpl::new);
    }

}
