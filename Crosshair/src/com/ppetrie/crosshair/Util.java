package com.ppetrie.crosshair;

import java.net.URI;

import javafx.stage.FileChooser.ExtensionFilter;

public class Util {
    
    public static boolean isValidUri(String uri) {
        try {
            URI.create(uri);
        } catch(IllegalArgumentException iae) {
            return false;
        }
        return true;
    }
    
    public static ExtensionFilter[] getImageExtFilters() {
        return new ExtensionFilter[] {
                new ExtensionFilter("PNG Image", "*.png"),
        };
    }
    
}
