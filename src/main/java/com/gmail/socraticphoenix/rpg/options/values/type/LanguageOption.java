package com.gmail.socraticphoenix.rpg.options.values.type;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.RPGPlugin;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.Text;

import java.util.Locale;

public class LanguageOption extends GeneralEnumOption {

    public LanguageOption() {
        super(Items.buildList(), "en-us", ItemTypes.BOOK, "rpg.options.language", RPGPlugin.ID, "language");
    }

    @Override
    public Text nameFor(Player player, String value) {
        return Text.of(RPGPlugin.getPlugin().getTranslationManager().getLanguages().get(Locale.forLanguageTag(value)).getName());
    }

    public void grabLangs() {
        RPGPlugin.getPlugin().getTranslationManager().getLanguages().forEach((key, value) -> {
            this.getValues().add(key.toLanguageTag());
        });
    }

}
