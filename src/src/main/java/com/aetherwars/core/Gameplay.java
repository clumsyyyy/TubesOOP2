package com.aetherwars.core;

import com.aetherwars.interfaces.Event;
import com.aetherwars.events.*;
import com.aetherwars.interfaces.Subscriber;

public class Gameplay implements Subscriber {
    @Override
    public void receiveEvent(Event evt) {
        if (evt instanceof OnCardHovered) {
            ((OnCardHovered)evt).getCard();
        }
    }
}
