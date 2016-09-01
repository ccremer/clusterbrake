package net.chrigel.clusterbrake.transcode.impl;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import net.chrigel.clusterbrake.transcode.Transcoder;
import net.chrigel.clusterbrake.transcode.TranscoderSettings;

/**
 *
 */
public class TranscoderModule
        extends AbstractModule {

    @Override
    protected void configure() {
        bind(Transcoder.class).to(HandbrakeCli.class).in(Singleton.class);
        bind(TranscoderSettings.class).to(TranscoderSettingsImpl.class);
    }

}
