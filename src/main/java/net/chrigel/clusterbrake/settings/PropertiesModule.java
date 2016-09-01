package net.chrigel.clusterbrake.settings;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.google.inject.spi.Message;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Provides a guice module which loads and applies settings from an external properties file.
 */
public class PropertiesModule extends AbstractModule {

    private final String fileName;
    private Properties properties;

    /**
     * Creates the module with the specified file name. The properties will be loaded via
     * {@code ClassLoader.getSystemResourceAsStream(fileName);}
     *
     * @param fileName the file name accessible by the ClassLoader.
     */
    public PropertiesModule(final String fileName) {
        this.fileName = fileName;
    }

    @Override
    protected final void configure() {
        properties = new Properties();
        try {
            properties.load(new FileInputStream(fileName));
            Names.bindProperties(binder(), properties);
        } catch (Exception ex) {
            addError(new Message("Could not load properties file: " + fileName, ex));
        }
    }

}
