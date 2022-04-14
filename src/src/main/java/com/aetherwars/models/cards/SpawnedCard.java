package com.aetherwars.models.cards;

import com.aetherwars.core.GameManager;
import com.aetherwars.interfaces.Informable;
import com.aetherwars.interfaces.Observable;
import com.aetherwars.models.Type;

import java.util.ArrayList;

// Using adapter with Character Card
public class SpawnedCard extends CharacterCard implements Observable, Informable {
    private final ArrayList<SpellCard> activeSpells;
    private int level;
    private int exp;
    private boolean canAttack;

    public SpawnedCard(CharacterCard card) {
        super(
            card.id, card.name, card.type, card.desc,
            card.image_path, card.required_mana, card.atk,
            card.hp, card.atk_up, card.hp_up
        );
        this.level = 1;
        this.exp = 0;
        this.canAttack = true;
        this.activeSpells = new ArrayList<>();
    }

    public void setLevel(int level){
        this.level = level;
    }

    public int getLevel(){
        return this.level;
    }

    public int getLevelUpExp() {
        return this.level * 2 - 1;
    }

    public int getExp(){
        return this.exp;
    }

    public void toggleAttack() {
        canAttack = !canAttack;
    }

    public void addExp(int exp){
        while (level < 10 && exp + this.exp > this.getLevelUpExp()) {
            exp -= this.getLevelUpExp() - this.exp;
            this.levelUp();
        }
        this.exp += exp;
    }

    public void levelUp(){
        this.exp = 0;
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
    public void atk(SpawnedCard target) {
        if (canAttack) {
            if (target != null) {
                Type curType = this.type;
                Type tgtType = target.type;
                if (curType == Type.OVERWORLD && tgtType == Type.END ||
                        curType == Type.END && tgtType == Type.NETHER ||
                        curType == Type.NETHER && tgtType == Type.OVERWORLD) {
                    target.setHP(target.getHP() - 2 * this.atk);
                } else if (curType == Type.OVERWORLD && tgtType == Type.NETHER ||
                        curType == Type.END && tgtType == Type.OVERWORLD ||
                        curType == Type.NETHER && tgtType == Type.END) {
                    target.setHP(target.getHP() - 0.5 * this.atk);
                } else {
                    target.setHP(target.getHP() - this.atk);
                }

                if (target.getHP() <= 0) {
                    this.addExp(target.getLevel());
                    // TODO: delete target card next round.
                }
            } else { // attack chara
                GameManager.getInstance().getOpponentPlayer().takeDamage(this.atk);
            }
            toggleAttack();
        }
    }

    private String ingfo() {
        return "Level: " + this.level + "\n" +
                "Exp: " + (this.level < 10 ? this.exp : "MAX") +
                          (this.level < 10 ? "/" + this.getLevelUpExp() : "") + "\n";
    }

    public String toString() {
        return super.toString() + ingfo();
    }

    public String getInfo() {
        return super.getInfo() + ingfo();
    }

    @Override
    public void update() {
        if (!canAttack)
            toggleAttack();
        for (int i = 0; i < activeSpells.size(); i++) {
            activeSpells.get(i).update();
            if (activeSpells.get(i).current_duration == 0) {
                activeSpells.remove(i);
            }
        }
    }
}
