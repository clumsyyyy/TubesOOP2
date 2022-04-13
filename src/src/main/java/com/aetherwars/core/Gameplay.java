package com.aetherwars.core;

import com.aetherwars.interfaces.Event;
import com.aetherwars.events.*;
import com.aetherwars.interfaces.Subscriber;
import com.aetherwars.models.Phase;

public class Gameplay implements Subscriber {
    GameManager gm;
    public Gameplay () {
        gm = GameManager.getInstance();
    }

    public void attack (int idx_card1, int idx_card2) {
        gm.sendEvent(new OnAttack(
            this,
            idx_card1,
            idx_card2
        ));
    }

    public void attackCharacter (int idx_card) {
        gm.sendEvent(new OnAttack(
            this,
            idx_card
        ));
    }

    @Override
    public void receiveEvent(Event evt) {
        if (evt instanceof OnCardHovered) {
            // show info by getting getCard(), or
            // subscribe to add OnCardHovered
            // ((OnCardHovered)evt).getCard();
        } else if (evt instanceof OnPhaseChange) {
            Phase p = ((OnPhaseChange) evt).getPhase();
            if (p == Phase.END) {

            }
        }
    }
}
