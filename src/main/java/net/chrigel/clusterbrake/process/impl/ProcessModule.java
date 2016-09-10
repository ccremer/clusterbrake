package net.chrigel.clusterbrake.process.impl;

import com.google.inject.AbstractModule;
import net.chrigel.clusterbrake.process.CommandLineInterpreter;
import net.chrigel.clusterbrake.process.ExternalProcess;

/**
 *
 */
public class ProcessModule
        extends AbstractModule {

    @Override
    protected void configure() {
        bind(ExternalProcess.class).to(ExternalProcessImpl.class);
        if (System.getProperty("os.name").startsWith("Windows")) {
            bind(CommandLineInterpreter.class).to(AutoResolvableInterpreter.class);
        } else {
            bind(CommandLineInterpreter.class).to(Bash.class);
        }
    }

}
