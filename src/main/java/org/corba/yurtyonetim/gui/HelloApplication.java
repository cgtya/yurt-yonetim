package org.corba.yurtyonetim.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("KYK Yurt Yönetim Sistemi");
        stage.setScene(scene);
        stage.setMinHeight(480);
        stage.setMinWidth(640);
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.jpg")));
        stage.getIcons().add(icon);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}