package net.chrigel.clusterbrake.util;

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
        Properties properties = new Properties();
        try (FileInputStream stream = new FileInputStream(fileName)) {
            properties.load(stream);
            Names.bindProperties(binder(), properties);
        } catch (Exception ex) {
            addError(new Message("Could not load properties file: " + fileName, ex));
        }
    }

}
