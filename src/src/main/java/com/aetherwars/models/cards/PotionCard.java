package com.aetherwars.models.cards;

import com.aetherwars.models.Type;

/**
 * Implementation for PotionCard class
 * extens SpellCard class
 */
public class PotionCard extends SpellCard {
    private int atk_value = 0;
    private int hp_value = 0;

    public PotionCard(int id, String name, String description, String image_path, int atk_value, int hp_value, int mana, int duration) {
        super(id, name, Type.PTN, description, image_path, mana, duration);
        this.atk_value = atk_value;
        this.hp_value = hp_value;
    }

    public double getAtkBuff() {
        return atk_value;
    }

    public double getHpBuff() {
        return hp_value;
    }

    public void setHpBuff(int val) {
        hp_value = val;
    }

    public void setAtkBuff(int val) {
        atk_value = val;
    }

    public String infoAtk() {
        if (getAtkBuff() >= 0) {
            return "ATK: +" + getAtkBuff() +  "\n";
        }
        return "ATK: -" + getAtkBuff() +  "\n";
    }

    public String infoHp() {
        if (getHpBuff() >= 0) {
            return "HP: +" + getHpBuff() +  "\n";
        }
        return "HP: -" + getHpBuff() +  "\n";
    }

    @Override
    public String getInfo() {
        return super.getInfo() + infoAtk() + infoHp();
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
