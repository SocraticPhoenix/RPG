package com.gmail.socraticphoenix.rpg.data.character;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.options.values.Option;
import com.gmail.socraticphoenix.rpg.options.values.SetOption;
import com.gmail.socraticphoenix.rpg.options.values.SetOptionMap;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class OptionData extends RPGData<OptionData> {
    private SetOptionMap options;

    public OptionData(SetOptionMap options) {
        super(0);
        this.options = options;
    }

    public OptionData() {
        this(new SetOptionMap(new LinkedHashMap<>()));
        RPGPlugin.registryFor(Option.class).get().elements().forEach(this::getOrCreate);
    }

    public Map<Option, SetOption> getOptions() {
        return options.getOptions();
    }

    public  <T> void put(SetOption<T> option) {
        options.getOptions().put(option.type(), option);
    }

    public <T> Optional<SetOption<T>> getOption(Option<T> option) {
        return Optional.of(options.getOptions().get(option));
    }

    public <T> SetOption<T> getOrCreate(Option<T> option) {
        return getOrCreate(option.defaultValue());
    }

    public <T> SetOption<T> getOrCreate(SetOption<T> option) {
        if (!options.getOptions().containsKey(option.type())) {
            put(option);
        }
        return options.getOptions().get(option.type());
    }

    @Override
    public OptionData copy() {
        Map<Option, SetOption> options = new LinkedHashMap<>();
        this.options.getOptions().entrySet().forEach(o -> {
            options.put(o.getKey(), o.getValue().copy());
        });
        return new OptionData(new SetOptionMap(options));
    }

    @Override
    public DataContainer fill(DataContainer container) {
        return container.set(OPTIONS, this.options);
    }

    @Override
    public Optional<OptionData> from(DataView container) {
        if (!container.contains(OPTIONS)) {
            return Optional.empty();
        }

        this.options = container.getSerializable(OPTIONS, SetOptionMap.class).get();

        return Optional.of(this);
    }

    public static class Builder implements DataBuilder<OptionData> {

        @Override
        public Optional<OptionData> build(DataView container) throws InvalidDataException {
            return new OptionData().from(container);
        }

    }

}
