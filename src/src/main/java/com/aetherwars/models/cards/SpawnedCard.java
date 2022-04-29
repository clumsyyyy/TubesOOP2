package com.aetherwars.models.cards;

import com.aetherwars.core.GameManager;
import com.aetherwars.events.OnCardAction;
import com.aetherwars.models.Type;

import java.util.ArrayList;

// Using adapter with Character Card
public class SpawnedCard extends CharacterCard {
    private final ArrayList<SpellCard> activeSpells;
    private int level;
    private int exp;
    private int swap_duration;
    private boolean is_swapped;
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
        this.is_swapped = false;
        this.swap_duration = 0;
        this.activeSpells = new ArrayList<>();
    }

    public boolean canAttack() {
        return canAttack;
    }

    public double getAtkBuff() {
        double atk_buf = 0;
        for (SpellCard c: activeSpells) {
            if (c instanceof PotionCard) {
                atk_buf += ((PotionCard) c).getAtkBuff();
            }
        }
        return atk_buf;
    }

    public double getHpBuff() {
        double hp_buf = 0;
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
        return this.hp + this.getHpBuff();
    }

    public double getATK(){
        double res = this.atk + this.getAtkBuff();
        return res < 0 ? 0 : res;
    }

    public void takeDamage(double damage) {
        double take = 0, hp_buff;
        for (int i = 0; i < activeSpells.size(); i++) {
            SpellCard c = activeSpells.get(i);
            if (c instanceof PotionCard) {
                PotionCard pc = (PotionCard) c;
                hp_buff = pc.getHpBuff();
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
        this.hp -= damage;
    }

    public void toggleAttack() {
        canAttack = !canAttack;
    }

    public void addExp(int exp){
        while (level < 10 && exp + this.exp >= this.getLevelUpExp()) {
            exp -= this.getLevelUpExp() - this.exp;
            this.levelUp();
        }
        if (level < 10) {
            this.exp += exp;
        }

    }

    public void levelUp(){
        if (this.level < 10) {
            this.exp = 0;
            this.base_atk += this.atk_up;
            this.base_hp += this.hp_up;
            this.atk = this.base_atk;
            this.level++;
        }
        this.hp = this.base_hp;
    }

    public void levelDown(){
        if (this.level > 1){
            this.exp = 0;
            this.base_atk -= this.atk_up;
            this.base_hp -= this.hp_up;
            this.atk = this.base_atk;
            if (this.hp > this.base_hp)
                this.hp = this.base_hp;
            this.level--;
        }
    }

    public void swapAtkHp() {
        this.is_swapped = !this.is_swapped;
        double temp = this.atk;
        this.atk = this.hp;
        this.hp = temp;
        for (SpellCard c: activeSpells) {
            if (c instanceof PotionCard) {
                PotionCard pc = (PotionCard) c;
                temp = pc.getAtkBuff();
                pc.setAtkBuff(pc.getHpBuff());
                pc.setHpBuff(temp);
            }
        }
    }

    public void addSpell(SpellCard sc) {
        if (sc instanceof SwapCard) {
            if (!is_swapped) {
                // swap atk and hp
                this.swapAtkHp();
            }
            this.swap_duration = sc.getDuration();
            for (SpellCard s: activeSpells) {
                if (s instanceof SwapCard) {
                    this.swap_duration = s.getDuration() + sc.getDuration();
                    s.setDuration(s.getDuration() + sc.getDuration());
                    return;
                }
            }
        } 
        activeSpells.add(sc);
        if (this.getHP() <= 0){
            GameManager.getInstance().getCurrentPlayer().getBoard().unregister(this);
        }
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
                target.takeDamage(2 * this.getATK());
            } else if (curType == Type.OVERWORLD && tgtType == Type.NETHER ||
                    curType == Type.END && tgtType == Type.OVERWORLD ||
                    curType == Type.NETHER && tgtType == Type.END) {
                target.takeDamage(0.5 * this.getATK());
            } else {
                target.takeDamage(this.getATK());
            }
        } else { // attack chara
            GameManager.getInstance().getOpponentPlayer().takeDamage(this.getATK());
        }
    }

    public boolean hasSwapEffect() {
        for (SpellCard s: activeSpells) {
            if (s instanceof SwapCard) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected String ingfo() {
        double hpBuff = getHpBuff();
        double atkBuff = getAtkBuff();
        String before = String.format("ATK: %s\nHP: %s\n",
            String.format("%.2f", this.atk) +
                (atkBuff != 0 ? " (" + (atkBuff > 0 ? "+":"") + atkBuff + ")" : ""),
            String.format("%.2f", this.hp) +
                (hpBuff != 0 ? " (" + (hpBuff > 0 ? "+":"") + hpBuff + ")" : "")
        );
        return before + "Level: " + this.level + "\n" +
                "Exp: " + (this.level < 10 ? this.exp : "MAX") +
                          (this.level < 10 ? "/" + this.getLevelUpExp() : "") + "\n" + 
                (this.swap_duration > 0 ? "Swap duration: " + this.swap_duration + "\n" : "");
    }

    public String toString() {
        return super.toString();
    }

    public String getInfo() {
        return super.getInfo();
    }

    @Override
    public void update() {
        if (!canAttack)
            toggleAttack();
        for (int i = 0; i < activeSpells.size(); i++) {
            activeSpells.get(i).update();
            if (activeSpells.get(i) instanceof SwapCard) {
                this.swap_duration = activeSpells.get(i).current_duration;
            }
            if (activeSpells.get(i).current_duration == 0) {
                activeSpells.remove(i);
                i--;
            }

        }
        if (is_swapped && this.swap_duration == 0) {
            this.swapAtkHp();
        }
    }

    @Override
    public void action(OnCardAction ec) {
        if (ec.getToCardIdx() == -1) {
            if (this.canAttack()) {
                // attack character directly
                this.atk(null);
                this.toggleAttack();
            }
        } else {
            GameManager gm = GameManager.getInstance();
            // use attack function on CharacterCard
            Card target_att = gm.getOpponentPlayer().getBoard().getCard(ec.getToCardIdx());
            if (target_att instanceof SpawnedCard) {
                SpawnedCard tg_att = (SpawnedCard) target_att;
                if (this.canAttack()) {
                    this.atk(tg_att);
                    tg_att.atk(this);
                    if (tg_att.getHP() <= 0) {
                        gm.getOpponentPlayer().getBoard().unregister(tg_att);
                        if (this.getHP() <= 0) {
                            this.addExp(tg_att.getLevel());
                        }
                    }
                    if (this.getHP() <= 0) {
                        gm.getCurrentPlayer().getBoard().unregister(this);
                    }
                    this.toggleAttack();
                }
            }
        }
    }
}
