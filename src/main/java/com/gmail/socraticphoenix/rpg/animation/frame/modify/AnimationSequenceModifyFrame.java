package com.gmail.socraticphoenix.rpg.animation.frame.modify;

import com.flowpowered.math.vector.Vector3d;
import com.gmail.socraticphoenix.collect.coupling.Pair;
import com.gmail.socraticphoenix.rpg.animation.frame.AnimationFrame;
import com.gmail.socraticphoenix.rpg.animation.frame.AnimationFrameIterator;
import com.gmail.socraticphoenix.rpg.animation.pixel.AnimationPixel;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AnimationSequenceModifyFrame implements AnimationFrame {
    private List<AnimationFrame> sequentialFrames;

    public AnimationSequenceModifyFrame(List<AnimationFrame> sequentialFrames) {
        this.sequentialFrames = sequentialFrames;
    }

    @Override
    public AnimationFrameIterator iterator() {
        return new AnimationFrameIterator() {
            private List<AnimationFrameIterator> sequentialIterators = sequentialFrames.stream().map(AnimationFrame::iterator).collect(Collectors.toList());;

            @Override
            public Collection<Pair<Vector3d, AnimationPixel>> nextFrame() {
                return sequentialIterators.get(0).nextFrame();
            }

            @Override
            public boolean hasNext() {
                while (!sequentialIterators.isEmpty() && !sequentialIterators.get(0).hasNext()) {
                    sequentialFrames.remove(0);
                }
                return !sequentialFrames.isEmpty();
            }
        };
    }

}
