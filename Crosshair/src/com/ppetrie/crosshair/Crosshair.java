package com.ppetrie.crosshair;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
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
    private static final int HEIGHT = 300;
    /**
     * Width of the main stage
     */
    private static final int WIDTH = 300;
    /**
     * Height of the crosshair
     */
    public static final int CH_HEIGHT = 50;
    /**
     * Width of the crosshair
     */
    public static final int CH_WIDTH = 50;
    public static final String LEFT = "\u2190", RIGHT = "\u2192", UP = "\u2191", DOWN = "\u2193", HIDE = "±";
    public static Button left, right, up, down, hide;
    /**
     * The name of the default profile
     */
    public static final String DEFAULT_PROFILE = "Default";
    
    private static Mover mover;
    public static Stage chStage;
    public static ListView<String> settings;
    public static TextField xField, yField;
    public static Label xLabel, yLabel;
    public static TextField nameField;
    
    private static DataStore data;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
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
        
        mover = new Mover();
        mover.init();
        
        GridPane root = new GridPane();
        
        createCrosshairButtons(root);
        createProfileList(root);
        createLabelsAndFields(root);
        createProfileButtons(root);
        
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        ImageView crosshair = new ImageView();
        crosshair.maxWidth(CH_WIDTH);
        crosshair.maxHeight(CH_HEIGHT);
        Image chImage = new Image("file:simple.png");
        crosshair.setImage(chImage);
        crosshair.autosize();
        crosshair.resize(CH_WIDTH / chImage.getWidth(), CH_HEIGHT / chImage.getHeight());
        
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
        Mover.updateFields();
    }
    
    
    private static void createCrosshairButtons(GridPane root) {
        left = new Button();
        left.setText(LEFT);
        root.add(left, 0, 1, 1, 1);
        left.addEventHandler(MouseEvent.ANY, mover);
        
        right = new Button();
        right.setText(RIGHT);
        root.add(right, 2, 1, 1, 1);
        right.addEventHandler(MouseEvent.ANY, mover);
        
        up = new Button();
        up.setText(UP);
        root.add(up, 1, 0, 1, 1);
        up.addEventHandler(MouseEvent.ANY, mover);
        
        down = new Button();
        down.setText(DOWN);
        root.add(down, 1, 2, 1, 1);
        down.addEventHandler(MouseEvent.ANY, mover);
        
        hide = new Button();
        hide.setText(HIDE);
        root.add(hide, 1, 1, 1, 1);
        hide.addEventFilter(MouseEvent.ANY, mover);
    }
    
    private static void createProfileList(GridPane root) {
        settings = new ListView<>();
        settings.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent e) {
                String name = settings.getSelectionModel().getSelectedItem();
                nameField.setText(name);
            }
            
        });
        root.add(settings, 0, 4, 9, 1);
    }
    
    private static void createLabelsAndFields(GridPane root) {
        xLabel = new Label("X:");
        root.add(xLabel, 3, 0, 1, 1);
        yLabel = new Label("Y:");
        root.add(yLabel, 3, 1, 1, 1);
        
        xField = new TextField("0.0");
        xField.addEventHandler(InputEvent.ANY, new EventHandler<InputEvent>() {

            @Override
            public void handle(InputEvent arg0) {
                try {
                    chStage.setX(Double.parseDouble(xField.getText()) - (CH_WIDTH / 2));
                } catch(NumberFormatException nfe) { }
            }
            
        });
        root.add(xField, 4, 0, 5, 1);
        yField = new TextField("0.0");
        yField.addEventHandler(InputEvent.ANY, new EventHandler<InputEvent>() {

            @Override
            public void handle(InputEvent arg0) {
                try {
                    chStage.setY(Double.parseDouble(yField.getText()) - (CH_HEIGHT / 2));
                } catch(NumberFormatException nfe) { }
            }
            
        });
        root.add(yField, 4, 1, 5, 1);
        
        Label nameLabel = new Label("Name:");
        root.add(nameLabel, 0, 3, 2, 1);
        
        nameField = new TextField(DEFAULT_PROFILE);
        root.add(nameField, 2, 3, 4, 1);
    }
    
    private static void createProfileButtons(GridPane root) {
        Button save = new Button("Save");
        root.add(save, 6, 3, 1, 1);
        
        Button load = new Button("Load");
        root.add(load, 7, 3, 1, 1);
        
        Button delete = new Button("Delete");
        root.add(delete, 8, 3, 1, 1);
        
        SaveLoad saveLoad = new SaveLoad(save, load, delete);
        save.addEventHandler(MouseEvent.MOUSE_CLICKED, saveLoad);
        load.addEventHandler(MouseEvent.MOUSE_CLICKED, saveLoad);
        delete.addEventHandler(MouseEvent.MOUSE_CLICKED, saveLoad);
    }
    
    /**
     * Updates the list of profiles on the main application window
     */
    private static void fillList() {
        settings.getItems().clear();
        for(String name : data.getNames()) {
            settings.getItems().add(name);
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
