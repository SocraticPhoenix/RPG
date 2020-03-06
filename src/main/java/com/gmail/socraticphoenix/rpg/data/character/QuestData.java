package com.gmail.socraticphoenix.rpg.data.character;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.quest.Quest;
import com.gmail.socraticphoenix.rpg.quest.QuestObjective;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.entity.living.player.Player;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class QuestData extends RPGData<QuestData> {
    private List<Quest> completed;
    private Map<Quest, List<QuestObjective>> activeQuests;

    public QuestData() {
        this(new LinkedHashMap<>(), new ArrayList<>());
    }

    public QuestData(Map<Quest, List<QuestObjective>> activeQuests, List<Quest> completed) {
        super(0);
        this.activeQuests = activeQuests;
        this.completed = completed;
    }

    public boolean isActive(Quest quest) {
        return this.activeQuests.containsKey(quest);
    }

    public boolean isCompleted(Quest quest) {
        return this.completed.contains(quest);
    }

    public List<Quest> getCompleted() {
        return completed;
    }

    public Map<Quest, List<QuestObjective>> getActiveQuests() {
        return activeQuests;
    }

    public void begin(Quest quest) {
        this.activeQuests.putIfAbsent(quest, quest.getObjectives());
    }

    public void complete(Player player, Quest quest, QuestObjective objective) {
        List<QuestObjective> objectives = this.activeQuests.get(quest);
        if (objectives != null) {
            objectives.remove(objective);
            objectives.addAll(objective.subsequent());

            quest.complete(player, objective);

            if (objectives.isEmpty()) {
                this.activeQuests.remove(quest);
                quest.complete(player);
            }
        }
    }

    @Override
    public QuestData copy() {
        Map<Quest, List<QuestObjective>> quests = new LinkedHashMap<>();
        activeQuests.forEach((quest, questObjectives) -> quests.put(quest, Items.looseClone(questObjectives)));
        return new QuestData(quests, Items.looseClone(this.completed));
    }

    @Override
    public DataContainer fill(DataContainer container) {
        List<DataContainer> containers = new ArrayList<>();

        this.activeQuests.forEach((quest, questObjectives) -> {
            containers.add(DataContainer.createNew()
                    .set(KEY, quest)
                    .set(VALUE, questObjectives));
        });

        return container.set(QUESTS, container)
                .set(COMPLETED, this.completed);
    }

    @Override
    public Optional<QuestData> from(DataView container) {
        if (!container.contains(QUESTS)) {
            return Optional.empty();
        }

        this.activeQuests = new LinkedHashMap<>();
        container.getViewList(QUESTS).get().forEach(view -> {
            this.activeQuests.put(view.getSerializable(KEY, Quest.class).get(), view.getSerializableList(VALUE, QuestObjective.class).get());
        });
        this.completed = container.getSerializableList(COMPLETED, Quest.class).get();

        return Optional.of(this);
    }

    public static class Builder implements DataBuilder<QuestData> {

        @Override
        public Optional<QuestData> build(DataView container) throws InvalidDataException {
            return new QuestData().from(container);
        }

    }

}
