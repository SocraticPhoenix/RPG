package com.gmail.socraticphoenix.rpg.spell.spells;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.collect.coupling.KeyValue;
import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.data.mob.ProjectileData;
import com.gmail.socraticphoenix.rpg.event.RPGProjectileHitEvent;
import com.gmail.socraticphoenix.rpg.modifiers.Modifiers;
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

public class Arrow extends AbstractSpell {
    private Type arrowType;

    public Arrow(Type arrowType, String key) {
        super(Items.buildList(Types.PHYSICAL_RANGED, arrowType), Cost.of(1, arrowType == Types.PHYSICAL_RANGED ? 0 : 5), key, ItemTypes.ARROW, RPGPlugin.ID);
        this.arrowType = arrowType;
    }

    @Override
    public void activate(Living caster, List<SetModifier> modifiers) {
        Projectiles.launch(caster, modifiers,this, EntityTypes.TIPPED_ARROW, CollisionPolicy.ENEMY_ONLY, Spells.getStartLocation(caster),
                Spells.looking(caster).normalize().mul(0.1), 0.01, 100);
    }

    @Listener
    public void onCollide(RPGProjectileHitEvent.Target ev, @Getter("getData")ProjectileData data) {
        if (Projectiles.isOwnedBy(data, this)) {
            ev.getOwner().ifPresent(caster -> {
                Living target = ev.getHit();
                Spells.damage(caster, target, data.getCasterMods(), Dice.roll(2, 2, 6), Types.PHYSICAL_RANGED,
                        KeyValue.of(Modifiers.SPELL, this), KeyValue.of(Modifiers.SPELL_TYPE, this.type()));

                if (this.arrowType != Types.PHYSICAL_RANGED) {
                    Spells.damage(caster, target, data.getCasterMods(), Dice.roll(0, data.getCasterLevel() * 2, 4), this);
                }
            });
        }
    }

}
