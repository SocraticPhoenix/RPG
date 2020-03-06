package com.gmail.socraticphoenix.rpg.animation.pixel;

import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;


public class AnimationParticlePixel implements AnimationPixel {
    private ParticleEffect effect;

    public AnimationParticlePixel(ParticleEffect effect) {
        this.effect = effect;
    }

    @Override
    public AnimationPixelHandler render(Location<World> location) {
        location.getExtent().spawnParticles(effect, location.getPosition());
        return new AnimationPixelHandler() {
            @Override
            public void reRender() {
                location.getExtent().spawnParticles(effect, location.getPosition());
            }

            @Override
            public void remove() {

            }
        };
    }


}
