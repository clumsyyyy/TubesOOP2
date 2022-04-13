package com.aetherwars.models;

import com.aetherwars.controllers.Player;
import com.aetherwars.core.GameManager;
import com.aetherwars.events.OnAttack;
import com.aetherwars.events.OnDrawCard;
import com.aetherwars.events.OnGameStart;
import com.aetherwars.events.OnPickCard;
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
        gm.initGame();
        assertEquals(gm.getDeck().getSize(), 40);
        gm.sendEvent(new OnGameStart(gm));
        Player a = gm.getPlayer(0);
        assertEquals(a.getBoard().getType(), BoardType.BOARD);
        assertEquals(a.getHand().getType(), BoardType.HAND);
        assertEquals(a.getHand().getSize(),3);
        Player b = gm.getPlayer(1);
        assertEquals(b.getBoard().getType(), BoardType.BOARD);
        assertEquals(b.getHand().getType(), BoardType.HAND);
        assertEquals(b.getHand().getSize(),3);
    }

    @Test
    public void test_b_playerDraw() {
        GameManager gm = GameManager.getInstance();
        List<Card> cards = gm.getDeck().getDrawCard();
        Card c = cards.get(0);
        gm.sendEvent(new OnDrawCard(this, c));
        Player a = gm.getPlayer(0);
        assertEquals(a.getHand().getSize(), 4);
        assertEquals(a.getHand().getCard(3), c);
        Player b = gm.getPlayer(1);
        assertEquals(b.getHand().getSize(),3);
        assertEquals(gm.getDeck().getSize(), 33);
    }

    @Test
    public void test_c_playerPick() {
        GameManager gm = GameManager.getInstance();
        Card c = gm.getCurrentPlayer().getHand().getCard(0);
        gm.sendEvent(new OnPickCard(this, c, 2));
        Player a = gm.getPlayer(0);
        assertEquals(a.getHand().getSize(), 3);
        assertEquals(a.getBoard().getCard(2), c);
        assertEquals(a.getBoard().getSize(), 1);
        Player b = gm.getPlayer(1);
        assertEquals(b.getHand().getSize(),3);
        assertEquals(b.getBoard().getSize(),0);
        assertEquals(gm.getDeck().getSize(), 33);
    }

    @Test
    public void test_d_playerAttack() {
        GameManager gm = GameManager.getInstance();
        gm.sendEvent(new OnAttack(this, 2, -1));
    }
}
