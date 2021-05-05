package hu.alkfejl.controller;

import hu.alkfejl.App;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class MainWindowController {
    @FXML
    public void onPlay() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Choose a game type");
        alert.setHeaderText("");
        alert.setContentText("");

        ButtonType pvai = new ButtonType("Player vs CPU");
        ButtonType local = new ButtonType("Local");
        ButtonType cancel = new ButtonType("Cancel");

        alert.getButtonTypes().setAll(pvai, local, cancel);

        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == local) {
            App.loadFXML("/fxml/game_window.fxml");
            GameController.isAi = false;
        }
        else if (result.get() == pvai) {
            App.loadFXML("/fxml/game_window.fxml");
            GameController.isAi = true;
        }
    }

    @FXML
    public void onProfile(){
        App.loadFXML("/fxml/profile.fxml");
    }

    @FXML
    public void onExit(){
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to quit?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(buttonType -> {
            if(buttonType.equals(ButtonType.YES))
                Platform.exit();
        });
    }
}
