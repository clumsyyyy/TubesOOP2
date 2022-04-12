package com.aetherwars.model.cards;

import com.aetherwars.model.Type;

/**
 * Implementation for the swap card class
 */
public class SwapCard extends SpellCard{
    public SwapCard(int id, String name, Type type, String description, String image_path, int mana, int duration) {
        super(id, name, type, description, image_path, mana, duration);
    }
}
