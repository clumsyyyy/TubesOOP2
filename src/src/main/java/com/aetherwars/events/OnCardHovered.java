package com.aetherwars.events;

import com.aetherwars.interfaces.Event;
import com.aetherwars.models.cards.Card;

public class OnCardHovered extends Event {
    private Card c;
    public OnCardHovered(Object sender, Card c) {
        super(sender);
        this.c = c;
    }
    public Card getCard() {
        return c;
    }
}
