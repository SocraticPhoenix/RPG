package com.gmail.socraticphoenix.rpg.event;

import com.gmail.socraticphoenix.rpg.modifiers.SetModifier;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.impl.AbstractEvent;

public class RPGUpdateModifiersEvent extends AbstractEvent {
    private Cause cause;
    private SetModifier update;

    public RPGUpdateModifiersEvent(SetModifier update, Living holder) {
        this.update = update;
        this.cause = Cause.builder().append(holder).build(EventContext.builder().build());
    }

    public SetModifier getUpdate() {
        return update;
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }

    public static class Add extends RPGUpdateModifiersEvent {

        public Add(SetModifier update, Living holder) {
            super(update, holder);
        }

    }

    public static class Remove extends RPGUpdateModifiersEvent {

        public Remove(SetModifier update, Living holder) {
            super(update, holder);
        }

    }

}
