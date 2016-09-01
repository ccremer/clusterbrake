package net.chrigel.clusterbrake.settings.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.io.File;
import net.chrigel.clusterbrake.settings.DirectorySettings;

/**
 *
 */
class DirectorySettingsImpl
        implements DirectorySettings {

    private final File inputDir;
    private final File outputDir;
    private final File configDir;

    @Inject
    DirectorySettingsImpl(
            @Named("dir.input") String inputDir,
            @Named("dir.output") String outputDir,
            @Named("dir.config") String configDir
    ) {
        this.inputDir = new File(inputDir);
        this.outputDir = new File(outputDir);
        this.configDir = new File(configDir);
    }

    @Override
    public File getInputBaseDir() {
        return inputDir;
    }

    @Override
    public File getOutputBaseDir() {
        return outputDir;
    }

    @Override
    public File getConfigBaseDir() {
        return configDir;
    }

}
