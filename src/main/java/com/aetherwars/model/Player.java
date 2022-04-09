package com.aetherwars.model;

/**
 * Implementation for player class
 */
public class Player {
    private String name;
    private int HP;
    private int mana;
    private Deck deck;

    /**
     * Constructor for player class
     */
    public Player(String name, int HP, int mana, Deck deck) {
        this.name = name;
        this.HP = HP;
        this.mana = mana;
        this.deck = deck;
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
