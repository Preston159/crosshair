package com.ppetrie.crosshair;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class CrosshairController {
    
    @FXML protected Button buttonLeft, buttonRight, buttonUp, buttonDown;
    @FXML protected Button buttonHide;
    @FXML protected TextField xField, yField;
    @FXML protected TextField nameField;
    @FXML protected TextField uriField;
    @FXML protected ListView<String> settings;
    
    @FXML public void initialize() {
        buttonLeft.addEventHandler(MouseEvent.ANY, Crosshair.mover);
        buttonRight.addEventHandler(MouseEvent.ANY, Crosshair.mover);
        buttonUp.addEventHandler(MouseEvent.ANY, Crosshair.mover);
        buttonDown.addEventHandler(MouseEvent.ANY, Crosshair.mover);
        
        Mover.Updater updater = new Mover.Updater();
        xField.addEventHandler(InputEvent.ANY, updater);
        yField.addEventHandler(InputEvent.ANY, updater);
    }
    
    @FXML protected void handleShowHide() {
        if(Crosshair.chStage.isShowing()) {
            Crosshair.chStage.hide();
        } else {
            Crosshair.chStage.show();
        }
    }
    
    @FXML protected void handleSaveProfile() {
        Crosshair.save(nameField.getText());
    }
    
    @FXML protected void handleLoadProfile() {
        Crosshair.load(nameField.getText());
    }
    
    @FXML protected void handleDeleteProfile() {
        Crosshair.delete(nameField.getText());
    }
    
    @FXML protected void handleProfileClick() {
        nameField.setText(settings.getSelectionModel().getSelectedItem());
    }
    
    @FXML protected void handleLoadCrosshairImage() {
        String newUri = uriField.getText();
        if(newUri.length() == 0) {
            handleResetCrosshairImage();
            return;
        }
        if(!Util.isValidUri(newUri)) {
            MessageBox mb = new MessageBox("The URI entered is invalid");
            mb.showAndRunOnOk(() -> {
                uriField.setText("");
            });
            return;
        }
        Crosshair.setCrosshairImage(newUri);
    }
    
    @FXML protected void handleBrowseCrosshairImage() {
        FileChooser chooser = new FileChooser();
        ExtensionFilter[] filters = Util.getImageExtFilters();
        for(ExtensionFilter filter : filters) {
            chooser.getExtensionFilters().add(filter);
        }
        File f = chooser.showOpenDialog(Crosshair.primaryStage);
        uriField.setText(f.toURI().toString());
        handleLoadCrosshairImage();
    }
    
    @FXML protected void handleResetCrosshairImage() {
        Crosshair.setCrosshairImage(Crosshair.DEFAULT_CROSSHAIR_URI);
        uriField.setText("");
    }
    
}
