package com.aetherwars.controllers;

import java.io.IOException;

import com.aetherwars.core.DisplayManager;
import com.aetherwars.core.GameManager;
import com.aetherwars.events.OnCardHovered;
import com.aetherwars.interfaces.Event;
import com.aetherwars.interfaces.Subscriber;
import com.aetherwars.models.cards.Card;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
public class InfoController implements Subscriber {
    @FXML
    public Label c_name;
    @FXML
    public Label c_info;
    @FXML
    public Label c_desc;
    @FXML
    public ImageView c_img;

    public InfoController(AnchorPane root) {
        GameManager.getInstance().addSubscriber(this);
        try {
            DisplayManager.loadGui("Info.fxml", root, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toggleCharacterInfo(boolean state) {
        c_name.setVisible(state);
        c_desc.setVisible(state);
        c_info.setVisible(state);
        c_img.setVisible(state);
    }

    @Override
    public void receiveEvent(Event evt) {
        if (evt instanceof OnCardHovered) {
            OnCardHovered och = (OnCardHovered) evt;
            Card c = och.getCard();
            if (c != null) {
                c_name.setText(c.getName());
                c_desc.setText(c.getDesc());
                c_info.setText(c.getInfo());
                c_img.setImage(new Image("/com/aetherwars/" + c.getImagePath()));
                toggleCharacterInfo(true);
            } else {
                toggleCharacterInfo(false);
            }
        }
    }
}
