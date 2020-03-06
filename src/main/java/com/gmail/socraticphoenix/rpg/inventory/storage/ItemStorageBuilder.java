package com.gmail.socraticphoenix.rpg.inventory.storage;

import com.flowpowered.math.vector.Vector2i;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ItemStorageBuilder extends AbstractDataBuilder<ItemStorage> {

    public ItemStorageBuilder() {
        super(ItemStorage.class, 0);
    }

    @Override
    protected Optional<ItemStorage> buildContent(DataView container) throws InvalidDataException {
        if (!container.contains(RPGData.ITEM_MAP, RPGData.DIMENSIONS)) {
            return Optional.empty();
        }


        Vector2i dimensions = container.getObject(RPGData.DIMENSIONS, Vector2i.class).get();
        Map<Vector2i, ItemStack> items = new HashMap<>();
        container.getViewList(RPGData.ITEM_MAP).get().forEach(view -> items.put(view.getObject(RPGData.POSITION, Vector2i.class).get(), view.getSerializable(RPGData.ITEM, ItemStack.class).get()));


        return Optional.of(new MutableItemStorage(items, dimensions));
    }

}
