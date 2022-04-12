package com.aetherwars.model.cards;



import java.io.File;


import com.aetherwars.model.Type;
import com.aetherwars.model.*;
import com.aetherwars.model.cards.CharacterCard;
import com.aetherwars.util.CSVReader;



public class MorphCard extends SpellCard{
    private int target_id = 0;

    public MorphCard(int id, String name, Type type, String description, String image_path, int mana, int target_id){
        super(id, name, type, description, image_path, mana, 0);
        this.target_id = target_id;
    }

    public void use(CharacterCard target){
        // File characterCSVFile = new File(getClass().getResource("card/data/character.csv").toURI());
        // CSVReader characterReader = new CSVReader(characterCSVFile, "\t");
        // characterReader.setSkipHeader(true);
        // List<String[]> characterRows = characterReader.read();

    }
}
