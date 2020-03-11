package com.gmail.socraticphoenix.rpg.projectile;

import com.flowpowered.math.vector.Vector3d;
import com.gmail.socraticphoenix.rpg.data.mob.ProjectileData;
import com.gmail.socraticphoenix.rpg.data.sponge.mob.CustomProjectileDataImpl;
import com.gmail.socraticphoenix.rpg.modifiers.Modifiers;
import com.gmail.socraticphoenix.rpg.modifiers.SetModifier;
import com.gmail.socraticphoenix.rpg.spell.Spell;
import com.gmail.socraticphoenix.rpg.stats.StatHelper;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.Living;

import java.util.List;

public interface Projectiles {

    static void launch(Living caster, List<SetModifier> casterMods, Spell spell, EntityType type, CollisionPolicy policy, Vector3d start, Vector3d velocity, double gravity, double range) {
        Entity entity = caster.getWorld().createEntity(type, start);

        if (entity.supports(Keys.AI_ENABLED)) {
            entity.offer(Keys.AI_ENABLED, false);
        }

        if (entity.supports(Keys.HAS_GRAVITY)) {
            entity.offer(Keys.HAS_GRAVITY, false);
        }

        entity.offer(new CustomProjectileDataImpl(new ProjectileData(
                spell.id(), casterMods, StatHelper.getLevel(caster),
                start, range, caster.getUniqueId(), policy, velocity, gravity)));
        caster.getWorld().spawnEntity(entity);
    }

    static boolean isOwnedBy(ProjectileData data, Spell spell) {
        return data.getId().equals(spell.id());
    }

}
