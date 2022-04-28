package com.aetherwars.models;

import com.aetherwars.core.GameManager;
import com.aetherwars.events.*;
import com.aetherwars.models.cards.Card;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PlayerTest {
    @Test
    public void test_a_playerStart() {
        GameManager gm = GameManager.getInstance();
        gm.initGame(40, null, null);
        assertEquals(gm.getCurrentPlayer().getDeck().getSize(), 40);
        assertEquals(gm.getOpponentPlayer().getDeck().getSize(), 40);
        gm.sendEvent(new OnGameStart(gm));
        Player a = gm.getPlayer(0);
        assertEquals(a.getBoard().getType(), BoardType.BOARD);
        assertEquals(a.getHand().getType(), BoardType.HAND);
        assertEquals(a.getHand().getSize(),3);
        assertEquals(a.getDeck().getSize(), 37);
        Player b = gm.getPlayer(1);
        assertEquals(b.getBoard().getType(), BoardType.BOARD);
        assertEquals(b.getHand().getType(), BoardType.HAND);
        assertEquals(b.getHand().getSize(),3);
        assertEquals(b.getDeck().getSize(), 37);
    }

    @Test
    public void test_b_playerDraw() {
        GameManager gm = GameManager.getInstance();
        List<Card> cards = gm.getCurrentPlayer().getDeck().getDrawCard(3);
        Card c = cards.get(0);
        gm.sendEvent(new OnDrawCard(this, c));
        Player a = gm.getPlayer(0);
        assertEquals(a.getHand().getSize(), 4);
        assertEquals(a.getHand().getCard(3), c);
        Player b = gm.getPlayer(1);
        assertEquals(b.getHand().getSize(),3);
        assertEquals(gm.getCurrentPlayer().getDeck().getSize(), 36);
    }

    @Test
    public void test_c_playerPick() {
        GameManager gm = GameManager.getInstance();
        Card c = gm.getCurrentPlayer().getHand().getCard(0);
        //gm.sendEvent(new OnPickCard(this, c, 2));
        Player a = gm.getPlayer(0);
        assertEquals(a.getHand().getSize(), 3);
        assertEquals(a.getBoard().getCard(2), c);
        assertEquals(a.getBoard().getSize(), 1);
        assertEquals(a.getDeck().getSize(), 36);
        Player b = gm.getPlayer(1);
        assertEquals(b.getHand().getSize(),3);
        assertEquals(b.getBoard().getSize(),0);
        assertEquals(b.getDeck().getSize(), 37);
    }

    @Test
    public void test_d_playerAttack() {
        //GameManager gm = GameManager.getInstance();
        //gm.sendEvent(new OnCardAction(this, 2, -1, CardAction.ATTACK));
    }
}
