package com.gmail.socraticphoenix.rpg.spell;

public class Cost {
    private int mana;
    private int cooldown;

    public Cost(int mana, int cooldown) {
        this.mana = mana;
        this.cooldown = cooldown;
    }

    public static Cost seconds(int duration, int mana) {
        return new Cost(mana, duration * 1000);
    }

    public static Cost seconds(double duration) {
        return seconds(duration, 0);
    }

    public static Cost seconds(double duration, int mana) {
        return new Cost(mana, (int) (duration * 1000));
    }

    public static Cost seconds(int duration) {
        return seconds(duration, 0);
    }

    public static Cost mana(int mana, int duration) {
        return seconds(duration, mana);
    }

    public static Cost mana(int mana) {
        return seconds(0, mana);
    }

    public static Cost free() {
        return mana(0);
    }

    public static Cost of(int mana, int duration) {
        return mana(mana, duration);
    }

    public int getMana() {
        return mana;
    }

    public int getCooldown() {
        return cooldown;
    }

}
