package com.gmail.socraticphoenix.rpg.data.sponge.item;

import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.data.item.ItemData;
import com.gmail.socraticphoenix.rpg.data.sponge.impl.RPGCustomDataImpl;

public class CustomItemDataImpl extends RPGCustomDataImpl<ItemData, CustomItemData, ImmutableCustomItemData> implements CustomItemData {

    public CustomItemDataImpl(ItemData value) {
        super(value, RPGDataKeys.ITEM_DATA, ItemData.class, CustomItemData.class);
    }

    @Override
    public CustomItemData copy() {
        return new CustomItemDataImpl(this.value.copy());
    }

    @Override
    public ImmutableCustomItemData asImmutable() {
        return new ImmutableCustomItemDataImpl(this.value.copy());
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

}
