package com.aetherwars.models;

import com.aetherwars.core.GameManager;
import com.aetherwars.events.*;
import com.aetherwars.interfaces.Event;
import com.aetherwars.interfaces.Publisher;
import com.aetherwars.interfaces.Subscriber;

/**
 * Implementation for player class
 */
public class Player extends Publisher implements Subscriber {
    public String getName() {
        return name;
    }

    public double getHP() {
        return HP;
    }

    public void takeDamage(double amount) {
        if (HP - amount > 0) {
            if (HP - amount >= 80){
                HP = 80;
            } else {
                HP -= amount;
            }
        } else {
            HP = 0;
        }
    }

    public void heal(double amount){
        if (HP + amount >= 80){
            HP = 80;
        } else {
            HP += amount;
        }
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) { this.mana = mana; }

    public Board getBoard() {
        return board;
    }
    public Board getHand() {
        return hand;
    }

    public Deck getDeck() { return deck; }

    public void setDeck(Deck deck) { this.deck = deck; }

    private String name;
    private double HP;
    private int mana;
    private int turn;
    private String image_path;
    private Board board;
    private Board hand;
    private Deck deck;

    /**
     * Constructor for player class
     * default HP point = 80
     */
    public Player(String name, Board board, Board hand, Deck deck, String image_path) {
        this.name = name;
        this.HP = 80;
        this.mana = 0;
        this.turn = 0;
        this.image_path = image_path;
        this.board = board;
        this.hand = hand;
        this.deck = deck;
        addSubscriber(this.board);
        addSubscriber(this.hand);
        addSubscriber(this.deck);
    }

    public String getImagePath(){
        return this.image_path;
    }

    public int getTurn(){
        return this.turn;
    }

    void getFirstDraw() {
        for (int i = 0; i < 3; i++) {
            hand.register(deck.takeCard());
        }
    }

    @Override
    public void receiveEvent(Event evt) {
        GameManager gm = GameManager.getInstance();
        if (evt instanceof OnGameStart) {
            getFirstDraw();
        } else if (evt instanceof OnPhaseChange) {
            switch (((OnPhaseChange) evt).getPhase()) {
                case DRAW:
                    // getFirstDraw();
                    if (this == gm.getCurrentPlayer()) {
                        turn++;

                        // TODO: RESET THE +50 POINT
                        mana = Math.min(turn, 10) + 50;
                        // update board
                        sendEvent(evt);
                    }
                    break;
                default:
                    break;
            }
        } else if (
            evt instanceof OnCardAction ||
            evt instanceof OnDrawCard
        ) {
            // event for specific player.
            if (this == gm.getCurrentPlayer()) {
                sendEvent(evt);
            }
        }
    }
}
