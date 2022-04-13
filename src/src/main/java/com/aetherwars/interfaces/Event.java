package com.aetherwars.interfaces;

public class Event {
    private Object sender;

    public Event (Object sender) {
        this.sender = sender;
    }

    public Object getSender() {
        return sender;
    }
}
