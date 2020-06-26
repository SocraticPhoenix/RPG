package com.gmail.socraticphoenix.rpg.spell;

public class Cost {
    private int mana;
    private int health;
    private int cooldown;
    private int charges;

    public Cost(int mana, int health, int cooldown, int charges) {
        this.mana = mana;
        this.cooldown = cooldown;
        this.health = health;
        this.charges = charges;
    }

    public static Cost of(double duration) {
        return of(duration, 0);
    }

    public static Cost of(double duration, int mana) {
        return of(duration, mana, 0);
    }

    public static Cost of(double duration, int mana, int health) {
        return of(duration, mana, health, 0);
    }

    public static Cost of(double duration, int mana, int health, int charges) {
        return new Cost(mana, health, (int) (duration * 1000), charges);
    }

    public static Cost free() {
        return of(0, 0, 0, 0);
    }

    public int getMana() {
        return mana;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getHealth() {
        return health;
    }

    public int getCharges() {
        return charges;
    }

    public boolean hasCharges() {
        return charges > 0;
    }

}
