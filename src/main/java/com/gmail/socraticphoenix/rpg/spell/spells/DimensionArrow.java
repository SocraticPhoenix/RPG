package com.gmail.socraticphoenix.rpg.spell.spells;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.data.mob.ProjectileData;
import com.gmail.socraticphoenix.rpg.event.RPGProjectileHitEvent;
import com.gmail.socraticphoenix.rpg.modifiers.SetModifier;
import com.gmail.socraticphoenix.rpg.projectile.CollisionPolicy;
import com.gmail.socraticphoenix.rpg.projectile.Projectiles;
import com.gmail.socraticphoenix.rpg.spell.*;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;

import java.util.List;

public class DimensionArrow extends AbstractSpell {

    public DimensionArrow() {
        super(Items.buildList(Types.PHYSICAL_RANGED, Types.DREAM), Cost.of(10, 5), "rpg.spells.dimension_arrow", ItemTypes.SPECTRAL_ARROW, RPGPlugin.ID);
    }

    @Override
    public void activate(Living caster, List<SetModifier> modifiers) {
        Projectiles.launch(caster, modifiers,this, EntityTypes.TIPPED_ARROW, CollisionPolicy.BLOCK_ONLY, Spells.getStartLocation(caster),
                Spells.looking(caster).normalize().mul(0.1), 0.01, 100);
    }

    @Listener
    public void onCollide(RPGProjectileHitEvent.Block ev, @Getter("getData") ProjectileData data) {
        if (Projectiles.isOwnedBy(data, this)) {
            ev.getOwner().ifPresent(caster -> {
                caster.setLocationSafely(ev.getProjectile().getLocation());
            });
        }
    }

}
