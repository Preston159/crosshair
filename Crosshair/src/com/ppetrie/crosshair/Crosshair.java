package com.ppetrie.crosshair;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class Crosshair extends Application {
    
    /**
     * Height of the main stage
     */
    static final int HEIGHT = 300;
    /**
     * Width of the main stage
     */
    static final int WIDTH = 390;
    /**
     * Height of the crosshair
     */
    public static final int CH_HEIGHT = 50;
    /**
     * Width of the crosshair
     */
    public static final int CH_WIDTH = 50;
    /**
     * The name of the default profile
     */
    public static final String DEFAULT_PROFILE = "Default";
    public static final String DEFAULT_CROSSHAIR_URI = "file:simple.png";
    
    static Stage primaryStage;
    static Mover mover;
    static Stage chStage;
//    static ListView<String> settings;
//    static TextField xField, yField;
//    static Label xLabel, yLabel;
//    static TextField nameField;
    static ImageView crosshair;
    
    private static DataStore data;
    static CrosshairController controller;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Crosshair.primaryStage = primaryStage;
        data = DataStore.load();
        if(data == null) {
            data = new DataStore();
        }
        
        Platform.setImplicitExit(false);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                data.save();
                chStage.close();
                Platform.exit();
                System.exit(0);
            }
        });
        
        GridPane root;
        mover = new Mover();
        mover.init();
        
        try {
            FXMLLoader loader = new FXMLLoader();
            root = loader.load(getClass().getResource("application.fxml").openStream());
            controller = loader.getController();
            Scene scene = new Scene(root, WIDTH, HEIGHT);
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
            primaryStage.setScene(scene);
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        primaryStage.show();
        
        crosshair = new ImageView();
        crosshair.maxWidth(CH_WIDTH);
        crosshair.maxHeight(CH_HEIGHT);
        setCrosshairImage(DEFAULT_CROSSHAIR_URI);
        
        StackPane chRoot = new StackPane();
        chRoot.getChildren().add(crosshair);
        chRoot.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
        Scene chScene = new Scene(chRoot, CH_WIDTH, CH_HEIGHT);
        chStage = new Stage();
        chScene.setFill(Color.TRANSPARENT);
        chStage.setScene(chScene);
        chStage.initStyle(StageStyle.TRANSPARENT);
        chStage.show();
        chStage.centerOnScreen();
        chStage.setAlwaysOnTop(true);
        
        if(data.getPosition(DEFAULT_PROFILE) == null) {
            save(DEFAULT_PROFILE);
        }
        load(DEFAULT_PROFILE);
        
        fillList();
        data.init();
        Mover.updateFields();
    }
    
    /**
     * Creates image selection label, textfield, and buttons
     * @param root  the root pane
     */
//    private static void createImageSelector(GridPane root) {
//        HBox imageSelectorPane = new HBox();
//        root.add(imageSelectorPane, 0, 5, 9, 1);
//        
//        Label imageUriLabel = new Label("Image URI:");
//        TextField imageUriField = new TextField();
//        Button browseButton = new Button("Browse");
//        Button loadButton = new Button("Load Image");
//        
//        imageSelectorPane.getChildren().addAll(imageUriLabel, imageUriField, browseButton, loadButton);
//    }
    
    /**
     * Updates the list of profiles on the main application window
     */
    private static void fillList() {
        controller.settings.getItems().clear();
        for(String name : data.getNames()) {
            controller.settings.getItems().add(name);
        }
    }
    
    public static void main(String[] args) {
        if(args.length > 0) {
            if(args[0].equalsIgnoreCase("report")) {
                ErrorReporter.createErrorReport();
                System.exit(0);
            }
        }
        launch(new String[0]);
    }
    
    /**
     * Updates the position of the crosshair on the screen
     * @param pos   The new position
     */
    public static void setCrosshairPosition(double[] pos) {
        chStage.setX(pos[0] - (CH_WIDTH / 2));
        chStage.setY(pos[1] - (CH_HEIGHT / 2));
    }
    
    /**
     * Gets the position of the crosshair on the screen
     * @return  The position
     */
    public static double[] getCrosshairPosition() {
        return new double[] { chStage.getX() + (CH_WIDTH / 2), chStage.getY() + (CH_HEIGHT / 2) };
    }
    
    /**
     * Updates the image used for the crosshair
     * @param uri   The new image's URI
     */
    public static void setCrosshairImage(String uri) {
        Image chImage = new Image(uri);
        crosshair.setImage(chImage);
        crosshair.autosize();
        crosshair.resize(CH_WIDTH / chImage.getWidth(), CH_HEIGHT / chImage.getHeight());
        data.setCrosshairUri(uri);
    }
    
    /**
     * Moves the crosshair to the position of a saved profile
     * @param name  The name of the saved profile
     */
    public static void load(String name) {
        double[] pos = data.getPosition(name);
        if(pos != null) {
            setCrosshairPosition(pos);
        }
    }
    
    /**
     * Saves the current crosshair position to a profile
     * @param name  The profile name
     */
    public static void save(String name) {
        double[] position = getCrosshairPosition();
        data.setPosition(name, position[0], position[1]);
        fillList();
    }
    
    /**
     * Deletes a profile
     * @param name  The profile name
     */
    public static void delete(String name) {
        data.deletePosition(name);
        fillList();
    }

}
