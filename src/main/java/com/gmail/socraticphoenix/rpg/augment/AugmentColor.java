package com.gmail.socraticphoenix.rpg.augment;

import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

public enum AugmentColor {
    RED("rpg.augment.red", TextColors.RED),
    BLUE("rpg.augment.blue", TextColors.BLUE),
    YELLOW("rpg.augment.yellow", TextColors.YELLOW),
    GREEN("rpg.augment.green", TextColors.GREEN),
    NEGATIVE("rpg.augment.negative", TextColors.DARK_AQUA),
    NONE("rpg.augment.none", TextColors.WHITE);

    private String key;
    private TextColor color;

    AugmentColor(String key, TextColor color) {
        this.color = color;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public TextColor getColor() {
        return color;
    }

}
