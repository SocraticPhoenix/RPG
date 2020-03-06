package com.gmail.socraticphoenix.rpg.animation.pixel;

import org.spongepowered.api.data.manipulator.mutable.entity.GravityData;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.Agent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.function.Consumer;

public class AnimationEntityPixel implements AnimationPixel {
    private EntityType type;
    private Consumer<Entity> editor;

    public AnimationEntityPixel(EntityType type, Consumer<Entity> editor) {
        this.type = type;
        this.editor = editor;
    }

    @Override
    public AnimationPixelHandler render(Location<World> location) {
        Entity entity = location.getExtent().createEntity(type, location.getPosition());

        entity.offer(entity.get(GravityData.class).get().gravity().set(false));

        if (entity instanceof Agent) {
            entity.offer(((Agent) entity).getAgentData().aiEnabled().set(false));
        }

        editor.accept(entity);
        location.getExtent().spawnEntity(entity);

        return new AnimationPixelHandler() {
            @Override
            public void reRender() {

            }

            @Override
            public void remove() {
                entity.remove();;
            }
        };
    }

}
