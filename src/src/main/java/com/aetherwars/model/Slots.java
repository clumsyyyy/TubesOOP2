package com.aetherwars.model;

import com.aetherwars.model.cards.Card;

import java.util.ArrayList;

public class Slots {

    protected int size;
    protected ArrayList<Card> cards;

    /**
     * Default constrcutor
     */
    public Slots() {
        this.cards = new ArrayList<>();
    }

    /**
     * Menampilkan informasi kartu berdasarkan indeks
     * @param index indeks dari kartu
     */
    public void showCardInfo(int index) {
        System.out.println(cards.get(index).toString());
    }
}
