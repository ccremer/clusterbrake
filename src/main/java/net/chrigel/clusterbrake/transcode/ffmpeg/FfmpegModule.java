package net.chrigel.clusterbrake.transcode.ffmpeg;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import net.chrigel.clusterbrake.media.OptionsFileParser;
import net.chrigel.clusterbrake.media.VideoOptionPackage;
import net.chrigel.clusterbrake.transcode.SimpleOptionPackage;
import net.chrigel.clusterbrake.transcode.SimpleOptionsFileParser;
import net.chrigel.clusterbrake.transcode.Transcoder;
import net.chrigel.clusterbrake.transcode.TranscoderSettings;

/**
 *
 */
public class FfmpegModule
        extends AbstractModule {

    @Override
    protected void configure() {
        bind(Transcoder.class).to(FfmpegCLI.class).in(Singleton.class);
        bind(TranscoderSettings.class).to(FfmpegSettings.class);

        bind(OptionsFileParser.class).to(SimpleOptionsFileParser.class);
        bind(VideoOptionPackage.class).to(SimpleOptionPackage.class);
    }

}
