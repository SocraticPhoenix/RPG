package com.gmail.socraticphoenix.rpg.registry.registries;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.quest.Quest;
import com.gmail.socraticphoenix.rpg.registry.AbstractRegistry;

public class QuestRegistry extends AbstractRegistry<Quest> {

    public QuestRegistry() {
        super(RPGPlugin.ID, "quests", Quest.class);
    }

}
