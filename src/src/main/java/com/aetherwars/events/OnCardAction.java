package com.aetherwars.events;

import com.aetherwars.interfaces.Event;

public class OnCardAction extends Event {
    public int getFromPlayerIdx() {
        return fromPlayerIdx;
    }

    public int getFromCardIdx() {
        return fromCardIdx;
    }

    public int getToPlayerIdx() {
        return toPlayerIdx;
    }

    public int getToCardIdx() {
        return toCardIdx;
    }

    public CardAction getAction() {
        return action;
    }

    public boolean getFromHand() {
        return fromHand;
    }

    private final int fromPlayerIdx;
    private final int fromCardIdx;
    private final int toPlayerIdx;
    private final int toCardIdx;
    private final boolean fromHand;
    private final CardAction action;
    public OnCardAction(Object sender, String s, boolean fromHand, int toPlayerIdx, int toCardIdx, CardAction action) {
        super(sender);
        String[] str = s.split(" ");
        this.fromHand = fromHand;
        this.fromPlayerIdx = Integer.parseInt(str[0]);
        this.fromCardIdx = Integer.parseInt(str[1]);
        this.toPlayerIdx = toPlayerIdx;
        this.toCardIdx = toCardIdx;
        this.action = action;
    }
}
