package com.gmail.socraticphoenix.rpg.quest;

import com.gmail.socraticphoenix.rpg.registry.AbstractRegistryItem;
import com.gmail.socraticphoenix.rpg.registry.RPGRegistryItem;
import org.spongepowered.api.entity.living.player.Player;

import java.util.List;

public abstract class Quest extends AbstractRegistryItem<RPGRegistryItem> {
    private List<QuestObjective> objectives;

    public Quest(List<QuestObjective> objectives, String pluginId, String id) {
        super(pluginId, id);
        this.objectives = objectives;
    }

    public List<QuestObjective> getObjectives() {
        return objectives;
    }

    public abstract void complete(Player player);

    public abstract void complete(Player player, QuestObjective objective);

}
