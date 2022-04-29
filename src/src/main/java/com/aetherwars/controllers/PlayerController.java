package com.aetherwars.controllers;

import java.io.IOException;

import com.aetherwars.core.DisplayManager;
import com.aetherwars.core.GameManager;
import com.aetherwars.events.CardAction;
import com.aetherwars.events.OnCardAction;
import com.aetherwars.interfaces.Event;
import com.aetherwars.interfaces.Subscriber;
import com.aetherwars.models.Phase;
import com.aetherwars.models.Player;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
public class PlayerController implements Subscriber {
    @FXML
    public Label mana;
    @FXML
    public Label name;
    @FXML
    public Label hp;
    @FXML
    public Label turn;
    @FXML
    public Label deck;
    @FXML
    public ProgressBar healthbar;
    @FXML 
    public Pane avatar;


    public int player_idx;
    public Player player;

    public PlayerController (FlowPane root, int index) {
        this.player_idx = index;
        GameManager gm = GameManager.getInstance();
        gm.addSubscriber(this);
        player = gm.getPlayer(index);
        try {
            DisplayManager.loadGui("Player.fxml", root, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        name.setText(player.getName());
        avatar.setBackground(DisplayManager.getImage(player.getImagePath()));
        avatar.setOnDragDropped(OnDragEndAvatar);
        avatar.setOnDragOver(OnDragAcceptAvatar);
        update();
    }

    public EventHandler<? super DragEvent> OnDragAcceptAvatar = (event -> {
        if (event.getDragboard().hasString()) {
            GameManager gm = GameManager.getInstance();
            if ((gm.getPhase() == Phase.ATTACK && 
            gm.getCurrentPlayer() != player &&
            player.getBoard().getSize() == 0)
            || (gm.getPhase() == Phase.PLAN &&
            gm.getCurrentPlayer() == player)) {
                // make sure si player controller ini
                // itu ga sama dengan current player
                event.acceptTransferModes(TransferMode.MOVE);
            }
        }
        event.consume();
    });

    
    public EventHandler<? super DragEvent> OnDragEndAvatar = (event -> {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasString()) {
            success = true;
            GameManager gm = GameManager.getInstance();
           
            if (gm.getPhase() == Phase.ATTACK) {
                gm.sendEvent(
                    new OnCardAction(this, db.getString(), false, player_idx, -1, CardAction.CHAR_ATTACK)
                );
            } else if (gm.getPhase() == Phase.PLAN) {
                gm.sendEvent(
                    new OnCardAction(this, db.getString(), false, player_idx, -1, CardAction.SPELL)
                );
            }
        }
        event.setDropCompleted(success);
        event.consume();
    });


    private void update() {
        hp.setText("HP: " + String.format("%.2f", player.getHP()) + " / 80");
        mana.setText("Mana: " + player.getMana());
        turn.setText("Turn: " + player.getTurn());
        deck.setText("Deck: " + player.getDeck().getSize());
        healthbar.setProgress((float) (player.getHP() / 80.0));
        GameManager gm = GameManager.getInstance();
        DropShadow avatarShadow = new DropShadow();
        if (healthbar.getProgress() >= 0.67){
            avatarShadow.setColor(Color.web("#03fc6f"));
            healthbar.setStyle("-fx-accent: #03fc6f;");
        } else {
            if (healthbar.getProgress() >= 0.33){
                avatarShadow.setColor(Color.web("#fad37a"));
                healthbar.setStyle("-fx-accent: #fad37a;");
            } else {
                avatarShadow.setColor(Color.web("#fa7a7a"));
                healthbar.setStyle("-fx-accent: #fa7a7a;");
            }
        }

        avatarShadow.setRadius(30.0);
        if (gm.getCurrentPlayer() == player) {
            avatar.setEffect(avatarShadow);
        } else {
            avatar.setEffect(null);
        }
    }
    
    @Override
    public void receiveEvent(Event evt) {
        update();
    }
}
