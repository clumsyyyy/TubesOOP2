package com.aetherwars.models;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.aetherwars.events.OnDrawCard;
import com.aetherwars.interfaces.Event;
import com.aetherwars.interfaces.Subscriber;
import com.aetherwars.models.cards.Card;

public class Deck implements Subscriber {
    private ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
    }

    public void addCard(Card c) {
        cards.add(c.cloneCard()); // clone it because we want to change it later
    }

    public Card takeCard() {
        int idx = cards.size() - 1;
        if (idx < 0) {
            System.out.println("No cards left.");
            return null;
        }
        return cards.remove(idx);
    }

    public List<Card> getDrawCard(int draw_cap) {
        return cards.subList(0, draw_cap);
    }

    public int getSize() {
        return cards.size();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    @Override
    public void receiveEvent(Event evt) {
        if (evt instanceof OnDrawCard) {
            // remove and shuffle deck
            Card c = ((OnDrawCard) evt).getSelectedCard();
            cards.remove(c);
            shuffle();
            // should change phase
        }
    }
}
