package com.aetherwars.controllers;

import java.io.IOException;
import java.util.Random;

import com.aetherwars.core.DisplayManager;
import com.aetherwars.core.GameManager;
import com.aetherwars.events.*;
import com.aetherwars.interfaces.Event;
import com.aetherwars.interfaces.Subscriber;
import com.aetherwars.models.Phase;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class PhaseController implements Subscriber {
    public Label draw_phase;
    public Label planning_phase;
    public Label attack_phase;
    public Label end_phase;
    public Button next_phase;

    public PhaseController (HBox parent) {
        GameManager.getInstance().addSubscriber(this);
        try {
            DisplayManager.loadGui("Phase.fxml", parent, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ChangePhase(Phase to) {
        draw_phase.setStyle("-fx-background-color: #c2c2c2;");
        planning_phase.setStyle("-fx-background-color: #c2c2c2;");
        attack_phase.setStyle("-fx-background-color: #c2c2c2;");
        end_phase.setStyle("-fx-background-color: #c2c2c2;");
        if (to == Phase.DRAW) {
            draw_phase.setStyle("-fx-background-color: #8aceed;");
        } else if (to == Phase.PLAN) {
            planning_phase.setStyle("-fx-background-color: #fad37a;");
        } else if (to == Phase.ATTACK) {
            attack_phase.setStyle("-fx-background-color: #fa7a7a;");
        } else if (to == Phase.END) {
            end_phase.setStyle("-fx-background-color: #e0acf2;");
        }
    }

    @FXML
    void nextPhase() {
        GameManager gm = GameManager.getInstance();
        Random rand = new Random();
        next_phase.setOnMousePressed((event -> {
            Phase nextPhase = null;
            switch (gm.getPhase()) {
                case DRAW:
                    nextPhase = Phase.PLAN;
                    break;
                case PLAN:
                    nextPhase = Phase.ATTACK;
                    break;
                case ATTACK:
                    nextPhase = Phase.END;
                    break;
                case END:
                    if (gm.getCurrentPlayer().getHand().getSize() == 5)
                        gm.getCurrentPlayer().getHand().unregister(rand.nextInt(5));
                    nextPhase = Phase.DRAW;
                    break;
            }
            gm.sendEvent(new OnPhaseChange(this, nextPhase));
        }));
    }

    @Override
    public void receiveEvent(Event evt) {
        if (evt instanceof OnPhaseChange) {
            ChangePhase(((OnPhaseChange) evt).getPhase());
        }
    }
}
