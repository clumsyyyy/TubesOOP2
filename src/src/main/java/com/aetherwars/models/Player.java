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
        HP -= amount;
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

    private String name;
    private double HP;
    private int mana;
    private int turn;
    private Board board;
    private Board hand;
    private Deck deck;

    /**
     * Constructor for player class
     * default HP point = 80
     */
    public Player(String name, Board board, Board hand, Deck deck) {
        this.name = name;
        this.HP = 80;
        this.mana = 0;
        this.turn = 0;
        this.board = board;
        this.hand = hand;
        this.deck = deck;
        addSubscriber(this.board);
        addSubscriber(this.hand);
        addSubscriber(this.deck);
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
                    if (this == gm.getCurrentPlayer()) {
                        turn++;
                        mana = Math.min(turn, 10);
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
