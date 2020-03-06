package com.gmail.socraticphoenix.rpg.inventory.storage;

import com.flowpowered.math.vector.Vector2i;
import com.gmail.socraticphoenix.rpg.inventory.InventoryHelper;
import com.google.inject.internal.cglib.core.$Customizer;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.property.SlotPos;

import java.util.*;

public interface ItemStorage extends DataSerializable {

    static ItemStorage of(Map<Vector2i, ItemStack> items, Vector2i dimensions) {
        return new MutableItemStorage(items, dimensions);
    }

    static ItemStorage of(int width, int height) {
        return of(new HashMap<>(), new Vector2i(width, height));
    }

    static ItemStorage of(List<ItemStack> items, int length) {
        ItemStorage storage = new MutableItemStorage(new HashMap<>(), new Vector2i(length, 1));
        storage.flatView().addAll(items);

        return storage;
    }

    static ItemStorage of(List<ItemStack> items) {
        return of(items, items.size());
    }

    Optional<ItemStack> get(Vector2i pos);

    Optional<ItemStack> set(Vector2i pos, ItemStack stack);

    Optional<ItemStack> remove(Vector2i pos);

    void clear();

    Vector2i dimensions();

    ItemStorage copy();

    ItemStorage asImmutable();

    ItemStorage asMutable();

    int itemCount();

    default int capacity() {
        return dimensions().getX() * dimensions().getY();
    }

    default Optional<ItemStack> get(int x, int y) {
        return get(new Vector2i(x, y));
    }

    default Optional<ItemStack> get(SlotPos pos) {
        return get(pos.getX(), pos.getY());
    }

    default Optional<ItemStack> set(int x, int y, ItemStack stack) {
        return set(new Vector2i(x, y), stack);
    }

    default Optional<ItemStack> set(SlotPos pos, ItemStack stack) {
        return set(pos.getX(), pos.getY(), stack);
    }

    default Optional<ItemStack> remove(int x, int y) {
        return remove(new Vector2i(x, y));
    }

    default Optional<ItemStack> remove(SlotPos pos) {
        return remove(pos.getX(), pos.getY());
    }

    default Flat flatView() {
        return new Flat(this);
    }

    default boolean contains(Vector2i pos) {
        return pos.getX() >= 0 && pos.getY() >= 0 && pos.getX() < dimensions().getX() && pos.getY() < dimensions().getY();
    }

    class Flat {
        private ItemStorage storage;
        private int width;
        private int size;

        public Flat(ItemStorage storage) {
            this.storage = storage;
            width = storage.dimensions().getX();
            size = storage.capacity();
        }

        public Optional<ItemStack> get(int index) {
            return storage.get(InventoryHelper.fromIndex(index, width));
        }

        public Optional<ItemStack> get(SlotIndex index) {
            return get(index.getValue());
        }

        public Optional<ItemStack> set(int index, ItemStack stack) {
            return storage.set(InventoryHelper.fromIndex(index, width), stack);
        }

        public Optional<ItemStack> set(SlotIndex index, ItemStack stack) {
            return set(index.getValue(), stack);
        }

        public Optional<ItemStack> remove(int index) {
            return storage.remove(InventoryHelper.fromIndex(index, width));
        }

        public Optional<ItemStack> remove(SlotIndex index) {
            return remove(index.getValue());
        }

        public void add(ItemStack stack) {
            for (int i = 0; i < size; i++) {
                if (!get(i).isPresent()) {
                    set(i, stack);
                    return;
                }
            }

            throw new IllegalStateException("Cannot add items to a full ItemStorage");
        }

        public void addAll(Iterable<ItemStack> stacks) {
            Iterator<ItemStack> iterator = stacks.iterator();

             for (int i = 0; i < size && iterator.hasNext(); i++) {
                if (!get(i).isPresent()) {
                    set(i, iterator.next());
                }
            }

             if (iterator.hasNext()) {
                 throw new IllegalStateException("Cannot add items to a full ItemStorage");
             }
        }

        public int itemCount() {
            return storage.itemCount();
        }

        public int size() {
            return size;
        }

        public ItemStorage parent() {
            return storage;
        }

        public boolean contains(int index) {
            return index >= 0 && index < size;
        }

        public boolean contains(SlotIndex index) {
            return contains(index.getValue());
        }

    }

}
