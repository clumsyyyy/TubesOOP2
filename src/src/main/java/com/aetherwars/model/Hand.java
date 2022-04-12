package com.aetherwars.model;

import com.aetherwars.model.cards.Card;

import java.util.ArrayList;

public class Hand extends Slots{

    /**
     * Default constructor
     */
    public Hand() {
        super();
        this.size = 5;
    }

    public void addCard(Card card) {
        if (cards.size() < this.size) {
            cards.add(card);
        } else {
            System.out.println("Deck is full!");
        }
    }

    public void discardCard(Card card){
        if (cards.contains(card)) {
            if (cards.size() > this.size) {
                cards.remove(card);
            } else {
                System.out.println("Couldn't discard this card, discarding would result in less than minimum card number!");
            }
        } else {
            System.out.println("This card is not in the deck!");
        }
    }

}
