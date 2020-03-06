package com.gmail.socraticphoenix.rpg.data.sponge.mob;

import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.data.mob.NPCData;
import com.gmail.socraticphoenix.rpg.data.sponge.impl.RPGCustomDataBuilder;

public class CustomNPCDataBuilder extends RPGCustomDataBuilder<NPCData, CustomNPCData, ImmutableCustomNPCData> {

    public CustomNPCDataBuilder() {
        super(RPGDataKeys.NPC_DATA, new NPCData(), NPCData.class, CustomNPCDataImpl::new);
    }

}
