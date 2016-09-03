package net.chrigel.clusterbrake.media.impl;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import net.chrigel.clusterbrake.media.FileScanner;
import net.chrigel.clusterbrake.media.OptionsFileParser;
import net.chrigel.clusterbrake.media.Video;
import net.chrigel.clusterbrake.media.VideoOptionPackage;
import net.chrigel.clusterbrake.media.VideoPackage;

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
        
        bind(OptionsFileParser.class).to(HandbrakeOptionParser.class);
        bind(VideoOptionPackage.class).to(HandbrakeOptionPackage.class);
        bind(VideoPackage.class).to(VideoPackageImpl.class);
    }

}
