package com.gmail.socraticphoenix.rpg.spell.spells;

import com.flowpowered.math.vector.Vector3d;
import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.animation.Animation;
import com.gmail.socraticphoenix.rpg.animation.pixel.AnimationParticlePixel;
import com.gmail.socraticphoenix.rpg.animation.pixel.AnimationPixel;
import com.gmail.socraticphoenix.rpg.modifiers.ModifiableType;
import com.gmail.socraticphoenix.rpg.modifiers.ModifiableTypes;
import com.gmail.socraticphoenix.rpg.modifiers.SetModifier;
import com.gmail.socraticphoenix.rpg.spell.AbstractSpell;
import com.gmail.socraticphoenix.rpg.spell.Cost;
import com.gmail.socraticphoenix.rpg.spell.Dice;
import com.gmail.socraticphoenix.rpg.spell.Spells;
import com.gmail.socraticphoenix.rpg.spell.Types;
import com.gmail.socraticphoenix.rpg.stats.StatHelper;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.item.ItemTypes;

import java.util.Collections;
import java.util.List;

public class Slash extends AbstractSpell {
    private static AnimationPixel pixel = new AnimationParticlePixel(ParticleEffect.builder().type(ParticleTypes.SWEEP_ATTACK).build());

    public Slash() {
        super(Items.buildList(Types.TRIGGERED, Types.MELEE), Cost.seconds(.5, 1), "rpg.spells.slash", ItemTypes.STONE_SWORD, RPGPlugin.ID);
    }

    @Override
    public void activate(Living caster, List<SetModifier> modifiers) {
        Vector3d start = Spells.getIntersection(caster, Spells.friends(caster), 2);

        Spells.getIntersecting(caster, Spells.area(start, 1), Spells.friends(caster)).forEach(e -> {
            Spells.damage(caster, e, this, Dice.roll(2, StatHelper.getLevel(caster), 6));
            Spells.flatKnockback(e, start, .5);
        });


        caster.getWorld().playSound(SoundTypes.ENTITY_PLAYER_ATTACK_SWEEP, start, 0.5);
        Animation.draw(caster.getWorld(), start, pixel);
    }

    @Override
    public List<SetModifier> passiveModifiers() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public void deactivate(Living caster) {

    }

}
