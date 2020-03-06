package com.gmail.socraticphoenix.rpg.data.mob;

import com.gmail.socraticphoenix.rpg.conversation.Conversation;
import com.gmail.socraticphoenix.rpg.conversation.Conversations;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public class NPCData extends RPGData<NPCData> {
    private Conversation conversation;

    public NPCData() {
        this(Conversations.EMPTY);
    }

    public NPCData(Conversation conversation) {
        super(0);
        this.conversation = conversation;
    }

    @Override
    public NPCData copy() {
        return new NPCData(this.conversation);
    }

    @Override
    public DataContainer fill(DataContainer container) {
        return container.set(CONVERSATION, this.conversation);
    }

    @Override
    public Optional<NPCData> from(DataView container) {
        if (!container.contains(CONVERSATION)) {
            return Optional.empty();
        }
        
        this.conversation = container.getSerializable(CONVERSATION, Conversation.class).get();
        
        return Optional.of(this);
    }

    public static class Builder implements DataBuilder<NPCData> {

        @Override
        public Optional<NPCData> build(DataView container) throws InvalidDataException {
            return new NPCData().from(container);
        }

    }


}
