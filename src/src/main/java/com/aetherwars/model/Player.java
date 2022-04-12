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
     * Constructor for player class
     * default HP point = 80
     */
    public Player(String name) {
        this.name = name;
        this.HP = 80;
        this.mana = 1;
        this.deck = new Deck();
        this.hand = new Hand();
        this.board = new Board();
    }

    /**
     * @brief interface to add a card to a player's deck
     * @param card card to be added
     */
    public void addToDeck(Card card){
        deck.addCard(card);
    }

    /**
     * @brief method to discard a card to a player's deck
     * @param card card to be discarded
     */
    public void discardFromDeck(Card card){
        deck.discardCard(card);
    }

    /**
     * @brief method to inspect a card's description from deck
     */
    public void inspectDeckCard(int i){
        this.deck.inspectCard(i);
    }

    /** 
     * #brief method to inspect a card's description from hand
    */
//    public void inspectHandCard(int i){
//        this.hand.inspectCard(i);
//    }
}
