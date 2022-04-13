package com.aetherwars.models.cards;

import com.aetherwars.interfaces.Prototype;
import com.aetherwars.models.Type;

/**
 * Implementation for PotionCard class
 * extens SpellCard class
 */
public class PotionCard extends SpellCard implements Prototype<Card> {
    private int atk_value = 0;
    private int hp_value = 0;
    private CharacterCard target;
    public PotionCard(int id, String name, String description, String image_path, int atk_value, int hp_value, int mana, int duration) {
        super(id, name, Type.PTN, description, image_path, mana, duration);
        this.atk_value = atk_value;
        this.hp_value = hp_value;
    }

    public void use(CharacterCard target) {
        this.target = target;
        this.target.setATK(target.getATK() + this.atk_value);
        this.target.setHP(target.getHP() + this.hp_value);
    }

    @Override
    public void update() {
        this.current_duration--;
        if (this.current_duration == 0){
            // reset value to base
            this.target.reset();
        }
    }

    @Override
    public Card clone() {
        return new PotionCard(
            this.id,
            this.name,
            this.desc,
            this.image_path,
            this.atk_value,
            this.hp_value,
            this.required_mana,
            this.init_duration
        );
    }
}
