package com.aetherwars.model.cards;

import com.aetherwars.model.Type;

/**
 * Implementatino for the spell card class
 */
public class SpellCard extends Card {
    protected int duration; // Permanent = 0 / Temporary = depends on dasta

    /**
     * User-defined constructor for spell card
     * @param id
     * @param name
     * @param type
     * @param description
     * @param image_path
     * @param mana
     * @param duration, 0 if effect's permanent
     */
    public SpellCard(int id, String name, Type type, String description, String image_path, int mana, int duration) {
        super(id, name, type, description, mana, image_path);
        this.duration = duration;
    }

    public void setDuration(int duration){
        this.duration = duration;
    }

    public int getDuration(){
        return this.duration;
    }
}
