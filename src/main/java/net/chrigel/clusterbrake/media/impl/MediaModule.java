package net.chrigel.clusterbrake.media.impl;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import net.chrigel.clusterbrake.media.FileScanner;
import net.chrigel.clusterbrake.media.Video;
import net.chrigel.clusterbrake.media.VideoOptionPackage;

/**
 *
 */
public class MediaModule
        extends AbstractModule {

    @Override
    protected void configure() {
        //  bind(Video.class).to(VideoImpl.class);
        bind(new TypeLiteral<FileScanner<Video>>() {
        }).to(VideoFileScanner.class);
        bind(new TypeLiteral<FileScanner<VideoOptionPackage>>() {
        }).to(OptionFileScanner.class);
    }

}
