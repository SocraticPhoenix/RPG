package com.gmail.socraticphoenix.rpg.options.values.type;

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
import com.gmail.socraticphoenix.rpg.translation.Messages;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.text.Text;

import java.util.List;

public class GeneralEnumOption extends AbstractRegistryItem<RPGRegistryItem> implements Option<String> {
    private String defaultValue;
    private List<String> values;
    private ItemType icon;
    private String nameStr;

    public GeneralEnumOption(List<String> values, String defaultValue, ItemType icon, String nameStr, String pluginId, String id) {
        super(pluginId, id);
        this.defaultValue = defaultValue;
        this.values = values;
        this.icon = icon;
        this.nameStr = nameStr;
    }

    @Override
    public SetOption<String> defaultValue() {
        return new BasicSetOption<>(this.defaultValue, this);
    }

    @Override
    public SetOption<String> build(DataContainer container) {
        return new BasicSetOption<>(container.getString(RPGData.OPTION).get(), this);
    }

    @Override
    public ItemType icon() {
        return this.icon;
    }

    @Override
    public Text nameFor(Player player, String value) {
        return Text.of(value);
    }

    @Override
    public Text name(Player player) {
        return Messages.translate(player, this.nameStr);
    }

    @Override
    public boolean hasSelectionGui() {
        return true;
    }

    public static final int MAX_DISPLAY = 27;

    @Override
    public void update(OptionMenu menu, Player player, InventoryData data, GridInventory inventory, SetOption<String> current) {
        int listIndex = RPGData.runtime(player).get().getPage() * MAX_DISPLAY;

        for (int y = InventoryHelper.PAGE_START.getY(); y < InventoryHelper.PAGE_LIMIT.getY(); y++) {
            for (int x = InventoryHelper.PAGE_START.getX(); x < InventoryHelper.PAGE_LIMIT.getX(); x++) {
                if (listIndex < values.size()) {
                    String value = values.get(listIndex);
                    inventory.set(x, y, ItemStack.builder()
                            .quantity(1)
                            .itemType(ItemTypes.BOOK)
                            .add(Keys.DISPLAY_NAME, this.nameFor(player, value))
                            .itemData(ButtonData.of(new RuntimeButtonAction((player1, inventoryEvent) -> {
                                current.setValue(value);
                                menu.finishModifying(player);
                            })))
                            .build());
                } else {
                    inventory.set(x, y, InventoryHelper.createBorderItem());
                }
                listIndex++;
            }
        }

    }

    @Override
    public String invName(Player player) {
        return name(player) + " " + (RPGData.runtime(player).map(RuntimeData::getPage).orElse(0) + 1);
    }

    @Override
    public int maxPage(Player player) {
        return values.size() / MAX_DISPLAY + 1;
    }

    public List<String> getValues() {
        return values;
    }

}
