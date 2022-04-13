package com.aetherwars.core;

import com.aetherwars.controllers.Board;
import com.aetherwars.controllers.Deck;
import com.aetherwars.controllers.Player;
import com.aetherwars.events.OnGameStart;
import com.aetherwars.events.OnPhaseChange;
import com.aetherwars.interfaces.Publisher;
import com.aetherwars.models.BoardType;
import com.aetherwars.models.DeckFactory;
import com.aetherwars.models.Phase;
import com.aetherwars.models.cards.Card;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class GameManager extends Publisher {
    static GameManager ins = null;
    private Player[] players;
    private Deck decks;
    private ArrayList<Card> cardList;

    public static GameManager getInstance() {
        if (ins == null)
            ins = new GameManager();
        return ins;
    }

    public void initGame(int deckSize, File deckFile) {
        ins.players = new Player[]{
            new Player("Alex", new Board(BoardType.BOARD), new Board(BoardType.HAND)),
            new Player("Steve", new Board(BoardType.BOARD), new Board(BoardType.HAND))
        };
        try {
            ins.cardList = Loader.loadCards();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (deckFile != null)
            ins.decks = DeckFactory.create(ins.cardList, deckFile);
        else
            ins.decks = DeckFactory.create(ins.cardList, deckSize);
        addSubscriber(ins.players[0]);
        addSubscriber(ins.players[1]);
        addSubscriber(ins.decks);
        sendEvent(new OnGameStart(this));
    }

    public void initGame() {
        initGame(40, null);
    }

}
