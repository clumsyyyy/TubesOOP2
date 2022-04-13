package com.aetherwars.models.cards;

import com.aetherwars.interfaces.Prototype;
import com.aetherwars.models.Type;

/**
 * Implementation for the swap card class
 */
public class SwapCard extends SpellCard implements Prototype<Card> {
    public SwapCard(
        int id,
        String name,
        String description,
        String image_path,
        int mana,
        int duration
    ) {
        super(id, name, Type.SWAP, description, image_path, mana, duration);
    }

    @Override
    public Card clone() {
        return new SwapCard(
            this.id,
            this.name,
            this.desc,
            this.image_path,
            this.required_mana,
            this.init_duration
        );
    }
}
