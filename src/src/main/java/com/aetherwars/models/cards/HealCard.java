package com.aetherwars.models.cards;

import com.aetherwars.core.GameManager;
import com.aetherwars.events.OnCardAction;
import com.aetherwars.models.Type;

public class HealCard extends SpellCard {
    private double heal_value = 0;

    public HealCard(int id, String name, String description, String image_path, int mana, double heal_value){
        super(id, name, Type.HEAL, description, image_path, mana, 0);
        this.heal_value = heal_value;
    }

    public double getHeal() {
        return heal_value;
    }

    @Override
    public Card cloneCard() {
        return new HealCard(
            this.id,
            this.name,
            this.desc,
            this.image_path,
            this.required_mana,
            this.heal_value
        );
    }

    @Override
    public void action(OnCardAction onActionEvent) {
        super.action(onActionEvent);
        GameManager.getInstance().getPlayer(onActionEvent.getToPlayerIdx()).heal(this.getHeal());
    }
}
