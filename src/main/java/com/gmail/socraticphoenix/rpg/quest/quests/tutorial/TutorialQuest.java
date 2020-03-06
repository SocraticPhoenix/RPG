package com.gmail.socraticphoenix.rpg.quest.quests.tutorial;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.quest.Quest;
import com.gmail.socraticphoenix.rpg.quest.QuestObjective;
import org.spongepowered.api.entity.living.player.Player;

public class TutorialQuest extends Quest {
    public static final QuestObjective SPEAK_WIZARD = new QuestObjective(Items.buildList(), RPGPlugin.ID, "rpg.quest.tutorial.speak");
    public static final QuestObjective KILL_QUEEN = new QuestObjective(Items.buildList(SPEAK_WIZARD), RPGPlugin.ID, "rpg.quest.tutorial.kill_queen");
    public static final QuestObjective CLEAR_SPIDERS = new QuestObjective(Items.buildList(KILL_QUEEN), RPGPlugin.ID, "rpg.quest.tutorial.clear_spiders");

    public TutorialQuest() {
        super(Items.buildList(CLEAR_SPIDERS), RPGPlugin.ID, "rpg.quest.tutorial");
    }

    @Override
    public void complete(Player player) {

    }

    @Override
    public void complete(Player player, QuestObjective objective) {

    }

}
