package com.ppetrie.crosshair;

import javafx.stage.FileChooser.ExtensionFilter;

public class Util {
    
    /**
     * Checks whether the given URI is valid.
     * @param uri   the URI to check
     * @return      true if the URI is valid, false otherwise
     */
    public static boolean isValidUri(String uri) {
        return uri.matches("(https?|file):\\/{0,2}([A-Z]:\\/)?([A-Za-z0-9%_\\-\\.]+\\/)*[A-Za-z0-9%_\\-\\.]+");
    }
    
    /**
     * @return  an array of {@link javafx.stage.FileChooser.ExtensionFilter ExtensionFilter}s for valid image types
     */
    public static ExtensionFilter[] getImageExtFilters() {
        return new ExtensionFilter[] {
                new ExtensionFilter("PNG Image", "*.png"),
        };
    }
    
}
