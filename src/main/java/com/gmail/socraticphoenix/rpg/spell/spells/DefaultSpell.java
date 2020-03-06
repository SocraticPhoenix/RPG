package com.gmail.socraticphoenix.rpg.spell.spells;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.collect.coupling.KeyValue;
import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.modifiers.Modifiers;
import com.gmail.socraticphoenix.rpg.modifiers.SetModifier;
import com.gmail.socraticphoenix.rpg.spell.AbstractSpell;
import com.gmail.socraticphoenix.rpg.spell.Cost;
import com.gmail.socraticphoenix.rpg.spell.Types;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.item.ItemTypes;

import java.util.HashMap;
import java.util.List;

public class DefaultSpell extends AbstractSpell {
    private List<SetModifier> modifiers;

    public DefaultSpell() {
        super(Items.buildList(Types.INHERITABLE, Types.PASSIVE), Cost.free(), "rpg.spells.default", ItemTypes.COMPASS, RPGPlugin.ID);
        this.modifiers = Items.buildList(
                new SetModifier(Modifiers.FLAT_SPEED_BOOST, Items.buildMap(HashMap::new, KeyValue.of(Modifiers.VALUE, 3.0))),
                new SetModifier(Modifiers.FLAT_JUMP_BOOST, Items.buildMap(HashMap::new, KeyValue.of(Modifiers.VALUE, 0.0))));
    }

    @Override
    public List<SetModifier> passiveModifiers() {
        return this.modifiers;
    }

    @Override
    public void activate(Living caster, List<SetModifier> modifiers) {

    }

    @Override
    public void deactivate(Living caster) {

    }

}
