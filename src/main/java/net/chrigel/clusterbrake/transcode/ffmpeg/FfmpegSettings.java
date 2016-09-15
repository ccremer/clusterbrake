package net.chrigel.clusterbrake.transcode.ffmpeg;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.chrigel.clusterbrake.transcode.TranscoderSettings;

/**
 *
 */
class FfmpegSettings
        implements TranscoderSettings {

    private final String cliPath;
    private final boolean redirectIO;

    @Inject
    FfmpegSettings(
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
        return "ffmpeg";
    }

}
