package com.ppetrie.crosshair;

import javafx.stage.FileChooser.ExtensionFilter;

public class Util {
    
    public static boolean isValidUri(String uri) {
        return uri.matches("(https?|file):\\/{0,2}([A-Z]:\\/)?([A-Za-z0-9%_\\-\\.]+\\/)*[A-Za-z0-9%_\\-\\.]+");
    }
    
    public static ExtensionFilter[] getImageExtFilters() {
        return new ExtensionFilter[] {
                new ExtensionFilter("PNG Image", "*.png"),
        };
    }
    
}
