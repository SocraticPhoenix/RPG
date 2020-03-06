package com.gmail.socraticphoenix.rpg.scoreboard;

import com.gmail.socraticphoenix.rpg.registry.AbstractRegistryItem;
import com.gmail.socraticphoenix.rpg.registry.RPGRegistryItem;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.function.Function;

public class ScoreboardStat extends AbstractRegistryItem<RPGRegistryItem> implements DataSerializable, RPGRegistryItem {
    private Function<Player, Text> statGrabber;
    private Function<Player, Text> name;

    public ScoreboardStat(Function<Player, Text> statGrabber, Function<Player, Text> name, String pluginId, String id) {
        super(pluginId, id);
        this.statGrabber = statGrabber;
        this.name = name;
    }

    public Function<Player, Text> getStatGrabber() {
        return statGrabber;
    }

    public Function<Player, Text> getName() {
        return name;
    }

}
