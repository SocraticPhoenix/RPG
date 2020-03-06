package com.gmail.socraticphoenix.rpg.stats;

import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.data.character.StatData;
import com.gmail.socraticphoenix.rpg.data.mob.MobData;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomWandDataImpl;
import com.gmail.socraticphoenix.rpg.data.sponge.mob.CustomMobData;
import com.gmail.socraticphoenix.rpg.data.sponge.mob.CustomMobDataImpl;
import com.google.common.base.Strings;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public interface StatHelper {

    static int getXp(Player player) {
        return RPGData.stats(player).get().getXp();
    }

    static int getLevel(Player player) {
        return getLevel(getXp(player));
    }

    static int getLevel(long xp) {
        return (int) (xp / 1000 + 1);
    }

    static int getXp(int level) {
        return (level - 1) * 1000;
    }

    static int getLevel(Living living) {
        if (living instanceof Player) {
            return getLevel((Player) living);
        } else {
            return living.get(CustomMobData.class).map(c -> getLevel(c.value().get().getStats().getXp())).orElse(1);
        }
    }

    static void updateBars(Player player) {
        RPGData.stats(player).ifPresent(data -> {
            double heartDisplay = (double) data.getHealth() / (double) data.getMaxHealth() * 20;
            double manaDisplay = (double) data.getMana() / (double) data.getMaxMana() * 20;

            int hrts = heartDisplay < 1 ? 1 : (int) heartDisplay;
            int mna = manaDisplay < 1 ? 1 : (int) manaDisplay;

            player.offer(Keys.FOOD_LEVEL, manaDisplay <= 0 ? 0 : mna);
            player.offer(Keys.HEALTH, heartDisplay <= 0 ? 0 : (double) hrts);
            player.offer(Keys.EXPERIENCE_LEVEL, getLevel(player));

            int xp = data.getXp() - getXp(getLevel(data.getXp()));
            int mxXp = getXp(getLevel(data.getXp()) + 1) - getXp(getLevel(data.getXp()));


            player.offer(Keys.EXPERIENCE_SINCE_LEVEL, (int) (double) xp / mxXp * player.get(Keys.EXPERIENCE_FROM_START_OF_LEVEL).get());
        });
    }

    static void updateHealth(Living living, Living source) {
        if (living instanceof Player) {
            updateBars((Player) living);
        } else {
            living.get(CustomMobData.class).ifPresent(data -> {
                StatData stats = data.value().get().getStats();
                if (stats.getHealth() <= 0) {
                    //TODO killed
                    living.offer(Keys.HEALTH, 0.0);
                }
                living.offer(Keys.CUSTOM_NAME_VISIBLE, true);
                living.offer(Keys.DISPLAY_NAME, getHealthbar(stats.getHealth(), stats.getMaxHealth()));
            });
        }
    }

    static Text getHealthbar(int health, int maxHealth) {
        int num;
        if (health == maxHealth) {
            num = 20;
        } else if (health == 0) {
            num = 0;
        } else {
            num = (int) ((double) health / maxHealth * 20) + 1;
        }

        return Text.of(TextColors.DARK_GRAY, "[", TextColors.RED, Strings.repeat("|", 20 - num), TextColors.GREEN, Strings.repeat("|", num), TextColors.DARK_GRAY, "]");
    }

    static void setHealth(Living living, int health, Living source) {
        if (living instanceof Player) {
            setHealth((Player) living, health);
        } else {
            living.get(CustomMobData.class).ifPresent(data -> {
                MobData mobData = data.value().get().copy();
                StatData stats = mobData.getStats();
                int trgtHealth = health;
                if (trgtHealth < 0) {
                    trgtHealth = 0;
                } else if (trgtHealth > stats.getMaxHealth()) {
                    trgtHealth = stats.getMaxHealth();
                }

                stats.setHealth(trgtHealth);

                living.offer(new CustomMobDataImpl(mobData));
                updateHealth(living, source);
            });
        }
    }

    static void setHealth(Player player, int health) {
        RPGData.stats(player).ifPresent(data -> {
            int trgtHealth = health;
            if (trgtHealth < 0) {
                trgtHealth = 0;
            } else if (trgtHealth > data.getMaxHealth()) {
                trgtHealth = data.getMaxHealth();
            }

            data.setHealth(trgtHealth);
            updateBars(player);
        });
    }

    static void setMana(Player player, int mana) {
        RPGData.stats(player).ifPresent(data -> {
            int trgtMana = mana;
            if (trgtMana < 0) {
                trgtMana = 0;
            } else if (trgtMana > data.getMaxMana()) {
                trgtMana = data.getMaxMana();
            }

            data.setMana(trgtMana);
            updateBars(player);
        });
    }

    static int getMana(Player player) {
        return RPGData.stats(player).get().getMana();
    }

    static int getHealth(Player player) {
        return RPGData.stats(player).get().getHealth();
    }

    static void damage(Living target, double damage, Living source) {
        if (target instanceof Player) {
            Player p = (Player) target;
            setHealth(p, getHealth(p) - (int) damage);
        } else {
            target.get(CustomMobData.class).ifPresent(data -> {
                MobData mobData = data.value().get();
                setHealth(target, mobData.getStats().getHealth() - (int) damage, source);
            });
        }
    }
}
