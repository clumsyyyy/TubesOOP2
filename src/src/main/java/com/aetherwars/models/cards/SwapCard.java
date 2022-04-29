package com.aetherwars.models.cards;

import com.aetherwars.core.GameManager;
import com.aetherwars.events.OnCardAction;
import com.aetherwars.models.Type;

/**
 * Implementation for the swap card class
 */
public class SwapCard extends SpellCard {
    public SwapCard(
        int id,
        String name,
        String description,
        String image_path,
        int mana,
        int duration
    ) {
        super(id, name, Type.SWAP, description, image_path, mana, duration);
    }

    @Override
    public Card cloneCard() {
        return new SwapCard(
            this.id,
            this.name,
            this.desc,
            this.image_path,
            this.required_mana,
            this.init_duration
        );
    }
    
    @Override
    public void action(OnCardAction ec) {
        super.action(ec);
        ((SpawnedCard)GameManager.getInstance()
            .getPlayer(ec.getToPlayerIdx())
            .getBoard().getCard(ec.getToCardIdx())
        ).addSpell(this);
    }
}
