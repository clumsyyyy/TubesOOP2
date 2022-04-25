package com.aetherwars.controllers;

import java.io.IOException;

import com.aetherwars.core.DisplayManager;

import javafx.fxml.FXML;
import javafx.scene.layout.*;

public class BoardController {
    @FXML
    public Pane board1;
    @FXML
    public Pane board2;
    @FXML
    public Pane board3;
    @FXML
    public Pane board4;
    @FXML
    public Pane board5;
    public Pane[] boards;
    @FXML
    public FlowPane player;

    public BoardController(FlowPane root, int index) {
        try {
            DisplayManager.loadGui("Board.fxml", root, this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        boards = new Pane[]{board1, board2, board3, board4, board5};
        for (int i = 0; i < 5; i++) {
            new CardController(boards[i], index, i, false);
        }
        new PlayerController(player, index);
    }
}
