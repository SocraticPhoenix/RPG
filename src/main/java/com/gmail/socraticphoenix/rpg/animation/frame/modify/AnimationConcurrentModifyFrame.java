package com.gmail.socraticphoenix.rpg.animation.frame.modify;

import com.flowpowered.math.vector.Vector3d;
import com.gmail.socraticphoenix.collect.coupling.Pair;
import com.gmail.socraticphoenix.rpg.animation.frame.AnimationFrame;
import com.gmail.socraticphoenix.rpg.animation.frame.AnimationFrameIterator;
import com.gmail.socraticphoenix.rpg.animation.pixel.AnimationPixel;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AnimationConcurrentModifyFrame implements AnimationFrame {
    private List<AnimationFrame> concurrentFrames;

    public AnimationConcurrentModifyFrame(List<AnimationFrame> concurrentFrames) {
        this.concurrentFrames = concurrentFrames;
    }

    @Override
    public AnimationFrameIterator iterator() {
        return new AnimationFrameIterator() {
            private List<AnimationFrameIterator> concurrentIterators = concurrentFrames.stream().map(AnimationFrame::iterator).collect(Collectors.toList());;

            @Override
            public Collection<Pair<Vector3d, AnimationPixel>> nextFrame() {
                return concurrentIterators.stream().filter(AnimationFrameIterator::hasNext).map(AnimationFrameIterator::nextFrame).flatMap(Collection::stream).collect(Collectors.toSet());
            }

            @Override
            public boolean hasNext() {
                return concurrentIterators.stream().anyMatch(AnimationFrameIterator::hasNext);
            }
        };
    }

}
