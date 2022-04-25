package com.aetherwars.controllers;

import java.io.IOException;

import com.aetherwars.core.DisplayManager;
import com.aetherwars.core.GameManager;
import com.aetherwars.interfaces.Event;
import com.aetherwars.interfaces.Subscriber;
import com.aetherwars.models.Player;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

public class PlayerController implements Subscriber {
    @FXML
    public Label mana;
    @FXML
    public Label name;
    @FXML
    public Label hp;

    public Player player;

    public PlayerController (FlowPane root, int index) {
        GameManager gm = GameManager.getInstance();
        gm.addSubscriber(this);
        player = gm.getPlayer(index);
        try {
            DisplayManager.loadGui("Player.fxml", root, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        name.setText("Player " + index);
        update();
    }

    private void update() {
        hp.setText("HP: " + String.format("%.2f", player.getHP()) + " / 80");
        mana.setText("Mana: " + player.getMana());
    }
    
    @Override
    public void receiveEvent(Event evt) {
        update();
    }
}
