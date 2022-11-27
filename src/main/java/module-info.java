module hu.petrik.furedidaniel__javafxrestclientdolgozat {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens hu.petrik.furedidaniel__javafxrestclientdolgozat to javafx.fxml, com.google.gson;
    exports hu.petrik.furedidaniel__javafxrestclientdolgozat;
}