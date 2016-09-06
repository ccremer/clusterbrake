package net.chrigel.clusterbrake.workflow.manualauto.settings.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.settings.DirectorySettings;
import net.chrigel.clusterbrake.workflow.manualauto.settings.InputSettings;

/**
 *
 */
class InputSettingsImpl
        implements InputSettings {

    private final File manualInputDir;
    private final File autoInputDir;
    private final List<String> videoExtensions;

    @Inject
    InputSettingsImpl(
            DirectorySettings dirSettings,
            @Named("workflow.input.dir.manual") String manualinputDir,
            @Named("workflow.input.dir.auto") String autoInputDir,
            @Named("workflow.input.extensions") String extensions
    ) {

        this.manualInputDir = new File(dirSettings.getInputBaseDir(), manualinputDir);
        this.autoInputDir = new File(dirSettings.getInputBaseDir(), autoInputDir);
        this.videoExtensions = new LinkedList<>(Arrays.asList(extensions.split(",")));

    }

    @Override
    public File getManualInputDirectory() {
        return manualInputDir;
    }

    @Override
    public File getAutoInputDirectory() {
        return autoInputDir;
    }

    @Override
    public List<String> getVideoExtensions() {
        return videoExtensions;
    }

}
