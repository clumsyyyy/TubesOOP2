package com.aetherwars.events;

import com.aetherwars.interfaces.Event;
import com.aetherwars.models.cards.Card;

public class OnPickCard extends Event {
    public Card getSelectedCard() {
        return selectedCard;
    }

    public int getIndex() {
        return index;
    }

    private final Card selectedCard;
    private final int index;
    public OnPickCard(Object sender, Card c, int index) {
        super(sender);
        this.selectedCard = c;
        this.index = index;
    }
}
