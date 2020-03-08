package com.ppetrie.crosshair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ErrorReporter {
    
    public static void createErrorReport() {
        File debugLog = new File("debug.log");
        String dlContent;
        if(!debugLog.exists()) {
            dlContent = "nonexistent";
        } else {
            int len = (int) debugLog.length();
            if(len == 0L) {
                dlContent = "empty";
            } else try(FileInputStream dlFis = new FileInputStream(debugLog)) {
                byte[] content = new byte[len];
                dlFis.read(content);
                dlContent = new String(content);
            } catch(IOException ioe) {
                ioe.printStackTrace();
                return;
            }
        }
        StringBuilder output = new StringBuilder();
        output.append("debug.log\n");
        output.append("----------\n");
        output.append(dlContent + "\n");
        output.append("\nenv\n");
        output.append("----------\n");
        appendEnv(output, "NUMBER_OF_PROCESSORS");
        appendEnv(output, "PROCESSOR_ARCHITECTURE");
        appendEnv(output, "PROCESSOR_IDENTIFIER");
        appendEnv(output, "OS");
        appendSysProp(output, "java.specification.version");
        appendSysProp(output, "sun.arch.data.model");
        appendSysProp(output, "java.version.date");
        appendSysProp(output, "os.name");
        appendSysProp(output, "os.version");
        appendSysProp(output, "os.arch");
        appendSysProp(output, "sun.os.patch.level");
        appendSysProp(output, "java.version");
        appendSysProp(output, "java.vm.version");
        appendSysProp(output, "javafx.version");
        byte[] out = output.toString().getBytes();
        File outFile = new File("debuginfo");
        if(outFile.exists()) {
            outFile.delete();
        }
        try(FileOutputStream fos = new FileOutputStream(outFile)) {
            fos.write(out);
        } catch(IOException ioe) {
            ioe.printStackTrace();
            return;
        }
        debugLog.delete();
    }
    
    private static void appendEnv(StringBuilder sb, String envName) {
        sb.append(envName + ": " + System.getenv(envName) + "\n");
    }
    
    private static void appendSysProp(StringBuilder sb, String propName) {
        sb.append(propName + ": " + System.getProperty(propName) + "\n");
    }
    
}
