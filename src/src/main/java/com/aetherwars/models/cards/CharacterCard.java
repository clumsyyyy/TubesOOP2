package com.aetherwars.models.cards;

import com.aetherwars.interfaces.Prototype;
import com.aetherwars.models.Type;

/**
 * Implementation file for character card
 * base_atk and base_hp used to keep early base values
 * to be used whenever reset
 * Leveling up and using potion will update the atk/hp value
 */
public class CharacterCard extends Card implements Prototype<Card> {
    private double base_atk;
    private double base_hp;
    private double atk;
    private double hp;
    private double atk_up;
    private double hp_up;
    private int level;
    private int exp;

    /**
     * @brief Default constructor for card
     */

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
        this.level = 1;
        this.exp = 0;
    }

    @Override
    public String toString() {
        return super.toString() +
                "atk: " + this.atk + "\n" +
                "hp: " + this.hp + "\n";
    }

    public void setLevel(int level){
        this.level = level;
    }

    public int getLevel(){
        return this.level;
    }

    public void setExp(int exp){
        this.exp = exp;
    }

    public int getExp(){
        return this.exp;
    }


    public void setHP(double hp) {
        this.hp = (hp > 0 ? hp : 0);
    }

    public double getHP() {
        return this.hp;
    }

    public void setATK(double atk) {
        this.atk = (atk > 0 ? atk : 0);
    }

    public double getATK(){
        return this.atk;
    }

    public void reset(){
        this.atk = this.base_atk;
        this.hp = this.base_hp;
    }

    /**
     * TODO: implementasinya masih kurang jelas, lagi ditanyain di QnA
     * @param exp
     */
    public void addExp(int exp){
        this.exp += exp;
    }

    public void levelUp(){
        if (this.level < 10) {
            this.atk += this.atk_up;
            this.hp += this.hp_up;
            this.level++;
        }
    }

    /**
     * @brief atks the target character card by type
     * @param target target card
     */
    public void atk(CharacterCard target) {
        if (this.type == Type.OVERWORLD && target.type == Type.END ||
                this.type == Type.END && target.type == Type.NETHER ||
                this.type == Type.NETHER && target.type == Type.OVERWORLD) {
            target.setHP(target.getHP() - 2 * this.atk);
        }
        else if (this.type == Type.OVERWORLD && target.type == Type.NETHER ||
                this.type == Type.END && target.type == Type.OVERWORLD ||
                this.type == Type.NETHER && target.type == Type.END) {
            target.setHP(target.getHP() - 0.5 * this.atk);
        }
        else {
            target.setHP(target.getHP() - this.atk);
        }

        if (target.getHP() <= 0) {
            this.addExp(target.getLevel());
        }
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
