package com.aetherwars.core;

import com.aetherwars.events.OnDrawCard;
import com.aetherwars.events.OnPhaseChange;
import com.aetherwars.interfaces.Event;
import com.aetherwars.interfaces.Subscriber;
import com.aetherwars.models.Phase;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.effect.DropShadow;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DisplayManager implements Subscriber {
    private static DisplayManager ins = null;
    public static final String FXML_PATH = "/com/aetherwars/gui/";
    private Stage stage;
    private Map<String, Scene> sceneMap = new HashMap<>();
    private static DropShadow cardShadow;
    static final BackgroundSize bgSize = new BackgroundSize(
        0.9, 0.9, true, true,
        true, false
    );

    public void init(Stage stage) throws IOException {
        cardShadow = new DropShadow();
        cardShadow.setRadius(20.0);
        cardShadow.setColor(Color.web("#fad37a"));
        // Font font = Font.loadFont("file:resources/com/aetherwars/font/font.ttf", 12);
        this.stage = stage;
        stage.setTitle("Minecraft: Aether Wars");
        addDisplay("main.board", "Game.fxml");
        addDisplay("panel.draw", "DrawPanel.fxml");
        addDisplay("panel.select", "SelectPanel.fxml");
        showDisplay("panel.select");
    }

    public final Stage getStage(){
        return this.stage;
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_PATH + fileName));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1280, 720);
        sceneMap.put(displayName, scene);
    }

    public static void loadGui(String fileName, Object root, Object controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(DisplayManager.class.getResource(FXML_PATH + fileName));
        loader.setRoot(root);
        loader.setController(controller);
        loader.load();
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
                case SELECT:
                    this.showDisplay("panel.select");
                default:
                    break;
            }
        } else if (evt instanceof OnDrawCard) {
            this.showDisplay("main.board");
            gm.sendEvent(new OnPhaseChange(this, Phase.PLAN));
        }
    }

    public static void cardHoverFX(Pane card){
        card.setPrefHeight(card.getPrefHeight() + 5);
        card.setPrefWidth(card.getPrefWidth() + 5);
        card.setEffect(cardShadow);
    }

    public static void cardExitFX(Pane card, boolean is_hand){
        card.setPrefHeight(card.getPrefHeight() - 5);
        card.setPrefWidth(card.getPrefWidth() - 5);
        if (is_hand){
            card.setEffect(null);
        }

    }

    public static void cardLabelHoverFX(Label lb){
        lb.setPrefWidth(lb.getPrefWidth() + 5);
    }

    public static void cardLabelExitFX(Label lb){
        lb.setPrefWidth(lb.getPrefWidth() - 5);
    }
}
