package com.aetherwars.core;

import com.aetherwars.events.OnDrawCard;
import com.aetherwars.events.OnGameStart;
import com.aetherwars.events.OnPhaseChange;
import com.aetherwars.interfaces.Event;
import com.aetherwars.interfaces.Subscriber;
import com.aetherwars.models.Phase;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DisplayManager implements Subscriber {
    private static DisplayManager ins = null;
    private Stage stage;
    private Scene curScene;
    private Map<String, Scene> sceneMap = new HashMap<>();
    static final BackgroundSize bgSize = new BackgroundSize(
        0.9, 0.9, true, true,
        true, false
    );

    public void init(Stage stage) throws IOException {
        this.stage = stage;
        stage.setTitle("Minecraft: Aether Wars");
        addDisplay("main.board", "Board.fxml");
        addDisplay("panel.discard", "DiscardPanel.fxml");
        addDisplay("panel.draw", "DrawPanel.fxml");
        showDisplay("main.board");
    }

    public static Background getImage(String filePath) {
        BackgroundImage bg = new BackgroundImage(
                new Image("/com/aetherwars/" + filePath),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                bgSize
        );
        return new Background(bg);
    }

    public void addDisplay(String displayName, String fileName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aetherwars/gui/" + fileName));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1280, 720);
        sceneMap.put(displayName, scene);
    }

    public void showDisplay(String displayName) {
        if (sceneMap.containsKey(displayName)) {
            stage.setScene(sceneMap.get(displayName));
            stage.show();
        }
    }

    public static DisplayManager getInstance() {
        if (ins == null)
            ins = new DisplayManager();
        return ins;
    }

    @Override
    public void receiveEvent(Event evt) {
        GameManager gm = GameManager.getInstance();
        if (evt instanceof OnPhaseChange) {
            switch (((OnPhaseChange) evt).getPhase()) {
                case DRAW:
                    this.showDisplay("panel.draw");
                    break;
            }
        } else if (evt instanceof OnDrawCard) {
            this.showDisplay("main.board");
            gm.sendEvent(new OnPhaseChange(this, Phase.PLAN));
        }
    }
}
