package com.gmail.socraticphoenix.rpg.data.sponge.item;

import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.data.item.ItemData;
import com.gmail.socraticphoenix.rpg.data.sponge.impl.RPGCustomDataBuilder;

public class CustomItemDataBuilder extends RPGCustomDataBuilder<ItemData, CustomItemData, ImmutableCustomItemData> {

    public CustomItemDataBuilder() {
        super(RPGDataKeys.ITEM_DATA, new ItemData(), ItemData.class, CustomItemDataImpl::new);
    }

}
