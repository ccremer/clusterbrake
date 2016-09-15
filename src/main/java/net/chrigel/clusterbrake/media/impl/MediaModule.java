package net.chrigel.clusterbrake.media.impl;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import net.chrigel.clusterbrake.media.FileScanner;
import net.chrigel.clusterbrake.media.VideoPackage;

/**
 *
 */
public class MediaModule
        extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<FileScanner<VideoPackage>>() {
        }).to(VideoFileScanner.class);
        
        bind(VideoPackage.class).to(VideoPackageImpl.class);
    }

}
