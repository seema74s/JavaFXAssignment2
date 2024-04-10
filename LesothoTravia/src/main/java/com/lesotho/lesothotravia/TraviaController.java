package com.lesotho.lesothotravia;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TraviaController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}