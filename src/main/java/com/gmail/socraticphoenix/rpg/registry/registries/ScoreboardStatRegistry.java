package com.gmail.socraticphoenix.rpg.registry.registries;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.registry.AbstractRegistry;
import com.gmail.socraticphoenix.rpg.scoreboard.ScoreboardStat;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

public class ScoreboardStatRegistry extends AbstractRegistry<ScoreboardStat> {
    public static final String TITLE = RPGPlugin.ID + ":title";

    public ScoreboardStatRegistry() {
        super(RPGPlugin.ID, "scoreboard_stats", ScoreboardStat.class);
    }

}
