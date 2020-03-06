package com.gmail.socraticphoenix.rpg.options.values.type;

import com.flowpowered.math.vector.Vector2i;
import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.data.character.InventoryData;
import com.gmail.socraticphoenix.rpg.data.character.RuntimeData;
import com.gmail.socraticphoenix.rpg.inventory.InventoryHelper;
import com.gmail.socraticphoenix.rpg.inventory.button.RuntimeButtonAction;
import com.gmail.socraticphoenix.rpg.inventory.button.data.ButtonData;
import com.gmail.socraticphoenix.rpg.inventory.player.menus.OptionMenu;
import com.gmail.socraticphoenix.rpg.options.values.Option;
import com.gmail.socraticphoenix.rpg.options.values.SetOption;
import com.gmail.socraticphoenix.rpg.options.values.set.BasicSetOption;
import com.gmail.socraticphoenix.rpg.registry.AbstractRegistryItem;
import com.gmail.socraticphoenix.rpg.registry.RPGRegistryItem;
import com.gmail.socraticphoenix.rpg.scoreboard.ScoreboardStat;
import com.gmail.socraticphoenix.rpg.scoreboard.ScoreboardStats;
import com.gmail.socraticphoenix.rpg.translation.Messages;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardOption extends AbstractRegistryItem<RPGRegistryItem> implements Option<List<ScoreboardStat>> {

    public ScoreboardOption() {
        super(RPGPlugin.ID, "scoreboard");
    }

    @Override
    public SetOption<List<ScoreboardStat>> defaultValue() {
        return new BasicSetOption<>(Items.buildList(ScoreboardStats.HEALTH, ScoreboardStats.MANA), this);
    }

    @Override
    public SetOption<List<ScoreboardStat>> build(DataContainer container) {
        return new BasicSetOption<>(container.getSerializableList(RPGData.OPTION, ScoreboardStat.class).get(), this);
    }

    @Override
    public ItemType icon() {
        return ItemTypes.MAP;
    }

    @Override
    public Text nameFor(Player player, List<ScoreboardStat> value) {
        return Text.of(String.valueOf(value.size()));
    }

    @Override
    public Text name(Player player) {
        return Messages.translate(player, "rpg.scoreboard");
    }

    @Override
    public boolean hasSelectionGui() {
        return true;
    }

    public static final int MAX_DISPLAY = 9;

    @Override
    public void update(OptionMenu menu, Player player, InventoryData data, GridInventory inventory, SetOption<List<ScoreboardStat>> current) {
        List<ScoreboardStat> available = Items.looseClone(RPGPlugin.registryFor(ScoreboardStat.class).get().elements(), ArrayList::new);

        Vector2i button1 = InventoryHelper.PAGE_START.add(5, 0);
        Vector2i button2 = button1.add(0, 1);
        Vector2i button3 = button1.add(0, 2);

        ItemStack finishButton = ItemStack.builder()
                .quantity(1)
                .itemType(ItemTypes.WOOL)
                .add(Keys.DYE_COLOR, DyeColors.BLUE)
                .add(Keys.DISPLAY_NAME, Text.of(TextColors.BLUE, Messages.translate(player, "rpg.menu.finish")))
                .itemData(ButtonData.of(new RuntimeButtonAction((player1, inventoryEvent) -> RPGData.runtime(player1).ifPresent(r -> {
                    if (r.getMenu() instanceof OptionMenu) {
                        ((OptionMenu) r.getMenu()).finishModifying(player1);
                    }
                }))))
                .build();

        inventory.set(button1.getX(), button1.getY(), finishButton);
        inventory.set(button2.getX(), button2.getY(), finishButton);
        inventory.set(button3.getX(), button3.getY(), finishButton);

        Vector2i startCurrent = InventoryHelper.PAGE_START;
        Vector2i endCurrent = InventoryHelper.PAGE_START.add(5, 3);
        List<ScoreboardStat> stats = current.getValue();
        int listIndex = 0;
        for (int y = startCurrent.getY(); y < endCurrent.getY(); y++) {
            for (int x = startCurrent.getX(); x < endCurrent.getX(); x++) {
                if (listIndex < stats.size()) {
                    int finalListIndex = listIndex;
                    inventory.set(x, y, ItemStack.builder()
                            .quantity(1)
                            .itemType(ItemTypes.CYAN_GLAZED_TERRACOTTA)
                            .add(Keys.DISPLAY_NAME, Text.of(TextColors.BLUE, stats.get(finalListIndex).getName().apply(player)))
                            .itemData(ButtonData.of(new RuntimeButtonAction(((player1, inventoryEvent) -> {
                                List<ScoreboardStat> newStats = Items.looseClone(stats);
                                newStats.remove(finalListIndex);
                                current.setValue(newStats);
                                InventoryHelper.updateAll(player1);
                            }))))
                            .build());
                } else {
                    inventory.set(x, y, InventoryHelper.createBorderItem());
                }
                listIndex++;
            }
        }

        Vector2i startAvailable = InventoryHelper.PAGE_START.add(6, 0);
        Vector2i endAvailable = startAvailable.add(3, 3);
        int availableIndex = RPGData.runtime(player).get().getPage() * MAX_DISPLAY;

        for (int y = startAvailable.getY(); y < endAvailable.getY(); y++) {
            for (int x = startAvailable.getX(); x < endAvailable.getX(); x++) {
                if (availableIndex < available.size()) {
                    int finalAvailableIndex = availableIndex;
                    inventory.set(x, y, ItemStack.builder()
                            .quantity(1)
                            .itemType(ItemTypes.GRAY_GLAZED_TERRACOTTA)
                            .add(Keys.DISPLAY_NAME, Text.of(TextColors.GRAY, available.get(availableIndex).getName().apply(player)))
                            .itemData(ButtonData.of(new RuntimeButtonAction((player1, inventoryEvent) -> {
                                if (stats.size() < 15 && !stats.contains(available.get(finalAvailableIndex))) {
                                    List<ScoreboardStat> newStats = Items.looseClone(stats);
                                    newStats.add(available.get(finalAvailableIndex));
                                    current.setValue(newStats);
                                    InventoryHelper.updateAll(player1);
                                }
                            })))
                            .build());
                } else {
                    inventory.set(x, y, InventoryHelper.createBorderItem());
                }
                availableIndex++;
            }
        }
    }

    @Override
    public String invName(Player player) {
        return name(player) + " " + (RPGData.runtime(player).map(RuntimeData::getPage).orElse(0) + 1);
    }

    @Override
    public int maxPage(Player player) {
        return RPGPlugin.registryFor(ScoreboardStat.class).get().elements().size() / MAX_DISPLAY + 1;
    }

}
