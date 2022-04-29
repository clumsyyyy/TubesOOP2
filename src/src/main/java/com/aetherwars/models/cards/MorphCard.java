package com.aetherwars.models.cards;


import com.aetherwars.core.GameManager;
import com.aetherwars.events.OnCardAction;
import com.aetherwars.models.Board;
import com.aetherwars.models.Type;


public class MorphCard extends SpellCard {
    private int target_id = 0;

    public MorphCard(int id, String name, String description, String image_path, int mana, int target_id){
        super(id, name, Type.MORPH, description, image_path, mana, 0);
        this.target_id = target_id;
    }

    public int getTargetId() {
        return target_id;
    }

    @Override
    public Card cloneCard() {
        return new MorphCard(
            this.id,
            this.name,
            this.desc,
            this.image_path,
            this.required_mana,
            this.target_id
        );
    }

    @Override
    public void action(OnCardAction ec) {
        super.action(ec);
        GameManager gm = GameManager.getInstance();
        Board tgt_b = gm.getPlayer(ec.getToPlayerIdx()).getBoard();
        tgt_b.unregister(ec.getToCardIdx());
        tgt_b.register(
            new SpawnedCard((CharacterCard) gm.getCardById(this.getTargetId())),
            ec.getToCardIdx()
        );
    }
}
