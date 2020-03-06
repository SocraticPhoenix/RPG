package com.gmail.socraticphoenix.rpg.click;

import com.gmail.socraticphoenix.collect.Items;

import java.util.ArrayList;
import java.util.List;

public class ClickHelper {
    //                             0  1  2  3  4  5  6  7  8  9
    private static int[] needed = {1, 1, 1, 2, 3, 3, 4, 4, 4, 4};

    public static List<List<ClickType>> generateKeybinds(int count) {
        List<List<ClickType>> keyBinds = new ArrayList<>();
        keyBinds.add(Items.buildList(ClickType.PRIMARY));

        build(needed[count], count, Items.buildList(ClickType.SECONDARY), keyBinds);

        return keyBinds;
    }

    private static void build(int len, int count, List<ClickType> bind, List<List<ClickType>> keyBinds) {
        if (count <= keyBinds.size()) {
            return;
        } else if (len <= bind.size()) {
            keyBinds.add(bind);
        } else {
            List<ClickType> right = Items.looseClone(bind);
            right.add(ClickType.SECONDARY);

            List<ClickType> left = Items.looseClone(bind);
            left.add(ClickType.PRIMARY);

            build(len, count, right, keyBinds);
            build(len, count, left, keyBinds);
        }
    }

}
