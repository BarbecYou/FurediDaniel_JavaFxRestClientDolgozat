package hu.petrik.furedidaniel__javafxrestclientdolgozat;

import javafx.scene.control.Alert;

public abstract class Controller {

    protected void error(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    protected void warning(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
