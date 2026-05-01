package com.bjj;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bjj/login.fxml"));
        Scene scene = new Scene(loader.load(), 850, 520);
        scene.getStylesheets().add(getClass().getResource("/com/bjj/style.css").toExternalForm());
        stage.setTitle("GES - Connexion");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}