package com.aetherwars.events;

import com.aetherwars.controllers.Player;
import com.aetherwars.interfaces.Event;
import com.aetherwars.models.cards.Card;

public class OnAttack extends Event {
    private Player pAtt;
    private Card cAtt;
    private Player pTgt;
    private Card cTgt;

    public OnAttack(Object sender, Player pAtt, Card cAtt, Player pTgt, Card cTgt) {
        super(sender);
        this.pAtt = pAtt;
        this.cAtt = cAtt;
        this.pTgt = pTgt;
        this.cTgt = cTgt;
    }

    public Player getAttackerPlayer() {
        return this.pAtt;
    }

    public Card getAttackerCard() {
        return this.cAtt;
    }

    public Player getTargetPlayer() {
        return this.pTgt;
    }

    public Card getTargetCard() {
        return this.cTgt;
    }

}
