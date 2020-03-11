package com.gmail.socraticphoenix.rpg.augment;

import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

public enum AugmentColor {
    RED("rpg.augment.red", TextColors.RED, DyeColors.RED),
    BLUE("rpg.augment.blue", TextColors.BLUE, DyeColors.BLUE),
    YELLOW("rpg.augment.yellow", TextColors.YELLOW, DyeColors.YELLOW),
    GREEN("rpg.augment.green", TextColors.GREEN, DyeColors.GREEN),
    NONE("rpg.augment.none", TextColors.WHITE, DyeColors.WHITE);

    private String key;
    private TextColor color;
    private DyeColor dyeColor;

    AugmentColor(String key, TextColor color, DyeColor dyeColor) {
        this.color = color;
        this.key = key;
        this.dyeColor = dyeColor;
    }

    public DyeColor getDyeColor() {
        return dyeColor;
    }

    public String getKey() {
        return key;
    }

    public TextColor getColor() {
        return color;
    }

}
