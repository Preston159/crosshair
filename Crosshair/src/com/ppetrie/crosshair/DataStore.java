package com.ppetrie.crosshair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class DataStore implements Serializable {
    private static final long serialVersionUID = -3250175611760498026L;
    
    private final int VERSION = 0;
    
    private int version;
    private ArrayList<Setting> settings;
    
    public DataStore() {
        settings = new ArrayList<>();
        version = VERSION;
    }
    
    public void checkVersion() {
        if(version != VERSION) {
            // TODO
        }
    }
    
    public ArrayList<String> getNames() {
        ArrayList<String> names = new ArrayList<>();
        for(Setting setting : settings) {
            names.add(setting.getName());
        }
        return names;
    }
    
    public double[] getPosition(String name) {
        for(Setting setting : settings) {
            if(setting.getName().equalsIgnoreCase(name)) {
                return new double[] { setting.getX(), setting.getY() };
            }
        }
        return null;
    }
    
    public void setPosition(String name, double x, double y) {
        for(Setting setting : settings) {
            if(setting.getName().equalsIgnoreCase(name)) {
                setting.setX(x);
                setting.setY(y);
                return;
            }
        }
        settings.add(new Setting(name, x, y));
    }
    
    public void deletePosition(String name) {
        for(int i = 0;i < settings.size();i++) {
            if(settings.get(i).getName().equalsIgnoreCase(name)) {
                settings.remove(i);
                return;
            }
        }
    }
    
    public boolean save() {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("positions.dat")))) {
            oos.writeUnshared(this);
        } catch(IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
        return true;
    }
    
    public static DataStore load() {
        File dataFile = new File("positions.dat");
        try {
            if(!dataFile.exists()) {
                dataFile.createNewFile();
                return null;
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            DataStore out = (DataStore) ois.readUnshared();
            out.checkVersion();
            return out;
        } catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static class Setting implements Serializable {
        private static final long serialVersionUID = 2476991651084982928L;
        
        private String name;
        private double x, y;
        
        private Setting(String name, double x, double y) {
            this.name = name;
            this.x = x;
            this.y = y;
        }
        
        public String getName() {
            return name;
        }
        
        public double getX() {
            return x;
        }
        
        public double getY() {
            return y;
        }
        
        private void setX(double x) {
            this.x = x;
        }
        
        private void setY(double y) {
            this.y = y;
        }
        
    }
    
}
