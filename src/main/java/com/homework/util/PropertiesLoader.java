package com.homework.util;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    private static final Properties PROPERTIES = new Properties();
    private static final String PROPERTIES_FILE = "db.properties";

    static {
        loadProperties();
    }

    private PropertiesLoader() {
    }

    public static String getProperties(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
        try (InputStream inFile = PropertiesLoader.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            PROPERTIES.load(inFile);
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

}
