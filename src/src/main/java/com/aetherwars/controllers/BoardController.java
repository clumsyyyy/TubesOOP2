package com.aetherwars.controllers;

import com.aetherwars.core.GameManager;
import com.aetherwars.events.OnGameStart;
import com.aetherwars.interfaces.Event;
import com.aetherwars.interfaces.Subscriber;
import com.aetherwars.models.Board;
import com.aetherwars.models.cards.Card;
import com.aetherwars.models.cards.CharacterCard;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

public class BoardController implements Subscriber {
    public SplitPane board;
    public AnchorPane p1_cards;
    public Pane p1_hand1;
    public Pane p1_hand2;
    public Pane p1_hand3;
    public Pane p1_hand4;
    public Pane p1_hand5;
    public Pane p2_hand1;
    public Pane p2_hand2;
    public Pane p2_hand3;
    public Pane p2_hand4;
    public Pane p2_hand5;
    public Pane[] p1;
    public Pane[] p2;
    public Pane[][] p = {null, null};
    final BackgroundSize bgSize = new BackgroundSize(0.9, 0.9, true, true, true, false);
    public Label c_name;
    public Label c_info;
    public Label c_desc;
    public ImageView c_img;

    public BoardController () {
        GameManager.getInstance().addSubscriber(this);
    }

    public EventHandler<? super MouseEvent> OnHoverExitCard = (event -> {
        c_name.setVisible(false);
        c_desc.setVisible(false);
        c_info.setVisible(false);
        c_img.setVisible(false);
    });

    public EventHandler<? super MouseEvent> OnHoverCard(int player_idx, int card_idx) {
        return (event -> {
            c_name.setVisible(true);
            c_desc.setVisible(true);
            c_info.setVisible(true);
            c_img.setVisible(true);
            GameManager gm = GameManager.getInstance();
            Card c = gm.getPlayer(player_idx).getHand().getCard(card_idx);
            if (c != null) {
                c_name.setText(c.getName());
                if (c instanceof CharacterCard) {
                    CharacterCard cd = (CharacterCard) c;
                    c_info.setText(String.format("Atk : %.2f %s\nHP : %.2f\nLevel : %d\nEXP : %d/%d\nType : %s",
                            cd.getATK(), "", cd.getHP(), cd.getLevel(), cd.getExp(), cd.getLevel(), cd.getType().toString()
                    ));
                }
                c_desc.setText(c.getDesc());
                c_img.setImage(new Image("/com/aetherwars/" + c.getImagePath()));
            }
        });
    }

    void init() {
        GameManager gm = GameManager.getInstance();
        // Start UI
        Board[] b = new Board[] {
                gm.getCurrentPlayer().getHand(),
                gm.getOpponentPlayer().getHand()
        };
        p1 = new Pane[] {p1_hand1, p1_hand2, p1_hand3, p1_hand4, p1_hand5};
        p2 = new Pane[] {p2_hand1, p2_hand2, p2_hand3, p2_hand4, p2_hand5};
        p = new Pane[][]{p1, p2};
        for (int k = 0; k < 2; k++) {
            for (int i = 0; i < 5; i++) {
                if (b[k].getCard(i) != null) {
                    System.out.println(b[k].getCard(i).getImagePath());
                    BackgroundImage bg = new BackgroundImage(
                            new Image("/com/aetherwars/" + b[k].getCard(i).getImagePath()),
                            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                            bgSize
                    );
                    p[k][i].setBackground(new Background(bg));
                    p[k][i].setOnMouseEntered(OnHoverCard(k, i));
                    p[k][i].setOnMouseExited(OnHoverExitCard);
                }
            }
        }
    }

    @Override
    public void receiveEvent(Event evt) {
        if (evt instanceof OnGameStart) {
            init();
        }
    }
}
