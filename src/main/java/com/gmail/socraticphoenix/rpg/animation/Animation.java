package com.gmail.socraticphoenix.rpg.animation;

import com.flowpowered.math.vector.Vector3d;
import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.animation.frame.AnimationFrame;
import com.gmail.socraticphoenix.rpg.animation.geometry.LineIterator;
import com.gmail.socraticphoenix.rpg.animation.pixel.AnimationPixel;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Animation {
    private List<Collection<AnimationFrame>> frames;
    private int ticksPerFrame;
    private int lingeringFrames;

    public Animation(List<Collection<AnimationFrame>> frames, int ticksPerFrame, int lingeringFrames) {
        this.frames = frames;
        this.ticksPerFrame = ticksPerFrame;
        this.lingeringFrames = lingeringFrames;
    }

    public static void drawLine(World world, Vector3d start, Vector3d end, double distance, AnimationPixel pixel) {
        LineIterator iterator = new LineIterator(start, end, distance);
        while (iterator.hasNext()) {
            pixel.render(world.getLocation(iterator.next()));
        }
    }

    public static void draw(World world, Vector3d location, AnimationPixel pixel) {
        pixel.render(world.getLocation(location));
    }

    public void startAnimation(Location<World> location, Function<Vector3d, Vector3d> transformer, Object plugin) {
        List<Collection<AnimationFrameRenderer>> renderers = frames.stream().map(c -> c.stream().map(AnimationFrame::renderer).collect(Collectors.toSet())).collect(Collectors.toList());

        Sponge.getScheduler().createTaskBuilder().delayTicks(0).intervalTicks(1).execute(new Consumer<Task>() {
            int tick = 0;
            int index = 0;
            int postFrames = lingeringFrames;

            @Override
            public void accept(Task task) {
                if (tick % ticksPerFrame == 0) {
                    for (int i = 0; i < index; i++) {
                        renderers.get(i).forEach(r -> r.removeOldest(postFrames));
                        renderers.get(i).forEach(AnimationFrameRenderer::reRender);
                        renderers.get(i).forEach(r -> r.renderNext(location, transformer));
                    }

                    if (index < renderers.size()) {
                        renderers.get(index).forEach(r -> r.renderNext(location, transformer));
                        index++;
                    }

                    if (renderers.stream().noneMatch(c -> c.stream().anyMatch(AnimationFrameRenderer::hasNext))) {
                        postFrames--;

                        if (postFrames == 0) {
                            renderers.forEach(c -> c.forEach(AnimationFrameRenderer::removeAll));
                            task.cancel();
                        }
                    }

                }

                tick++;
            }
        }).submit(plugin);
    }

}
