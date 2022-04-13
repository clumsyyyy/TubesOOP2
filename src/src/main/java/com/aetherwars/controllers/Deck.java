package com.aetherwars.controllers;
import java.util.ArrayList;

import com.aetherwars.interfaces.Event;
import com.aetherwars.interfaces.Subscriber;
import com.aetherwars.models.cards.Card;

public class Deck implements Subscriber {
    private int MIN_CAP = 40;
    private int MAX_CAP = 60;
    private ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<Card>();
    }

    public void addCard(Card c) {
        cards.add(c.clone()); // clone it because we want to change it later
    }

    public void getRandomCard() {

    }

    public void inspectCard(int i){
        if (i >= 0 && i < cards.size()) {
            System.out.println(cards.get(i).toString());
        } else {
            System.out.println("This card is not in the deck!");
        }
    }

    @Override
    public void receiveEvent(Event evt) {

    }
}
