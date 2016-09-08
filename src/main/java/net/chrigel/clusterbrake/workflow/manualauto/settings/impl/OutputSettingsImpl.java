package net.chrigel.clusterbrake.workflow.manualauto.settings.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.chrigel.clusterbrake.workflow.manualauto.settings.OutputSettings;

/**
 *
 */
class OutputSettingsImpl
        implements OutputSettings {

    private final String defaultExtension;

    @Inject
    OutputSettingsImpl(
            @Named("workflow.output.defaultExtension") String defaultExtension
    ) {
        this.defaultExtension = defaultExtension;
    }

    @Override
    public String getDefaultExtension() {
        return defaultExtension;
    }

}
