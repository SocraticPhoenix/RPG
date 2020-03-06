package com.gmail.socraticphoenix.rpg.spell;

import com.flowpowered.math.imaginary.Quaterniond;
import com.flowpowered.math.vector.Vector3d;
import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.augment.AugmentColor;
import com.gmail.socraticphoenix.rpg.augment.AugmentSlot;
import com.gmail.socraticphoenix.rpg.click.ClickType;
import com.gmail.socraticphoenix.rpg.click.ItemClickPredicate;
import com.gmail.socraticphoenix.rpg.data.mob.MobData;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomItemData;
import com.gmail.socraticphoenix.rpg.data.sponge.mob.CustomMobData;
import com.gmail.socraticphoenix.rpg.event.RPGClickEvent;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.data.character.CooldownData;
import com.gmail.socraticphoenix.rpg.data.character.SpellBookData;
import com.gmail.socraticphoenix.rpg.data.item.ItemData;
import com.gmail.socraticphoenix.rpg.data.item.WandData;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomWandData;
import com.gmail.socraticphoenix.rpg.modifiers.ModifiableType;
import com.gmail.socraticphoenix.rpg.modifiers.ModifiableTypes;
import com.gmail.socraticphoenix.rpg.modifiers.Modifiers;
import com.gmail.socraticphoenix.rpg.modifiers.SetModifier;
import com.gmail.socraticphoenix.rpg.options.values.Option;
import com.gmail.socraticphoenix.rpg.registry.RPGRegisterEvent;
import com.gmail.socraticphoenix.rpg.spell.spells.Blast;
import com.gmail.socraticphoenix.rpg.spell.spells.DefaultSpell;
import com.gmail.socraticphoenix.rpg.spell.spells.NoopSpell;
import com.gmail.socraticphoenix.rpg.spell.spells.Slash;
import com.gmail.socraticphoenix.rpg.stats.StatHelper;
import com.gmail.socraticphoenix.rpg.translation.Messages;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.property.entity.EyeLocationProperty;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.DamageTypes;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.title.Title;
import org.spongepowered.api.util.AABB;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.EntityUniverse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class Spells {
    public static final ItemClickPredicate SPELL_PREDICATE = new SpellSlotPredicate();
    public static final DamageSource SOURCE = DamageSource.builder().type(DamageTypes.CUSTOM).build();

    public static final Spell NONE = new NoopSpell();
    public static final Spell DEFAULT = new DefaultSpell();
    public static final Spell BLAST = new Blast();
    public static final Spell SLASH = new Slash();

    @Listener
    public void onRegister(RPGRegisterEvent ev) {
        ev.register(Spell.class, NONE, DEFAULT, BLAST, SLASH);

        ev.register(ItemClickPredicate.class, SPELL_PREDICATE);
    }

    private static final int words_per_line = 5;

    public static List<Text> spellbookLore(Player player, Spell spell) {
        List<Text> result = new ArrayList<>();
        limitLoreLine(Messages.translateString(player, spell.translateDescKey()), words_per_line).forEach(s -> result.add(Text.of(TextColors.LIGHT_PURPLE, s)));
        result.add(Text.EMPTY);
        Cost cost = spell.cost();
        result.add(Text.of(TextColors.AQUA, Messages.translate(player, "rpg.menu.spellbook.mana"), ": ", cost.getMana()));
        result.add(Text.of(TextColors.RED, Messages.translate(player, "rpg.menu.spellbook.cd"), ": ", CooldownData.getFormattedString(player, cost.getCooldown())));
        result.add(Text.EMPTY);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < spell.types().size(); i++) {
            builder.append(Messages.translateString(player, spell.types().get(i).getKey()));
            if (i != spell.types().size() - 1) {
                builder.append(", ");
            }
        }

        result.add(Text.of(TextColors.BLUE, "Types:"));
        limitLoreLine(builder.toString(), words_per_line).forEach(s -> result.add(Text.of(TextColors.GRAY, s)));

        return result;
    }

    public static List<Text> wandLore(Player player, ItemStack stack) {
        return wandLore(player, stack.get(CustomWandData.class).map(c -> c.value().get()).orElse(null), stack.get(CustomItemData.class).map(c -> c.value().get()).orElse(null));
    }

    public static List<Text> wandLore(Player player, WandData spell, ItemData equip) {
        List<Text> result = new ArrayList<>();

        if (equip != null) {
            if (equip.getAugment().getColor() != AugmentColor.NONE) {
                result.add(Text.of(equip.getAugment().getColor().getColor(), Messages.translate(player, "rpg.augment.item",
                        Messages.translateString(player, equip.getAugment().getColor().getKey()))));
                for (SetModifier modifier : equip.getAugment().getAugments()) {
                    limitLoreLine((String) modifier.getModifier().getDesc().apply(player, modifier.getArguments()), words_per_line).forEach(s -> {
                        result.add(Text.of(TextColors.LIGHT_PURPLE, s));
                    });
                }
            }

            for (AugmentSlot slot : equip.getAugmentSlots()) {
                if (slot.getAugments().isEmpty()) {
                    result.add(Text.of(slot.getColor().getColor(), Messages.translate(player, "rpg.augment.slot.empty",
                            Messages.translateString(player, slot.getColor().getKey()))));
                } else {
                    result.add(Text.of(slot.getColor().getColor(), Messages.translate(player, "rpg.augment.slot.filled",
                            Messages.translateString(player, slot.getColor().getKey()))));

                    for (SetModifier modifier : slot.getAugments()) {
                        limitLoreLine((String) modifier.getModifier().getDesc().apply(player, modifier.getArguments()), words_per_line).forEach(s -> {
                            result.add(Text.of(TextColors.LIGHT_PURPLE, s));
                        });
                    }
                }
            }

            for (SetModifier modifier : equip.getModifiers()) {
                limitLoreLine((String) modifier.getModifier().getDesc().apply(player, modifier.getArguments()), words_per_line).forEach(s -> {
                    result.add(Text.of(TextColors.DARK_PURPLE, s));
                });
            }
        }

        if (spell != null) {
            if (spell.isOmniwand()) {
                result.add(Text.of(TextColors.DARK_PURPLE, Messages.translate(player, "rpg.wand.omniwand")));
            }

            List<SpellSlot> slots = spell.getSlots();
            for (int i = 0; i < slots.size(); i++) {
                SpellSlot slot = slots.get(i);

                Text prefix = Text.of(TextColors.GOLD, clickString(player, slot.getSequence()));

                Text builder = Text.of(TextColors.BLUE, Messages.translate(player, slot.getSpell().rawId()));

                if (slot.hasModifiers()) {
                    builder = Text.of(TextColors.LIGHT_PURPLE, "[", builder, TextColors.LIGHT_PURPLE, "]");
                }

                if (slot.isBypass()) {
                    builder = Text.of(TextColors.GREEN, "[", builder, TextColors.GREEN, "]");
                }

                if (slot.isLocked()) {
                    builder = Text.of(TextColors.RED, TextStyles.BOLD, "[", TextStyles.RESET, builder, TextColors.RED, TextStyles.BOLD, "]");
                }

                result.add(Text.of(prefix, ": ", builder));
            }
        }

        return result;
    }

    public static void damage(Living caster, Living target, Spell spell, double damage) {
        target.damage(0, SOURCE);
        damage = modifyStat(caster, ModifiableTypes.DAMAGE_INCREASE, damage, spell, Modifiers.getRelevant(caster));
        damage = modifyStat(target, ModifiableTypes.DAMAGE_REDUCTION, damage, spell, Modifiers.getRelevant(target));
        applyHitModifiers(caster, target, spell, Modifiers.getRelevant(caster));
        StatHelper.damage(target, damage, caster);
    }

    public static <T> T modifyStat(Living caster, ModifiableType stat, T value, Spell spell, List<SetModifier> modifiers) {
        Map<String, Object> context = new HashMap<>();
        context.put(Modifiers.SPELL, spell);
        return Modifiers.modify(caster, value, stat, context, modifiers);
    }

    public static void applyHitModifiers(Living caster, Living target, Spell spell, List<SetModifier> modifiers) {
        Map<String, Object> context = new HashMap<>();
        context.put(Modifiers.SPELL, spell);
        Modifiers.modify(caster, target, ModifiableTypes.ON_HIT, context, modifiers);
    }

    public static String clickString(Player player, List<ClickType> seq) {
        StringBuilder builder = new StringBuilder();
        seq.forEach(c -> builder.append(Messages.translateString(player, c.getName() + ".short")));
        return builder.toString();
    }

    public static List<String> limitLoreLine(String str, int wordsPerLine) {
        List<String> strings = new ArrayList<>();

        String[] words = str.split(" ");
        int index = 0;

        StringBuilder builder = new StringBuilder();

        while (index < words.length) {
            int limit = index + wordsPerLine;
            for (int i = index; i < limit && index < words.length; i++, index++) {
                builder.append(words[i]).append(" ");
            }
            strings.add(builder.toString());
            builder = new StringBuilder();
        }

        return strings;
    }

    public static Vector3d looking(Living living) {
        Vector3d rotation = living.getHeadRotation();
        return Quaterniond.fromAxesAnglesDeg(rotation.getX(), -rotation.getY(), rotation.getZ()).getDirection();
    }

    public static AABB area(Vector3d location, double radius) {
        return new AABB(location.add(radius, radius, radius), location.sub(radius, radius, radius));
    }

    static void cast(Player player, Spell spell, List<SetModifier> modifiers, boolean checkSpellbook) {
        if (checkSpellbook) {
            Optional<SpellBookData> spellbook = RPGData.spellbook(player);
            if (spellbook.isPresent()) {
                if (!spellbook.get().knows(spell)) {
                    player.sendTitle(Title.builder()
                            .actionBar(Text.of(TextColors.RED, Messages.translate(player, spell.rawId()), " - ", Messages.translate(player, "rpg.spells.msg.not_in_book")))
                            .stay(1)
                            .build());
                    return;
                }
            } else {
                player.sendTitle(Title.builder()
                        .actionBar(Text.of(TextColors.RED, Messages.translate(player, spell.rawId()), " - ", Messages.translate(player, "rpg.spells.msg.not_in_book")))
                        .stay(1)
                        .build());
                return;
            }
        }

        RPGData.cooldowns(player).ifPresent(manager -> {
            if (!manager.finished(spell)) {
                player.sendTitle(Title.builder()
                        .actionBar(Text.of(TextColors.RED, Messages.translate(player, spell.rawId()), " - ", manager.getRemainingString(player, spell)))
                        .stay(1)
                        .build());
                return;
            }

            List<SetModifier> relevant = Modifiers.getRelevant(player);
            relevant.addAll(modifiers);

            Map<String, Object> context = new HashMap<>();
            context.put(Modifiers.SPELL, spell);

            Cost cost = spell.cost();

            double mana = cost.getMana();
            double cooldown = cost.getCooldown();

            mana = Modifiers.modify(player, mana, ModifiableTypes.COST_MANA, context, relevant);
            cooldown = Modifiers.modify(player, cooldown, ModifiableTypes.COST_COOLDOWN, context, relevant);

            if (StatHelper.getMana(player) < cost.getMana()) {
                player.sendTitle(Title.builder()
                        .actionBar(Text.of(TextColors.RED, Messages.translate(player, spell.rawId()), " - ", Messages.translate(player, "rpg.spells.msg.mana")))
                        .stay(1)
                        .build());
                return;
            }

            manager.put(spell, (int) cooldown);
            StatHelper.setMana(player, StatHelper.getMana(player) - (int) mana);
            spell.activate(player, modifiers);
            player.sendTitle(Title.builder()
                    .actionBar(Text.of(TextColors.GREEN, Messages.translate(player, spell.rawId()), " - ", Messages.translate(player, "rpg.spells.msg.cast")))
                    .stay(1)
                    .build());
        });
    }

    public static final Predicate<BlockRayHit<World>> HIT_FILTER = b -> {
        BlockType type = b.getExtent().getBlockType(b.getBlockPosition());
        return type == BlockTypes.AIR ||
                type == BlockTypes.WATER || type == BlockTypes.FLOWING_WATER ||
                type == BlockTypes.LAVA || type == BlockTypes.FLOWING_LAVA;
    };

    public static Vector3d getIntersection(World world, Vector3d start, Vector3d direction, Predicate<Entity> friends, double range) {
        Vector3d target = BlockRay.from(world, start).direction(direction).distanceLimit(range)
                .skipFilter(HIT_FILTER).end().map(BlockRayHit::getPosition).orElse(start.add(direction.normalize().mul(range)));

        Set<EntityUniverse.EntityHit> entities = world.getIntersectingEntities(start, target, e -> e.getEntity() instanceof Living && !friends.test(e.getEntity()));
        if (entities.isEmpty()) {
            return target;
        } else {
            double dist = Double.MAX_VALUE;

            for (EntityUniverse.EntityHit hit : entities) {
                if (hit.getDistance() < dist) {
                    target = hit.getIntersection();
                    dist = hit.getDistance();
                }
            }

            return target;
        }
    }

    public static Vector3d getIntersection(Living caster, Predicate<Entity> friends, double range) {
        World world = caster.getWorld();

        Vector3d start = getStartLocation(caster);

        Vector3d target = BlockRay.from(caster).distanceLimit(range)
                .skipFilter(HIT_FILTER).end().map(BlockRayHit::getPosition).orElse(start.add(Spells.looking(caster).normalize().mul(range)));

        Set<EntityUniverse.EntityHit> entities = world.getIntersectingEntities(start, target, e -> e.getEntity() instanceof Living && !friends.test(e.getEntity()));
        if (entities.isEmpty()) {
            return target;
        } else {
            double dist = Double.MAX_VALUE;

            for (EntityUniverse.EntityHit hit : entities) {
                if (hit.getDistance() < dist) {
                    target = hit.getIntersection();
                    dist = hit.getDistance();
                }
            }

            return target;
        }
    }

    public static Optional<Living> getIntersectedEntity(Living caster, Predicate<Entity> friends, double range) {
        World world = caster.getWorld();

        Vector3d start = getStartLocation(caster);

        Vector3d target = BlockRay.from(caster).distanceLimit(range)
                .skipFilter(HIT_FILTER).end().map(BlockRayHit::getPosition).orElse(start.add(Spells.looking(caster).normalize().mul(range)));

        Set<EntityUniverse.EntityHit> entities = world.getIntersectingEntities(start, target, e -> e.getEntity() instanceof Living && !friends.test(e.getEntity()));
        if (entities.isEmpty()) {
            return Optional.empty();
        } else {
            double dist = Double.MAX_VALUE;
            Entity entity = null;

            for (EntityUniverse.EntityHit hit : entities) {
                if (hit.getDistance() < dist) {
                    dist = hit.getDistance();
                    entity = hit.getEntity();
                }
            }

            return Optional.ofNullable((Living) entity);
        }
    }

    public static void knockback(Entity target, Vector3d source, double power) {
        target.setVelocity(target.getVelocity().add(target.getLocation().getPosition().sub(source).normalize().mul(power)));
    }

    public static void flatKnockback(Entity target, Vector3d source, double power) {
        Vector3d vel = target.getLocation().getPosition().sub(source);
        vel = new Vector3d(vel.getX(), 0, vel.getZ());
        target.setVelocity(target.getVelocity().add(vel.normalize().mul(power)));
    }


    public static Vector3d getStartLocation(Entity caster) {
        if (caster.getProperty(EyeLocationProperty.class).isPresent()) {
            return caster.getProperty(EyeLocationProperty.class).get().getValue();
        } else {
            return caster.getLocation().getPosition();
        }
    }

    public static Set<Living> getIntersecting(Living caster, AABB aabb, Predicate<Entity> friends) {
        return (Set<Living>) (Set) caster.getWorld().getIntersectingEntities(aabb, e -> e instanceof Living && !friends.test(e));
    }

    public static Predicate<Entity> friends(Living caster) {
        if (caster instanceof Player) {
            return e -> {
                if (e == caster || e instanceof Player) {
                    return true;
                }

                if (e.get(CustomMobData.class).isPresent()) {
                    return e.get(CustomMobData.class).get().value().get().getOwner() != MobData.NONE;
                }

                return false;
            };
        } else {
            if (caster.get(CustomMobData.class).isPresent() && caster.get(CustomMobData.class).get().value().get().getOwner() != MobData.NONE) {
                return e -> e == caster || e instanceof Player;
            } else {
                return e -> {
                    if (e == caster) {
                        return true;
                    }

                    if (caster.get(CustomMobData.class).isPresent()) {
                        return e.get(CustomMobData.class).get().value().get().getOwner() == MobData.NONE;
                    } else {
                        return !(e instanceof Player);
                    }
                };
            }
        }
    }

    public static void cast(Player player, SpellSlot slot) {
        cast(player, slot.getSpell(), slot.getModifiers(), !slot.isBypass());
    }

    @Listener
    public void onClick(RPGClickEvent ev, @First Player player) {
        if (ev.getStack().get(CustomWandData.class).isPresent()) {
            CustomWandData data = ev.getStack().get(CustomWandData.class).get();
            for (SpellSlot slot : data.value().get().getSlots()) {
                if (slot.getSequence().equals(ev.getSequence())) {
                    cast(player, slot);
                    return;
                }
            }

            player.clearTitle();
        }
    }

}
