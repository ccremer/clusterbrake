package net.chrigel.clusterbrake.media.impl;

import com.google.inject.AbstractModule;
import net.chrigel.clusterbrake.media.VideoScanner;

/**
 *
 */
public class MediaModule
        extends AbstractModule {

    @Override
    protected void configure() {
      //  bind(Video.class).to(VideoImpl.class);
        bind(VideoScanner.class).to(VideoScannerImpl.class);
    }

}
