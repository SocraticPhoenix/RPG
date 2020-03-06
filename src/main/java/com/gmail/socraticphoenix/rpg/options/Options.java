package com.gmail.socraticphoenix.rpg.options;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.options.values.Option;
import com.gmail.socraticphoenix.rpg.options.values.type.GeneralBooleanOption;
import com.gmail.socraticphoenix.rpg.options.values.type.LanguageOption;
import com.gmail.socraticphoenix.rpg.options.values.type.ScoreboardOption;
import com.gmail.socraticphoenix.rpg.registry.RPGRegisterEvent;
import com.gmail.socraticphoenix.rpg.scoreboard.ScoreboardStat;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.item.ItemTypes;

import java.util.List;

public class Options {
    public static final Option<String> LANGUAGE = new LanguageOption();
    public static final Option<List<ScoreboardStat>> SCOREBOARD = new ScoreboardOption();
    public static final Option<Boolean> USE_NUMBER_SELECTION = new GeneralBooleanOption(false, ItemTypes.ARROW, "rpg.options.use_number_selection", RPGPlugin.ID, "use_number_selection");

    @Listener
    public void onRegister(RPGRegisterEvent ev) {
        ev.register(Option.class, USE_NUMBER_SELECTION, SCOREBOARD, LANGUAGE);

        ((LanguageOption) LANGUAGE).grabLangs();
    }

}
