package com.ppetrie.crosshair;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class SaveLoad implements EventHandler<MouseEvent> {
    
    private Button save, load, delete;
    
    public SaveLoad(Button save, Button load, Button delete) {
        this.save = save;
        this.load = load;
        this.delete = delete;
    }

    /**
     * Handles the save, load, and delete button presses
     */
    @Override
    public void handle(MouseEvent e) {
        Button button = (Button) e.getSource();
        String name = Crosshair.controller.nameField.getText();
        if(button == load) {
            Crosshair.load(name);
        } else if(button == save) {
            Crosshair.save(name);
        } else if(button == delete) {
            Crosshair.delete(name);
        }
    }
    
}
