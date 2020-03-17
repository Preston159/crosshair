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
    
    /**
     * Crosshair movement button
     */
    @FXML protected Button buttonLeft, buttonRight, buttonUp, buttonDown;
    /**
     * Crosshair show/hide button
     */
    @FXML protected Button buttonHide;
    /**
     * Crosshair position field
     */
    @FXML protected TextField xField, yField;
    /**
     * Profile name field
     */
    @FXML protected TextField nameField;
    /**
     * Crosshair image URI field
     */
    @FXML protected TextField uriField;
    /**
     * List containing names of saved profiles
     */
    @FXML protected ListView<String> settings;
    
    /**
     * Initializes the controller and event handlers
     */
    @FXML public void initialize() {
        buttonLeft.addEventHandler(MouseEvent.ANY, Crosshair.mover);
        buttonRight.addEventHandler(MouseEvent.ANY, Crosshair.mover);
        buttonUp.addEventHandler(MouseEvent.ANY, Crosshair.mover);
        buttonDown.addEventHandler(MouseEvent.ANY, Crosshair.mover);
        
        Mover.Updater updater = new Mover.Updater();
        xField.addEventHandler(InputEvent.ANY, updater);
        yField.addEventHandler(InputEvent.ANY, updater);
    }
    
    /**
     * Toggles the visibility of the crosshair
     */
    @FXML protected void handleShowHide() {
        if(Crosshair.chStage.isShowing()) {
            Crosshair.chStage.hide();
        } else {
            Crosshair.chStage.show();
        }
    }
    
    /**
     * Saves the current position to the selected profile
     */
    @FXML protected void handleSaveProfile() {
        Crosshair.save(nameField.getText());
    }
    
    /**
     * Loads the selected profile
     */
    @FXML protected void handleLoadProfile() {
        Crosshair.load(nameField.getText());
    }
    
    /**
     * Deletes the selected profile
     */
    @FXML protected void handleDeleteProfile() {
        Crosshair.delete(nameField.getText());
    }
    
    /**
     * Populates the profile name field with the clicked profile name
     */
    @FXML protected void handleProfileClick() {
        nameField.setText(settings.getSelectionModel().getSelectedItem());
    }
    
    /**
     * Loads the URI field's URI as an image and sets the crosshair image to same
     */
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
    
    /**
     * Opens a file selection dialog to select a crosshair image, populates the URI field with the selected
     *   image, and loads it 
     */
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
    
    /**
     * Resets the crosshair image to the default
     */
    @FXML protected void handleResetCrosshairImage() {
        Crosshair.setCrosshairImage(Crosshair.DEFAULT_CROSSHAIR_URI);
        uriField.setText("");
    }
    
}
