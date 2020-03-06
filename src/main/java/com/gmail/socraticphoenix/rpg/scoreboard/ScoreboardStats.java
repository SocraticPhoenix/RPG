package com.gmail.socraticphoenix.rpg.scoreboard;

import com.flowpowered.math.imaginary.Quaterniond;
import com.flowpowered.math.vector.Vector3d;
import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.data.RPGPlayerData;
import com.gmail.socraticphoenix.rpg.data.character.CharacterData;
import com.gmail.socraticphoenix.rpg.data.character.StatData;
import com.gmail.socraticphoenix.rpg.options.Options;
import com.gmail.socraticphoenix.rpg.registry.RPGRegisterEvent;
import com.gmail.socraticphoenix.rpg.stats.StatHelper;
import com.gmail.socraticphoenix.rpg.translation.Messages;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.critieria.Criteria;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlots;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.scoreboard.objective.displaymode.ObjectiveDisplayModes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.util.Direction;

import java.text.DecimalFormat;
import java.util.List;

public class ScoreboardStats implements Runnable {
    private int counter = 0;

    private static final DecimalFormat TWO_DECIMALS = new DecimalFormat("#.##");

    public static final ScoreboardStat HEALTH = new ScoreboardStat(p ->
            RPGData.stats(p).map(dt -> Text.of(TextColors.GREEN, Messages.translate(p, "rpg.health"), ": " + dt.getHealth() + " / " + dt.getMaxHealth())).orElse(Text.of("Health: ?/?")),
            p -> Messages.translate(p, "rpg.health"), RPGPlugin.ID, "health");
    public static final ScoreboardStat MANA = new ScoreboardStat(p ->
            RPGData.stats(p).map(dt -> Text.of(TextColors.AQUA, Messages.translate(p, "rpg.mana"), ": " + dt.getMana() + " / " + dt.getMaxMana())).orElse(Text.of("Mana: ?/?")),
            p -> Messages.translate(p, "rpg.mana"), RPGPlugin.ID, "mana");
    public static final ScoreboardStat COORDINATES = new ScoreboardStat(p -> Text.of(TextColors.YELLOW, Messages.translate(p, "rpg.scoreboard.coords"), ": ", (int) p.getLocation().getX() + ", " + (int) p.getLocation().getY() + ", " + (int) p.getLocation().getZ()),
            p -> Messages.translate(p, "rpg.scoreboard.coords"), RPGPlugin.ID, "coords");
    public static final ScoreboardStat X = new ScoreboardStat(p -> Text.of(TextColors.LIGHT_PURPLE, Messages.translate(p, "rpg.scoreboard.x"), ": ", TWO_DECIMALS.format(p.getLocation().getX())),
            p -> Messages.translate(p, "rpg.scoreboard.x"), RPGPlugin.ID, "x");
    public static final ScoreboardStat Y = new ScoreboardStat(p -> Text.of(TextColors.LIGHT_PURPLE, Messages.translate(p, "rpg.scoreboard.y"), ": ", TWO_DECIMALS.format(p.getLocation().getY())),
            p -> Messages.translate(p, "rpg.scoreboard.y"), RPGPlugin.ID, "y");
    public static final ScoreboardStat Z = new ScoreboardStat(p -> Text.of(TextColors.LIGHT_PURPLE, Messages.translate(p, "rpg.scoreboard.z"), ": ", TWO_DECIMALS.format(p.getLocation().getZ())),
            p -> Messages.translate(p, "rpg.scoreboard.z"), RPGPlugin.ID, "z");
    public static final ScoreboardStat PLAYER_COUNT = new ScoreboardStat(p -> Text.of(TextColors.YELLOW, Messages.translate(p, "rpg.scoreboard.players"), ": ", Sponge.getServer().getOnlinePlayers().size() + " / " + Sponge.getServer().getMaxPlayers()),
            p -> Messages.translate(p, "rpg.scoreboard.players"), RPGPlugin.ID, "player_count");
    public static final ScoreboardStat COMPASS = new ScoreboardStat(p -> {
        Vector3d rotation = p.getHeadRotation();
        return Text.of(TextColors.GRAY, Messages.translate(p, "rpg.scoreboard.compass"), ": ", Messages.translate(p, "rpg.scoreboard.compass." + Direction.getClosest(Quaterniond.fromAxesAnglesDeg(rotation.getX(), -rotation.getY(), rotation.getZ()).getDirection()).toString().toLowerCase()));
    }, p -> Messages.translate(p, "rpg.scoreboard.compass"), RPGPlugin.ID, "compass");
    public static final ScoreboardStat FIRST_NAME = new ScoreboardStat(p -> {
        return Text.of(RPGData.active(p).map(CharacterData::getFirstName).orElse(""));
    }, p -> Messages.translate(p, "rpg.scoreboard.first_name"), RPGPlugin.ID, "first_name");
    public static final ScoreboardStat LAST_NAME = new ScoreboardStat(p -> {
        return Text.of(RPGData.active(p).map(CharacterData::getLastName).orElse(""));
    }, p -> Messages.translate(p, "rpg.scoreboard.last_name"), RPGPlugin.ID, "last_name");
    public static final ScoreboardStat TOTAL_XP = new ScoreboardStat(p -> {
        return Text.of(TextColors.DARK_GREEN, Messages.translate(p, "rpg.scoreboard.total_xp"), ": ", RPGData.stats(p).map(StatData::getXp).orElse(0));
    }, p -> Messages.translate(p, "rpg.scoreboard.total_xp"), RPGPlugin.ID, "total_xp");
    public static final ScoreboardStat LEVEL = new ScoreboardStat(p -> {
        return Text.of(TextColors.DARK_GREEN, Messages.translate(p, "rpg.scoreboard.level"), ": ", StatHelper.getLevel(p));
    }, p -> Messages.translate(p, "rpg.scoreboard.level"), RPGPlugin.ID, "level");

    @Listener
    public void onRegister(RPGRegisterEvent ev) {
        ev.register(ScoreboardStat.class, HEALTH, MANA, COORDINATES, X, Y, Z, COMPASS, FIRST_NAME, LAST_NAME, PLAYER_COUNT, LEVEL, TOTAL_XP);
    }

    @Override
    public void run() {
        Sponge.getServer().getOnlinePlayers().forEach(p -> {
            RPGData.options(p).ifPresent(options -> {
                List<ScoreboardStat> stats = options.getOrCreate(Options.SCOREBOARD).getValue();

                if (stats.isEmpty()) {
                    Sponge.getServer().getServerScoreboard().ifPresent(p::setScoreboard);
                    return;
                }

                Objective objective = Objective.builder()
                        .name("RPG_S" + counter++)
                        .criterion(Criteria.DUMMY)
                        .objectiveDisplayMode(ObjectiveDisplayModes.INTEGER)
                        .displayName(Text.of(TextColors.BLUE, TextStyles.BOLD, p.getName()))
                        .build();

                for (int i = 0; i < stats.size(); i++) {
                    Text text = stats.get(i).getStatGrabber().apply(p);
                    if (text.toPlain().length() > 40) {
                        text = Text.of(TextColors.RED, "Error: too long!");
                    }
                    objective.getOrCreateScore(text)
                            .setScore(15 - i);
                }

                if (p.getScoreboard().getObjectives().stream().anyMatch(o -> o.getName().startsWith("RPG_S"))) {
                    p.getScoreboard().getObjectives().forEach(p.getScoreboard()::removeObjective);
                    p.getScoreboard().addObjective(objective);
                } else {
                    p.setScoreboard(Scoreboard.builder().objectives(Items.buildList(objective)).build());
                }
                p.getScoreboard().updateDisplaySlot(objective, DisplaySlots.SIDEBAR);
            });
        });
    }

}
