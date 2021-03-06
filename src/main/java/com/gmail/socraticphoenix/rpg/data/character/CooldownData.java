package com.gmail.socraticphoenix.rpg.data.character;

import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.spell.Spell;
import com.gmail.socraticphoenix.rpg.translation.Messages;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.entity.living.player.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class CooldownData extends RPGData<CooldownData> {
    private Map<Spell, Long> cds;
    private Map<Spell, Integer> charges;
    private Map<Spell, Integer> maxCharges;

    public CooldownData(Map<Spell, Long> cds, Map<Spell, Integer> charges) {
        super(0);
        this.cds = cds;
        this.charges = charges;
    }

    public CooldownData() {
        this(new HashMap<>(), new HashMap<>());
    }

    public Map<Spell, Long> getCds() {
        return cds;
    }

    public static double[] getFormatted(long ms) {
        //           hours, minutes, seconds
        double[] result = {0, 0, 0};
        long hours = TimeUnit.MILLISECONDS.toHours(ms);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(ms - TimeUnit.HOURS.toMillis(hours));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(ms - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes));
        long millis = ms - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes) - TimeUnit.SECONDS.toMillis(seconds);

        result[0] = hours;
        result[1] = minutes;

        double s = seconds + (millis / 1000.0);
        result[2] = s;

        return result;
    }

    private static final DecimalFormat TWO_PLACES = new DecimalFormat("#.##");

    public static String getFormattedString(Player player, long ms) {
        double[] parts = getFormatted(ms);

        if (parts[0] != 0) {
            return Messages.translateString(player, "rpg.cd.h-m-s",
                    Messages.translate(player, "rpg.cd.hour", (long) parts[0]),
                    Messages.translate(player, "rpg.cd.minute", (long) parts[1]),
                    Messages.translateString(player, "rpg.cd.second", TWO_PLACES.format(parts[2])));
        } else if (parts[1] != 0) {
            return Messages.translateString(player, "rpg.cd.m-s",
                    Messages.translate(player, "rpg.cd.minute", (long) parts[1]),
                    Messages.translateString(player, "rpg.cd.second", TWO_PLACES.format(parts[2])));
        } else {
            return Messages.translateString(player, "rpg.cd.s",
                    Messages.translateString(player, "rpg.cd.second", TWO_PLACES.format(parts[2])));
        }
    }

    public String getRemainingString(Player player, Spell spell) {
        return getFormattedString(player, remaining(spell));
    }

    public long remaining(Spell spell) {
        Long cd = this.cds.get(spell);
        if (cd == null) {
            return 0;
        } else {
            return cd - System.currentTimeMillis();
        }
    }

    public void put(Spell spell, long ms) {
        this.cds.put(spell, ms + System.currentTimeMillis());
    }

    public boolean finished(Spell spell) {
        Long cd = this.cds.get(spell);
        if (cd == null) {
            return true;
        } else {
            long current = System.currentTimeMillis();
            if (cd <= current) {
                cds.remove(spell);
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean hasCharges(Spell spell, int mxCharges) {
        Integer charges = this.charges.get(spell);
        if (charges != null) {
            int mx = this.maxCharges.get(spell);
            int nCharges = charges;
            if (mx < mxCharges) {
                nCharges = nCharges + (mxCharges - mx);
            } else if (mx > mxCharges) {
                nCharges = Math.min(0, nCharges - (mx - mxCharges));
            }

            return nCharges > 0;
        } else {
            return true;
        }
    }

    public int getCharges(Spell spell) {
        Integer charges = this.charges.get(spell);
        if (charges != null) {
            return charges;
        }

        return spell.cost().getCharges();
    }

    public int maxCharges(Spell spell) {
        Integer charges = this.maxCharges.get(spell);
        if (charges != null) {
            return charges;
        }
        return spell.cost().getCharges();
    }

    public void reduceCharges(Spell spell, int mxCharges) {
        Integer charges = this.charges.get(spell);
        if (charges == null) {
            if (mxCharges > 0) {
                this.maxCharges.put(spell, mxCharges);
                this.charges.put(spell, mxCharges - 1);
            }
        } else {
            int mx = this.maxCharges.get(spell);
            int nCharges = charges - 1;
            if (mx < mxCharges) {
                nCharges = nCharges + (mxCharges - mx);
            } else if (mx > mxCharges) {
                nCharges = Math.min(0, nCharges - (mx - mxCharges));
            }
            this.charges.put(spell, nCharges);
            this.maxCharges.put(spell, mxCharges);
        }
    }

    public void cleanUp() {
        long current = System.currentTimeMillis();
        cds.entrySet().removeIf(e -> e.getValue() <= current);
    }

    @Override
    public CooldownData copy() {
        Map<Spell, Long> cds = new HashMap<>();
        cds.putAll(this.cds);

        Map<Spell, Integer> charges = new HashMap<>();
        charges.putAll(this.charges);
        return new CooldownData(cds, charges);
    }

    @Override
    public DataContainer fill(DataContainer container) {
        cleanUp();
        List<DataView> cooldowns = new ArrayList<>();
        List<DataView> charges = new ArrayList<>();
        this.cds.forEach((s, cd) -> {
            DataContainer entry = DataContainer.createNew();
            entry.set(SPELL, s);
            entry.set(COOLDOWN, cd);
            cooldowns.add(entry);
        });
        this.charges.forEach((s, ch) -> {
            DataContainer entry = DataContainer.createNew();
            entry.set(SPELL, s);
            entry.set(CHARGE, ch);
            entry.set(MAX_CHARGES, this.maxCharges.get(s));
            charges.add(entry);
        });

        return container
                .set(CHARGES, charges)
                .set(COOLDOWNS, cooldowns);
    }

    @Override
    public Optional<CooldownData> from(DataView container) {
        if (!container.contains(COOLDOWNS, CHARGES)) {
            return Optional.empty();
        }

        Map<Spell, Long> cds = new HashMap<>();
        container.getViewList(COOLDOWNS).get().forEach(v -> cds.put(v.getSerializable(SPELL, Spell.class).get(), v.getLong(COOLDOWN).get()));
        this.cds = cds;

        Map<Spell, Integer> charges = new HashMap<>();
        Map<Spell, Integer> maxCharges = new HashMap<>();
        container.getViewList(CHARGES).get().forEach(v -> {
            Spell spell = v.getSerializable(SPELL, Spell.class).get();
            charges.put(spell, v.getInt(CHARGE).get());
            maxCharges.put(spell, v.getInt(MAX_CHARGES).get());
        });
        this.charges = charges;

        return Optional.of(this);
    }

    public static class Builder implements DataBuilder<CooldownData> {

        @Override
        public Optional<CooldownData> build(DataView container) throws InvalidDataException {
            return new CooldownData().from(container);
        }

    }

}
