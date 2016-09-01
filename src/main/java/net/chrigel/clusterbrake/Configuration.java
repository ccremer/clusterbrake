package net.chrigel.clusterbrake;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.PropertiesConfigurationLayout;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 *
 */
public class Configuration {

    private final PropertiesConfigurationLayout layout;
    private final PropertiesConfiguration config;

    Configuration() {
        config = new PropertiesConfiguration();
        layout = new PropertiesConfigurationLayout();
    }

    void load(String configFileName) throws ConfigurationException, IOException {
        layout.load(config, new InputStreamReader(new FileInputStream(new File(configFileName))));
    }

    Object getProperty(String key) {
        return config.getProperty(key);
    }

    void setProperty(String key, Object value) {
        config.setProperty(key, value);
    }

    void save(String configFileName) throws ConfigurationException, IOException {
        layout.save(config, new FileWriter(configFileName));
    }

}
