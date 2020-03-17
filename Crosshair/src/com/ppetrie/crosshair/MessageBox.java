package com.ppetrie.crosshair;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class MessageBox {
    
    private static final int WIDTH = 300, HEIGHT = 150;
    
    private final Stage stage;
    private final Button okButton;
    
    public MessageBox(String message) {
        Stage mbStage = new Stage();
        AnchorPane mbRoot = new AnchorPane();
        
        Label messageLabel = new Label(message);
        messageLabel.setPrefWidth(WIDTH - 10d);
        messageLabel.setMaxWidth(WIDTH - 10d);
        messageLabel.setWrapText(true);
        okButton = new Button("OK");
        okButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                mbStage.close();
            }
        });
        okButton.setPrefHeight(30d);
        AnchorPane.setTopAnchor(messageLabel, 5d);
        AnchorPane.setLeftAnchor(messageLabel, 5d);
        AnchorPane.setRightAnchor(messageLabel, 5d);
        AnchorPane.setBottomAnchor(okButton, 5d);
        AnchorPane.setLeftAnchor(okButton, 100d);
        AnchorPane.setRightAnchor(okButton, 100d);
        mbRoot.getChildren().addAll(messageLabel, okButton);
        
        Scene mbScene = new Scene(mbRoot, WIDTH, HEIGHT);
        mbStage.setScene(mbScene);
        mbStage.setResizable(false);
        mbStage.centerOnScreen();
        mbStage.setAlwaysOnTop(true);
        stage = mbStage;
    }
    
    public void show() {
        stage.show();
    }
    
    public void showAndRunOnOk(Runnable run) {
        okButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                run.run();
            }
        });
        show();
    }
    
}
