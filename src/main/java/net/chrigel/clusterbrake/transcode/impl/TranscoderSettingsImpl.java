package net.chrigel.clusterbrake.transcode.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.chrigel.clusterbrake.transcode.TranscoderSettings;

/**
 *
 */
class TranscoderSettingsImpl
        implements TranscoderSettings {

    private final String cliPath;
    private final boolean redirectIO;

    @Inject
    TranscoderSettingsImpl(
            @Named("node.cli.path") String cliPath,
            @Named("node.cli.redirectIO") boolean redirectIO
    ) {
        this.cliPath = cliPath;
        this.redirectIO = redirectIO;
    }

    @Override
    public String getCLIPath() {
        return cliPath;
    }

    @Override
    public boolean isIORedirected() {
        return redirectIO;
    }

}
