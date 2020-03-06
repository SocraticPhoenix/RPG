package com.gmail.socraticphoenix.rpg.animation.frame.modify;

import com.flowpowered.math.vector.Vector3d;
import com.gmail.socraticphoenix.collect.coupling.Pair;
import com.gmail.socraticphoenix.rpg.animation.frame.AnimationFrame;
import com.gmail.socraticphoenix.rpg.animation.frame.AnimationFrameIterator;
import com.gmail.socraticphoenix.rpg.animation.pixel.AnimationPixel;

import java.util.Collection;
import java.util.stream.Collectors;

public class AnimationStaticPixelModifyFrame implements AnimationFrame {
    private AnimationFrame frame;
    private AnimationPixel pixel;

    public AnimationStaticPixelModifyFrame(AnimationFrame frame, AnimationPixel pixel) {
        this.frame = frame;
        this.pixel = pixel;
    }

    @Override
    public AnimationFrameIterator iterator() {
        return new AnimationFrameIterator() {
            AnimationFrameIterator iterator = frame.iterator();

            @Override
            public Collection<Pair<Vector3d, AnimationPixel>> nextFrame() {
                return iterator.nextFrame().stream().map(p -> Pair.of(p.getA(), pixel)).collect(Collectors.toSet());
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
        };
    }

}
