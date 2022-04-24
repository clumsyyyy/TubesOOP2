package com.aetherwars.controllers;

import java.io.IOException;

import com.aetherwars.core.DisplayManager;

import javafx.fxml.FXML;
import javafx.scene.layout.*;

public class HandController  {
    @FXML public Pane hand1;
    @FXML public Pane hand2;
    @FXML public Pane hand3;
    @FXML public Pane hand4;
    @FXML public Pane hand5;
    public Pane[] hands;

    public HandController(GridPane root, int index){
        try {
            DisplayManager.loadGui("Hand.fxml", root, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        hands = new Pane[] {hand1, hand2, hand3, hand4, hand5};
        for (int i = 0; i < 5; i++){
            new CardController(hands[i], index, i, true);
        }
    }
}
