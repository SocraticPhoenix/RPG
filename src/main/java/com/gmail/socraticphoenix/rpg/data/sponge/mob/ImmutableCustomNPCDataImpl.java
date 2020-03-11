package com.gmail.socraticphoenix.rpg.data.sponge.mob;

import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.data.mob.NPCData;
import com.gmail.socraticphoenix.rpg.data.sponge.impl.RPGImmutableCustomDataImpl;

public class ImmutableCustomNPCDataImpl extends RPGImmutableCustomDataImpl<NPCData, CustomNPCData, ImmutableCustomNPCData> implements ImmutableCustomNPCData {

    public ImmutableCustomNPCDataImpl(NPCData value) {
        super(value, RPGDataKeys.NPC_DATA);
    }

    @Override
    public CustomNPCData asMutable() {
        return new CustomNPCDataImpl(this.value.copy());
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

}
