package com.gmail.socraticphoenix.rpg.translation;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.translation.FixedTranslation;

public class Messages {

    public static Text translate(Player player, String key, Object... args) {
        return Text.of(translateString(player, key, args));
    }

    public static String translateString(Player player, String key, Object...args) {
        String str = RPGPlugin.getPlugin().getTranslationManager().translate(player, key);
        for (int i = 0; i < args.length; i++) {
           str= str.replace("${" + i + "}", String.valueOf(args[i]));
        }
        return str;
    }

}
