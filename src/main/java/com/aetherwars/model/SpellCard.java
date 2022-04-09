package com.aetherwars.model;

public class SpellCard extends Card {
    private int duration; // Permanent = 0 / Temporary = depends on dasta

    /**
     * User-defined constructor for spell card
     * @param id
     * @param name
     * @param type
     * @param description
     * @param image_path
     * @param mana
     * @param duration
     */
    public SpellCard(int id, String name, Type type, String description, String image_path, int mana, int duration) {
        super(id, name, type, description, mana, image_path);
        this.duration = duration;
    }

    public void use(){
        // TODO: harus diimplementasikan bareng" sama match nya
    }
}
