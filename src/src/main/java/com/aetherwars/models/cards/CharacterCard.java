package com.aetherwars.models.cards;

import com.aetherwars.models.Type;

/**
 * Implementation file for character card
 * base_atk and base_hp used to keep early base values
 * to be used whenever reset
 * Leveling up and using potion will update the atk/hp value
 */
public class CharacterCard extends Card {
    protected double base_atk;
    protected double base_hp;
    protected double atk;
    protected double hp;
    protected double atk_up;
    protected double hp_up;

    /**
     * @brief User-defined constructor for card, used during parsing
     * (check util/CardParser.java)
     * @param name card name
     * @param description card description
     * @param type card type
     */
    public CharacterCard(int id, String name, Type type, String description, String image_path,
                         int mana, double atk, double hp, double atk_up, double hp_up){
        super(id, name, type, description, mana, image_path);
        this.base_atk = atk;
        this.base_hp = atk;
        this.atk = atk;
        this.hp = hp;
        this.atk_up = atk_up;
        this.hp_up = hp_up;
    }

    public double getAtkUp() {
        return atk_up;
    }

    public double getHpUp() {
        return hp_up;
    }

    protected String ingfo() {
        return "ATK: " + this.atk + "\n" +
                "HP: " + this.hp + "\n";
    }

    @Override
    public String toString() {
        return super.toString() + ingfo();
    }

    public String getInfo() {
        return super.getInfo() + ingfo();
    }

    @Override
    public Card clone() {
        return new CharacterCard(
                this.id,
                this.name,
                this.type,
                this.desc,
                this.image_path,
                this.required_mana,
                this.atk,
                this.hp,
                this.atk_up,
                this.hp_up
        );
    }
}
