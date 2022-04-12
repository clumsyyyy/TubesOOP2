package com.aetherwars.model;
import java.util.ArrayList;

public class Deck {
    private int MIN_CAP = 40;
    private int MAX_CAP = 60;
    private ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<Card>();
    }

    public void addCard(Card card) {
        if (cards.size() < MAX_CAP) {
            cards.add(card);
        } else {
            System.out.println("Deck is full!");
        }
    }

    public void discardCard(Card card){
        if (cards.contains(card)) {
            if (cards.size() > MIN_CAP) {
                cards.remove(card);
            } else {
                System.out.println("Couldn't discard this card, discarding would result in less than minimum card number!");
            }
        } else {
            System.out.println("This card is not in the deck!");
        }
    }
}
