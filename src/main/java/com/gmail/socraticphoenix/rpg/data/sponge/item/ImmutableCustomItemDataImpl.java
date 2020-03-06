package com.gmail.socraticphoenix.rpg.data.sponge.item;

import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.data.item.ItemData;
import com.gmail.socraticphoenix.rpg.data.sponge.impl.RPGImmutableCustomDataImpl;

public class ImmutableCustomItemDataImpl extends RPGImmutableCustomDataImpl<ItemData, CustomItemData, ImmutableCustomItemData> implements ImmutableCustomItemData {

    public ImmutableCustomItemDataImpl(ItemData value) {
        super(value, RPGDataKeys.ITEM_DATA);
    }

    @Override
    public CustomItemData asMutable() {
        return new CustomItemDataImpl(this.value.copy());
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

}
