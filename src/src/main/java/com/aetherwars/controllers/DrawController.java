package com.aetherwars.controllers;

import com.aetherwars.core.DisplayManager;
import com.aetherwars.core.GameManager;
import com.aetherwars.events.OnDrawCard;
import com.aetherwars.events.OnGameStart;
import com.aetherwars.events.OnPhaseChange;
import com.aetherwars.interfaces.Event;
import com.aetherwars.interfaces.Informable;
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
    public Label player_label;
    public Label draw1_mana;
    public Label draw2_mana;
    public Label draw3_mana;
    public Label draw1_name;
    public Label draw2_name;
    public Label draw3_name;
    public Label draw1_info;
    public Label draw2_info;
    public Label draw3_info;
    public Label draw1_desc;
    public Label draw2_desc;
    public Label draw3_desc;
    public Pane[] draw_img;
    public Label[] draw_mana;
    public Label[] draw_name;
    public Label[] draw_desc;
    public Label[] draw_info;

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

    public EventHandler<? super MouseEvent> OnDrawHoverCard(int i) {
        return (event -> {
            DisplayManager.cardHoverFX(draw_img[i]);
            DisplayManager.cardLabelHoverFX(draw_mana[i]);
        });
    }


    public EventHandler<? super MouseEvent> OnDrawHoverExitCard(int i) {
        return (event -> {
            DisplayManager.cardExitFX(draw_img[i]);
            DisplayManager.cardLabelExitFX(draw_mana[i]);
        });
    }

    @Override
    public void receiveEvent(Event evt) {
        GameManager gm = GameManager.getInstance();
        if (evt instanceof OnGameStart) {
            draw_img = new Pane[] {draw_1, draw_2, draw_3};
            draw_mana = new Label[] {draw1_mana, draw2_mana, draw3_mana};
            draw_name = new Label[] {draw1_name, draw2_name, draw3_name};
            draw_desc = new Label[] {draw1_desc, draw2_desc, draw3_desc};
            draw_info = new Label[] {draw1_info, draw2_info, draw3_info};
        } else if (evt instanceof OnPhaseChange) {
            if (((OnPhaseChange) evt).getPhase() == Phase.DRAW) {
                player_label.setText(gm.getCurrentPlayer().getName() + ", it's your turn to draw!");
                for (int i = 0; i < 3; i++) {
                    Card c = gm.getCurrentPlayer().getDeck().takeCard();
                    draw_img[i].setBackground(DisplayManager.getImage(c.getImagePath()));
                    draw_img[i].setOnMousePressed(OnSelectCard(c));
                    draw_img[i].setOnMouseEntered(OnDrawHoverCard(i));
                    draw_img[i].setOnMouseExited(OnDrawHoverExitCard(i));
                    draw_mana[i].setText(" [" + (i + 1) + "] MANA: " + c.getRequiredMana());
                    draw_name[i].setText(c.getName());
                    draw_info[i].setText(((Informable)c).getInfo());
                    draw_desc[i].setText(c.getDesc());
                }
            }
        }
    }
}
