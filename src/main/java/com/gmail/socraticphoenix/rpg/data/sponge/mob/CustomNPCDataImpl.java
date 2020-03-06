package com.gmail.socraticphoenix.rpg.data.sponge.mob;

import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.data.mob.NPCData;
import com.gmail.socraticphoenix.rpg.data.sponge.impl.RPGCustomDataImpl;

public class CustomNPCDataImpl extends RPGCustomDataImpl<NPCData, CustomNPCData, ImmutableCustomNPCData> implements CustomNPCData {

    public CustomNPCDataImpl(NPCData value) {
        super(value, RPGDataKeys.NPC_DATA, NPCData.class, CustomNPCDataImpl.class);
    }

    @Override
    public CustomNPCData copy() {
        return null;
    }

    @Override
    public ImmutableCustomNPCData asImmutable() {
        return null;
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

}