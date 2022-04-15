package com.aetherwars.controllers;

import com.aetherwars.core.GameManager;
import com.aetherwars.events.OnGameStart;
import com.aetherwars.interfaces.Event;
import com.aetherwars.interfaces.Informable;
import com.aetherwars.interfaces.Subscriber;
import com.aetherwars.models.Board;
import com.aetherwars.models.cards.Card;
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
    public Pane[] p1;
    public Pane[] p2;
    public Pane p2_hand1;
    public Pane p2_hand2;
    public Pane p2_hand3;
    public Pane p2_hand4;
    public Pane p2_hand5;
    public Pane[][] p;
    final BackgroundSize bgSize = new BackgroundSize(
        0.9, 0.9, true, true,
        true, false
    );
    public Label c_name;
    public Label c_info;
    public Label c_desc;
    public ImageView c_img;
    public Pane p1_board1;
    public Pane p1_board2;
    public Pane p1_board3;
    public Pane p1_board4;
    public Pane p1_board5;
    public Pane[] p1b;
    public Pane p2_board1;
    public Pane p2_board2;
    public Pane p2_board3;
    public Pane p2_board4;
    public Pane p2_board5;
    public Pane[] p2b;
    public Pane[][] pb;
    public Label p1_hand1_mana;
    public Label p1_hand2_mana;
    public Label p1_hand3_mana;
    public Label p1_hand4_mana;
    public Label p1_hand5_mana;
    public Label[] p1_hand_mana;
    public Label p2_hand1_mana;
    public Label p2_hand2_mana;
    public Label p2_hand3_mana;
    public Label p2_hand4_mana;
    public Label p2_hand5_mana;
    public Label[] p2_hand_mana;
    public Label[][] p_hand_mana;
    public Label p1_mana;
    public Label p1_name;
    public Label p1_hp;
    public Label p2_mana;
    public Label p2_name;
    public Label p2_hp;


    public BoardController () {
        GameManager.getInstance().addSubscriber(this);
    }

    public EventHandler<? super MouseEvent> OnHoverExitCard = (event -> {
        toggleCharacterInfo(false);
    });

    public void toggleCharacterInfo(boolean state) {
        c_name.setVisible(state);
        c_desc.setVisible(state);
        c_info.setVisible(state);
        c_img.setVisible(state);
    }

    public EventHandler<? super MouseEvent> OnHoverCard(int player_idx, int card_idx, boolean isHand) {
        return (event -> {
            toggleCharacterInfo(true);
            GameManager gm = GameManager.getInstance();
            Card c;
            if (isHand)
                c = gm.getPlayer(player_idx).getHand().getCard(card_idx);
            else
                c = gm.getPlayer(player_idx).getBoard().getCard(card_idx);
            if (c != null) {
                c_name.setText(c.getName());
                c_info.setText(((Informable)c).getInfo());
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
        p1_name.setText(gm.getCurrentPlayer().getName());
        p2_name.setText(gm.getOpponentPlayer().getName());
        p1_hp.setText("HP: " + String.format("%.2f", gm.getCurrentPlayer().getHP()) + " / 80");
        p2_hp.setText("HP: " + String.format("%.2f", gm.getOpponentPlayer().getHP())+ " / 80");
        p1_mana.setText("Mana: " + gm.getCurrentPlayer().getMana());
        p2_mana.setText("Mana: " + gm.getOpponentPlayer().getMana());
        p1 = new Pane[]{p1_hand1, p1_hand2, p1_hand3, p1_hand4, p1_hand5};
        p2 = new Pane[]{p2_hand1, p2_hand2, p2_hand3, p2_hand4, p2_hand5};
        p1_hand_mana = new Label[]{p1_hand1_mana, p1_hand2_mana, p1_hand3_mana, p1_hand4_mana, p1_hand5_mana};
        p2_hand_mana = new Label[]{p2_hand1_mana, p2_hand2_mana, p2_hand3_mana, p2_hand4_mana, p2_hand5_mana};
        p = new Pane[][]{p1, p2};
        p_hand_mana = new Label[][]{p1_hand_mana, p2_hand_mana};
        for (int k = 0; k < 2; k++) {
            for (int i = 0; i < 5; i++) {
                if (b[k].getCard(i) != null) {
                    BackgroundImage bg = new BackgroundImage(
                            new Image("/com/aetherwars/" + b[k].getCard(i).getImagePath()),
                            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                            bgSize
                    );
                    p[k][i].setBackground(new Background(bg));
                    p[k][i].setOnMouseEntered(OnHoverCard(k, i, true));
                    p[k][i].setOnMouseExited(OnHoverExitCard);
                    p_hand_mana[k][i].setText(" [" + (i + 1) + "] MANA: " + b[k].getCard(i).getRequiredMana());
                } else {
                    p_hand_mana[k][i].setText(" [" + (i + 1) + "] EMPTY");
                }
            }
        }
        b = new Board[] {
            gm.getCurrentPlayer().getBoard(),
            gm.getOpponentPlayer().getBoard()
        };
        p1b = new Pane[]{p1_board1, p1_board2, p1_board3, p1_board4, p1_board5};
        p2b = new Pane[]{p1_board1, p1_board2, p1_board3, p1_board4, p1_board5};
        pb = new Pane[][]{p1b, p2b};
        for (int k = 0; k < 2; k++) {
            for (int i = 0; i < 5; i++) {
                if (b[k].getCard(i) != null) {
                    p[k][i].setOnMouseEntered(OnHoverCard(k, i, false));
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
