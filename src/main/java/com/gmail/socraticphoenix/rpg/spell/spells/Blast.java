package com.gmail.socraticphoenix.rpg.spell.spells;

import com.flowpowered.math.vector.Vector3d;
import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.animation.Animation;
import com.gmail.socraticphoenix.rpg.animation.frame.AnimationSingleUnitFrame;
import com.gmail.socraticphoenix.rpg.animation.pixel.AnimationParticlePixel;
import com.gmail.socraticphoenix.rpg.animation.pixel.AnimationPixel;
import com.gmail.socraticphoenix.rpg.animation.unit.AnimationLineUnit;
import com.gmail.socraticphoenix.rpg.animation.unit.AnimationMultipathUnit;
import com.gmail.socraticphoenix.rpg.modifiers.ModifiableType;
import com.gmail.socraticphoenix.rpg.modifiers.ModifiableTypes;
import com.gmail.socraticphoenix.rpg.modifiers.SetModifier;
import com.gmail.socraticphoenix.rpg.spell.AbstractSpell;
import com.gmail.socraticphoenix.rpg.spell.Cost;
import com.gmail.socraticphoenix.rpg.spell.Dice;
import com.gmail.socraticphoenix.rpg.spell.Spells;
import com.gmail.socraticphoenix.rpg.spell.Types;
import com.gmail.socraticphoenix.rpg.stats.StatHelper;
import com.graphbuilder.curve.BezierCurve;
import com.graphbuilder.curve.GroupIterator;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.property.block.SolidCubeProperty;
import org.spongepowered.api.data.property.entity.EyeLocationProperty;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.event.cause.entity.damage.DamageTypes;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.world.extent.EntityUniverse;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class Blast extends AbstractSpell {
    private static AnimationPixel pixel1 = new AnimationParticlePixel(ParticleEffect.builder().type(ParticleTypes.MAGIC_CRITICAL_HIT).quantity(1).build());
    private static AnimationPixel pixel2 = new AnimationParticlePixel(ParticleEffect.builder().quantity(5)
            .offset(Vector3d.ONE)
            .type(ParticleTypes.LARGE_EXPLOSION).build());

    public Blast() {
        super(Items.buildList(Types.TRIGGERED, Types.INHERITABLE), Cost.of(2, 5), "rpg.spells.blast", ItemTypes.FIRE_CHARGE, RPGPlugin.ID);
    }

    @Override
    public void activate(Living caster, List<SetModifier> modifiers) {
        Vector3d start = Spells.getStartLocation(caster);
        Vector3d central = Spells.getIntersection(caster, e -> e.equals(caster), 10);

        Spells.getIntersectingSphere(caster, central, 3, Spells.enemies(caster)).forEach(e -> {
            Spells.damage(caster, e, modifiers, Dice.roll(4, StatHelper.getActualLevel(caster) * 2, 6), this);
            Spells.knockback(caster, e, modifiers, 1, central, this);
        });

        Animation.drawLine(caster.getWorld(), start, central, 0.5, pixel1);
        Animation.draw(caster.getWorld(), central, pixel2);
        caster.getWorld().playSound(SoundTypes.ENTITY_ENDERDRAGON_FIREBALL_EXPLODE, central, 0.5);
    }

}
