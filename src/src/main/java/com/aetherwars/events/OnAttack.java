package com.aetherwars.events;

import com.aetherwars.interfaces.Event;

public class OnAttack extends Event {
    private final int cAtt;
    private final int cTgt;

    public OnAttack(Object sender, int idx_cAtt, int idx_cTgt) {
        super(sender);
        this.cAtt = idx_cAtt;
        this.cTgt = idx_cTgt;
    }

    public OnAttack(Object sender, int idx_cAtt) {
        this(sender, idx_cAtt, -1);
    }

    public int getAttackerCardIdx() {
        return this.cAtt;
    }

    public int getTargetCardIdx() {
        return this.cTgt;
    }
}
