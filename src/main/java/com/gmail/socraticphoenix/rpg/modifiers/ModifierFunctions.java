package com.gmail.socraticphoenix.rpg.modifiers;

public interface ModifierFunctions {

    ModifierFunction<Double> PERCENTAGE_BOOST = (current, lastPrio, iterations, arguments, context) -> current + lastPrio * ((Double) arguments.get(Modifiers.VALUE) / 100);

    ModifierFunction<Double> FLAT_BOOST = (current, lastPrio, iterations, arguments, context) -> current + ((Double) arguments.get(Modifiers.VALUE));

}
