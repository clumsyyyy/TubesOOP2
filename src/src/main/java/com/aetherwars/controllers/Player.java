package com.aetherwars.controllers;

import com.aetherwars.events.OnGameStart;
import com.aetherwars.events.OnPhaseChange;
import com.aetherwars.interfaces.Event;
import com.aetherwars.interfaces.Publisher;
import com.aetherwars.interfaces.Subscriber;

/**
 * Implementation for player class
 */
public class Player extends Publisher implements Subscriber {
    private String name;
    private int HP;
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
        sendEvent(evt);
        if (evt instanceof OnGameStart) {

        }
    }
}
