package com.ppetrie.crosshair;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;

public class Mover implements EventHandler<MouseEvent> {
    
    public static volatile double[] movement = { 0d, 0d };
    private static Thread moverThread;
    private volatile Object block = new Object();
    
    /**
     * Initialize and start the background thread responsible for moving the crosshair
     */
    public void init() {
        moverThread = new Thread(() -> {
            while(true) {
                while(movement[0] != 0d || movement[1] != 0d) {
                    if(movement[0] != 0d) {
                        Crosshair.chStage.setX(Crosshair.chStage.getX() + movement[0]);
                        movement[0] *= 1.1;
                    }
                    if(movement[1] != 0d) {
                        Crosshair.chStage.setY(Crosshair.chStage.getY() + movement[1]);
                        movement[1] *= 1.1;
                    }
                    updateFields();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
                try {
                    synchronized(block) {
                        block.wait();
                    }
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        });
        moverThread.start();
    }

    /**
     * Handles movement button presses
     */
    @Override
    public void handle(MouseEvent event) {
        Button button = (Button) event.getSource();
        synchronized(block) {
            if(event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                if(button == Crosshair.controller.buttonLeft) {
                    movement[0] = -1;
                } else if(button == Crosshair.controller.buttonRight) {
                    movement[0] = 1;
                } else if(button == Crosshair.controller.buttonUp) {
                    movement[1] = -1;
                } else if(button == Crosshair.controller.buttonDown) {
                    movement[1] = 1;
                }
                synchronized(block) {
                    block.notifyAll();
                }
            } else if(event.getEventType() == MouseEvent.MOUSE_RELEASED) {
                if(button == Crosshair.controller.buttonLeft || button == Crosshair.controller.buttonRight) {
                    movement[0] = 0;
                } else if(button == Crosshair.controller.buttonUp || button == Crosshair.controller.buttonDown) {
                    movement[1] = 0;
                }
            }
        }
    }
    
    /**
     * Updates the text fields in the main window with the crosshair's current position
     */
    public static void updateFields() {
        Crosshair.controller.xField.setText(String.format("%.0f", Crosshair.chStage.getX() + (Crosshair.CH_WIDTH / 2)));
        Crosshair.controller.yField.setText(String.format("%.0f", Crosshair.chStage.getY() + (Crosshair.CH_HEIGHT / 2)));
    }
    
    public static class Updater implements EventHandler<InputEvent> {

        @Override
        public void handle(InputEvent event) {
            int x, y;
            try {
                x = Integer.valueOf(Crosshair.controller.xField.getText()) - (Crosshair.CH_WIDTH / 2);
                y = Integer.valueOf(Crosshair.controller.yField.getText()) - (Crosshair.CH_HEIGHT / 2);
                Crosshair.chStage.setX(x);
                Crosshair.chStage.setY(y);
            } catch(NumberFormatException nfe) { }
        }
        
    }

}
