package com.aetherwars.model.cards;

import com.aetherwars.model.Type;

/**
 * Implementation for PotionCard class
 * extens SpellCard class
 */
public class PotionCard extends SpellCard {
    private int atk_value = 0;
    private int hp_value = 0;
    private CharacterCard target;
    public PotionCard(int id, String name, Type type, String description, String image_path, int atk_value, int hp_value, int mana, int duration) {
        super(id, name, type, description, image_path, mana, duration);
        this.atk_value = atk_value;
        this.hp_value = hp_value;
    }

    public void use(CharacterCard target) {
        this.target = target;
        this.target.setATK(target.getATK() + this.atk_value);
        this.target.setHP(target.getHP() + this.hp_value);
    }

    public void update(){
        this.duration--;
        if (this.duration == 0){
            // reset value to base
            this.target.reset();
        }
    }
}
