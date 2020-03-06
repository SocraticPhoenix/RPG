package com.gmail.socraticphoenix.rpg.animation.pixel;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public interface AnimationPixel {

    AnimationPixelHandler render(Location<World> location);

}
