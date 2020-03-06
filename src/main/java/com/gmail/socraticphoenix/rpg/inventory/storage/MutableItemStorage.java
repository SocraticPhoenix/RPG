package com.gmail.socraticphoenix.rpg.inventory.storage;

import com.flowpowered.math.vector.Vector2i;
import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.*;

public class MutableItemStorage implements ItemStorage {
    private Map<Vector2i, ItemStack> items;
    private Vector2i dimensions;

    public MutableItemStorage(Map<Vector2i, ItemStack> items, Vector2i dimensions) {
        this.items = items;
        this.dimensions = dimensions;
    }

    public Optional<ItemStack> get(Vector2i pos) {
        checkBounds(pos);
        return Optional.ofNullable(items.get(pos));
    }

    public Optional<ItemStack> set(Vector2i pos, ItemStack stack) {
        checkBounds(pos);
        return Optional.ofNullable(items.put(pos, stack));
    }

    @Override
    public Optional<ItemStack> remove(Vector2i pos) {
        return Optional.ofNullable(items.remove(pos));
    }

    @Override
    public void clear() {
        items.clear();
    }

    public Vector2i dimensions() {
        return dimensions;
    }

    @Override
    public ItemStorage copy() {
        return new MutableItemStorage(new HashMap<>(this.items), this.dimensions);
    }

    public ItemStorage asImmutable() {
        Map<Vector2i, ItemStack> items = new HashMap<>();
        this.items.forEach((vector2i, stack) -> items.put(vector2i, stack.copy()));

        return new ImmutableItemStorage(ImmutableMap.copyOf(items), this.dimensions);
    }

    @Override
    public ItemStorage asMutable() {
        return this;
    }

    @Override
    public int itemCount() {
        return items.size();
    }

    private void checkBounds(Vector2i loc) {
        if (loc.getX() < 0 || loc.getY() < 0 || loc.getX() >= dimensions.getX() || loc.getY() >= dimensions.getY()) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + loc + " (Dimensions: " + dimensions + ")");
        }
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        DataContainer container = DataContainer.createNew();
        container.set(RPGData.DIMENSIONS, this.dimensions);

        List<DataView> slots = new ArrayList<>();
        items.forEach((vector2i, stack) -> slots.add(DataContainer.createNew().set(RPGData.POSITION, vector2i).set(RPGData.ITEM, stack)));

        container.set(RPGData.ITEM_MAP, slots);

        return container;
    }

}
