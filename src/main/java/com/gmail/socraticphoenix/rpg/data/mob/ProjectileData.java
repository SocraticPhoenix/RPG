package com.gmail.socraticphoenix.rpg.data.mob;

import com.flowpowered.math.imaginary.Quaterniond;
import com.flowpowered.math.vector.Vector3d;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.event.RPGProjectileHitEvent;
import com.gmail.socraticphoenix.rpg.event.RPGProjectileTickEvent;
import com.gmail.socraticphoenix.rpg.modifiers.SetModifier;
import com.gmail.socraticphoenix.rpg.projectile.CollisionPolicy;
import com.gmail.socraticphoenix.rpg.spell.Spells;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProjectileData extends RPGData<ProjectileData> {
    private String id;
    private List<SetModifier> casterMods;
    private int casterLevel;

    private Vector3d origin;
    private double range;
    private UUID owner;

    private CollisionPolicy policy;
    private Vector3d velocity;
    private double gravity;

    public ProjectileData() {
        super(0);
        this.casterLevel = 1;
        this.id = "projectile";
        this.casterMods = new ArrayList<>();
        this.origin = Vector3d.ZERO;
        this.range = 0;
        this.owner = MobData.NONE;
        this.policy = CollisionPolicy.ENEMY_ONLY;
        this.velocity = Vector3d.ZERO;
        this.gravity = 0;
    }

    public ProjectileData(String id, List<SetModifier> casterMods, int casterLevel, Vector3d origin, double range, UUID owner, CollisionPolicy policy, Vector3d velocity, double gravity) {
        super(0);
        this.id = id;
        this.casterMods = casterMods;
        this.casterLevel = casterLevel;
        this.origin = origin;
        this.range = range;
        this.owner = owner;
        this.policy = policy;
        this.velocity = velocity;
        this.gravity = gravity;
    }

    public List<SetModifier> getCasterMods() {
        return casterMods;
    }

    public void setCasterMods(List<SetModifier> casterMods) {
        this.casterMods = casterMods;
    }

    public int getCasterLevel() {
        return casterLevel;
    }

    public void setCasterLevel(int casterLevel) {
        this.casterLevel = casterLevel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Vector3d getOrigin() {
        return origin;
    }

    public void setOrigin(Vector3d origin) {
        this.origin = origin;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public CollisionPolicy getPolicy() {
        return policy;
    }

    public void setPolicy(CollisionPolicy policy) {
        this.policy = policy;
    }

    public Vector3d getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3d velocity) {
        this.velocity = velocity;
    }

    public double getGravity() {
        return gravity;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public void update(Entity projectile) {
        Vector3d curr = projectile.getLocation().getPosition();
        Vector3d prev = curr;
        curr = curr.add(this.velocity);
        curr = curr.sub(0, this.gravity, 0);

        Vector3d dir = curr.sub(prev);

        Vector3d rotation = Quaterniond.fromRotationTo(Vector3d.UNIT_Y, dir).getAxesAnglesDeg(); //TODO testing
        projectile.setRotation(rotation);
        if (projectile instanceof Living) {
            Living living = (Living) projectile;
            living.setHeadRotation(rotation);
        }

        projectile.setLocation(projectile.getLocation().setPosition(curr));

        Optional<Entity> owner = projectile.getWorld().getEntity(this.owner);
        if (owner.isPresent()) {
            Vector3d finalCurr = curr;
            projectile.getBoundingBox().ifPresent(aabb -> {
                Predicate<Entity> filter = this.policy == CollisionPolicy.ENEMY_ONLY ? Spells.enemies(owner.get()) :
                        this.policy == CollisionPolicy.FRIENDLY_ONLY ? Spells.friends(owner.get()) :
                                this.policy == CollisionPolicy.BLOCK_ONLY ? e -> false :
                                    e -> true;

                Set<Living> collided = this.policy == CollisionPolicy.BLOCK_ONLY ? new HashSet<>() :
                        Spells.getIntersectingRectangle((Living) owner.get(), projectile.getLocation().getPosition(), aabb, filter);

                if (collided.isEmpty()) {
                    if (!projectile.getWorld().getIntersectingBlockCollisionBoxes(aabb).isEmpty()) {
                        Sponge.getEventManager().post(new RPGProjectileHitEvent.Block(projectile, this));
                        projectile.remove();
                        return;
                    } else if (finalCurr.distanceSquared(this.origin) > this.range * this.range) {
                        Sponge.getEventManager().post(new RPGProjectileHitEvent.Expire(projectile, this));
                        projectile.remove();
                        return;
                    }
                } else {
                    double distance = Double.MAX_VALUE;
                    Living closest = null;

                    for (Living e : collided) {
                        if (e.getLocation().getPosition().distanceSquared(finalCurr) < distance) {
                            closest = e;
                        }
                    }

                    Sponge.getEventManager().post(new RPGProjectileHitEvent.Target(projectile, closest, this));
                    projectile.remove();
                    return;
                }

                Sponge.getEventManager().post(new RPGProjectileTickEvent(projectile, this));
            });
        } else {
            projectile.remove();
        }
    }

    @Override
    public ProjectileData copy() {
        return new ProjectileData(this.id, this.casterMods.stream().map(SetModifier::copy).collect(Collectors.toList()), this.casterLevel, this.origin, this.range, this.owner, this.policy, this.velocity, this.gravity);
    }

    @Override
    public DataContainer fill(DataContainer container) {
        return container.set(ID, this.id)
                .set(MODIFIERS, this.casterMods)
                .set(STATS_LEVEL, this.casterLevel)
                .set(ORIGIN, this.origin)
                .set(RANGE, this.range)
                .set(OWNER, this.owner)
                .set(POLICY, this.policy.toString())
                .set(VELOCITY, this.velocity)
                .set(GRAVITY, this.gravity);
    }

    @Override
    public Optional<ProjectileData> from(DataView container) {
        if (!container.contains(ID, MODIFIERS, STATS_LEVEL, ORIGIN, RANGE, OWNER, POLICY, VELOCITY, GRAVITY)) {
            return Optional.empty();
        }

        this.id = container.getString(ID).get();
        this.casterMods = container.getSerializableList(MODIFIERS, SetModifier.class).get();
        this.casterLevel = container.getInt(STATS_LEVEL).get();
        this.origin = container.getObject(ORIGIN, Vector3d.class).get();
        this.range = container.getDouble(RANGE).get();
        this.owner = container.getObject(OWNER, UUID.class).get();
        this.policy = CollisionPolicy.valueOf(container.getString(POLICY).get());
        this.velocity = container.getObject(VELOCITY, Vector3d.class).get();
        this.gravity = container.getDouble(GRAVITY).get();

        return Optional.of(this);
    }

    public static class Builder implements DataBuilder<ProjectileData> {

        @Override
        public Optional<ProjectileData> build(DataView container) throws InvalidDataException {
            return new ProjectileData().from(container);
        }

    }

}
