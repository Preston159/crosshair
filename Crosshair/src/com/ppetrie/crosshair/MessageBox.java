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
    
    /**
     * The width of the message box.
     */
    private static final int WIDTH = 300;
    /**
     * The height of the message box.
     */
    private static final int HEIGHT = 150;
    
    /**
     * The stage of this message box.
     */
    private final Stage stage;
    /**
     * The OK button on this message box.
     */
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
    
    /**
     * Show this message box.<br />
     * This method does not block the thread.
     */
    public void show() {
        stage.show();
    }
    
    /**
     * Show this message box and run the provided {@link java.lang.Runnable Runnable} on OK.<br />
     * This method does not block the thread.<br />
     * The provided callback will not be run if the user closes the dialog by pressing X.
     * @param run   the callback function
     */
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
