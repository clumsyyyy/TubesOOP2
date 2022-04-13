package com.aetherwars.models;

import com.aetherwars.controllers.Deck;
import com.aetherwars.models.cards.Card;
import com.aetherwars.util.CSVReader;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DeckFactory {
    static final List<Type> typeVal = Collections.unmodifiableList(Arrays.asList(Type.values()));
    public static Deck create(ArrayList<Card> cards, int N) {
        Deck deck = new Deck();
        Random random = new Random();
        Map<Type, Integer> countType = new HashMap<Type, Integer>();
        // random first
        for (Type t : typeVal) {
            countType.put(t, 0);
        }
        int cnt = random.nextInt();
        countType.put(typeVal.get(0), cnt);
        int sum = cnt;
        // random type count with bound to sum
        for (int i = 1; i < typeVal.size(); i++) {
            cnt = random.nextInt(N/5);
            countType.put(typeVal.get(i), cnt);
            sum += cnt;
        }
        // leftover N-sum, if any.
        for (int i = 0; i < N-sum; i++) {
            cnt = (N - sum) / 5;
            countType.put(typeVal.get(i), countType.get(typeVal.get(i)) + cnt);
            sum += cnt;
        }
        // leftover, again, but we set all to countType[0]
        countType.put(typeVal.get(0), countType.get(typeVal.get(0)) + N - sum);
        // now get the card
        for (int i = 0; i < N; i++) {
            Card c;
            do {
                c = cards.get(random.nextInt(cards.size()));
            } while (countType.get(c.getType()) == 0);
            countType.put(c.getType(), countType.get(c.getType()) - 1);
            deck.addCard(c);
        }
        return deck;
    }

    public static Deck create(ArrayList<Card> cards, File file) {
        Deck deck = new Deck();
        CSVReader fread = new CSVReader(file, "\t");
        fread.setSkipHeader(true);
        List<String[]> deckRows = null;
        try {
             deckRows = fread.read();
        } catch (IOException err) {
            System.out.println(err.getMessage());
        }
        if (deckRows != null) {
            for (String[] rows : deckRows) {
                boolean isFound = false;
                for (Card c : cards) {
                    if (c.getId() == Integer.parseInt(rows[0])) {
                        deck.addCard(c);
                        isFound = true;
                        break;
                    }
                }
                if (!isFound) {
                    System.out.println("Undefined card id: " + rows[0]);
                    break;
                }
            }
        }
        return deck;
    }
}
