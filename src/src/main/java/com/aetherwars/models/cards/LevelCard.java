package com.aetherwars.models.cards;

import com.aetherwars.models.Type;
import java.lang.Math;

public class LevelCard extends SpellCard {
    private Type level_type = null;

    public LevelCard(int id, String name, String description, String image_path, Type type){
        super(id, name, Type.LVL, description, image_path, 0, 0);
        this.level_type = type;
    }

    public Type getLevelType() {
        return level_type;
    }

    @Override
    public Card clone() {
        return new LevelCard(
            this.id,
            this.name,
            this.desc,
            this.image_path,
            this.level_type
        );
    }

    public int getRequiredMana(int lvl) {
        return (int) Math.ceil(((double)lvl) / 2.0);
    }
}
