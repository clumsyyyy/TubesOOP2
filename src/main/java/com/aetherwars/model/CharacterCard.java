package com.aetherwars.model;

/**
 * Implementation file for character card
 */
public class CharacterCard extends Card {
  private double base_atk;
  private double base_hp;
  private double attack;
  private double health;
  private double attack_up;
  private double health_up;
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
                       int mana, double attack, double health, double attack_up, double health_up){
    super(id, name, type, description, mana, image_path);
    this.base_atk = attack;
    this.base_hp = attack;
    this.attack = attack;
    this.health = health;
    this.attack_up = attack_up;
    this.health_up = health_up;
    this.level = 1;
    this.exp = 0;
  }

  @Override
  public String toString() {
    return super.toString() +
            "Attack: " + this.attack + "\n" +
            "Health: " + this.health + "\n";
  }

  public int getLevel(){
    return this.level;
  }

  public void setHealth(double health) {
    this.health = (health > 0 ? health : 0);
  }

  public double getHealth() {
    return this.health;
  }

  /**
   * TODO: implementasinya masih kurang jelas,
   * lagi ditanyain di QnA
   * @param exp
   */
  public void addExp(int exp){
    this.exp += exp;
  }

  public void levelUp(){
    if (this.level < 10) {
      this.attack += this.attack_up;
      this.health += this.health_up;
      this.level++;
    }
  }

  /**
   * @brief Attacks the target character card by type
   * @param target target card
   */
  public void attack(CharacterCard target) {
    if (this.type == Type.OVERWORLD && target.type == Type.END ||
            this.type == Type.END && target.type == Type.NETHER ||
            this.type == Type.NETHER && target.type == Type.OVERWORLD) {
      target.setHealth(target.getHealth() - 2 * this.attack);
    }
    else if (this.type == Type.OVERWORLD && target.type == Type.NETHER ||
            this.type == Type.END && target.type == Type.OVERWORLD ||
            this.type == Type.NETHER && target.type == Type.END) {
      target.setHealth(target.getHealth() - 0.5 * this.attack);
    }
    else {
      target.setHealth(target.getHealth() - this.attack);
    }

    if (target.getHealth() <= 0) {
      this.addExp(target.getLevel());
    }
  }
}
