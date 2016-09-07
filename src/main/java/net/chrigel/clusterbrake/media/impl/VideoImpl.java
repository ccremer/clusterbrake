package net.chrigel.clusterbrake.media.impl;

import net.chrigel.clusterbrake.media.FileContainer;
import net.chrigel.clusterbrake.media.Video;

/**
 *
 */
class VideoImpl
        implements Video {

    private final FileContainer source;

    VideoImpl() {
        this.source = null;
    }

    VideoImpl(FileContainer source) {
        this.source = source;

    }

    @Override
    public FileContainer getSourceFile() {
        return source;
    }

}
