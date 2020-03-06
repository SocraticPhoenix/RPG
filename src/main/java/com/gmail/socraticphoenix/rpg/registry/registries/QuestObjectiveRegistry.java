package com.gmail.socraticphoenix.rpg.registry.registries;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.quest.QuestObjective;
import com.gmail.socraticphoenix.rpg.registry.AbstractRegistry;

public class QuestObjectiveRegistry extends AbstractRegistry<QuestObjective> {

    public QuestObjectiveRegistry() {
        super(RPGPlugin.ID, "quest_objectives", QuestObjective.class);
    }

}
