package com.aetherwars.controllers;

import java.io.IOException;

import com.aetherwars.core.DisplayManager;
import com.aetherwars.core.GameManager;
import com.aetherwars.events.CardAction;
import com.aetherwars.events.OnCardAction;
import com.aetherwars.events.OnCardHovered;
import com.aetherwars.interfaces.Event;
import com.aetherwars.interfaces.Subscriber;
import com.aetherwars.models.Phase;
import com.aetherwars.models.Player;
import com.aetherwars.models.cards.Card;
import com.aetherwars.models.cards.CharacterCard;
import com.aetherwars.models.cards.SpawnedCard;
import com.aetherwars.models.cards.SpellCard;
import com.aetherwars.models.cards.LevelCard;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

public class CardController implements Subscriber {
    @FXML
    private Pane card;
    @FXML
    private Label card_label;

    private Player player;
    private int player_idx;
    private int card_idx;
    private boolean is_hand;

    public CardController (Pane root, int player_idx, int card_idx, boolean is_hand) {
        GameManager gm = GameManager.getInstance();
        gm.addSubscriber(this);
        player = gm.getPlayer(player_idx);
        this.player_idx = player_idx;
        this.card_idx = card_idx;
        this.is_hand = is_hand;

        try {
            DisplayManager.loadGui("Card.fxml", root, this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        card.setOnMouseEntered(OnHoverCard);
        card.setOnMouseExited(OnHoverExitCard);
        if (is_hand) {
            card.setOnDragDetected(OnDragCardPlanHand);
        } else {
            card.setOnDragOver(OnDragAcceptCardBoard);
            card.setOnDragDropped(OnDragEndCardBoard);
            card.setOnDragDetected(OnDragCardAttackBoard);
        }
        update();
    }

    public EventHandler<? super MouseEvent> OnHoverCard = (event -> {
        Card c;
        if (is_hand)
            c = player.getHand().getCard(card_idx);
        else
            c = player.getBoard().getCard(card_idx);
        GameManager.getInstance().sendEvent(new OnCardHovered(this, c));
        if (c != null) {
            DisplayManager.cardHoverFX(card);
            DisplayManager.cardLabelHoverFX(card_label);
        }
    });

    public EventHandler<? super MouseEvent> OnHoverExitCard = (event -> {
        GameManager.getInstance().sendEvent(new OnCardHovered(this, null));
        Card c;
        if (is_hand)
            c = player.getHand().getCard(card_idx);
        else
            c = player.getBoard().getCard(card_idx);
        if (c != null) {
            DisplayManager.cardExitFX(card);
            DisplayManager.cardLabelExitFX(card_label);
        }
    });

    public EventHandler<? super MouseEvent> OnDragCardPlanHand = (event -> {
        GameManager gm = GameManager.getInstance();
        if (gm.getCurrentPlayer() != player)
            return;
        Card c = player.getHand().getCard(card_idx);
        if (gm.getPhase() == Phase.PLAN) {
            if (c instanceof CharacterCard || c instanceof SpellCard) {
                Dragboard db = card.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(String.format("%d %d", player_idx, card_idx));
                db.setContent(content);
            }
        }
        event.consume();
    });

    public EventHandler<? super DragEvent> OnDragAcceptCardBoard = (event -> {
        if (event.getDragboard().hasString()) {
            GameManager gm = GameManager.getInstance();
            Card c = player.getBoard().getCard(card_idx);
            String[] s = event.getDragboard().getString().split(" ");
            Player fromPlayer = gm.getPlayer(Integer.parseInt(s[0]));
            Card cfrom = fromPlayer.getHand().getCard(
                    Integer.parseInt(s[1])
            );

            if (
                // make sure it's plan
                (gm.getPhase() == Phase.PLAN &&
                        ( // and check if it's character usage or spell card usage
                            (c == null && player_idx == gm.getCurrentPlayerIdx()
                                    && !(cfrom instanceof SpellCard)) ||
                            (c != null && cfrom instanceof SpellCard)
                        )
                        // and make sure players from have enough mana to use card
                        && fromPlayer.getMana() >=
                         ((cfrom instanceof LevelCard) ? 
                         ((LevelCard)cfrom).getRequiredMana(((SpawnedCard)c).getLevel()) : 
                         cfrom.getRequiredMana())
                ) ||
                // ... or if it's attack and coming from another player which card is exist
                (gm.getPhase() == Phase.ATTACK &&
                        player_idx != gm.getCurrentPlayerIdx() &&
                        c != null)
            )
            event.acceptTransferModes(TransferMode.MOVE);
        }
        event.consume();
    });

    public EventHandler<? super DragEvent> OnDragEndCardBoard = (event -> {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasString()) {
            success = true;
            GameManager gm = GameManager.getInstance();
            if (gm.getPhase() == Phase.PLAN) {
                Card cfrom = player.getHand().getCard(
                        Integer.parseInt(db.getString().split(" ")[1])
                );
                if (cfrom instanceof CharacterCard) {
                    gm.sendEvent(
                            new OnCardAction(this, db.getString(), player_idx, card_idx, CardAction.PICK)
                    );
                } else if (cfrom instanceof SpellCard) {
                    gm.sendEvent(
                            new OnCardAction(this, db.getString(), player_idx, card_idx, CardAction.SPELL)
                    );
                }
            } else if (gm.getPhase() == Phase.ATTACK) {
                gm.sendEvent(
                        new OnCardAction(this, db.getString(), player_idx, card_idx, CardAction.ATTACK)
                );
            }
        }
        event.setDropCompleted(success);
        event.consume();
    });

    public EventHandler<? super MouseEvent> OnDragCardAttackBoard = (event -> {
        GameManager gm = GameManager.getInstance();
        if (gm.getCurrentPlayerIdx() != player_idx)
            return;
        Card c = player.getBoard().getCard(card_idx);
        if (c instanceof SpawnedCard && gm.getPhase() == Phase.ATTACK) {
            Dragboard db = card.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(String.format("%d %d", player_idx, card_idx));
            db.setContent(content);
        }
        event.consume();
    });

    private void update() {
        if (is_hand) {
            Card c = player.getHand().getCard(card_idx);
            if (c != null) {
                card.setBackground(DisplayManager.getImage(c.getImagePath()));
                card_label.setText(" [" + (card_idx + 1) + "] MANA: " + c.getRequiredMana());
            } else {
                card.setBackground(null);
                card_label.setText(" [" + (card_idx + 1) + "] EMPTY");
            }
        } else {
            Card c = player.getBoard().getCard(card_idx);
            if (c != null) {
                card.setBackground(DisplayManager.getImage(c.getImagePath()));
                if (c instanceof SpawnedCard){
                    SpawnedCard temp = (SpawnedCard)c;
                    card_label.setText("LV: " + temp.getLevel() + " / EXP: " + temp.getExp());
                } else {
                    card_label.setText("SPELL");
                }
            } else {
                card.setBackground(null);
                card_label.setText("EMPTY");
            }
        }
    }

    @Override
    public void receiveEvent(Event evt) {
        update();
    }
}
