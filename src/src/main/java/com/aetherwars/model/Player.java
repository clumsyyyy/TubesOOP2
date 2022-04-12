package com.aetherwars.model;

import com.aetherwars.model.cards.Card;

/**
 * Implementation for player class
 */
public class Player {
    private String name;
    private int HP;
    private int mana;
    private Deck deck;
    private Hand hand;
    private Board board;

    /**
     * User-defined Constructor for player class
     * starting HP point = 80, starting mana = 1
     * @param name nama player
     */
    public Player(String name) {
        this.name = name;
        this.HP = 80;
        this.mana = 1;
        this.deck = new Deck(40);
        this.hand = new Hand();
        this.board = new Board();
    }

}
