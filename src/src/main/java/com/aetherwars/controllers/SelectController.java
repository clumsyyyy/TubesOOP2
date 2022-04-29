package com.aetherwars.controllers;


import java.io.File;

import com.aetherwars.core.DisplayManager;
import com.aetherwars.core.GameManager;
import com.aetherwars.events.OnGameStart;
import com.aetherwars.events.OnPhaseChange;
import com.aetherwars.interfaces.Event;
import com.aetherwars.interfaces.Subscriber;
import com.aetherwars.models.Phase;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.FileChooser;

public class SelectController implements Subscriber  {
    public Button import_btn;
    public Button random_btn;
    @FXML public AnchorPane select_panel;

    FileChooser f1 = new FileChooser();
    FileChooser f2 = new FileChooser();
   
    final BackgroundSize bgSize = new BackgroundSize(
            1, 1, true, true,
            true, false
    );

    EventHandler<ActionEvent> random_evt = new EventHandler<ActionEvent>(){ 
        @Override
        public void handle(ActionEvent event) {
            GameManager manager = GameManager.getInstance();
            manager.initGame(40, null, null);
            manager.sendEvent(new OnGameStart(this));
            manager.sendEvent(new OnPhaseChange(this, Phase.DRAW));
        }
    };

    EventHandler<ActionEvent> import_evt = new EventHandler<ActionEvent>(){ 
        @Override
        public void handle(ActionEvent event) {
            f1.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            f1.setTitle("Select deck file for player 1: ");
            
            f2.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            f2.setTitle("Select deck file for player 2: ");
            GameManager manager = GameManager.getInstance();
            File file1 = f1.showOpenDialog(DisplayManager.getInstance().getStage());
            File file2 = f2.showOpenDialog(DisplayManager.getInstance().getStage());
            //manager.updateCards(file1, file2);
            manager.initGame(40, file1, file2);
            manager.sendEvent(new OnGameStart(this));
            manager.sendEvent(new OnPhaseChange(this, Phase.DRAW));
        }
    };
    

    public SelectController(){
        GameManager.getInstance().addSubscriber(this);
    }

    @Override
    public void receiveEvent(Event evt){
        if (evt instanceof OnPhaseChange && ((OnPhaseChange) evt).getPhase() == Phase.SELECT){
            random_btn.setOnAction(random_evt);
            import_btn.setOnAction(import_evt);
            select_panel.setBackground(DisplayManager.getImage("background/background.png"));
        } 
    }
}