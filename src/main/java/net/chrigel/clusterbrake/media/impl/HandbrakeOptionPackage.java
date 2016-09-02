package net.chrigel.clusterbrake.media.impl;

import java.io.File;
import java.util.List;
import net.chrigel.clusterbrake.media.VideoOptionPackage;

/**
 *
 */
class HandbrakeOptionPackage
        implements VideoOptionPackage {

    private final File source;

    HandbrakeOptionPackage(File source) {
        this.source = source;
    }

    @Override
    public List<String> getOptions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setOptions(List<String> options) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public File getOptionFile() {
        return source;
    }

}
