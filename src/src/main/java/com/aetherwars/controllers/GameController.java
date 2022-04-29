package com.aetherwars.controllers;

import com.aetherwars.core.DisplayManager;
import com.aetherwars.core.GameManager;
import com.aetherwars.events.*;
import com.aetherwars.interfaces.Event;
import com.aetherwars.interfaces.Subscriber;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class GameController implements Subscriber {
    public SplitPane board;
    public FlowPane p1_hand;
    public FlowPane p2_hand;
    public FlowPane p1_board;
    public FlowPane p2_board;
    public HBox panel_phase;
    public AnchorPane info;
    public Media music;
    public MediaPlayer music_player;
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
        board.setBackground(DisplayManager.getImage("background/background.png"));
        try {
            Media music = new Media(getClass().getResource("/com/aetherwars/music/main.mp3").toURI().toString());
            this.music_player = new MediaPlayer(music);
            this.music_player.cycleCountProperty().set(MediaPlayer.INDEFINITE);
            this.music_player.setAutoPlay(true);
            this.music_player.setVolume(1);
        } catch(Exception e) { }
    }

    @Override
    public void receiveEvent(Event evt) {
        if (evt instanceof OnGameStart) {
            init();
        }
    }
}
