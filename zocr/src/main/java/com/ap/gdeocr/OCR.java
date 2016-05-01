package com.ap.gdeocr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//Application class have to be public
class OCR extends Application {
    private static Parent root;
    private static Scene scene;

    public static Scene getScene() {
        return scene;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        root = FXMLLoader.load(getClass().getResource("ocr.fxml"));
        primaryStage.setTitle("Ocr App");
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}

