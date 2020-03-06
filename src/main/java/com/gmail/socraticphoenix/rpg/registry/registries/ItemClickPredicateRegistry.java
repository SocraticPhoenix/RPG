package com.gmail.socraticphoenix.rpg.registry.registries;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.click.ItemClickPredicate;
import com.gmail.socraticphoenix.rpg.registry.AbstractRegistry;
import com.gmail.socraticphoenix.rpg.registry.RPGRegistry;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class ItemClickPredicateRegistry extends AbstractRegistry<ItemClickPredicate> implements RPGRegistry<ItemClickPredicate> {
    public static final String ALWAYS_TRUE = RPGPlugin.ID + ":always_true";

    public ItemClickPredicateRegistry() {
        super(RPGPlugin.ID, "item_click_predicates", ItemClickPredicate.class);
        register(new ItemClickPredicate((stack, seq) -> true, RPGPlugin.ID, "always_true"));
    }

}
