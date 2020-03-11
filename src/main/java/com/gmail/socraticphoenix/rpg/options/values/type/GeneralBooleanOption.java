package com.gmail.socraticphoenix.rpg.options.values.type;

import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.data.character.InventoryData;
import com.gmail.socraticphoenix.rpg.inventory.menus.OptionMenu;
import com.gmail.socraticphoenix.rpg.options.values.Option;
import com.gmail.socraticphoenix.rpg.options.values.SetOption;
import com.gmail.socraticphoenix.rpg.options.values.set.BasicSetOption;
import com.gmail.socraticphoenix.rpg.registry.AbstractRegistryItem;
import com.gmail.socraticphoenix.rpg.registry.RPGRegistryItem;
import com.gmail.socraticphoenix.rpg.translation.Messages;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.text.Text;

public class GeneralBooleanOption extends AbstractRegistryItem<RPGRegistryItem> implements Option<Boolean> {
    private boolean defaultValue;
    private ItemType icon;
    private String nameStr;

    public GeneralBooleanOption(boolean defaultValue, ItemType icon, String nameStr, String pluginId, String id) {
        super(pluginId, id);
        this.defaultValue = defaultValue;
        this.icon = icon;
        this.nameStr = nameStr;
    }


    @Override
    public SetOption<Boolean> defaultValue() {
        return new BasicSetOption<>(this.defaultValue, this);
    }

    @Override
    public SetOption<Boolean> build(DataContainer container) {
        return new BasicSetOption<>(container.getBoolean(RPGData.OPTION).get(), this);
    }

    @Override
    public ItemType icon() {
        return this.icon;
    }

    @Override
    public Text nameFor(Player player, Boolean value) {
        return value ? Messages.translate(player, "rpg.value.true") : Messages.translate(player, "rpg.value.false");
    }

    @Override
    public Text name(Player player) {
        return Messages.translate(player, nameStr);
    }

    @Override
    public boolean hasSelectionGui() {
        return false;
    }

    @Override
    public void update(OptionMenu menu, Player player, InventoryData data, GridInventory inventory, SetOption<Boolean> current) {
        current.setValue(!current.getValue());
    }

    @Override
    public String invName(Player player) {
        return "";
    }

    @Override
    public int maxPage(Player player) {
        return 0;
    }


}
