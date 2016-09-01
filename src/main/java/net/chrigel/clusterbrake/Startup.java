package net.chrigel.clusterbrake;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import net.chrigel.clusterbrake.settings.NodeSettings;
import net.chrigel.clusterbrake.settings.PropertiesModule;
import net.chrigel.clusterbrake.settings.impl.SettingsModule;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.states.StateMachineModule;
import net.chrigel.clusterbrake.transcode.impl.TranscoderModule;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.PropertiesConfigurationLayout;
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
        });

        logger.info("Working dir: {}", new File("").getAbsolutePath());

        if (args == null || args.length == 0) {
            logger.error("Configuration file not provided in arguments. Exiting...");
        }
        String configFileName = args[0];
        logger.info("Using configuration file: {}", configFileName);

        logger.debug("Creating guice modules...");
        List<Module> modules = new LinkedList<>();

        modules.add(new PropertiesModule(configFileName));
        modules.add(new StateMachineModule());
        modules.add(new TranscoderModule());
        modules.add(new SettingsModule());

        logger.info("Booting application...");
        Injector injector = Guice.createInjector(modules);

        updateNodeIDIfFirstStartup(injector.getInstance(NodeSettings.class), configFileName);
        
        injector.getInstance(StateContext.class);
    }

    private static void updateNodeIDIfFirstStartup(NodeSettings settings, String configFileName) {
        String nodeID = settings.getNodeID();
        logger.info("Current node id is: {}", nodeID);
        if (nodeID == null || "".equals(nodeID)) {
            logger.debug("Generating new node id...");
            nodeID = UUID.randomUUID().toString();

            PropertiesConfiguration config = new PropertiesConfiguration();
            PropertiesConfigurationLayout layout = new PropertiesConfigurationLayout();
            try {
                logger.debug("Loading file {} and update node id with {}", configFileName, nodeID);
                layout.load(config, new InputStreamReader(new FileInputStream(new File(configFileName))));
                config.setProperty("node.id", nodeID);
                layout.save(config, new FileWriter(configFileName));
                settings.setNodeID(nodeID);
            } catch (ConfigurationException | IOException ex) {
                logger.error("Could not save new configuration file: {}", ex);
                System.exit(1);
            }

        }
    }

}
