package net.chrigel.clusterbrake.media.impl;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.media.VideoOptionPackage;

/**
 *
 */
class HandbrakeOptionPackage
        implements VideoOptionPackage {

    private File source;
    private List<String> options = new LinkedList<>();

    HandbrakeOptionPackage() {
    }

    HandbrakeOptionPackage(File source) {
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
    public File getOptionFile() {
        return source;
    }

    @Override
    public void setOptionFile(File file) {
        this.source = file;
    }

}
