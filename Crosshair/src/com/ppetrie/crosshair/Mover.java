package com.ppetrie.crosshair;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
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
        synchronized(block) {
            if(event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                switch(((Button) event.getSource()).getText()) {
                case Crosshair.UP:
                    movement[1] = -1d;
                    break;
                case Crosshair.DOWN:
                    movement[1] = 1d;
                    break;
                case Crosshair.LEFT:
                    movement[0] = -1d;
                    break;
                case Crosshair.RIGHT:
                    movement[0] = 1d;
                    break;
                case Crosshair.HIDE:
                    if(Crosshair.chStage.isShowing()) {
                        Crosshair.chStage.hide();
                    } else {
                        Crosshair.chStage.show();
                    }
                    break;
                default: break;
                }
                synchronized(block) {
                    block.notifyAll();
                }
            } else if(event.getEventType() == MouseEvent.MOUSE_RELEASED) {
                switch(((Button) event.getSource()).getText()) {
                case Crosshair.UP:
                case Crosshair.DOWN:
                    movement[1] = 0d;
                    break;
                case Crosshair.LEFT:
                case Crosshair.RIGHT:
                    movement[0] = 0d;
                    break;
                default: break;
                }
            }
        }
    }
    
    /**
     * Updates the text fields in the main window with the crosshair's current position
     */
    public static void updateFields() {
        Crosshair.xField.setText(String.format("%.0f", Crosshair.chStage.getX() + (Crosshair.CH_WIDTH / 2)));
        Crosshair.yField.setText(String.format("%.0f", Crosshair.chStage.getY() + (Crosshair.CH_HEIGHT / 2)));
    }

}
