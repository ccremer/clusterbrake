package net.chrigel.clusterbrake.media.impl;

import net.chrigel.clusterbrake.media.FileContainer;
import net.chrigel.clusterbrake.media.VideoOptionPackage;
import net.chrigel.clusterbrake.media.VideoPackage;

/**
 *
 */
public class VideoPackageImpl
        implements VideoPackage {

    private FileContainer output;
    private VideoOptionPackage settings;
    private FileContainer source;

    @Override
    public FileContainer getOutputFile() {
        return output;
    }

    @Override
    public void setOutputFile(FileContainer file) {
        this.output = file;
    }

    @Override
    public VideoOptionPackage getSettings() {
        return settings;
    }

    @Override
    public void setSettings(VideoOptionPackage settings) {
        this.settings = settings;
    }

    @Override
    public FileContainer getSourceFile() {
        return source;
    }

    @Override
    public void setSourceFile(FileContainer source) {
        this.source = source;
    }

}
