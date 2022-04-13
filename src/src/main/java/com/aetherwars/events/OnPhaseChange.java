package com.aetherwars.events;

import com.aetherwars.interfaces.Event;
import com.aetherwars.models.Phase;

public class OnPhaseChange extends Event {
    private Phase p;
    public OnPhaseChange (Object sender, Phase p) {
        super(sender);
        this.p = p;
    }

    public Phase getPhase() {
        return p;
    }
}
