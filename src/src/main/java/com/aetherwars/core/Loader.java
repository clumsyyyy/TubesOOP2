package com.aetherwars.core;

import com.aetherwars.models.Type;
import com.aetherwars.models.cards.*;
import com.aetherwars.util.CSVReader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Loader {
    private static final String CHARACTER_FPATH = "../card/data/character.csv";
    private static final String MORPH_FPATH = "../card/data/spell_morph.csv";
    private static final String PTN_FPATH = "../card/data/spell_ptn.csv";
    private static final String SWAP_FPATH = "../card/data/spell_swap.csv";
    
    private static final ArrayList<Card> CardList = new ArrayList<>();

    public static ArrayList<Card> loadCards() throws IOException, URISyntaxException {
        File characterCSVFile = new File(Loader.class.getResource(CHARACTER_FPATH).toURI());
        CSVReader characterReader = new CSVReader(characterCSVFile, "\t");
        characterReader.setSkipHeader(true);
        List<String[]> characterRows = characterReader.read();
        for (String[] row : characterRows) {
            CharacterCard c = new CharacterCard(
                Integer.parseInt(row[0]), 
                row[1], 
                Type.valueOf(row[2]),
                row[3], 
                row[4], 
                Integer.parseInt(row[7]), 
                Double.parseDouble(row[5]),
                Double.parseDouble(row[6]), 
                Double.parseDouble(row[8]), 
                Double.parseDouble(row[9])
            );
            CardList.add(c);
            System.out.println(c);
        }

        File morphCSVFile = new File(Loader.class.getResource(MORPH_FPATH).toURI());
        CSVReader morphReader = new CSVReader(morphCSVFile, "\t");
        morphReader.setSkipHeader(true);
        List<String[]> morphRows = morphReader.read();
        for (String[] row : morphRows){
            MorphCard m = new MorphCard(
                Integer.parseInt(row[0]), 
                row[1],
                row[2], 
                row[3], 
                Integer.parseInt(row[5]), 
                Integer.parseInt(row[4])
            );
            CardList.add(m);
        }

        File potionCSVFile = new File(Loader.class.getResource(PTN_FPATH).toURI());
        CSVReader potionReader = new CSVReader(potionCSVFile, "\t");
        potionReader.setSkipHeader(true);
        List<String[]>potionRows = potionReader.read();
        for (String[] row : potionRows){
            PotionCard p = new PotionCard(
                Integer.parseInt(row[0]), 
                row[1],
                row[2], 
                row[3], 
                Double.parseDouble(row[4]), 
                Double.parseDouble(row[5]),
                Integer.parseInt(row[6]), 
                Integer.parseInt(row[7])
            );
            CardList.add(p);
        }


        File swapCSVFile = new File(Loader.class.getResource(SWAP_FPATH).toURI());
        CSVReader swapReader = new CSVReader(swapCSVFile, "\t");
        swapReader.setSkipHeader(true);
        List<String[]> swapRows = swapReader.read();
        for (String[] row : swapRows){
            SwapCard s = new SwapCard(
                Integer.parseInt(row[0]), 
                row[1],
                row[2], 
                row[3], 
                Integer.parseInt(row[5]), 
                Integer.parseInt(row[4])
            );
            CardList.add(s);
        }
        LevelCard lv_up = new LevelCard(
            401, 
            "Level Up", 
            "Levels up your/enemy card",
            "card/image/spell/level/level_up.png",
            Type.UP);
        CardList.add(lv_up);
        LevelCard lv_down = new LevelCard(
            402, 
            "Level Down", 
            "Levels down your/enemy card",
            "card/image/spell/level/level_down.png",
            Type.DOWN);
        CardList.add(lv_down);

        for (int i = 1; i < 11; i++){
            HealCard heal = new HealCard(
                500 + i,
                "Heal - " + i,
                "Heals yourself by " + i + " points",
                "card/image/spell/heal/heal" + i + ".png",
                i,
                (double) i
            );
            CardList.add(heal);
        }
        return CardList;
    }

}
