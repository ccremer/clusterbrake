package net.chrigel.clusterbrake;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import net.chrigel.clusterbrake.media.impl.MediaModule;
import net.chrigel.clusterbrake.settings.PropertiesModule;
import net.chrigel.clusterbrake.settings.impl.SettingsModule;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.transcode.impl.TranscoderModule;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
public class Startup {

    private static Logger logger;

    public static void main(String[] args) throws Exception {

        logger = LogManager.getLogger(Startup.class);
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            logger.error("Uncaught exception:", throwable);
            System.exit(1);
        });

        logger.info("Working dir: {}", new File("").getAbsolutePath());

        if (args == null || args.length == 0) {
            logger.error("Configuration file not provided in arguments. Exiting...");
        }
        String configFileName = args[0];
        Configuration nodeConfig = loadNodeConfiguration(configFileName);

        File configDir = new File(nodeConfig.getProperty("dir.config").toString());
        File commonConfigFile = new File(configDir, nodeConfig.getProperty("properties.common.path").toString());
        File workflowConfigFile = new File(configDir, nodeConfig.getProperty("properties.workflow.path").toString());

        logger.debug("Creating guice modules...");
        List<Module> modules = new LinkedList<>();

        modules.add(new PropertiesModule(configFileName));
        modules.add(new PropertiesModule(commonConfigFile.getPath()));
        modules.add(new PropertiesModule(workflowConfigFile.getPath()));
        modules.add(new TranscoderModule());
        modules.add(new SettingsModule());
        modules.add(new MediaModule());
        modules.add(getWorkflowModule(loadWorkflowConfiguration(nodeConfig, configDir)));

        logger.info("Booting application...");
        Injector injector = Guice.createInjector(modules);

        injector.getInstance(StateContext.class).reset();
    }

    private static void generateNodeIDOnFirstLaunch(Configuration config) {
        String nodeID = config.getProperty("node.id").toString();
        if (nodeID == null || "".equals(nodeID)) {
            logger.debug("Generating new node id...");
            nodeID = UUID.randomUUID().toString();
            config.setProperty("node.id", nodeID);
        }
    }

    private static Module getWorkflowModule(Configuration config) throws IllegalStateException {

        String className = config.getProperty("workflow.module").toString();
        try {
            logger.debug("Loading class: {}", className);
            final Class toLoad = ClassLoader.getSystemClassLoader().loadClass(className);
            final Object instance = toLoad.newInstance();
            if (instance instanceof Module) {
                return (Module) instance;
            } else {
                throw new IllegalStateException("Specified class does not implement " + Module.class.getName());
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private static Configuration loadNodeConfiguration(String configFileName)
            throws IOException, ConfigurationException {
        logger.info("Loading configuration file: {}", configFileName);
        Configuration nodeConfig = new Configuration();
        nodeConfig.load(configFileName);
        generateNodeIDOnFirstLaunch(nodeConfig);
        logger.info("Updating configuration file: {}", configFileName);
        nodeConfig.save(configFileName);
        return nodeConfig;
    }

    private static Configuration loadWorkflowConfiguration(Configuration nodeConfiguration, File configDir)
            throws ConfigurationException, IOException {

        File configFile = new File(configDir,
                nodeConfiguration.getProperty("properties.workflow.path").toString());
        String configFileName = configFile.getPath();
        logger.info("Loading configuration file: {}", configFileName);
        Configuration config = new Configuration();
        config.load(configFileName);
        return config;
    }

}
