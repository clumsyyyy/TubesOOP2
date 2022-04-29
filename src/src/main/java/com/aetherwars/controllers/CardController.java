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
import com.aetherwars.models.cards.HealCard;
import com.aetherwars.models.cards.SpawnedCard;
import com.aetherwars.models.cards.SpellCard;
import com.aetherwars.models.cards.LevelCard;
import com.aetherwars.models.cards.MorphCard;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
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

        ContextMenu ctx_menu = new ContextMenu();
        MenuItem add_item = new MenuItem("Add EXP");
        MenuItem discard_item = new MenuItem("Discard");
        add_item.setOnAction(add_event);
        discard_item.setOnAction(discard_event);

        card.setOnMouseEntered(OnHoverCard);
        card.setOnMouseExited(OnHoverExitCard);
        if (is_hand) {
            card.setOnDragDetected(OnDragCardPlanHand);
            ctx_menu.getItems().add(discard_item);
        } else {
            card.setOnDragOver(OnDragAcceptCardBoard);
            card.setOnDragDropped(OnDragEndCardBoard);
            card.setOnDragDetected(OnDragCardAttackBoard);
            ctx_menu.getItems().addAll(add_item, discard_item);
        }
        card.setOnContextMenuRequested((e) -> {
            if ((is_hand?player.getHand():player.getBoard()).getCard(card_idx) != null &&
            GameManager.getInstance().getCurrentPlayerIdx() == player_idx &&
            GameManager.getInstance().getPhase() == Phase.PLAN){
                ctx_menu.show(card, e.getScreenX(), e.getScreenY());
            }
        });
        update();
    }

    public EventHandler<ActionEvent> add_event = new EventHandler<ActionEvent>(){ 
        @Override
        public void handle(ActionEvent event) {
            if (player.getMana() - 1 >= 0){
                player.setMana(player.getMana() - 1);
                SpawnedCard sc = (SpawnedCard) player.getBoard().getCard(card_idx);
                sc.addExp(1);
            }
        }
    };

    public EventHandler<ActionEvent> discard_event = new EventHandler<ActionEvent>(){ 
        @Override
        public void handle(ActionEvent event) {

            if (is_hand){
                player.getHand().unregister(card_idx);
            } else {
                player.getBoard().unregister(card_idx);
            }
            
        }
    };


    public EventHandler<? super MouseEvent> OnHoverCard = (event -> {
        Card c;
        if (is_hand)
            c = player.getHand().getCard(card_idx);
        else
            c = player.getBoard().getCard(card_idx);
        GameManager.getInstance().sendEvent(new OnCardHovered(this, c));
        update();
        if (c != null) {
            DisplayManager.cardHoverFX(card);
            DisplayManager.cardLabelHoverFX(card_label);
        }
    });

    public EventHandler<? super MouseEvent> OnHoverExitCard = (event -> {
        GameManager.getInstance().sendEvent(new OnCardHovered(this, null));
        update();
        Card c;
        if (is_hand)
            c = player.getHand().getCard(card_idx);
        else
            c = player.getBoard().getCard(card_idx);
        if (c != null) {
            DisplayManager.cardExitFX(card, is_hand);
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
                        ( // and check if it's character usage or spell card usage and not heal card
                            (c == null && player_idx == gm.getCurrentPlayerIdx()
                                && !(cfrom instanceof SpellCard)) ||
                            (c != null && cfrom instanceof SpellCard) ||
                            ((cfrom instanceof MorphCard)
                                && player_idx != gm.getCurrentPlayerIdx())
                        ) && !(cfrom instanceof HealCard)
                        // and make sure players from have enough mana to use card
                        && fromPlayer.getMana() >=
                         ((cfrom instanceof LevelCard) ? 
                         ((LevelCard)cfrom).getRequiredMana(((SpawnedCard)c).getLevel()) : 
                         cfrom.getRequiredMana())
                ) ||
                // ... or if it's attack and coming from another player which card is exist
                (gm.getPhase() == Phase.ATTACK &&
                        (player_idx != gm.getCurrentPlayerIdx() && c != null))
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
                String[] d = db.getString().split(" ");
                Card cfrom = gm.getPlayer(Integer.parseInt(d[0])).getHand().getCard(
                        Integer.parseInt(d[1])
                );
                if (cfrom instanceof CharacterCard) {
                    gm.sendEvent(
                        new OnCardAction(this, db.getString(), true, player_idx, card_idx, CardAction.PICK)
                    );
                } else if (cfrom instanceof SpellCard) {
                    gm.sendEvent(
                        new OnCardAction(this, db.getString(), true, player_idx, card_idx, CardAction.SPELL)
                    );
                }
            } else if (gm.getPhase() == Phase.ATTACK) {
                gm.sendEvent(
                    new OnCardAction(this, db.getString(), false, player_idx, card_idx, CardAction.ATTACK)
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
                card.setEffect(null);
            }
        } else {
            Card c = player.getBoard().getCard(card_idx);
            GameManager gm = GameManager.getInstance();

            if (c != null) {
                card.setBackground(DisplayManager.getImage(c.getImagePath()));
                if (c instanceof SpawnedCard){
                    SpawnedCard temp = (SpawnedCard)c;
                    card_label.setText("LV: " + temp.getLevel() + " / " + (temp.getLevel() < 10 ? "EXP: " + temp.getExp() : "MAX"));
                    DropShadow cardShadow = new DropShadow();
                    if (temp.canAttack())
                        cardShadow.setColor(Color.web("#03fc6f"));
                    else
                        cardShadow.setColor(Color.web("#fa7a7a"));
                    card.setEffect(cardShadow);
                } else {
                    card_label.setText("SPELL");
                }
            } else {
                card.setBackground(null);
                card_label.setText("EMPTY");
                card.setEffect(null);
            }

            if (gm.getCurrentPlayer() != player){
                card.setEffect(null);
            }
        }
    }

    @Override
    public void receiveEvent(Event evt) {
        if (!(evt instanceof OnCardHovered))
            update();
    }
}