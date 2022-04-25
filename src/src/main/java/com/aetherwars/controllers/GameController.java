package com.aetherwars.controllers;

import com.aetherwars.core.GameManager;
import com.aetherwars.events.*;
import com.aetherwars.interfaces.Event;
import com.aetherwars.interfaces.Subscriber;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.*;

public class GameController implements Subscriber {
    public SplitPane board;
    public FlowPane p1_hand;
    public FlowPane p2_hand;
    public FlowPane p1_board;
    public FlowPane p2_board;
    public HBox panel_phase;
    public AnchorPane info;

    public GameController () {
        GameManager.getInstance().addSubscriber(this);
    }

    void init() {
        // Start UI
        new PhaseController(panel_phase);
        new BoardController(p1_board, 0);
        new BoardController(p2_board, 1);
        new HandController(p1_hand, 0);
        new HandController(p2_hand, 1);
        new InfoController(info);
    }

    @Override
    public void receiveEvent(Event evt) {
        if (evt instanceof OnGameStart) {
            init();
        }
    }
}
