package com.gmail.socraticphoenix.rpg.spell;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.click.ClickType;
import com.gmail.socraticphoenix.rpg.click.ItemClickPredicate;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomWandData;

import java.util.List;

public class SpellSlotPredicate extends ItemClickPredicate {

    public SpellSlotPredicate() {
        super((itemStack, clickTypes) -> itemStack.get(CustomWandData.class).map(data -> {
            if (clickTypes.size() >= 4) {
                return true;
            }

            boolean possible = true;
            for (SpellSlot slot : data.value().get().getSlots()) {
                if (slot.getSequence().equals(clickTypes)) {
                    return true;
                } else if (startsWith(clickTypes, slot.getSequence())) {
                    possible = false;
                }
            }


            return possible;
        }).orElse(false), RPGPlugin.ID, "spell_slot");
    }

    private static boolean startsWith(List<ClickType> test, List<ClickType> sequence) {
        if (test.size() > sequence.size()) {
            return false;
        }

        for (int i = 0; i < test.size(); i++) {
            if (!test.get(i).equals(sequence.get(i))) {
                return false;
            }
        }

        return true;
    }

}
