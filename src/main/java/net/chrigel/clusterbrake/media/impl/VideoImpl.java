package net.chrigel.clusterbrake.media.impl;

import java.io.File;
import net.chrigel.clusterbrake.media.Video;

/**
 *
 */
class VideoImpl
        implements Video {

    private final File source;

    VideoImpl(File source) {
        this.source = source;

    }

    @Override
    public File getSourceFile() {
        return source;
    }

}
