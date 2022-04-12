package com.aetherwars;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import com.aetherwars.model.*;
import com.aetherwars.model.cards.*;
import com.aetherwars.util.CSVReader;

public class AetherWars extends Application {
  private static final String CHARACTER_FPATH = "card/data/character.csv";
  private static final String MORPH_FPATH = "card/data/spell_morph.csv";
  private static final String PTN_FPATH = "card/data/spell_ptn.csv";
  private static final String SWAP_FPATH = "Card/data/spell_swap.csv";


  private ArrayList<CharacterCard> CharacterList = new ArrayList<CharacterCard>();
  private ArrayList<MorphCard> MorphList = new ArrayList<MorphCard>();
  private ArrayList<PotionCard> PotionList = new ArrayList<PotionCard>();
  private ArrayList<SwapCard> SwapList = new ArrayList<SwapCard>();


  public void loadCards() throws IOException, URISyntaxException {
    File characterCSVFile = new File(getClass().getResource(CHARACTER_FPATH).toURI());
    CSVReader characterReader = new CSVReader(characterCSVFile, "\t");
    characterReader.setSkipHeader(true);
    List<String[]> characterRows = characterReader.read();
    for (String[] row : characterRows) {
      CharacterCard c = new CharacterCard(Integer.parseInt(row[0]), row[1], Type.valueOf(row[2]),
      row[3], row[4], Integer.parseInt(row[5]), Double.parseDouble(row[6]),
      Double.parseDouble(row[7]), Double.parseDouble(row[8]), Double.parseDouble(row[9]));
      CharacterList.add(c);
    }

    File morphCSVFile = new File(getClass().getResource(MORPH_FPATH).toURI());
    CSVReader morphReader = new CSVReader(morphCSVFile, "\t");
    morphReader.setSkipHeader(true);
    List<String[]> morphRows = morphReader.read();
    for (String[] row : morphRows){
      MorphCard m = new MorphCard(Integer.parseInt(row[0]), row[1], Type.MORPH,
      row[2], row[3], Integer.parseInt(row[5]), Integer.parseInt(row[4]));
      MorphList.add(m);
    }

    File potionCSVFile = new File(getClass().getResource(PTN_FPATH).toURI());
    CSVReader potionReader = new CSVReader(potionCSVFile, "\t");
    potionReader.setSkipHeader(true);
    List<String[]>potionRows = potionReader.read();
    for (String[] row : potionRows){
      PotionCard p = new PotionCard(Integer.parseInt(row[0]), row[1], Type.PTN,
      row[2], row[3], Integer.parseInt(row[4]), Integer.parseInt(row[5]),
      Integer.parseInt(row[6]), Integer.parseInt(row[7]));
      PotionList.add(p);
    }


    File swapCSVFile = new File(getClass().getResource(SWAP_FPATH).toURI());
    CSVReader swapReader = new CSVReader(swapCSVFile, "\t");
    swapReader.setSkipHeader(true);
    List<String[]> swapRows = swapReader.read();
    for (String[] row : swapRows){
      SwapCard s = new SwapCard(Integer.parseInt(row[0]), row[1], Type.SWAP,
      row[2], row[3], Integer.parseInt(row[5]), Integer.parseInt(row[4]));
      SwapList.add(s);
    }
  
  }

  @Override
  public void start(Stage stage) {
    Text text = new Text();
    text.setText("Loading...");
    text.setX(50);
    text.setY(50);

    Group root = new Group();
    root.getChildren().add(text);

    Scene scene = new Scene(root, 1280, 720);

    stage.setTitle("Minecraft: Aether Wars");
    stage.setScene(scene);
    stage.show();

    try {
      this.loadCards();
      text.setText("Minecraft: Aether Wars!");
    } catch (Exception e) {
      text.setText("Failed to load cards: " + e);
    }
  }

  public static void main(String[] args) {
    launch();
  }
}
