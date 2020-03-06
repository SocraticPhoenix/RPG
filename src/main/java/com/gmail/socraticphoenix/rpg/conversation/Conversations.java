package com.gmail.socraticphoenix.rpg.conversation;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.registry.RPGRegisterEvent;
import org.spongepowered.api.event.Listener;

public class Conversations {
    public static final Conversation EMPTY = new Conversation(RPGPlugin.ID, "empty", ConversationNode.builder().promptKey("empty").build());

    @Listener
    public void onRegister(RPGRegisterEvent ev) {
        ev.register(Conversation.class, EMPTY);
    }

}
