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

    private final File source;
    private List<String> options;

    HandbrakeOptionPackage(File source) {
        this.source = source;
        this.options = new LinkedList<>();
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

}
