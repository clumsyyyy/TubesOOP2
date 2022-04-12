package com.aetherwars.model;

import com.aetherwars.model.cards.Card;

import java.util.ArrayList;

public class Board {
    private int MIN_CAP = 40;
    private int MAX_CAP = 60;
    private ArrayList<Card> cards;

    public Board() {
        cards = new ArrayList<Card>();
    }
}
