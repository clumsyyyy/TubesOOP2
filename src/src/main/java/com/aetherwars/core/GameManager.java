package com.aetherwars.core;

import com.aetherwars.models.Board;
import com.aetherwars.models.Deck;
import com.aetherwars.models.Player;
import com.aetherwars.events.OnPhaseChange;
import com.aetherwars.interfaces.Event;
import com.aetherwars.interfaces.Publisher;
import com.aetherwars.interfaces.Subscriber;
import com.aetherwars.models.BoardType;
import com.aetherwars.models.DeckFactory;
import com.aetherwars.models.Phase;
import com.aetherwars.models.cards.Card;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class GameManager extends Publisher implements Subscriber {
    private static GameManager ins = null;
    private Player[] players;
    private Phase phase;
    private int currentPlayer = 0;
    private ArrayList<Card> cardList;

    public static GameManager getInstance() {
        if (ins == null)
            ins = new GameManager();
        return ins;
    }

    public int getCurrentPlayerIdx() {
        return currentPlayer;
    }

    public Player getCurrentPlayer() {
        return players[currentPlayer];
    }

    public Player getOpponentPlayer() {
        return players[(currentPlayer + 1) % 2];
    }

    public void changeCurrentPlayer() {
        currentPlayer = (currentPlayer + 1) % 2;
    }

    public Player getPlayer(int index) {
        return players[index];
    }

    public Phase getPhase() {
        return phase;
    }

    public void initGame(int deckSize, File deckFile1, File deckFile2) {
        try {
            ins.cardList = Loader.loadCards();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Deck[] decks;
        if (deckFile1 != null & deckFile2 != null)
            decks = new Deck[]{
                DeckFactory.create(ins.cardList, deckFile1),
                DeckFactory.create(ins.cardList, deckFile2),
            };
        else
            decks = new Deck[]{
                DeckFactory.create(ins.cardList, deckSize),
                DeckFactory.create(ins.cardList, deckSize)
            };
        ins.players = new Player[]{
            new Player("Alex",
                new Board(BoardType.BOARD),
                new Board(BoardType.HAND),
                decks[0],
                "card/image/character/player1.png"
            ),
            new Player("Steve",
                new Board(BoardType.BOARD),
                new Board(BoardType.HAND),
                decks[1],
                "card/image/character/player2.png"
            )
        };
        addSubscriber(ins.players[0]);
        addSubscriber(ins.players[1]);
        addSubscriber(ins);
    }
    
    public void updateCards( File deckFile1, File deckFile2) {

        Deck[] decks;
        if (deckFile1 != null && deckFile2 != null){
            decks = new Deck[]{
                DeckFactory.create(ins.cardList, deckFile1),
                DeckFactory.create(ins.cardList, deckFile2),
            };
                
            players[0].setDeck(decks[0]);
            players[1].setDeck(decks[1]);
        }

    }

    public void initGame() {
        initGame(40, null, null);
    }

    public Card getCardById(int id) {
        for (Card c: cardList) {
            if (c.getId() == id)
                return c;
        }
        return null;
    }

    @Override
    public void receiveEvent(Event evt) {
        if (evt instanceof OnPhaseChange) {
            Phase curPhase = ((OnPhaseChange) evt).getPhase();
            phase = curPhase;
            if (curPhase == Phase.END) {
                changeCurrentPlayer();
                GameManager gm = GameManager.getInstance();
                if (gm.getCurrentPlayer().getHP() <= 0 ||
                    gm.getOpponentPlayer().getHP() <= 0 ||
                    gm.getCurrentPlayer().getDeck().getSize() == 0 ||
                    gm.getOpponentPlayer().getDeck().getSize() == 0) {

                    Alert a = new Alert(AlertType.INFORMATION);
                    a.initModality(Modality.APPLICATION_MODAL);
                    a.initOwner(DisplayManager.getInstance().getStage());

                    if (gm.getCurrentPlayer().getHP() <= 0 ) {
                        a.setHeaderText("GAME OVER - " + gm.getOpponentPlayer().getName() + " wins!");
                        a.setContentText(gm.getCurrentPlayer().getName() + " lost because he has no HP left!");
                    } else if (gm.getOpponentPlayer().getHP() <= 0){
                        a.setHeaderText("GAME OVER - " + gm.getCurrentPlayer().getName() + " wins!");
                        a.setContentText(gm.getOpponentPlayer().getName() + " lost because he has no HP left!");
                    } else if (gm.getCurrentPlayer().getDeck().getSize() == 0){
                        a.setHeaderText("GAME OVER - " + gm.getOpponentPlayer().getName() + " wins!");
                        a.setContentText(gm.getCurrentPlayer().getName() + " lost - no cards remaining!");
                    }else if (gm.getOpponentPlayer().getDeck().getSize() == 0){
                        a.setHeaderText("GAME OVER - " + gm.getCurrentPlayer().getName() + " wins!");
                        a.setContentText(gm.getOpponentPlayer().getName() + " lost - no cards remaining!");
                    }
                    
                    a.showAndWait();
                    System.exit(0);
                }
            }
        }
        // cek apakah end game.
        // kalau deck kosong atau hp = 0.
    }
}