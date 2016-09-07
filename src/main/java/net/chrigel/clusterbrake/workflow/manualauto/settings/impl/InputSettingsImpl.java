package net.chrigel.clusterbrake.workflow.manualauto.settings.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.workflow.manualauto.settings.InputSettings;

/**
 *
 */
class InputSettingsImpl
        implements InputSettings {

    private final List<String> videoExtensions;

    @Inject
    InputSettingsImpl(
            @Named("workflow.input.extensions") String extensions
    ) {
        this.videoExtensions = new LinkedList<>(Arrays.asList(extensions.split(",")));
    }


    @Override
    public List<String> getVideoExtensions() {
        return videoExtensions;
    }

}
