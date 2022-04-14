package com.aetherwars;


import com.aetherwars.core.GameManager;
import com.aetherwars.events.OnGameStart;
import com.aetherwars.events.OnPhaseChange;
import javafx.application.Application;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

import com.aetherwars.models.*;

public class AetherWars extends Application {
    @Override
    public void start(Stage stage) {
        try{
            GameManager manager = GameManager.getInstance();
            manager.initGame();
//        Text text = new Text();
//        text.setText("Loading...");
//        text.setX(50);
//        text.setY(50);
//
//        Group root = new Group();
//        root.getChildren().add(text);
//
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gui/Board.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 720);

            manager.sendEvent(new OnGameStart(this));
            stage.setTitle("Minecraft: Aether Wars");
            stage.setScene(scene);
            stage.show();

            manager.sendEvent(new OnPhaseChange(this, Phase.DRAW));
        } catch (Exception e) {
            System.out.println("bruh? " + e);
        }

    }

    public static void main(String[] args) {
        launch();
    }
}
