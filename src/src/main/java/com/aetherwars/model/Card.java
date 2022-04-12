package com.aetherwars.model;

/**
 * Implementation for Card parent class
 */
public class Card {
    protected int id;
    protected String name;
    protected String desc;
    protected Type type;
    protected int required_mana;
    protected String image_path;

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
                "Type: " + this.type + "\n" +
                "Description: " + this.desc + "\n" +
                "Required Mana: " + this.required_mana + "\n" +
                "Image Path: " + this.image_path + "\n";
    }
}
