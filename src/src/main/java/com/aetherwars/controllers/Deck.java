package com.aetherwars.controllers;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.aetherwars.core.GameManager;
import com.aetherwars.events.OnDrawCard;
import com.aetherwars.events.OnPhaseChange;
import com.aetherwars.interfaces.Event;
import com.aetherwars.interfaces.Subscriber;
import com.aetherwars.models.Phase;
import com.aetherwars.models.cards.Card;

public class Deck implements Subscriber {
    private final int MIN_CAP = 40;
    private final int MAX_CAP = 60;
    private ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
    }

    public void addCard(Card c) {
        cards.add(c.clone()); // clone it because we want to change it later
    }

    public Card takeCard() {
        int idx = cards.size() - 1;
        if (idx < 0) {
            System.out.println("No cards left.");
            return null;
        }
        return cards.remove(idx);
    }

    public List<Card> getDrawCard() {
        return cards.subList(0, 3);
    }

    public int getSize() {
        return cards.size();
    }



    @Override
    public void receiveEvent(Event evt) {
        if (evt instanceof OnDrawCard) {
            // remove and shuffle deck
            Card c = ((OnDrawCard) evt).getSelectedCard();
            cards.remove(c);
            Collections.shuffle(cards);
            // should change phase
        }
    }
}
