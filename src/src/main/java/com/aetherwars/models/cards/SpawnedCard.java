package com.aetherwars.models.cards;

import com.aetherwars.core.GameManager;
import com.aetherwars.models.Type;

import java.util.ArrayList;

// Using adapter with Character Card
public class SpawnedCard extends CharacterCard {
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

    public boolean canAttack() {
        return canAttack;
    }

    public int getAtkBuff() {
        int atk_buf = 0;
        for (SpellCard c: activeSpells) {
            if (c instanceof PotionCard) {
                atk_buf += ((PotionCard) c).getAtkBuff();
            }
        }
        return atk_buf;
    }

    public int getHpBuff() {
        int hp_buf = 0;
        for (SpellCard c: activeSpells) {
            if (c instanceof PotionCard) {
                hp_buf += ((PotionCard) c).getHpBuff();
            }
        }
        return hp_buf;
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

    public double getHP() {
        if (hasSwapEffect())
            return this.atk + this.getAtkBuff();
        else
            return this.hp + this.getHpBuff();
    }

    public double getATK(){
        if (hasSwapEffect())
            return this.hp + this.getHpBuff();
        else
            return this.atk + this.getAtkBuff();
    }

    public void takeDamage(double damage) {
        double take = 0, hp_buff;
        boolean hasSwap = hasSwapEffect();
        for (int i = 0; i < activeSpells.size(); i++) {
            SpellCard c = activeSpells.get(i);
            if (c instanceof PotionCard) {
                PotionCard pc = (PotionCard) c;
                if (hasSwap) {
                    hp_buff = pc.getAtkBuff();
                } else {
                    hp_buff = pc.getHpBuff();
                }
                if (hp_buff >= 0) {
                    take = Math.min(damage, hp_buff);
                    damage -= take;
                    take = hp_buff - take;
                    pc.setHpBuff(take);
                    if (take <= 0) {
                        activeSpells.remove(i);
                        i--;
                    }
                } else {
                    damage -= hp_buff;
                }
            }
        }
        if (damage <= 0)
            return;
        if (hasSwap)
            atk -= damage;
        else
            hp -= damage;
    }

    public void toggleAttack() {
        canAttack = !canAttack;
    }

    public void addExp(int exp){
        while (level < 10 && exp + this.exp >= this.getLevelUpExp()) {
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

    public void addSpell(SpellCard sc) {
        if (sc instanceof SwapCard) {
            for (SpellCard s: activeSpells) {
                if (s instanceof SwapCard) {
                    s.setDuration(s.getDuration() + sc.getDuration());
                    return;
                }
            }
        }
        activeSpells.add(sc);
    }

    /**
     * @brief atks the target character card by type
     * @param target target card
     */
    public void atk(SpawnedCard target) {
        if (target != null) {
            Type curType = this.type;
            Type tgtType = target.type;
            if (curType == Type.OVERWORLD && tgtType == Type.END ||
                    curType == Type.END && tgtType == Type.NETHER ||
                    curType == Type.NETHER && tgtType == Type.OVERWORLD) {
                target.takeDamage(2 * this.atk);
            } else if (curType == Type.OVERWORLD && tgtType == Type.NETHER ||
                    curType == Type.END && tgtType == Type.OVERWORLD ||
                    curType == Type.NETHER && tgtType == Type.END) {
                target.takeDamage(0.5 * this.atk);
            } else {
                target.takeDamage(this.atk);
            }

            if (target.getHP() <= 0) {
                this.addExp(target.getLevel());
                // TODO: delete target card after this
            }
        } else { // attack chara
            GameManager.getInstance().getOpponentPlayer().takeDamage(this.atk);
        }
    }

    public boolean hasSwapEffect() {
        boolean res = false;
        for (SpellCard s: activeSpells) {
            if (s instanceof SwapCard) {
                res = true;
            }
        }
        return res;
    }

    protected String ingfo() {
        boolean swap = hasSwapEffect();
        double hpBuff = swap ? getAtkBuff() : getHpBuff();
        double atkBuff = swap ? getHpBuff() : getAtkBuff();
        String before = String.format("ATK: %s\nHP: %s\n",
            String.format("%.2f", (swap ? this.hp : this.atk)) +
                (atkBuff != 0 ? " (" + (atkBuff > 0 ? "+":"") + atkBuff + ")" : ""),
            String.format("%.2f", (swap ? this.atk : this.hp)) +
                (hpBuff != 0 ? " (" + (hpBuff > 0 ? "+":"") + hpBuff + ")" : "")
        );
        return before + "Level: " + this.level + "\n" +
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
                i--;
            }
        }
    }
}
