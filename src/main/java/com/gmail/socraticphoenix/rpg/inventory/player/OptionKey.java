package com.gmail.socraticphoenix.rpg.inventory.player;

public class OptionKey {
    private String name;
    private Class type;

    public OptionKey(String name, Class type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Class getType() {
        return type;
    }

}
