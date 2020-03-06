package com.gmail.socraticphoenix.rpg.click;

public enum ClickType {
    PRIMARY("rpg.left"),
    SECONDARY("rpg.right");

    private String name;

    ClickType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
