package com.gmail.socraticphoenix.rpg.options.values.set;

import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.options.values.Option;
import com.gmail.socraticphoenix.rpg.options.values.SetOption;
import org.spongepowered.api.data.DataContainer;

public class BasicSetOption<T> extends SetOption<T> {

    public BasicSetOption(T value, Option<T> type) {
        super(value, type);
    }

    @Override
    public SetOption<T> copy() {
        return new BasicSetOption<>(this.value, this.type);
    }

    @Override
    public void fill(DataContainer container) {
        container.set(RPGData.OPTION, this.value);
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

}
