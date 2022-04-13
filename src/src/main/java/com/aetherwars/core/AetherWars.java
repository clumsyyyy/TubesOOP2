package com.aetherwars.core;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import com.aetherwars.events.OnGameStart;
import com.aetherwars.events.OnPhaseChange;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import com.aetherwars.models.*;
import com.aetherwars.models.cards.*;
import com.aetherwars.util.CSVReader;

public class AetherWars extends Application {
    @Override
    public void start(Stage stage) {
        GameManager manager = GameManager.getInstance();
        manager.initGame();
        manager.sendEvent(new OnGameStart(this));
        Text text = new Text();
        text.setText("Loading...");
        text.setX(50);
        text.setY(50);

        Group root = new Group();
        root.getChildren().add(text);

        Scene scene = new Scene(root, 1280, 720);

        stage.setTitle("Minecraft: Aether Wars");
        stage.setScene(scene);
        stage.show();

        text.setText("Minecraft: Aether Wars!");
        manager.sendEvent(new OnPhaseChange(this, Phase.DRAW));
    }

    public static void main(String[] args) {
        launch();
    }
}
