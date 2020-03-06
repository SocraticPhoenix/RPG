package com.gmail.socraticphoenix.rpg.options.values;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class SetOptionMap implements DataSerializable {
    private Map<Option, SetOption> options;

    public SetOptionMap(Map<Option, SetOption> options) {
        this.options = options;
    }

    public Map<Option, SetOption> getOptions() {
        return options;
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew().set(RPGData.OPTION_MAP, Items.looseClone(options.values(), ArrayList::new));
    }

    public static class Builder implements DataBuilder<SetOptionMap> {

        @Override
        public Optional<SetOptionMap> build(DataView container) throws InvalidDataException {
            if (!container.contains(RPGData.OPTION_MAP)) {
                return Optional.empty();
            }

            Map<Option, SetOption> options = new LinkedHashMap<>();
            container.getSerializableList(RPGData.OPTION_MAP, SetOption.class).get().forEach(setOption -> options.put(setOption.type(), setOption));

            return Optional.of(new SetOptionMap(options));
        }

    }

}
