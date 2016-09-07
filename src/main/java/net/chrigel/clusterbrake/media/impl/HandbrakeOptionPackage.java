package net.chrigel.clusterbrake.media.impl;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.media.FileContainer;
import net.chrigel.clusterbrake.media.VideoOptionPackage;

/**
 *
 */
class HandbrakeOptionPackage
        implements VideoOptionPackage {

    private FileContainer source;
    private List<String> options = new LinkedList<>();

    HandbrakeOptionPackage() {
    }

    HandbrakeOptionPackage(FileContainer source) {
        this.source = source;
    }

    @Override
    public List<String> getOptions() {
        return options;
    }

    @Override
    public void setOptions(List<String> options) {
        this.options = options;
    }

    @Override
    public FileContainer getOptionFile() {
        return source;
    }

    @Override
    public void setOptionFile(FileContainer file) {
        this.source = file;
    }

}
