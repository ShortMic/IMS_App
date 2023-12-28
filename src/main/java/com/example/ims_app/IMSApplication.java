package com.example.ims_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A GUI Inventory Management System Program that keeps track of data on Products and their possibly associated parts.
 *
 * @author Michael Short
 * @version 1.0
 */
public class IMSApplication extends Application {
    public static Map<String, Scene> scenes;
    public static Inventory inventory;
    public static int uniqueId = 0;
    public static Stage currentStage;

    /**
     * The Application start method which houses the top level commands that sets and loads the main scene components, controller and view
     * for the Window stage display. The main inventory collection is also initialized with a few sample parts/products.
     *
     * @param stage represents a Stage object in which displays the window GUI and child scene objects
     */
    @Override
    public void start(Stage stage) throws IOException {
        currentStage = stage;
        scenes = new HashMap<String, Scene>();
        Product ravioli = new Product(uniqueId++, "Canned Ravioli", 3.00, 12, 1, 20);
        inventory = new Inventory(ravioli);

        FXMLLoader fxmlLoader = new FXMLLoader(IMSApplication.class.getResource("ims-mainView.fxml"));
        scenes.put("mainScene", new Scene(new FXMLLoader(IMSApplication.class.getResource("ims-mainView.fxml")).load(), 748, 430));
        scenes.put("addProduct", new Scene(new FXMLLoader(IMSApplication.class.getResource("ims-addProductView.fxml")).load(), 387, 438));
        fxmlLoader.setRoot(scenes.get("mainScene"));
        currentStage.setScene(scenes.get("mainScene"));
        currentStage.show();
    }

    /**
     * The Application main method which launches the app. Javadoc method and class details/descriptions are located
     * in the root/javadoc folder.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}