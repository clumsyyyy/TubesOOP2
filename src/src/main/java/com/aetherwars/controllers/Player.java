package com.aetherwars.controllers;

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

    public Board getBoard() {
        return board;
    }

    public Board getHand() {
        return hand;
    }

    private String name;
    private double HP;
    private int mana;
    private Board board;
    private Board hand;

    /**
     * Constructor for player class
     * default HP point = 80
     */
    public Player(String name, Board board, Board hand) {
        this.name = name;
        this.HP = 80;
        this.mana = 1;
        this.board = board;
        this.hand = hand;
        addSubscriber(this.board);
        addSubscriber(this.hand);
    }

    @Override
    public void receiveEvent(Event evt) {
        GameManager gm = GameManager.getInstance();
        if (evt instanceof OnGameStart) {
            sendEvent(evt);
        } else if (evt instanceof OnPhaseChange) {
            switch (((OnPhaseChange) evt).getPhase()) {
                case END: // update board
                    sendEvent(evt);
                    break;
            }
        } else if (evt instanceof OnAttack || evt instanceof OnDrawCard || evt instanceof OnPickCard) {
            // event for specific player.
            if (this == gm.getCurrentPlayer()) {
                sendEvent(evt);
            }
        }
    }
}
