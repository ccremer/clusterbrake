package net.chrigel.clusterbrake.media.impl;

import net.chrigel.clusterbrake.media.FileContainer;
import net.chrigel.clusterbrake.media.Video;
import net.chrigel.clusterbrake.media.VideoOptionPackage;
import net.chrigel.clusterbrake.media.VideoPackage;

/**
 *
 */
public class VideoPackageImpl
        implements VideoPackage {

    private Video video;
    private FileContainer outputFile;
    private VideoOptionPackage settings;

    @Override
    public Video getVideo() {
        return video;
    }

    @Override
    public void setVideo(Video video) {
        this.video = video;
    }

    @Override
    public FileContainer getOutputFile() {
        return outputFile;
    }

    @Override
    public void setOutputFile(FileContainer file) {
        this.outputFile = file;
    }

    @Override
    public VideoOptionPackage getSettings() {
        return settings;
    }

    @Override
    public void setSettings(VideoOptionPackage settings) {
        this.settings = settings;
    }

}
