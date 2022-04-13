package com.aetherwars.models.cards;



import com.aetherwars.interfaces.Prototype;
import com.aetherwars.models.Type;


public class MorphCard extends SpellCard implements Prototype<Card> {
    private int target_id = 0;

    public MorphCard(int id, String name, String description, String image_path, int mana, int target_id){
        super(id, name, Type.MORPH, description, image_path, mana, 0);
        this.target_id = target_id;
    }

    public void use(CharacterCard target){
        // File characterCSVFile = new File(getClass().getResource("card/data/character.csv").toURI());
        // CSVReader characterReader = new CSVReader(characterCSVFile, "\t");
        // characterReader.setSkipHeader(true);
        // List<String[]> characterRows = characterReader.read();
    }

    @Override
    public Card clone() {
        return new MorphCard(
            this.id,
            this.name,
            this.desc,
            this.image_path,
            this.required_mana,
            this.target_id
        );
    }
}
