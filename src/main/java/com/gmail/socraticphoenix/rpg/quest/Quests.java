package com.gmail.socraticphoenix.rpg.quest;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.quest.quests.tutorial.TutorialQuest;
import com.gmail.socraticphoenix.rpg.registry.RPGRegisterEvent;
import org.spongepowered.api.event.Listener;

import java.util.List;

public class Quests {
    public static final Quest TUTORIAL = new TutorialQuest();


    @Listener
    public void onRegister(RPGRegisterEvent ev) {
        List<Quest> quests = Items.buildList(TUTORIAL);

        quests.forEach(q -> {
            ev.register(Quest.class, q);
            register(ev, q.getObjectives());
        });
    }

    private static void register(RPGRegisterEvent ev, List<QuestObjective> objectives) {
        objectives.forEach(obj -> {
            ev.register(QuestObjective.class, obj);
            register(ev, obj.subsequent());
        });
    }

}
