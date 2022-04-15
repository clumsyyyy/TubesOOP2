package com.aetherwars.controllers;

import com.aetherwars.core.DisplayManager;
import com.aetherwars.core.GameManager;
import com.aetherwars.events.OnDrawCard;
import com.aetherwars.events.OnGameStart;
import com.aetherwars.events.OnPhaseChange;
import com.aetherwars.interfaces.Event;
import com.aetherwars.interfaces.Subscriber;
import com.aetherwars.models.Phase;
import com.aetherwars.models.cards.Card;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

public class DrawController implements Subscriber {
    public Pane draw_1;
    public Pane draw_2;
    public Pane draw_3;
    public Label draw1_mana;
    public Label draw2_mana;
    public Label draw3_mana;
    public Pane[] draw_img;
    public Label[] draw_mana;

    final BackgroundSize bgSize = new BackgroundSize(
            0.9, 0.9, true, true,
            true, false
    );

    public DrawController () {
        GameManager.getInstance().addSubscriber(this);
    }

    public EventHandler<? super MouseEvent> OnSelectCard(Card c) {
        return (event -> {
            GameManager.getInstance().sendEvent(new OnDrawCard(this, c));
        });
    }

    @Override
    public void receiveEvent(Event evt) {
        GameManager gm = GameManager.getInstance();
        if (evt instanceof OnGameStart) {
            draw_img = new Pane[] {draw_1, draw_2, draw_3};
            draw_mana = new Label[] {draw1_mana, draw2_mana, draw3_mana};
        } else if (evt instanceof OnPhaseChange) {
            if (((OnPhaseChange) evt).getPhase() == Phase.DRAW) {
                for (int i = 0; i < 3; i++) {
                    Card c = gm.getCurrentPlayer().getDeck().takeCard();
                    draw_img[i].setBackground(DisplayManager.getImage(c.getImagePath()));
                    draw_mana[i].setText(" [" + (i + 1) + "] MANA: " + c.getRequiredMana());
                    draw_img[i].setOnMousePressed(OnSelectCard(c));
                }
            }
        }
    }
}
