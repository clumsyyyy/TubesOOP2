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

    public void initGame(int deckSize, File deckFile) {
        try {
            ins.cardList = Loader.loadCards();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Deck[] decks;
        if (deckFile != null)
            decks = new Deck[]{
                DeckFactory.create(ins.cardList, deckFile),
                DeckFactory.create(ins.cardList, deckFile),
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

    public void initGame() {
        initGame(40, null);
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
            }
        }
        // cek apakah end game.
        // kalau deck kosong atau hp = 0.
    }
}
