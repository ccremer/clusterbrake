package net.chrigel.clusterbrake.transcode.handbrake;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import net.chrigel.clusterbrake.media.OptionsFileParser;
import net.chrigel.clusterbrake.media.VideoOptionPackage;
import net.chrigel.clusterbrake.transcode.Transcoder;
import net.chrigel.clusterbrake.transcode.TranscoderSettings;

/**
 *
 */
public class HandbrakeModule
        extends AbstractModule {

    @Override
    protected void configure() {
        bind(Transcoder.class).to(HandbrakeCli.class).in(Singleton.class);
        bind(TranscoderSettings.class).to(HandbrakeSettings.class);

        bind(OptionsFileParser.class).to(HandbrakeOptionParser.class);
        bind(VideoOptionPackage.class).to(HandbrakeOptionPackage.class);
    }

}
