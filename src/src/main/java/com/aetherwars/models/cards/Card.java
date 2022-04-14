package com.aetherwars.models.cards;

import com.aetherwars.interfaces.Informable;
import com.aetherwars.interfaces.Observable;
import com.aetherwars.interfaces.Prototype;
import com.aetherwars.models.Type;

/**
 * Implementation for Card parent class
 */
public class Card implements Prototype<Card>, Observable, Informable {
    protected int id;
    protected String name;
    protected String desc;
    protected Type type;
    protected int required_mana;
    protected String image_path;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public Type getType() {
        return type;
    }

    public int getRequiredMana() {
        return required_mana;
    }

    public String getImagePath() {
        return image_path;
    }

    /**
     * User-defined constructor for card
     * @param name
     * @param type
     * @param desc
     * @param required_mana
     * @param img_path
     */
    public Card(int id, String name, Type type, String desc, int required_mana, String img_path) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.desc = desc;
        this.required_mana = required_mana;
        this.image_path = img_path;
    }

    public String toString() {
        return "ID: " + this.id + "\n" +
                "Name: " + this.name + "\n" +
                "Description: " + this.desc + "\n" +
                getInfo();
    }

    public String getInfo() {
        return "Type: " + this.type + "\n" +
                "Required Mana: " + this.required_mana + "\n";
    }

    @Override
    public Card clone() {
        return new Card(
            this.id,
            this.name,
            this.type,
            this.desc,
            this.required_mana,
            this.image_path
        );
    }

    @Override
    public void update() {
        // do nothing
    }
}
