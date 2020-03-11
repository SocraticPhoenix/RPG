package com.gmail.socraticphoenix.rpg.spell.spells;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.modifiers.SetModifier;
import com.gmail.socraticphoenix.rpg.spell.AbstractSpell;
import com.gmail.socraticphoenix.rpg.spell.Cost;
import com.gmail.socraticphoenix.rpg.spell.Types;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;

import java.util.Collections;
import java.util.List;

public class NoopSpell extends AbstractSpell {

    public NoopSpell() {
        super(Items.buildList(Types.TRIGGERED), Cost.free(), "rpg.spells.noop", ItemTypes.COAL_BLOCK, RPGPlugin.ID);
    }

}
