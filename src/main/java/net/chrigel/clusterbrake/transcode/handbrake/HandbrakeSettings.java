package net.chrigel.clusterbrake.transcode.handbrake;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.chrigel.clusterbrake.transcode.TranscoderSettings;

/**
 *
 */
class HandbrakeSettings
        implements TranscoderSettings {

    private final String cliPath;
    private final boolean redirectIO;

    @Inject
    HandbrakeSettings(
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

    @Override
    public String getOptionsFileExtension() {
        return "handbrake";
    }

}
