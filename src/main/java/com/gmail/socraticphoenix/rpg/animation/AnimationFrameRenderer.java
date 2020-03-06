package com.gmail.socraticphoenix.rpg.animation;

import com.flowpowered.math.vector.Vector3d;
import com.gmail.socraticphoenix.rpg.animation.frame.AnimationFrame;
import com.gmail.socraticphoenix.rpg.animation.frame.AnimationFrameIterator;
import com.gmail.socraticphoenix.rpg.animation.pixel.AnimationPixelHandler;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AnimationFrameRenderer {
    private AnimationFrameIterator iterator;

    public AnimationFrameRenderer(AnimationFrameIterator iterator) {
        this.iterator = iterator;
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    private List<Collection<AnimationPixelHandler>> handlers = new ArrayList<>();

    public void renderNext(Location<World> location, Function<Vector3d, Vector3d> transformer) {
        handlers.add(iterator.nextFrame().stream().map(pair -> pair.getB().render(new Location<World>(location.getExtent(), transformer.apply(pair.getA())))).collect(Collectors.toSet()));
    }

    public void reRender() {
        handlers.forEach(c -> c.forEach(AnimationPixelHandler::reRender));
    }

    public void removeOldest(int lingering) {
        if (!handlers.isEmpty() && handlers.size() > lingering) {
            handlers.get(0).forEach(AnimationPixelHandler::remove);
            handlers.remove(0);
        }
    }

    public void removeAll() {
        handlers.forEach(c -> c.forEach(AnimationPixelHandler::remove));
        handlers.clear();
    }

}
