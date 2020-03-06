package com.gmail.socraticphoenix.rpg.animation.frame;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.animation.AnimationFrameRenderer;
import com.gmail.socraticphoenix.rpg.animation.frame.modify.AnimationConcurrentModifyFrame;
import com.gmail.socraticphoenix.rpg.animation.frame.modify.AnimationSequenceModifyFrame;
import com.gmail.socraticphoenix.rpg.animation.frame.modify.AnimationStaticPixelModifyFrame;
import com.gmail.socraticphoenix.rpg.animation.pixel.AnimationPixel;

import java.util.Collections;
import java.util.List;

public interface AnimationFrame {

    AnimationFrameIterator iterator();

    default AnimationFrame pixel(AnimationPixel pixel) {
        return new AnimationStaticPixelModifyFrame(this, pixel);
    }

    default AnimationFrameRenderer renderer() {
        return new AnimationFrameRenderer(this.iterator());
    }

    default AnimationFrame concurrent(AnimationFrame... others) {
        List<AnimationFrame> frames = Items.buildList(this);
        Collections.addAll(frames, others);
        return new AnimationConcurrentModifyFrame(frames);
    }

    default AnimationFrame after(AnimationFrame... others) {
        List<AnimationFrame> frames = Items.buildList(others);
        frames.add(this);
        return new AnimationSequenceModifyFrame(frames);
    }

    default AnimationFrame before(AnimationFrame... others) {
        List<AnimationFrame> frames = Items.buildList(this);
        Collections.addAll(frames, others);
        return new AnimationSequenceModifyFrame(frames);
    }

}
