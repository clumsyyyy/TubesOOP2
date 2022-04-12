package com.aetherwars.model;
import java.util.ArrayList;

import com.aetherwars.model.cards.Card;

public class Deck extends Slots{

    /**
     * User-defined constructor
     * @param size ukuran deck
     */
    public Deck(int size) {
        super();
        this.size = size;
    }

    /**
     * Inisialisasi isi deck
     */
    public void initializeDeck(){
        // TODO : menambahkan implementasi random generate cards sesuai ukuran deck
    }

    public void drawDeck(){
        // TODO: menambahkan implementasi mengambil 3 kartu dari deck secara random

    }

}
