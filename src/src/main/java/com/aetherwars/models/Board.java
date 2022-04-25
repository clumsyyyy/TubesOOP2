package com.aetherwars.models;

import com.aetherwars.core.GameManager;
import com.aetherwars.events.*;
import com.aetherwars.interfaces.Event;
import com.aetherwars.interfaces.Observer;
import com.aetherwars.interfaces.Subscriber;
import com.aetherwars.models.cards.*;

// Board implements Observer
public class Board implements Observer<Card>, Subscriber {
    private final Card[] cards = new Card[] {null, null, null, null, null};
    private final BoardType type;
    private final int MAX_CAP = 5;

    public BoardType getType() {
        return type;
    }

    public Board (BoardType type) {
        this.type = type;
    }

    public int getSize() {
        int count = 0;
        for (int i = 0; i < MAX_CAP; i++) {
            if (cards[i] != null) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void register(Card card, int index) {
        if (index >= 0 && index < MAX_CAP) {
            cards[index] = card;
        } else {
            System.out.println("Invalid index");
        }
    }

    @Override
    public void register(Card obj) {
        for (int i = 0; i < MAX_CAP; i++) {
            if (cards[i] == null) {
                register(obj, i);
                return;
            }
        }
        System.out.println("No space left!");
    }

    @Override
    public void unregister(Card card) {
        for (int i = 0; i < MAX_CAP; i++) {
            if (cards[i] == card) {
                cards[i] = null;
                return;
            }
        }
        System.out.println("This card is not in the board!");
    }

    @Override
    public void unregister(int index) {
        if (cards[index] != null) {
            cards[index] = null;
        } else {
            System.out.println("Invalid index");
        }
    }

    public Card getCard(int index) {
        return cards[index];
    }

    @Override
    public void notifyObjects() {
        for (Card c: cards) {
            if (c != null)
                c.update();
        }
    }

    void attack(OnCardAction evt) {
        Card card_att = cards[evt.getFromCardIdx()];
        if (card_att instanceof SpawnedCard) {
            SpawnedCard sc_att = (SpawnedCard) card_att;
            if (evt.getToCardIdx() == -1 && sc_att.canAttack()) {
                // attack character directly
                sc_att.atk(null);
                sc_att.toggleAttack();
            } else {
                GameManager gm = GameManager.getInstance();
                // use attack function on CharacterCard
                Card target_att = gm.getOpponentPlayer().getBoard().getCard(evt.getToCardIdx());
                if (target_att instanceof SpawnedCard) {
                    SpawnedCard tg_att = (SpawnedCard) target_att;
                    if (sc_att.canAttack()) {
                        sc_att.atk(tg_att);
                        tg_att.atk(sc_att);
                        if (tg_att.getHP() <= 0) {
                            gm.getOpponentPlayer().getBoard().unregister(tg_att);
                        }
                        if (sc_att.getHP() <= 0) {
                            gm.getCurrentPlayer().getBoard().unregister(sc_att);
                        }
                        sc_att.toggleAttack();
                    }
                }
            }
        }
    }

    @Override
    public void receiveEvent(Event evt) {
        GameManager gm = GameManager.getInstance();
        if (type == BoardType.HAND) {
           if (evt instanceof OnDrawCard) {
               register(((OnDrawCard) evt).getSelectedCard());
           } else if (evt instanceof OnCardAction) {
               OnCardAction ec = (OnCardAction) evt;
               if (ec.getAction() == CardAction.PICK)
                   // Prereq: player has enough mana
                   unregister(ec.getFromCardIdx());
           }
        } else {
            if (evt instanceof OnCardAction) {
                OnCardAction ec = (OnCardAction) evt;
                Player p = gm.getCurrentPlayer();
                switch (ec.getAction()) {
                    case CHAR_ATTACK:
                    case ATTACK:
                        attack(ec);
                        break;
                    case PICK:
                        // Prereq: player has enough mana
                        CharacterCard cc = (CharacterCard) p.getHand().getCard(ec.getFromCardIdx());
                        SpawnedCard c = new SpawnedCard(cc);
                        register(c, ec.getToCardIdx());
                        p.setMana(p.getMana() - cc.getRequiredMana());
                        break;
                    case SPELL:
                        // Prereq: player has enough mana
                        SpellCard sc = (SpellCard) p.getHand().getCard(ec.getFromCardIdx());
                        p.getHand().unregister(ec.getFromCardIdx());
                        p.setMana(p.getMana() - sc.getRequiredMana());
                        //get mana + getreqmana - getmana dari lvl
                        Player target = gm.getPlayer(ec.getToPlayerIdx());
                        Board tgt_b = target.getBoard();
                        SpawnedCard sc_tgt = (SpawnedCard) tgt_b.getCard(ec.getToCardIdx());
                        switch (sc.getType()) {
                            case SWAP:
                            case PTN:
                                sc_tgt.addSpell(sc);
                                break;
                            case LVL:
                                LevelCard lv_sc = (LevelCard) sc;
                                p.setMana(p.getMana() - lv_sc.getRequiredMana(sc_tgt.getLevel()));
                                if (lv_sc.getLevelType() == Type.UP){
                                    sc_tgt.levelUp();
                                } else {
                                    sc_tgt.levelDown();
                                }
                                break;
                            case MORPH:
                                tgt_b.unregister(ec.getToCardIdx());
                                MorphCard mc = (MorphCard) sc;
                                tgt_b.register(
                                    new SpawnedCard((CharacterCard) gm.getCardById(mc.getTargetId())),
                                    ec.getToCardIdx()
                                );
                                break;
                            default:
                                break;
                        }
                        break;
                    // case CHAR_ATTACK:
                    //     SpawnedCard spawned = (SpawnedCard) p.getBoard().getCard(ec.getFromCardIdx());
                    //     gm.getOpponentPlayer().takeDamage(spawned.getATK());
                    //     break;
                }
            } else if (evt instanceof OnPhaseChange) {
                switch (((OnPhaseChange) evt).getPhase()) {
                    case DRAW:
                        notifyObjects();
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
