package com.gmail.socraticphoenix.rpg.quest;

import com.gmail.socraticphoenix.rpg.registry.AbstractRegistryItem;

import java.util.List;

public class QuestObjective extends AbstractRegistryItem<QuestObjective> {
    private List<QuestObjective> subsequent;

    public QuestObjective(List<QuestObjective> subsequent, String pluginId, String id) {
        super(pluginId, id);
        this.subsequent = subsequent;
    }

    public List<QuestObjective> subsequent() {
        return this.subsequent;
    }

}
