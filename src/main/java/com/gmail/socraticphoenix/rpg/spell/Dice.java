package com.gmail.socraticphoenix.rpg.spell;

import java.util.Random;

public interface Dice {
    Random RANDOM = new Random();

    static int roll(int base, int number, int faces) {
        for (int i = 0; i < number; i++) {
            base += RANDOM.nextInt(faces) + 1;
        }

        return base;
    }

}
