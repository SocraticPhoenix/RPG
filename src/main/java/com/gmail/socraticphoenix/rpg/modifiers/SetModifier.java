package com.gmail.socraticphoenix.rpg.modifiers;

import com.gmail.socraticphoenix.rpg.data.RPGData;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SetModifier<T> implements DataSerializable, Comparable<SetModifier<T>> {
    private Modifier<T> modifier;
    private Map<String, Object> arguments;

    public SetModifier(Modifier<T> modifier, Map<String, Object> arguments) {
        this.modifier = modifier;
        this.arguments = arguments;
    }

    public Modifier<T> getModifier() {
        return modifier;
    }

    public void setModifier(Modifier<T> modifier) {
        this.modifier = modifier;
    }

    public Map<String, Object> getArguments() {
        return arguments;
    }

    public void setArguments(Map<String, Object> arguments) {
        this.arguments = arguments;
    }

    public SetModifier<T> copy() {
        return new SetModifier<>(this.modifier, new HashMap<>(this.arguments));
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        DataContainer map = DataContainer.createNew();
        this.arguments.forEach((s, o) -> {
            map.set(DataQuery.of(s), o);
        });

        return DataContainer.createNew()
                .set(RPGData.MODIFIER, this.modifier)
                .set(RPGData.ARGUMENTS, map);
    }

    @Override
    public int compareTo(SetModifier<T> o) {
        return this.modifier.compareTo(o.modifier);
    }
    
    public static class Builder implements DataBuilder<SetModifier> {

        @Override
        public Optional<SetModifier> build(DataView container) throws InvalidDataException {
            if (!container.contains(RPGData.MODIFIER, RPGData.ARGUMENTS)) {
                return Optional.empty();
            }

            Modifier modifier = container.getSerializable(RPGData.MODIFIER, Modifier.class).get();
            Map arguments = new HashMap<>();
            DataView argsSlzd = container.getView(RPGData.ARGUMENTS).get();
            argsSlzd.getKeys(false).forEach(d -> {
                String k = d.asString('.');
                arguments.put(k, modifier.getArgumentLoader().apply(k, argsSlzd));
            });

            return Optional.of(new SetModifier(modifier, arguments));
        }
        
    }
    
}
