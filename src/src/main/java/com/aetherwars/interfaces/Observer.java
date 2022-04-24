package com.aetherwars.interfaces;

public interface Observer<T extends Observable> {
    void register(T obj, int index);
    void register(T obj);
    void unregister(T obj);
    void unregister(int index);
    void notifyObjects();
}

