package com.homework.util;

import java.io.InputStream;
import java.util.Properties;

public class InitProperties {

    private static final Properties PROPERTIES = new Properties();
    private static final String PROPERTIES_FILE = "db.properties";

    static {
        loadProperties();
    }

    private InitProperties() {
    }

    public static String getProperties(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
        try (InputStream inFile = InitProperties.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            PROPERTIES.load(inFile);
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

}
