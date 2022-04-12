package com.aetherwars.model;

/**
 * Implementation for player class
 */
public class Player {
    private String name;
    private int HP;
    private int mana;
    private Deck deck;
    private Deck hand;

    /**
     * Constructor for player class
     * default HP point = 80
     */
    public Player(String name, int HP, int mana, Deck deck, Deck hand) {
        this.name = name;
        this.HP = HP;
        this.mana = mana;
        this.deck = deck;
        this.hand = hand;
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
}
