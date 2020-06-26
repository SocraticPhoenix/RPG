package com.gmail.socraticphoenix.rpg.spell;

import com.flowpowered.math.TrigMath;
import com.flowpowered.math.imaginary.Quaterniond;
import com.flowpowered.math.vector.Vector3d;
import com.gmail.socraticphoenix.collect.coupling.KeyValue;
import com.gmail.socraticphoenix.rpg.augment.AugmentColor;
import com.gmail.socraticphoenix.rpg.augment.AugmentSlot;
import com.gmail.socraticphoenix.rpg.click.ClickType;
import com.gmail.socraticphoenix.rpg.click.ItemClickPredicate;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.data.character.CooldownData;
import com.gmail.socraticphoenix.rpg.data.character.SpellBookData;
import com.gmail.socraticphoenix.rpg.data.item.ItemData;
import com.gmail.socraticphoenix.rpg.data.item.WandData;
import com.gmail.socraticphoenix.rpg.data.mob.MobData;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomItemData;
import com.gmail.socraticphoenix.rpg.data.sponge.item.CustomWandData;
import com.gmail.socraticphoenix.rpg.data.sponge.mob.CustomMobData;
import com.gmail.socraticphoenix.rpg.event.RPGClickEvent;
import com.gmail.socraticphoenix.rpg.modifiers.*;
import com.gmail.socraticphoenix.rpg.registry.RPGRegisterEvent;
import com.gmail.socraticphoenix.rpg.spell.spells.*;
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

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Spells {
    public static final ItemClickPredicate SPELL_PREDICATE = new SpellSlotPredicate();
    public static final DamageSource SOURCE = DamageSource.builder().type(DamageTypes.CUSTOM).build();

    public static final Spell NONE = new NoopSpell();
    public static final Spell DEFAULT = new DefaultSpell();
    public static final Spell BLAST = new Blast();
    public static final Spell LIGHT_SLASH = new Slash(caster -> Dice.roll(4, 1, 6), "rpg.spells.light_slash");
    public static final Spell SLASH = new Slash(caster -> Dice.roll(2, StatHelper.getActualLevel(caster), 6), "rpg.spells.slash");
    public static final Spell ARROW = new Arrow(Types.PHYSICAL_RANGED, "rpg.spells.arrow");
    public static final Spell DIMENSION_ARROW = new DimensionArrow();

    @Listener
    public void onRegister(RPGRegisterEvent ev) {
        ev.register(Spell.class, NONE, DEFAULT, BLAST, LIGHT_SLASH, SLASH, ARROW, DIMENSION_ARROW);

        ev.register(ItemClickPredicate.class, SPELL_PREDICATE);
    }

    private static final int chars_per_line = 40;

    public static List<Text> spellSlotLore(Player player, SpellSlot slot) {
        Spell spell = slot.getSpell();

        List<Text> lore = new ArrayList<>();
        lore.add(Text.of(TextColors.BLUE, Messages.translate(player, spell.rawId())));

        for (AugmentSlot aug : slot.getAugmentSlots()) {
            if (aug.getAugments().isEmpty()) {
                lore.add(Text.of(aug.getColor().getColor(), Messages.translate(player, "rpg.augment.slot.empty",
                        Messages.translateString(player, aug.getColor().getKey()))));
            } else {
                lore.add(Text.of(aug.getColor().getColor(), Messages.translate(player, "rpg.augment.slot.filled",
                        Messages.translateString(player, aug.getColor().getKey()))));

                for (SetModifier modifier : aug.getAugments()) {
                    Spells.limitLoreLine((String) modifier.getModifier().getDesc().apply(player, modifier.getArguments())).forEach(s -> {
                        lore.add(Text.of(TextColors.LIGHT_PURPLE, s));
                    });
                }
            }
        }

        List<SetModifier> modifiers = slot.getModifiers();
        for (SetModifier modifier : modifiers) {
            Spells.limitLoreLine((String) modifier.getModifier().getDesc().apply(player, modifier.getArguments())).forEach(s -> {
                lore.add(Text.of(TextColors.DARK_PURPLE, s));
            });
        }

        if (slot.isBypass()) {
            Spells.limitLoreLine(Messages.translateString(player, "rpg.menu.wand.bypass")).forEach(s -> lore.add(Text.of(TextColors.GREEN, s)));
        }

        if (slot.isRestricted()) {
            Spells.limitLoreLine(Messages.translateString(player, "rpg.menu.wand.locked",
                    Messages.translateString(player, slot.getAllowed().getKey()))).forEach(s -> lore.add(Text.of(TextColors.RED, s)));
        }

        return lore;
    }

    public static List<Text> spellbookLore(Player player, Spell spell) {
        List<Text> result = new ArrayList<>();
        limitLoreLine(Messages.translateString(player, spell.translateDescKey())).forEach(s -> result.add(Text.of(TextColors.LIGHT_PURPLE, s)));
        result.add(Text.EMPTY);
        Cost cost = spell.cost();
        result.add(Text.of(TextColors.AQUA, Messages.translate(player, "rpg.menu.spellbook.mana"), ": ", cost.getMana()));
        result.add(Text.of(TextColors.RED, Messages.translate(player, "rpg.menu.spellbook.cd"), ": ", CooldownData.getFormattedString(player, cost.getCooldown())));
        result.add(Text.of(TextColors.GREEN, Messages.translate(player, "rpg.menu.spellbook.health"), ": ", cost.getHealth()));

        if (cost.getCharges() > 0) {
            result.add(Text.of(TextColors.DARK_GRAY, Messages.translate(player, "rpg.menu.spellbook.charges"), ": ", cost.getCharges()));
        }

        result.add(Text.EMPTY);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < spell.types().size(); i++) {
            builder.append(Messages.translateString(player, spell.types().get(i).getKey()));
            if (i != spell.types().size() - 1) {
                builder.append(", ");
            }
        }

        result.add(Text.of(TextColors.BLUE, "Types:"));
        limitLoreLine(builder.toString()).forEach(s -> result.add(Text.of(TextColors.GRAY, s)));

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
                    limitLoreLine((String) modifier.getModifier().getDesc().apply(player, modifier.getArguments())).forEach(s -> {
                        result.add(Text.of(TextColors.LIGHT_PURPLE, s));
                    });
                }
            }

            for (AugmentSlot aug : equip.getAugmentSlots()) {
                if (aug.getAugments().isEmpty()) {
                    result.add(Text.of(aug.getColor().getColor(), Messages.translate(player, "rpg.augment.slot.empty",
                            Messages.translateString(player, aug.getColor().getKey()))));
                } else {
                    result.add(Text.of(aug.getColor().getColor(), Messages.translate(player, "rpg.augment.slot.filled",
                            Messages.translateString(player, aug.getColor().getKey()))));

                    for (SetModifier modifier : aug.getAugments()) {
                        limitLoreLine((String) modifier.getModifier().getDesc().apply(player, modifier.getArguments())).forEach(s -> {
                            result.add(Text.of(TextColors.LIGHT_PURPLE, s));
                        });
                    }
                }
            }

            for (SetModifier modifier : equip.getModifiers()) {
                limitLoreLine((String) modifier.getModifier().getDesc().apply(player, modifier.getArguments())).forEach(s -> {
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

                if (slot.isRestricted()) {
                    builder = Text.of(TextColors.RED, "[", TextStyles.RESET, builder, TextStyles.BOLD, "]");
                }

                result.add(Text.of(prefix, ": ", builder));
            }
        }

        return result;
    }

    public static void flatKnockback(Living caster, Living target, List<SetModifier> casterMods, double power, Vector3d source, Spell spell) {
        knockback(caster, target, casterMods, power, new Vector3d(source.getX(), target.getLocation().getPosition().getY(), source.getZ()), spell);
    }

    public static void flatKnockback(Living caster, Living target, List<SetModifier> casterMods, double power, Vector3d source, Type type, KeyValue<String, Object>... context) {
        knockback(caster, target, casterMods, power, new Vector3d(source.getX(), target.getLocation().getPosition().getY(), source.getZ()), type, context);
    }

    public static void knockback(Living caster, Living target, List<SetModifier> casterMods, double power, Vector3d source, Spell spell) {
        knockback(caster, target, casterMods, power, source, spell.type(), KeyValue.of(Modifiers.SPELL, spell), KeyValue.of(Modifiers.SPELL_TYPE, spell));
    }

    public static void knockback(Living caster, Living target, List<SetModifier> casterMods, double power, Vector3d source, Type type, KeyValue<String, Object>... context) {
        Map<String, Object> contextMap = new HashMap<>();
        contextMap.put(Modifiers.ELEMENT_TYPE, type);
        contextMap.put(Modifiers.CAUSER, target);
        contextMap.put(Modifiers.RECEIVER, target);
        Stream.of(context).forEach(k -> contextMap.put(k.getKey(), k.getValue()));

        List<SetModifier> targetMods = Modifiers.getRelevant(target);
        power = Modifiers.modify(power, ModifiableTypes.KNOCKBACK, contextMap, KeyValue.of(caster, casterMods), KeyValue.of(target, targetMods));

        target.setVelocity(target.getVelocity().add(target.getLocation().getPosition().sub(source).normalize().mul(power)));
    }

    public static void damage(Living caster, Living target, List<SetModifier> casterMods, double damage, Spell spell) {
        damage(caster, target, casterMods, damage, spell.type(), KeyValue.of(Modifiers.SPELL, spell), KeyValue.of(Modifiers.SPELL_TYPE, spell.type()));
    }

    public static void damage(Living caster, Living target, List<SetModifier> casterMods, double damage, Type type, KeyValue<String, Object>... context) {
        target.damage(0, SOURCE);

        Map<String, Object> contextMap = new HashMap<>();
        contextMap.put(Modifiers.ELEMENT_TYPE, type);
        contextMap.put(Modifiers.CAUSER, target);
        contextMap.put(Modifiers.RECEIVER, target);
        Stream.of(context).forEach(k -> contextMap.put(k.getKey(), k.getValue()));

        List<SetModifier> targetMods = Modifiers.getRelevant(target);

        damage = Modifiers.modify(damage, ModifiableTypes.DAMAGE, contextMap, KeyValue.of(caster, casterMods), KeyValue.of(target, targetMods));
        StatHelper.damage(target, damage, caster);

        Modifiers.modify(damage, ModifiableTypes.ON_DAMAGE, contextMap, KeyValue.of(caster, casterMods), KeyValue.of(target, targetMods));
    }

    public static String clickString(Player player, List<ClickType> seq) {
        StringBuilder builder = new StringBuilder();
        seq.forEach(c -> builder.append(Messages.translateString(player, c.getName() + ".short")));
        return builder.toString();
    }

    public static List<String> limitLoreLine(String str) {
        List<String> strings = new ArrayList<>();

        String[] words = str.split(" ");
        int index = 0;

        StringBuilder builder = new StringBuilder();

        while (index < words.length) {
            int limit = 0;
            for (; limit < chars_per_line && index < words.length; limit += words[index].length(), index++) {
                builder.append(words[index]).append(" ");
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

            List<SetModifier> relevant = Modifiers.snapshotRelevant(player);
            relevant.addAll(modifiers);

            Map<String, Object> context = new HashMap<>();
            context.put(Modifiers.SPELL, spell);

            Cost cost = spell.cost();

            double mana = cost.getMana();
            double cooldown = cost.getCooldown();
            double health = cost.getHealth();
            double charges = cost.getCharges();

            mana = Modifiers.modify(player, mana, ModifiableTypes.COST_MANA, context, relevant);
            cooldown = Modifiers.modify(player, cooldown, ModifiableTypes.COST_COOLDOWN, context, relevant);
            health = Modifiers.modify(player, health, ModifiableTypes.COST_HEALTH, context, relevant);
            charges = Modifiers.modify(player, charges, ModifiableTypes.CHARGES, context, relevant);

            if (cost.hasCharges()) {
                if (!manager.hasCharges(spell, (int) charges)) {
                    player.sendTitle(Title.builder()
                            .actionBar(Text.of(TextColors.RED, Messages.translate(player, spell.rawId()), " - ", Messages.translate(player, "rpg.spells.msg.charges")))
                            .stay(1)
                            .build());
                    return;
                }
            }

            if (StatHelper.getMana(player) < mana) {
                player.sendTitle(Title.builder()
                        .actionBar(Text.of(TextColors.RED, Messages.translate(player, spell.rawId()), " - ", Messages.translate(player, "rpg.spells.msg.mana")))
                        .stay(1)
                        .build());
                return;
            } else if (StatHelper.getHealth(player) < health) {
                player.sendTitle(Title.builder()
                        .actionBar(Text.of(TextColors.RED, Messages.translate(player, spell.rawId()), " - ", Messages.translate(player, "rpg.spells.msg.health")))
                        .stay(1)
                        .build());
                return;
            }

            manager.put(spell, (int) cooldown);
            manager.reduceCharges(spell, (int) charges);
            StatHelper.setMana(player, StatHelper.getMana(player) - (int) mana);
            StatHelper.setHealth(player, StatHelper.getHealth(player) - (int) health);

            spell.activate(player, modifiers);

            if (cost.hasCharges()) {
                player.sendTitle(Title.builder()
                        .actionBar(Text.of(TextColors.GREEN, Messages.translate(player, spell.rawId()), " - ", Messages.translate(player, "rpg.spells.msg.cast_charges", manager.getCharges(spell), manager.maxCharges(spell))))
                        .stay(1)
                        .build());
            } else {
                player.sendTitle(Title.builder()
                        .actionBar(Text.of(TextColors.GREEN, Messages.translate(player, spell.rawId()), " - ", Messages.translate(player, "rpg.spells.msg.cast")))
                        .stay(1)
                        .build());
            }
        });
    }

    private static final BlockType[] passable = new BlockType[]
            {BlockTypes.AIR, BlockTypes.WATER, BlockTypes.FLOWING_WATER,
            BlockTypes.LAVA, BlockTypes.FLOWING_LAVA,
            BlockTypes.GRASS, BlockTypes.TALLGRASS,
            BlockTypes.RED_FLOWER, BlockTypes.YELLOW_FLOWER,
            BlockTypes.WHEAT, BlockTypes.CARROTS, BlockTypes.BEETROOTS, BlockTypes.NETHER_WART,
            BlockTypes.STONE_BUTTON, BlockTypes.WOODEN_BUTTON, BlockTypes.STONE_PRESSURE_PLATE,
            BlockTypes.WOODEN_PRESSURE_PLATE, BlockTypes.HEAVY_WEIGHTED_PRESSURE_PLATE, BlockTypes.LIGHT_WEIGHTED_PRESSURE_PLATE,
            BlockTypes.DEADBUSH, BlockTypes.RAIL, BlockTypes.ACTIVATOR_RAIL, BlockTypes.DETECTOR_RAIL, BlockTypes.GOLDEN_RAIL,
            BlockTypes.WEB, BlockTypes.CARPET, BlockTypes.VINE, BlockTypes.TRIPWIRE, BlockTypes.STANDING_SIGN,
            BlockTypes.WALL_SIGN, BlockTypes.PORTAL, BlockTypes.END_PORTAL,
            BlockTypes.DOUBLE_PLANT, BlockTypes.SAPLING,
            BlockTypes.BROWN_MUSHROOM, BlockTypes.RED_MUSHROOM};

    public static final Predicate<BlockRayHit<World>> SKIP_FILTER = b -> {
        BlockType type = b.getExtent().getBlockType(b.getBlockPosition());

        for (BlockType blockType : passable) {
            if (type == blockType) {
                return true;
            }
        }

        return false;
    };

    public static final Predicate<BlockRayHit<World>> STOP_FILTER = SKIP_FILTER.negate();

    public static Vector3d getIntersection(World world, Vector3d start, Vector3d direction, Predicate<Entity> friends, double range) {
        Vector3d target = BlockRay.from(world, start).direction(direction).distanceLimit(range)
                .skipFilter(SKIP_FILTER).stopFilter(STOP_FILTER).end().map(BlockRayHit::getPosition).orElse(start.add(direction.normalize().mul(range)));

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

    public static Vector3d getIntersection(Living caster, Predicate<Entity> filter, double range) {
        World world = caster.getWorld();

        Vector3d start = getStartLocation(caster);

        Vector3d target = BlockRay.from(caster).distanceLimit(range)
                .skipFilter(SKIP_FILTER).stopFilter(STOP_FILTER).end().map(BlockRayHit::getPosition).orElse(start.add(Spells.looking(caster).normalize().mul(range)));

        Set<EntityUniverse.EntityHit> entities = world.getIntersectingEntities(start, target, e -> e.getEntity() instanceof Living && filter.test(e.getEntity()));
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
                .skipFilter(SKIP_FILTER).stopFilter(STOP_FILTER).end().map(BlockRayHit::getPosition).orElse(start.add(Spells.looking(caster).normalize().mul(range)));

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

    public static Vector3d getStartLocation(Entity caster) {
        if (caster.getProperty(EyeLocationProperty.class).isPresent()) {
            return caster.getProperty(EyeLocationProperty.class).get().getValue();
        } else {
            return caster.getLocation().getPosition();
        }
    }

    public static Set<Living> getIntersectingRectangle(Living caster, Vector3d source, AABB aabb, Predicate<Entity> filter) {
        return (Set) caster.getWorld().getIntersectingEntities(aabb, e -> e instanceof Living && filter.test(e) && hasLos(source, e));
    }

    public static Set<Living> getIntersectingSphere(Living caster, Vector3d center, double radius, Predicate<Entity> filter) {
        double radMx = radius + 1;
        double rSq = radius * radius;
        AABB max = new AABB(center.add(radMx, radMx, radMx), center.sub(radMx, radMx, radMx));

        return (Set) caster.getWorld().getIntersectingEntities(max, e -> e instanceof Living && e.getLocation().getPosition().distanceSquared(center) <= rSq && filter.test(e) && hasLos(center, e));
    }

    public static Set<Living> getIntersectingCone(Living caster, Vector3d center, Vector3d direction, double radius, double angleDegs, Predicate<Entity> filter) {
        double radMx = radius + 1;
        double rSq = radius * radius;
        double mag = direction.length();
        AABB max = new AABB(center.add(radMx, radMx, radMx), center.sub(radMx, radMx, radMx));

        return (Set) caster.getWorld().getIntersectingEntities(max, e -> {
            if (e instanceof Living && e.getLocation().getPosition().distanceSquared(center) <= rSq && filter.test(e) && hasLos(center, e)) {
                Vector3d newDir = e.getLocation().getPosition().sub(center);

                double ang = TrigMath.acos(direction.dot(newDir) / (mag * newDir.length()));
                return ang < Math.toRadians(angleDegs);
            }
            return false;
        });
    }

    public static boolean hasLos(Vector3d center, Entity entity) {
        return true;


    }

    public static Predicate<Entity> enemies(Entity caster) {
        return friends(caster).negate();
    }

    public static Predicate<Entity> friends(Entity caster) {
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
        cast(player, slot.getSpell(), slot.getAllMods(), !slot.isBypass());
    }

    @Listener
    public void onClick(RPGClickEvent ev, @First Player player) {
        if (ev.getStack().get(CustomWandData.class).isPresent()) {
            if (RPGData.canUse(ev.getStack(), player)) {
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

}
