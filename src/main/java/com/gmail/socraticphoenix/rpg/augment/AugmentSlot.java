package com.gmail.socraticphoenix.rpg.augment;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.modifiers.SetModifier;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AugmentSlot implements DataSerializable {
    private AugmentColor color;
    private ItemStack gem;
    private List<SetModifier> augments;

    public AugmentSlot(AugmentColor color, ItemStack gem, List<SetModifier> augments) {
        this.color = color;
        this.augments = augments;
        this.gem = gem;
    }

    public void setColor(AugmentColor color) {
        this.color = color;
    }

    public void setAugments(List<SetModifier> augments) {
        this.augments = augments;
    }

    public AugmentColor getColor() {
        return color;
    }

    public List<SetModifier> getAugments() {
        return augments;
    }

    public ItemStack getGem() {
        return gem;
    }

    public void setGem(ItemStack gem) {
        this.gem = gem;
    }

    public AugmentSlot copy() {
        return new AugmentSlot(this.color, this.gem.copy(), this.augments.stream().map(SetModifier::copy).collect(Collectors.toList()));
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew()
                .set(RPGData.AUGMENT_COLOR, this.color.toString())
                .set(RPGData.AUGMENT_GEM, this.gem)
                .set(RPGData.AUGMENTS, this.augments);
    }

    public static class Builder implements DataBuilder<AugmentSlot> {

        @Override
        public Optional<AugmentSlot> build(DataView container) throws InvalidDataException {
            if (!container.contains(RPGData.AUGMENT_COLOR, RPGData.AUGMENT_GEM, RPGData.AUGMENTS)) {
                return Optional.empty();
            }

            return Optional.of(new AugmentSlot(AugmentColor.valueOf(container.getString(RPGData.AUGMENT_COLOR).get()),
                    container.getSerializable(RPGData.AUGMENT_GEM, ItemStack.class).get(),
                    container.getSerializableList(RPGData.AUGMENTS, SetModifier.class).get()));
        }

    }

}
