package com.gmail.socraticphoenix.rpg.conversation;

import com.gmail.socraticphoenix.rpg.registry.AbstractRegistryItem;

public class Conversation extends AbstractRegistryItem<Conversation> {
    private ConversationNode node;

    public Conversation(String pluginId, String id, ConversationNode node) {
        super(pluginId, id);
        this.node = node;
    }

    public ConversationNode getNode() {
        return node;
    }

}
