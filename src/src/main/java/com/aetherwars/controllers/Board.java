package com.aetherwars.controllers;

import com.aetherwars.events.OnPhaseChange;
import com.aetherwars.interfaces.Event;
import com.aetherwars.interfaces.Observer;
import com.aetherwars.interfaces.Subscriber;
import com.aetherwars.models.BoardType;
import com.aetherwars.models.cards.*;

import java.util.ArrayList;

// Board implements Obverser
public class Board implements Observer<Card>, Subscriber {
    private ArrayList<Card> cards;
    private BoardType type;
    private int MAX_CAP = 5;

    public BoardType getType() {
        return type;
    }

    public Board (BoardType type) {
        this.type = type;
    }

    @Override
    public void register(Card card, int index) {
        if (index >= 0 && index < cards.size()) {
            if (cards.size() < MAX_CAP) {
                cards.add(card);
            } else {
                System.out.println("Board is full!");
            }
        } else {
            System.out.println("Invalid index");
        }
    }

    @Override
    public void register(Card obj) {
        register(obj, cards.size()-1);
    }

    @Override
    public void unregister(Card card) {
        if (cards.contains(card)) {
            cards.remove(card);
        } else {
            System.out.println("This card is not in the board!");
        }
    }

    @Override
    public void unregister(int index) {
        if (index >= 0 && index < cards.size()) {
            cards.remove(index);
        } else {
            System.out.println("Invalid index");
        }
    }

    @Override
    public void notifyObjects() {
        for (Card c: cards) {
            if (c instanceof PotionCard)
                ((PotionCard) c).update();
        }
    }

    @Override
    public void receiveEvent(Event evt) {
        if (evt instanceof OnPhaseChange && type == BoardType.BOARD) {
            notifyObjects();
        }
    }
}
