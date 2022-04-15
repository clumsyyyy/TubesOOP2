package com.aetherwars.controllers;

import com.aetherwars.core.DisplayManager;
import com.aetherwars.core.GameManager;
import com.aetherwars.events.*;
import com.aetherwars.interfaces.Event;
import com.aetherwars.interfaces.Informable;
import com.aetherwars.interfaces.Subscriber;
import com.aetherwars.models.Board;
import com.aetherwars.models.Phase;
import com.aetherwars.models.Player;
import com.aetherwars.models.cards.Card;
import com.aetherwars.models.cards.CharacterCard;
import com.aetherwars.models.cards.SpawnedCard;
import com.aetherwars.models.cards.SpellCard;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.zip.DataFormatException;

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
    public Label draw_phase;
    public Label planning_phase;
    public Label attack_phase;
    public Label end_phase;
    public Button next_phase;

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

    private Board[] b;
    private Player[] players;

    public EventHandler<? super MouseEvent> OnHoverCard(int player_idx, int card_idx, boolean isHand) {
        return (event -> {
            Card c;
            if (isHand)
                c = players[player_idx].getHand().getCard(card_idx);
            else
                c = players[player_idx].getBoard().getCard(card_idx);
            if (c != null) {
                toggleCharacterInfo(true);
                c_name.setText(c.getName());
                c_info.setText(((Informable)c).getInfo());
                c_desc.setText(c.getDesc());
                c_img.setImage(new Image("/com/aetherwars/" + c.getImagePath()));
            }
        });
    }

    public void ChangePhase(Phase to) {
        draw_phase.setStyle("-fx-background-color: #c2c2c2;");
        planning_phase.setStyle("-fx-background-color: #c2c2c2;");
        attack_phase.setStyle("-fx-background-color: #c2c2c2;");
        end_phase.setStyle("-fx-background-color: #c2c2c2;");
        if (to == Phase.DRAW) {
            draw_phase.setStyle("-fx-background-color: #8aceed;");
        } else if (to == Phase.PLAN) {
            planning_phase.setStyle("-fx-background-color: #fad37a;");
        } else if (to == Phase.ATTACK) {
            attack_phase.setStyle("-fx-background-color: #fa7a7a;");
        } else if (to == Phase.END) {
            end_phase.setStyle("-fx-background-color: #e0acf2;");
        }
    }

    public EventHandler<? super MouseEvent> OnDragCardPlanHand (int player_idx, int card_idx) {
        return (event -> {
            GameManager gm = GameManager.getInstance();
            if (gm.getCurrentPlayerIdx() != player_idx)
                return;
            Card c = players[player_idx].getHand().getCard(card_idx);
            if (gm.getPhase() == Phase.PLAN) {
                if (c instanceof CharacterCard || c instanceof SpellCard) {
                    Dragboard db = p[player_idx][card_idx].startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.putString(String.format("%d %d", player_idx, card_idx));
                    db.setContent(content);
                }
            }
            event.consume();
        });
    }

    public EventHandler<? super DragEvent> OnDragAcceptCardBoard (int player_idx, int card_idx) {
        return (event -> {
            if (event.getDragboard().hasString()) {
                GameManager gm = GameManager.getInstance();
                Card c = players[player_idx].getBoard().getCard(card_idx);
                String[] s = event.getDragboard().getString().split(" ");
                Player fromPlayer = players[Integer.parseInt(s[0])];
                Card cfrom = fromPlayer.getHand().getCard(
                    Integer.parseInt(s[1])
                );
                if (
                    // make sure it's plan
                    (gm.getPhase() == Phase.PLAN &&
                        ( // and check if it's character usage or spell card usage
                            (c == null && player_idx == gm.getCurrentPlayerIdx()) ||
                            (c != null && cfrom instanceof SpellCard)
                        )
                        // and make sure players from have enough mana to use card
                        && fromPlayer.getMana() >= cfrom.getRequiredMana()) ||
                    // ... or if it's attack and coming from another player which card is exist
                    (gm.getPhase() == Phase.ATTACK &&
                    player_idx != gm.getCurrentPlayerIdx() &&
                    c != null)
                )
                    event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
    }

    public EventHandler<? super DragEvent> OnDragEndCardBoard (int pidx, int cidx) {
        return (event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                success = true;
                GameManager gm = GameManager.getInstance();
                if (gm.getPhase() == Phase.PLAN) {
                    Card cfrom = players[pidx].getHand().getCard(
                        Integer.parseInt(db.getString().split(" ")[1])
                    );
                    if (cfrom instanceof CharacterCard) {
                        gm.sendEvent(
                            new OnCardAction(this, db.getString(), pidx, cidx, CardAction.PICK)
                        );
                    } else if (cfrom instanceof SpellCard) {
                        gm.sendEvent(
                            new OnCardAction(this, db.getString(), pidx, cidx, CardAction.SPELL)
                        );
                    }
                } else if (gm.getPhase() == Phase.ATTACK) {
                    gm.sendEvent(
                        new OnCardAction(this, db.getString(), pidx, cidx, CardAction.ATTACK)
                    );
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    public EventHandler<? super MouseEvent> OnDragCardAttackBoard (int player_idx, int card_idx) {
        return (event -> {
            GameManager gm = GameManager.getInstance();
            if (gm.getCurrentPlayerIdx() != player_idx)
                return;
            Card c = players[player_idx].getBoard().getCard(card_idx);
            if (c instanceof SpawnedCard && gm.getPhase() == Phase.ATTACK) {
                Dragboard db = pb[player_idx][card_idx].startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(String.format("%d %d", player_idx, card_idx));
                db.setContent(content);
            }
            event.consume();
        });
    }

    void updateBoard(boolean firstInit) {
        p1_hp.setText("HP: " + String.format("%.2f", players[0].getHP()) + " / 80");
        p2_hp.setText("HP: " + String.format("%.2f", players[1].getHP())+ " / 80");
        p1_mana.setText("Mana: " + players[0].getMana());
        p2_mana.setText("Mana: " + players[1].getMana());
        // Update card on hand
        for (int k = 0; k < 2; k++) {
            for (int i = 0; i < 5; i++) {
                if (b[k].getCard(i) != null) {
                    p[k][i].setBackground(DisplayManager.getImage(b[k].getCard(i).getImagePath()));
                    p_hand_mana[k][i].setText(" [" + (i + 1) + "] MANA: " + b[k].getCard(i).getRequiredMana());
                } else {
                    p[k][i].setBackground(null);
                    p_hand_mana[k][i].setText(" [" + (i + 1) + "] EMPTY");
                }
                if (firstInit) {
                    p[k][i].setOnMouseEntered(OnHoverCard(k, i, true));
                    p[k][i].setOnMouseExited(OnHoverExitCard);
                    p[k][i].setOnDragDetected(OnDragCardPlanHand(k, i));
                }
            }
        }
        // Update card on board
        for (int k = 2; k < 4; k++) {
            for (int i = 0; i < 5; i++) {
                if (b[k].getCard(i) != null) {
                    pb[k-2][i].setBackground(DisplayManager.getImage(b[k].getCard(i).getImagePath()));
                } else {
                    pb[k-2][i].setBackground(null);
                }
                if (firstInit) {
                    pb[k-2][i].setOnMouseEntered(OnHoverCard(k-2, i, false));
                    pb[k-2][i].setOnMouseExited(OnHoverExitCard);
                    pb[k-2][i].setOnDragOver(OnDragAcceptCardBoard(k-2, i));
                    pb[k-2][i].setOnDragDropped(OnDragEndCardBoard(k-2, i));
                    pb[k-2][i].setOnDragDetected(OnDragCardAttackBoard(k-2, i));
                }
            }
        }
    }

    void init() {
        GameManager gm = GameManager.getInstance();
        // Start UI
        b = new Board[] {
            gm.getCurrentPlayer().getHand(),
            gm.getOpponentPlayer().getHand(),
            gm.getCurrentPlayer().getBoard(),
            gm.getOpponentPlayer().getBoard()
        };
        players = new Player[] {
            gm.getCurrentPlayer(),
            gm.getOpponentPlayer()
        };
        p1_name.setText(gm.getCurrentPlayer().getName());
        p2_name.setText(gm.getOpponentPlayer().getName());
        p1 = new Pane[]{p1_hand1, p1_hand2, p1_hand3, p1_hand4, p1_hand5};
        p2 = new Pane[]{p2_hand1, p2_hand2, p2_hand3, p2_hand4, p2_hand5};
        p1_hand_mana = new Label[]{p1_hand1_mana, p1_hand2_mana, p1_hand3_mana, p1_hand4_mana, p1_hand5_mana};
        p2_hand_mana = new Label[]{p2_hand1_mana, p2_hand2_mana, p2_hand3_mana, p2_hand4_mana, p2_hand5_mana};
        p = new Pane[][]{p1, p2};
        p_hand_mana = new Label[][]{p1_hand_mana, p2_hand_mana};
        p1b = new Pane[]{p1_board1, p1_board2, p1_board3, p1_board4, p1_board5};
        p2b = new Pane[]{p2_board1, p2_board2, p2_board3, p2_board4, p2_board5};
        pb = new Pane[][]{p1b, p2b};
        Random rand = new Random();
        next_phase.setOnMousePressed((event -> {
            Phase nextPhase = null;
            switch (gm.getPhase()) {
                case DRAW:
                    nextPhase = Phase.PLAN;
                    break;
                case PLAN:
                    nextPhase = Phase.ATTACK;
                    break;
                case ATTACK:
                    nextPhase = Phase.END;
                    break;
                case END:
                    if (gm.getCurrentPlayer().getHand().getSize() == 5)
                        gm.getCurrentPlayer().getHand().unregister(rand.nextInt(5));
                    nextPhase = Phase.DRAW;
                    break;
            }
            gm.sendEvent(new OnPhaseChange(this, nextPhase));
        }));
        updateBoard(true);
    }

    @Override
    public void receiveEvent(Event evt) {
        if (evt instanceof OnGameStart) {
            init();
        } else if (evt instanceof OnPhaseChange) {
            ChangePhase(((OnPhaseChange) evt).getPhase());
        }
        updateBoard(false);
    }
}
